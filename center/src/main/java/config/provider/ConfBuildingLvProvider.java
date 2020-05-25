package config.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfBuildingLv;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfBuildingLvProvider extends BaseProvider<ConfBuildingLv> {

	private Map<Integer, Map<Integer, ConfBuildingLv>> levelMap = new HashMap<Integer, Map<Integer, ConfBuildingLv>>();

	public static ConfBuildingLvProvider getInst() {
		return BeanManager.getBean(ConfBuildingLvProvider.class);
	}

	public ConfBuildingLvProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfBuildingLv[]> getClassType() {
		return ConfBuildingLv[].class;
	}

	@Override
	protected ConfBuildingLv convertDBResult2Bean(ASObject o) {
		ConfBuildingLv conf = new ConfBuildingLv();
		conf.setBuildingId(o.getInt("buildingId"));
		conf.setLevel(o.getInt("level"));
		conf.setIronCost(o.getInt("ironCost"));
		conf.setOilCost(o.getInt("oilCost"));
		conf.setCopperCost(o.getInt("copperCost"));
		conf.setSiliconCost(o.getInt("siliconCost"));
		conf.setGoldCost(o.getInt("goldCost"));
		conf.setStoneOut(o.getInt("stoneOut"));
		conf.setIronOut(o.getInt("ironOut"));
		conf.setOilOut(o.getInt("oilOut"));
		conf.setCopperOut(o.getInt("copperOut"));
		conf.setSiliconOut(o.getInt("siliconOut"));
		conf.setStoneMax(o.getInt("stoneMax"));
		conf.setIronMax(o.getInt("ironMax"));
		conf.setOilMax(o.getInt("oilMax"));
		conf.setCopperMax(o.getInt("copperMax"));
		conf.setSiliconMax(o.getInt("siliconMax"));
		conf.setUpTime(o.getInt("upTime"));
		conf.setLordLv(o.getInt("lordLv"));
		conf.setCommandLv(o.getInt("commandLv"));
		conf.setStoneOutAdd(o.getInt("stoneOutAdd"));
		conf.setIronOutAdd(o.getInt("ironOutAdd"));
		conf.setOilOutAdd(o.getInt("oilOutAdd"));
		conf.setCopperOutAdd(o.getInt("copperOutAdd"));
		conf.setSiliconOutAdd(o.getInt("siliconOutAdd"));
		conf.setStoneMaxAdd(o.getInt("stoneMaxAdd"));
		conf.setIronMaxAdd(o.getInt("ironMaxAdd"));
		conf.setOilMaxAdd(o.getInt("oilMaxAdd"));
		conf.setCopperMaxAdd(o.getInt("copperMaxAdd"));
		conf.setSiliconMaxAdd(o.getInt("siliconMaxAdd"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfBuildingLv conf) {
		ASObject o = new ASObject();
//		o.put("buildingId", conf.getBuildingId());
//		o.put("name", conf.getName());
//		o.put("type", conf.getType());
//		o.put("canUp", conf.getCanUp());
//		o.put("canDestory", conf.getCanDestory());
//		o.put("initLv", conf.getInitLv());
//		o.put("pros", conf.getPros());
//		o.put("canResource", conf.getCanResource());
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			Map<Integer, ConfBuildingLv> buildingLvMap = levelMap.get(e.getBuildingId());
			if (buildingLvMap == null) {
				buildingLvMap = new HashMap<>();
				levelMap.put(e.getBuildingId(), buildingLvMap);
			}
			buildingLvMap.put(e.getLevel(), e);
		});
	}

	public ConfBuildingLv getConfBuildingLevel(int buildingId, int buildLevel) {
		Map<Integer, ConfBuildingLv> indexIdMap = levelMap.get(buildingId);
		if (indexIdMap != null) {
			return indexIdMap.get(buildLevel);
		}
		return null;
	}
}
