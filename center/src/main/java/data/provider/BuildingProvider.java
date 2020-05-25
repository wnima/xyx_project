package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.Building;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class BuildingProvider extends DataProvider<Building> {

	private static final Logger logger = LoggerFactory.getLogger(BuildingProvider.class);

	public static BuildingProvider getInst() {
		return BeanManager.getBean(BuildingProvider.class);
	}

	public BuildingProvider() {
		super("");
	}

	@Override
	protected Class<Building[]> getClassType() {
		return Building[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected Building convertDBResult2Bean(ASObject o) {
		Building bean = new Building();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(Building bean) {
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
