package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.Arena;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ArenaProvider extends DataProvider<Arena> {

	private static final Logger logger = LoggerFactory.getLogger(ArenaProvider.class);

	public static ArenaProvider getInst() {
		return BeanManager.getBean(ArenaProvider.class);
	}

	public ArenaProvider() {
		super("");
	}

	@Override
	protected Class<Arena[]> getClassType() {
		return Arena[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected Arena convertDBResult2Bean(ASObject o) {
		Arena bean = new Arena();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(Arena bean) {
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
