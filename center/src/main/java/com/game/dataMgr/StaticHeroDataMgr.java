package com.game.dataMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import config.bean.ConfHero;

/**
 * done
 * 
 * @author Administrator
 *
 */
public class StaticHeroDataMgr extends BaseDataMgr {

	private Map<Integer, ConfHero> heroMap = new HashMap<Integer, ConfHero>();

	private Map<Integer, List<ConfHero>> starMapList = new HashMap<Integer, List<ConfHero>>();

	private Map<Integer, List<ConfHero>> starLvMapList = new HashMap<Integer, List<ConfHero>>();

	@Override
	public void init() {
//		heroMap = staticDataDao.selectHeroMap();
//		Iterator<StaticHero> it = heroMap.values().iterator();
//		while (it.hasNext()) {
//			StaticHero next = it.next();
//			int star = next.getStar();
//			List<StaticHero> llList = starMapList.get(star);
//			if (llList == null) {
//				llList = new ArrayList<StaticHero>();
//				starMapList.put(star, llList);
//			}
//			
//			llList.add(next);
//
//			if (next.getLevel() != 0 || next.getCompound() != 0) {
//				continue;
//			}
//			List<StaticHero> llLvList = starLvMapList.get(star);
//			if (llLvList == null) {
//				llLvList = new ArrayList<StaticHero>();
//				starLvMapList.put(star, llLvList);
//			}
//			llLvList.add(next);
//		}
	}

	public ConfHero getStaticHero(int heroId) {
		return heroMap.get(heroId);
	}

	public List<ConfHero> getStarList(int star) {
		return starMapList.get(star);
	}

	public List<ConfHero> getStarListLv(int star) {
		return starLvMapList.get(star);
	}

	public int costSoul(int star) {
		if (star < 1 || star > 4) {
			return 0;
		}
		return starMapList.get(star).get(0).getSoul();
	}
}
