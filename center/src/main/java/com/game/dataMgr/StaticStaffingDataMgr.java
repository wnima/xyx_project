package com.game.dataMgr;

import java.util.Map;

import config.bean.ConfStaffing;
import config.bean.ConfStaffingLv;
import config.bean.ConfStaffingWorld;
import data.bean.Lord;

public class StaticStaffingDataMgr extends BaseDataMgr {

	private Map<Integer, ConfStaffingLv> lvMap;
	private Map<Integer, ConfStaffing> staffingMap;
	private Map<Integer, ConfStaffingWorld> worldMap;

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
//		lvMap = staticDataDao.selectStaffingLv();
//		staffingMap = staticDataDao.selectStaffing();
//		worldMap = staticDataDao.selectStaffingWorld();
	}

	public boolean addStaffingExp(Lord lord, int add) {
		int lv = lord.getStaffingLv();

		boolean up = false;
		int exp = lord.getStaffingExp() + add;
		while (true) {
			if (lv >= 1000) {
				break;
			}

			ConfStaffingLv staticStaffingLv = lvMap.get(lv + 1);
			if (exp >= staticStaffingLv.getExp()) {
				up = true;
				exp -= staticStaffingLv.getExp();
				lv++;
				continue;
			} else {
				break;
			}
		}

		lord.setStaffingLv(lv);
		lord.setStaffingExp(exp);
		return up;
	}

	public int getTotalExp(Lord lord) {
		if (lord.getStaffingLv() == 0) {
			return lord.getStaffingExp();
		}
		return lvMap.get(lord.getStaffingLv()).getTotalExp() + lord.getStaffingExp();
	}

	public boolean subStaffingExp(Lord lord, int sub) {
		int lv = lord.getStaffingLv();
		boolean down = false;

		int exp = lord.getStaffingExp() - sub;
		while (true) {
			if (lv < 1) {
				break;
			}

			if (exp < 0) {
				ConfStaffingLv staticStaffingLv = lvMap.get(lv);
				exp += staticStaffingLv.getExp();
				lv--;
				down = true;

				if (exp < 0) {
					continue;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		if (exp < 0) {
			exp = 0;
		}

		lord.setStaffingLv(lv);
		lord.setStaffingExp(exp);
		return down;
	}

	public ConfStaffing getStaffing(int staffingId) {
		return staffingMap.get(staffingId);
	}

	public ConfStaffingWorld calcWolrdLv(int totalLv) {
		ConfStaffingWorld staticStaffingWorld = null;
		int worldLv = 0;
		while (true) {
			if (worldLv >= 10) {
				break;
			}

			ConfStaffingWorld world = worldMap.get(worldLv);
			if (totalLv >= world.getSumStaffing()) {
				worldLv++;
				staticStaffingWorld = world;
				continue;
			} else {
				break;
			}
		}

		return staticStaffingWorld;
	}

	public ConfStaffingWorld getStaffingWorld(int lv) {
		return worldMap.get(lv);
	}

	/**
	 * 
	 * Method: calcStaffing
	 * 
	 * @Description: 不考虑人数限制时，应该可以得到的称号
	 * @param lv
	 * @param ranks
	 * @return
	 * @return StaticStaffing
	 * @throws
	 */
	public ConfStaffing calcStaffing(int lv, int ranks) {
		ConfStaffing staticStaffing = null;
		int id = 1;
		while (id <= 11) {
			ConfStaffing staffing = staffingMap.get(id);
			if (lv >= staffing.getStaffingLv() && ranks >= staffing.getRank()) {
				staticStaffing = staffing;
				id++;
				continue;
			} else {
				break;
			}
		}

		return staticStaffing;
	}

	public Map<Integer, ConfStaffing> getStaffingMap() {
		return staffingMap;
	}

	public void setStaffingMap(Map<Integer, ConfStaffing> staffingMap) {
		this.staffingMap = staffingMap;
	}
}
