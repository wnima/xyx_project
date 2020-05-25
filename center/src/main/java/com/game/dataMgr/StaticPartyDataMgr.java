package com.game.dataMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.util.RandomHelper;

import config.bean.ConfLiveTask;
import config.bean.ConfParty;
import config.bean.ConfPartyBuildLevel;
import config.bean.ConfPartyCombat;
import config.bean.ConfPartyContribute;
import config.bean.ConfPartyLively;
import config.bean.ConfPartyProp;
import config.bean.ConfPartyScience;
import config.bean.ConfPartyTrend;
import config.bean.ConfPartyWeal;

public class StaticPartyDataMgr extends BaseDataMgr {

	private Map<Integer, Map<Integer, ConfPartyBuildLevel>> buildMap = new HashMap<Integer, Map<Integer, ConfPartyBuildLevel>>();

	private Map<Integer, Map<Integer, ConfPartyContribute>> contributeMap = new HashMap<Integer, Map<Integer, ConfPartyContribute>>();

	private Map<Integer, ConfPartyLively> livelyMap = new HashMap<Integer, ConfPartyLively>();

	private Map<Integer, ConfPartyProp> propMap = new HashMap<Integer, ConfPartyProp>();

	private Map<Integer, Map<Integer, ConfPartyScience>> scienceMap = new HashMap<Integer, Map<Integer, ConfPartyScience>>();

	private Map<Integer, ConfPartyWeal> wealMap = new HashMap<Integer, ConfPartyWeal>();

	private Map<Integer, ConfLiveTask> liveTaskMap = new HashMap<Integer, ConfLiveTask>();

	private Map<Integer, ConfPartyTrend> partyTrendMap = new HashMap<Integer, ConfPartyTrend>();

	private Map<Integer, ConfPartyCombat> partyCombatMap = new HashMap<Integer, ConfPartyCombat>();

	private Map<Integer, ConfParty> partyMap = new HashMap<Integer, ConfParty>();

	@Override
	public void init() {
//		List<StaticPartyBuildLevel> buildLv = staticDataDao.selectPartyBuildLevel();
//		for (StaticPartyBuildLevel e : buildLv) {
//			int buildingId = e.getType();
//			int buildingLv = e.getBuildLv();
//			Map<Integer, StaticPartyBuildLevel> tempMap = buildMap.get(buildingId);
//			if (tempMap == null) {
//				tempMap = new HashMap<Integer, StaticPartyBuildLevel>();
//				buildMap.put(buildingId, tempMap);
//			}
//			tempMap.put(buildingLv, e);
//		}
//		livelyMap = staticDataDao.selectPartyLivelyMap();
//
//		List<StaticPartyContribute> contributeList = staticDataDao.selectPartyContribute();
//		for (StaticPartyContribute e : contributeList) {
//			int resourceId = e.getType();
//			int count = e.getCount();
//			Map<Integer, StaticPartyContribute> tempMap = contributeMap.get(resourceId);
//			if (tempMap == null) {
//				tempMap = new HashMap<Integer, StaticPartyContribute>();
//				contributeMap.put(resourceId, tempMap);
//			}
//			tempMap.put(count, e);
//		}
//
//		propMap = staticDataDao.selectPartyProp();
//
//		List<StaticPartyScience> scienceLvs = staticDataDao.selectPartyScience();
//		for (StaticPartyScience e : scienceLvs) {
//			int scienceId = e.getScienceId();
//			int scienceLv = e.getScienceLv();
//			Map<Integer, StaticPartyScience> tempMap = scienceMap.get(scienceId);
//			if (tempMap == null) {
//				tempMap = new HashMap<Integer, StaticPartyScience>();
//				scienceMap.put(scienceId, tempMap);
//			}
//			tempMap.put(scienceLv, e);
//		}
//
//		wealMap = staticDataDao.selectPartyWealMap();
//
//		liveTaskMap = staticDataDao.selectLiveTaskMap();
//
//		partyTrendMap = staticDataDao.selectTrend();
//
//		partyCombatMap = staticDataDao.selectPartyCombat();
//
//		partyMap = staticDataDao.selectParty();
//
//		initTotalTank();
	}

	private void initTotalTank() {

		Iterator<ConfPartyCombat> it = partyCombatMap.values().iterator();
		while (it.hasNext()) {
			int total = 0;
			ConfPartyCombat staticPartyCombat = (ConfPartyCombat) it.next();
			List<List<Integer>> list = staticPartyCombat.getForm();
			for (List<Integer> one : list) {
				total += one.get(1);
			}
			staticPartyCombat.setTotalTank(total);
		}
	}

	public ConfPartyContribute getStaticContribute(int resourceId, int count) {
		return contributeMap.get(resourceId).get(count);
	}

	public ConfPartyBuildLevel getBuildLevel(int buildingId, int level) {
		return buildMap.get(buildingId).get(level);
	}

	public Map<Integer, ConfPartyProp> getPropMap() {
		return propMap;
	}

	public List<ConfPartyProp> getCommonProp() {
		List<ConfPartyProp> rs = new ArrayList<ConfPartyProp>();
		Iterator<ConfPartyProp> it = propMap.values().iterator();
		while (it.hasNext()) {
			ConfPartyProp next = it.next();
			if (next.getTreasure() == 1) {
				rs.add(next);
			}
		}
		return rs;
	}

