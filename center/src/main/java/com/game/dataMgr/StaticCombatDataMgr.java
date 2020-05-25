package com.game.dataMgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.bean.ConfCombat;
import config.bean.ConfExplore;
import config.bean.ConfSection;

public class StaticCombatDataMgr extends BaseDataMgr {

	// 普通副本
	private Map<Integer, ConfCombat> combatMap;

	private Map<Integer, ConfSection> sectionMap;

	private ConfSection equipSection;

	private ConfSection partSection;

	private ConfSection timeSection;

	private ConfSection militarySection;// 军工副本

	// 探险
	private Map<Integer, ConfExplore> exploreMap;

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub
		initSection();
		initCombat();
		initExplore();
	}

	private void initSection() {
//		sectionMap = staticDataDao.selectSection();
//		if (sectionMap == null) {
//			sectionMap = new HashMap<>();
//		}
//
//		Iterator<StaticSection> it = sectionMap.values().iterator();
//		while (it.hasNext()) {
//			StaticSection staticSection = (StaticSection) it.next();
//			if (staticSection.getType() == 2) {
//				equipSection = staticSection;
//			} else if (staticSection.getType() == 3) {
//				partSection = staticSection;
//			} else if (staticSection.getType() == 4) {
//				timeSection = staticSection;
//			}else if(staticSection.getType() ==6) {
//				militarySection = staticSection;
//			}
//		}
	}

	private void initCombat() {
		combatMap = new HashMap<>();
//		List<StaticCombat> list = staticDataDao.selectCombat();
//		int preId = 0;
//		for (StaticCombat staticCombat : list) {
//			int combatId = staticCombat.getCombatId();
//			combatMap.put(combatId, staticCombat);
//
//			StaticSection staticSection = sectionMap.get(staticCombat.getSectionId());
//			if (staticSection.getStartId() == 0) {
//				staticSection.setStartId(combatId);
//			}
//			if (staticSection.getEndId() < combatId) {
//				staticSection.setEndId(combatId);
//			}
//
//			staticCombat.setPreId(preId);
//			preId = staticCombat.getCombatId();
//		}
	}

	private void initExplore() {
		exploreMap = new HashMap<>();
//		List<StaticExplore> list = staticDataDao.selectExplore();
//		int preId1 = 0;
//		int preId2 = 0;
//		int preId3 = 0;
//		int preId4 = 0;
//		int preId5 = 0;
//		for (StaticExplore staticExplore : list) {
//			if (staticExplore.getType() == 1) {
//				staticExplore.setPreId(preId1);
//				preId1 = staticExplore.getExploreId();
//			} else if (staticExplore.getType() == 2) {
//				staticExplore.setPreId(preId2);
//				preId2 = staticExplore.getExploreId();
//			} else if (staticExplore.getType() == 3) {
//				staticExplore.setPreId(preId3);
//				preId3 = staticExplore.getExploreId();
//			} 
//			else if (staticExplore.getType() == 4) {
//				staticExplore.setPreId(preId4);
//				preId4 = staticExplore.getExploreId();
//			} else {
//				staticExplore.setPreId(preId5);
//				preId5 = staticExplore.getExploreId();
//			}
//			calcDropWeight(staticExplore);
//			exploreMap.put(staticExplore.getExploreId(), staticExplore);
//		}
	}

	private void calcDropWeight(ConfExplore staticExplore) {
		List<List<Integer>> list = staticExplore.getDropOne();
		if (list != null && !list.isEmpty()) {
			int weight = 0;
			for (List<Integer> one : list) {
				if (one.size() != 4) {
					continue;
				}

				weight += one.get(3);
			}
			staticExplore.setWeight(weight);
		}
	}

	public ConfSection getStaticSection(int sectionId) {
		return sectionMap.get(sectionId);
	}

	public ConfCombat getStaticCombat(int combatId) {
		return combatMap.get(combatId);
	}

	public ConfExplore getStaticExplore(int exploreId) {
		return exploreMap.get(exploreId);
	}

	public ConfSection getEquipSection() {
		return equipSection;
	}

	public ConfSection getPartSection() {
		return partSection;
	}

	public ConfSection getTimeSection() {
		return timeSection;
	}

	public Map<Integer, ConfExplore> getAllExplore() {
		return exploreMap;
	}

	public ConfSection getMilitarySection() {
		return militarySection;
	}

}
