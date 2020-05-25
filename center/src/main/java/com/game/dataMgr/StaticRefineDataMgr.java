/**   
 * @Title: StaticRefineDataMgr.java    
 * @Package com.game.dataMgr    
 * @Description: TODO  
 * @author ChenKui   
 * @date 2015-8-27    
 * @version V1.0   
 */
package com.game.dataMgr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import config.bean.ConfRefine;
import config.bean.ConfRefineLv;

public class StaticRefineDataMgr extends BaseDataMgr {

	private Map<Integer, ConfRefine> refineMap;

	private Map<Integer, Map<Integer, ConfRefineLv>> refineLvMap = new HashMap<Integer, Map<Integer, ConfRefineLv>>();

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
//		refineMap = staticDataDao.selectRefineMap();
//		List<StaticRefineLv> refineLvList = staticDataDao.selectRefineLv();
//		for (StaticRefineLv staticRefineLv : refineLvList) {
//			int refineId = staticRefineLv.getRefineId();
//			int level = staticRefineLv.getLevel();
//			Map<Integer, StaticRefineLv> levelMap = refineLvMap.get(refineId);
//			if (levelMap == null) {
//				levelMap = new HashMap<Integer, StaticRefineLv>();
//				refineLvMap.put(refineId, levelMap);
//			}
//			levelMap.put(level, staticRefineLv);
//		}
	}

	public ConfRefine getStaticRefine(int refineId) {
		return refineMap.get(refineId);
	}

	public Map<Integer, ConfRefine> getStaticRefineMap() {
		return refineMap;
	}

	public ConfRefineLv getStaticRefineLv(int refineId, int level) {
		return refineLvMap.get(refineId).get(level);
	}

	public ConfRefine getRefineBuild(int buildingId) {
		Iterator<ConfRefine> it = refineMap.values().iterator();
		while (it.hasNext()) {
			ConfRefine next = it.next();
			if (next.getBuildId() == buildingId) {
				return next;
			}
		}
		return null;
	}

	public ConfRefine getRefineCapacity() {
		Iterator<ConfRefine> it = refineMap.values().iterator();
		while (it.hasNext()) {
			ConfRefine next = it.next();
			if (next.getCapacity() == 1) {
				return next;
			}
		}
		return null;
	}

}