	public List<ConfPartyProp> getPartyShopProp() {
		List<ConfPartyProp> rs = new ArrayList<ConfPartyProp>();
		List<int[]> tempList = new ArrayList<int[]>();
		Iterator<ConfPartyProp> it = propMap.values().iterator();
		while (it.hasNext()) {
			ConfPartyProp next = it.next();
			if (next.getTreasure() == 2) {
				int[] entity = { next.getKeyId(), next.getProbability() };
				tempList.add(entity);
			}
		}
		// 随机3个珍品
		for (int i = 0; i < 3; i++) {
			int seeds[] = { 0, 0 };
			for (int[] e : tempList) {
				seeds[0] += e[1];
			}
			seeds[0] = RandomHelper.randomInSize(seeds[0]);
			Iterator<int[]> its = tempList.iterator();
			while (its.hasNext()) {
				int[] pp = its.next();
				seeds[1] += pp[1];
				if (seeds[0] < seeds[1]) {
					rs.add(getStaticPartyProp(pp[0]));
					its.remove();
					break;
				}
			}
		}
		return rs;
	}

	public ConfPartyProp getStaticPartyProp(int keyId) {
		return propMap.get(keyId);
	}

	public ConfPartyWeal getStaticWeal(int wealLv) {
		return wealMap.get(wealLv);
	}

	public ConfPartyScience getPartyScience(int scienceId, int level) {
		return scienceMap.get(scienceId).get(level);
	}

	public Map<Integer, ConfPartyLively> getLivelyMap() {
		return livelyMap;
	}

	public ConfLiveTask getLiveTask(int taskId) {
		return liveTaskMap.get(taskId);
	}

	public Map<Integer, ConfLiveTask> getLiveTaskMap() {
		return liveTaskMap;
	}

	public Map<Integer, Map<Integer, ConfPartyScience>> getScienceMap() {
		return scienceMap;
	}

	public int costLively(int lively) {
		ConfPartyLively maxLive = getMaxLive();
		if (maxLive != null && maxLive.getLivelyExp() <= lively) {
			lively = lively - maxLive.getCostLively();
			lively = lively < 0 ? 0 : lively;
			return lively;
		}

		int size = livelyMap.size();
		ConfPartyLively entity = null;
		for (int i = 0; i < size; i++) {
			ConfPartyLively ee = livelyMap.get(i + 1);
			if (lively <= ee.getLivelyExp()) {
				entity = ee;
				break;
			}
		}
		if (entity != null) {
			lively = lively - entity.getCostLively();
			lively = lively < 0 ? 0 : lively;
		}
		return lively;
	}

	public ConfPartyLively getMaxLive() {
		ConfPartyLively max = null;
		Iterator<ConfPartyLively> it = livelyMap.values().iterator();
		while (it.hasNext()) {
			ConfPartyLively next = it.next();
			if (max == null) {
				max = next;
			} else {
				if (next.getLivelyExp() > max.getLivelyExp()) {
					max = next;
				}
			}
		}
		return max;
	}

	public int getPartyLiveBuild(int lively) {
		ConfPartyLively maxLive = getMaxLive();
		if (maxLive != null && maxLive.getLivelyExp() <= lively) {
			return maxLive.getScience();
		}

		int size = livelyMap.size();
		ConfPartyLively entity = null;
		for (int i = 0; i < size; i++) {
			ConfPartyLively ee = livelyMap.get(i + 1);
			if (lively <= ee.getLivelyExp()) {
				entity = ee;
				break;
			}
		}
		if (entity != null) {
			return entity.getScience();
		}
		return 1;
	}

	public int getPartyLiveResource(int lively) {
		int size = livelyMap.size();
		ConfPartyLively entity = null;
		for (int i = 0; i < size; i++) {
			ConfPartyLively ee = livelyMap.get(i + 1);
			if (lively <= ee.getLivelyExp()) {
				entity = ee;
				break;
			}
		}

		if (entity != null) {
			return entity.getResource();
		} else {
			ConfPartyLively ee = livelyMap.get(size);
			if (ee.getLivelyExp() <= lively) {
				return ee.getResource();
			}
		}
		return 0;
	}

	public List<ConfPartyScience> getInitScience() {
		List<ConfPartyScience> rs = new ArrayList<ConfPartyScience>();
		Iterator<Map<Integer, ConfPartyScience>> it = scienceMap.values().iterator();
		while (it.hasNext()) {
			Map<Integer, ConfPartyScience> map = it.next();
			rs.add(map.get(0));
		}
		return rs;
	}

	public ConfPartyTrend getPartyTrend(int trendId) {
		return partyTrendMap.get(trendId);
	}

	public ConfPartyCombat getPartyCombat(int combatId) {
		return partyCombatMap.get(combatId);
	}

	public Map<Integer, ConfPartyCombat> getPartyCombatMap() {
		return partyCombatMap;
	}

	public int getLvNum(int lv) {
		if (partyMap.containsKey(lv)) {
			return partyMap.get(lv).getPartyNum();
		}
		return 0;
	}
}
