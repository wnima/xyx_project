package com.game.dataMgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.bean.ConfWarAward;

public class StaticWarAwardDataMgr extends BaseDataMgr {

	private Map<Integer, ConfWarAward> awardMap = new HashMap<Integer, ConfWarAward>();

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
//		awardMap = staticDataDao.selectWarAward();
	}

	public List<List<Integer>> getRankAward(int rank) {
		return awardMap.get(rank).getRankAwards();
	}

	public List<List<Integer>> getWinAward(int rank) {
		return awardMap.get(rank).getWinAwards();
	}

	public List<List<Integer>> getHurtAward(int rank) {
		return awardMap.get(rank).getHurtAwards();
	}

	public List<List<Integer>> getScoreAward(int rank) {
		return awardMap.get(rank).getScoreAwards();
	}

	public List<List<Integer>> getScorePartyAward(int rank) {
		return awardMap.get(rank).getScorePartyAwards();
	}
}
