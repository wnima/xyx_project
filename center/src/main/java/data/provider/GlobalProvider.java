package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.DbGlobal;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class GlobalProvider extends DataProvider<DbGlobal> {

	private static final Logger logger = LoggerFactory.getLogger(GlobalProvider.class);

	public static GlobalProvider getInst() {
		return BeanManager.getBean(GlobalProvider.class);
	}

	public GlobalProvider() {
		super("");
	}

	@Override
	protected Class<DbGlobal[]> getClassType() {
		return DbGlobal[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected DbGlobal convertDBResult2Bean(ASObject o) {
		DbGlobal bean = new DbGlobal();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(DbGlobal bean) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	@Override
	protected void cronTask() {
	}

}
