package config.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPartyBuildLevel;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartyBuildLevelProvider extends BaseProvider<ConfPartyBuildLevel> {

	private Map<Integer, Map<Integer, ConfPartyBuildLevel>> buildMap = new HashMap<Integer, Map<Integer, ConfPartyBuildLevel>>();

	public static ConfPartyBuildLevelProvider getInst() {
		return BeanManager.getBean(ConfPartyBuildLevelProvider.class);
	}

	public ConfPartyBuildLevelProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPartyBuildLevel[]> getClassType() {
		return ConfPartyBuildLevel[].class;
	}

	@Override
	protected ConfPartyBuildLevel convertDBResult2Bean(ASObject o) {
		ConfPartyBuildLevel conf = new ConfPartyBuildLevel();
		conf.setType(o.getInt("type"));
		conf.setBuildLv(o.getInt("buildLv"));
		conf.setNeedExp(o.getInt("needExp"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPartyBuildLevel conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			int buildingId = e.getType();
			int buildingLv = e.getBuildLv();
			Map<Integer, ConfPartyBuildLevel> tempMap = buildMap.get(buildingId);
			if (tempMap == null) {
				tempMap = new HashMap<Integer, ConfPartyBuildLevel>();
				buildMap.put(buildingId, tempMap);
			}
			tempMap.put(buildingLv, e);
		});
	}

	public ConfPartyBuildLevel getBuildLevel(int buildingId, int level) {
		return buildMap.get(buildingId).get(level);
	}

}
