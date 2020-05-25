package com.game.dataMgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.bean.ConfBuilding;
import config.bean.ConfBuildingLv;

/**
 * done
 * @author Administrator
 *
 */
public class StaticBuildingDataMgr extends BaseDataMgr {

	private Map<Integer, ConfBuilding> buildingMap;

	/**
	 * @Fields levelMap : MAP<buildingId, MAP<buildingLevel,
	 *         StaticBuildingLevel>>
	 */
	private Map<Integer, Map<Integer, ConfBuildingLv>> levelMap;

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
//		buildingMap = staticDataDao.selectBuilding();
		initLevel();
	}

	private void calcLevelAdd(List<ConfBuildingLv> list) {
		for (ConfBuildingLv buildingLevel : list) {
			int preLv = buildingLevel.getLevel() - 1;
			if (preLv == 0) {
				buildingLevel.setStoneOutAdd(buildingLevel.getStoneOut());
				buildingLevel.setIronOutAdd(buildingLevel.getIronOut());
				buildingLevel.setOilOutAdd(buildingLevel.getOilOut());
				buildingLevel.setCopperOutAdd(buildingLevel.getCopperOut());
				buildingLevel.setSiliconOutAdd(buildingLevel.getSiliconOut());

				buildingLevel.setStoneMaxAdd(buildingLevel.getStoneMax());
				buildingLevel.setIronMaxAdd(buildingLevel.getIronMax());
				buildingLevel.setOilMaxAdd(buildingLevel.getOilMax());
				buildingLevel.setCopperMaxAdd(buildingLevel.getCopperMax());
				buildingLevel.setSiliconMaxAdd(buildingLevel.getSiliconMax());
			} else {

				ConfBuildingLv pre = levelMap.get(buildingLevel.getBuildingId()).get(preLv);
				if (pre == null) {
					continue;
				}

				buildingLevel.setStoneOutAdd(buildingLevel.getStoneOut() - pre.getStoneOut());
				buildingLevel.setIronOutAdd(buildingLevel.getIronOut() - pre.getIronOut());
				buildingLevel.setOilOutAdd(buildingLevel.getOilOut() - pre.getOilOut());
				buildingLevel.setCopperOutAdd(buildingLevel.getCopperOut() - pre.getCopperOut());
				buildingLevel.setSiliconOutAdd(buildingLevel.getSiliconOut() - pre.getSiliconOut());

				buildingLevel.setStoneMaxAdd(buildingLevel.getStoneMax() - pre.getStoneMax());
				buildingLevel.setIronMaxAdd(buildingLevel.getIronMax() - pre.getIronMax());
				buildingLevel.setOilMaxAdd(buildingLevel.getOilMax() - pre.getOilMax());
				buildingLevel.setCopperMaxAdd(buildingLevel.getCopperMax() - pre.getCopperMax());
				buildingLevel.setSiliconMaxAdd(buildingLevel.getSiliconMax() - pre.getSiliconMax());
			}
		}
	}

	private void initLevel() {
		levelMap = new HashMap<Integer, Map<Integer, ConfBuildingLv>>();
//		List<StaticBuildingLv> list = staticDataDao.selectBuildingLv();
//		for (StaticBuildingLv buildingLevel : list) {
//			Map<Integer, StaticBuildingLv> indexIdMap = levelMap.get(buildingLevel.getBuildingId());
//			if (indexIdMap == null) {
//				indexIdMap = new HashMap<>();
//				levelMap.put(buildingLevel.getBuildingId(), indexIdMap);
//			}
//
//			indexIdMap.put(buildingLevel.getLevel(), buildingLevel);
//		}

//		calcLevelAdd(list);
	}

	public ConfBuilding getStaticBuilding(int buildingId) {
		return buildingMap.get(buildingId);
	}

	public ConfBuildingLv getStaticBuildingLevel(int buildingId, int buildLevel) {
		Map<Integer, ConfBuildingLv> indexIdMap = levelMap.get(buildingId);
		if (indexIdMap != null) {
			return indexIdMap.get(buildLevel);
		}
		return null;
	}

}
