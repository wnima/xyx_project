package config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.DBManager;
import database.DBUtil;
import database.DataQueryResult;
import inject.BeanManager;
import json.JsonUtil;
import service.BaseApp;
import util.ASObject;
import util.LogHelper;

public abstract class BaseProvider<T extends IConfigBean> {

	private static final Logger logger = LoggerFactory.getLogger(BaseProvider.class);

	public static BaseApp app;

	public static String confPath;

	private Class<?> classType;

	private Map<Integer, T> configMap;

	protected String confString;

	protected void initString() {

	}

	protected void init() {
	}

	protected abstract Class<T[]> getClassType();

	protected static List<BaseProvider<?>> providerList = new ArrayList<>();

	private String jsonName;

	private String tableName;

	protected BaseProvider(String jsonName, String tableName) {
		this.jsonName = jsonName;
		this.tableName = tableName;
		this.confPath = app.getConfDir();
	}

	public static void init(List<Class<?>> configList) {
		for (Class<?> aClass : configList) {
			logger.info(" init class {}", aClass);
			BaseProvider<?> provider = (BaseProvider<?>) BeanManager.getBean(aClass);
			providerList.add(provider);
		}
	}

	public void reLoad() {
		doLoad();
		initString();
	}

	public String getConfString() {
		return confString;
	}

	public static void loadAll() {
		providerList.forEach(e -> e.loadConfig());
	}

	public boolean loadConfig() {
		doLoad();
		initString();
		return true;
	}

	public void doLoad() {
		if (!initFromMysql()) {
			initFromJson();
		}
		this.init();
	}

	private boolean initFromMysql() {
		if (tableName == null || tableName.equals("")) {
			return false;
		}
		List<ASObject> result = DataQueryResult.load(DBManager.getConfigDatabase(), tableName, null);
		Map<Integer, T> configMap = new HashMap<>();
		if (result.isEmpty())
			return false;
		result.forEach(e ->{
			int id = e.getInt(getIdKey());
			configMap.put(id, convertDBResult2Bean(e));
		});
		this.configMap = configMap;
		return true;
	}

	private boolean initFromJson() {
		if (jsonName == null || jsonName.equals("")) {
			return true;
		}
		configMap = JsonUtil.getJsonMap(getClassType(), confPath + jsonName);
		List<ASObject> dataList = new ArrayList<>();
		configMap.values().forEach(e -> {
			ASObject o = convertBean2DbData(e);
			if (null != o) {
				dataList.add(o);
			}
		});
		try {
			DBUtil.batchInsertOrUpdate(DBManager.getConfigDatabase(), tableName, Arrays.asList(getIdKey()), dataList.toArray(), null);
		} catch (SQLException e) {
			// logger.error(e.getMessage(), e);
			LogHelper.ERROR.error(e.getMessage(), e);
			throw new RuntimeException("insert conf 2 table Exception :" + tableName);
		}
		return true;
	}

	protected void updateMysql() {
		List<ASObject> dataList = new ArrayList<>();
		configMap.values().forEach(e -> {
			ASObject o = convertBean2DbData(e);
			if (null != o) {
				dataList.add(o);
			}
		});
		try {
			DBUtil.batchInsertOrUpdate(DBManager.getConfigDatabase(), tableName, Arrays.asList(getIdKey()), dataList.toArray(), null);
		} catch (SQLException e) {
			LogHelper.ERROR.error(e.getMessage(), e);
			throw new RuntimeException("insert conf 2 table Exception :" + tableName);
		}
	}

	public void putConfig(T t) {
		configMap.put(t.getId(), t);
	}

	public T getConfigById(int id) {
		return configMap.get(id);
	}

	public boolean isContain(int id) {
		return configMap.containsKey(id);
	}

	public List<T> getAllConfig() {
		if (configMap == null) {
			return new ArrayList<T>();
		}
		return new ArrayList<>(configMap.values());
	}

	public List<T> getConfigList(Function<T, Boolean> fun) {
		List<T> result = new ArrayList<>();
		for (T t : configMap.values()) {
			if (fun.apply(t))
				result.add(t);
		}
		return result;
	}

	protected String getIdKey() {
		return "id";
	}

	protected abstract T convertDBResult2Bean(ASObject o);

	protected abstract ASObject convertBean2DbData(T t);

	public Map<Integer, T> getConfigMap() {
		return configMap;
	}
}
