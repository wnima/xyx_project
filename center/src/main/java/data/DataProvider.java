package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actor.CenterActorManager;
import database.DBManager;
import database.DBUtil;
import database.DataQueryResult;
import inject.BeanManager;
import util.ASObject;
import util.DateUtil;
import util.LogHelper;

public abstract class DataProvider<T extends DataBean> {

	private static final Logger logger = LoggerFactory.getLogger(DataProvider.class);

	private Class<?> classType;

	private Map<Long, T> beanMap = new ConcurrentHashMap<Long, T>();
	private Map<Long, ASObject> cache = new ConcurrentHashMap<Long, ASObject>();
	private Map<Long, Signet> signets = new ConcurrentHashMap<Long, Signet>();

	protected void initString() {
	}

	protected void init() {
	}

	protected abstract Class<T[]> getClassType();

	protected static List<DataProvider<?>> providerList = new ArrayList<>();

	private String db;
	private String tableName;
	private long skip = 0;

	protected DataProvider(String tableName) {
		this.db = DBManager.getDefaultDatabase();
		this.tableName = tableName;
	}

	protected DataProvider(String db, String tableName) {
		this.db = db;
		this.tableName = tableName;
	}

	public static void init(List<Class<?>> configList) {
		for (Class<?> aClass : configList) {
			logger.info(" init class {}", aClass);
			DataProvider<?> provider = (DataProvider<?>) BeanManager.getBean(aClass);
			providerList.add(provider);
		}
	}

	public void reLoad() {
		doLoad();
		initString();
	}

	public static void loadAll() {
		providerList.forEach(e -> {
			e.loadData();
			e.doRegisterCornTask();
		});
	}

	public static void flushAll() {
		providerList.forEach(e -> {
			CenterActorManager.getDbFlushActor().put(() -> {
				e.flushProvider();
				return null;
			});
		});
	}

	public boolean loadData() {
		doLoad();
		initString();
		return true;
	}

	public void doLoad() {
		initFromMysql();
		this.init();
	}

	public void doRegisterCornTask() {
		CenterActorManager.getDBTimer().register(6000, 6000, () -> this.cleanDirtyStamp(), CenterActorManager.getDbFlushActor(), "cleanDirty");
		CenterActorManager.getDBTimer().register(6000, 6000, () -> this.flushCache(), CenterActorManager.getDbFlushActor(), "flushCache");
		CenterActorManager.getDBTimer().register(6000, 6000, () -> this.cronTask(), CenterActorManager.getDbCheckActor(), "DBCornTask");
	}

	private boolean initFromMysql() {
		if (tableName == null || tableName.equals("")) {
			return false;
		}
		Map<String, Object> filter = loadFilter();
		List<ASObject> result = DataQueryResult.load(db, tableName, filter);
		Map<Long, T> map = new ConcurrentHashMap<Long, T>();
		if (result.isEmpty())
			return false;
		result.forEach(e -> map.put(e.getLong(getIdKey()), convertDBResult2Bean(e)));
		this.beanMap = map;
		return true;
	}

	public T insert(T t) {
		ASObject o = this.convertBean2DbData(t);
		long id;
		try {
			id = DBUtil.executeInsert(db, tableName, o);
			t.setId(id);
		} catch (SQLException e) {
			LogHelper.ERROR.error(e.getMessage(), e);
			throw new RuntimeException("insert bean table Exception :" + tableName);
		}
		return t;
	}

	protected void flushProvider() {
		beanMap.values().forEach(e -> {
			ASObject as = convertBean2DbData(e);
			update(as);
		});
	}

	protected void flushCache() {
		cache.values().stream().forEach(e -> {
			String key = getIdKey();
			Map<String, Object> where = new HashMap<String, Object>();
			where.put(key, e.get(key));
			try {
				DBUtil.executeUpdate(db, tableName, where, (Map<String, Object>) e);
			} catch (SQLException e1) {
				logger.error(e1.getMessage(), e1);
			}
		});
	}

	public void update(ASObject as) {
		String key = getIdKey();
		Map<String, Object> where = new HashMap<String, Object>();
		where.put(key, as.get(key));
		try {
			DBUtil.executeInsertOrUpdate(db, tableName, where, (Map<String, Object>) as, isGenerateKey());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void putBean(T t) {
		beanMap.put(t.getId(), t);
	}

	public T getBeanById(long id) {
		return beanMap.get(id);
	}

	public List<T> getAllBean() {
		if (beanMap == null) {
			return new ArrayList<T>();
		}
		return new ArrayList<>(beanMap.values());
	}

	public List<T> getBeanList(Function<T, Boolean> fun) {
		List<T> result = new ArrayList<>();
		for (T t : beanMap.values()) {
			if (fun.apply(t))
				result.add(t);
		}
		return result;
	}

	public void putCache(T t) {
		ASObject o = convertBean2DbData(t);
		cache.put(t.getId(), o);
	}

	public void udp(T t) {
		ASObject o = convertBean2DbData(t);
		cache.put(t.getId(), o);
		sign(t);
	}

	private void sign(T t) {
		if (signets.containsKey(t.getId())) {
			signets.get(t.getId()).setTime(DateUtil.getSecondTime());
		} else {
			signets.put(t.getId(), new Signet(t.getId(), DateUtil.getSecondTime()));
		}
	}

	private void cleanDirtyStamp() {
		int currentSecond = DateUtil.getSecondTime();
		List<Signet> dirtyList = signets.values().stream().filter(e -> (e.getTime() + 3600) < currentSecond).collect(Collectors.toList());
		dirtyList.forEach(e -> {
			long id = e.getId();
			ASObject as = cache.remove(id);
			signets.remove(id);
			update(as);// safe
		});
	}

	public void removeCache(long id) {
		this.cache.remove(id);
	}

	public ASObject getCacheById(long id) {
		return cache.get(id);
	}

	public List<ASObject> getAllCache() {
		if (cache == null) {
			return new ArrayList<ASObject>();
		}
		return new ArrayList<>(cache.values());
	}

	public List<ASObject> getCacheList(Function<ASObject, Boolean> fun) {
		List<ASObject> result = new ArrayList<>();
		for (ASObject o : cache.values()) {
			if (fun.apply(o))
				result.add(o);
		}
		return result;
	}

	protected abstract Map<String, Object> loadFilter();

	protected abstract T convertDBResult2Bean(ASObject o);

	protected abstract ASObject convertBean2DbData(T t);

	protected void cronTask() {
	}

	protected boolean isGenerateKey() {
		return true;
	}

	public Map<Long, T> getBMap() {
		return beanMap;
	}

	protected String getIdKey() {
		return "id";
	}
}
