package com.game.dataMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.util.RandomHelper;

import config.bean.ConfSign;
import config.bean.ConfSignLogin;

public class StaticSignDataMgr extends BaseDataMgr {


	private Map<Integer, ConfSign> signMap = new HashMap<Integer, ConfSign>();

	private Map<Integer, List<ConfSignLogin>> signLoginMap = new HashMap<Integer, List<ConfSignLogin>>();

	@Override
	public void init() {
//		signMap = staticDataDao.selectSign();
//		this.iniSignLogin();
	}

	public void iniSignLogin() {
//		List<StaticSignLogin> signLogins = staticDataDao.selectSignLogin();
//		for (StaticSignLogin e : signLogins) {
//			int grid = e.getGrid();
//			List<StaticSignLogin> loginList = signLoginMap.get(grid);
//			if (loginList == null) {
//				loginList = new ArrayList<StaticSignLogin>();
//				signLoginMap.put(grid, loginList);
//			}
//			loginList.add(e);
//		}
	}

	public Map<Integer, ConfSign> getSignMap() {
		return signMap;
	}

	public ConfSign getSign(int signId) {
		return signMap.get(signId);
	}

	public ConfSignLogin getSignLoginByGrid(int grid) {
		List<ConfSignLogin> list = signLoginMap.get(grid);
		int seeds[] = { 0, 0 };
		for (ConfSignLogin e : list) {
			seeds[0] += e.getProbability();
		}
		seeds[0] = RandomHelper.randomInSize(seeds[0]);
		for (ConfSignLogin e : list) {
			seeds[1] += e.getProbability();
			if (seeds[0] <= seeds[1]) {
				return e;
			}
		}
		return null;
	}

}
