package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfBuilding;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfBuildingProvider extends BaseProvider<ConfBuilding> {

	public static ConfBuildingProvider getInst() {
		return BeanManager.getBean(ConfBuildingProvider.class);
	}

	public ConfBuildingProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfBuilding[]> getClassType() {
		return ConfBuilding[].class;
	}

	@Override
	protected ConfBuilding convertDBResult2Bean(ASObject o) {
		ConfBuilding conf = new ConfBuilding();
		conf.setBuildingId(o.getInt("buildingId"));
		conf.setName(o.getString("name"));
		conf.setType(o.getInt("type"));
		conf.setCanUp(o.getInt("canUp"));
		conf.setCanDestory(o.getInt("canDestory"));
		conf.setInitLv(o.getInt("initLv"));
		conf.setPros(o.getInt("pros"));
		conf.setCanResource(o.getInt("canResource"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfBuilding conf) {
		ASObject o = new ASObject();
		o.put("buildingId", conf.getBuildingId());
		o.put("name", conf.getName());
		o.put("type", conf.getType());
		o.put("canUp", conf.getCanUp());
		o.put("canDestory", conf.getCanDestory());
		o.put("initLv", conf.getInitLv());
		o.put("pros", conf.getPros());
		o.put("canResource", conf.getCanResource());
		return o;
	}

	@Override
	public void init() {
	}

}
