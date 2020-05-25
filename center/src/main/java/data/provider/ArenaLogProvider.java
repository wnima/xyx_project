package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.ArenaLog;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ArenaLogProvider extends DataProvider<ArenaLog> {

	private static final Logger logger = LoggerFactory.getLogger(ArenaLogProvider.class);

	public static ArenaLogProvider getInst() {
		return BeanManager.getBean(ArenaLogProvider.class);
	}

	public ArenaLogProvider() {
		super("");
	}

	@Override
	protected Class<ArenaLog[]> getClassType() {
		return ArenaLog[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected ArenaLog convertDBResult2Bean(ASObject o) {
		ArenaLog bean = new ArenaLog();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(ArenaLog bean) {
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
