package com.game.dataMgr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import config.bean.ConfBuff;
import config.bean.ConfSkill;
import config.bean.ConfTank;

public class StaticTankDataMgr extends BaseDataMgr {

	private Map<Integer, ConfTank> tankMap;

	private Map<Integer, ConfBuff> buffMap;

	private Map<Integer, ConfSkill> skillMap;

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
//		tankMap = staticDataDao.selectTank();
//		buffMap = staticDataDao.selectBuff();
//		skillMap = staticDataDao.selectSkill();
//		initTankAura();
	}

	public void initTankAura() {
		Iterator<ConfTank> it = tankMap.values().iterator();
		while (it.hasNext()) {
			ConfTank staticTank = (ConfTank) it.next();
			List<Integer> aura = staticTank.getAura();
			ArrayList<ConfBuff> list = new ArrayList<>();
			if (aura != null && !aura.isEmpty()) {
				for (Integer buffId : aura) {
					list.add(buffMap.get(buffId));
				}
			}
			staticTank.setBuffs(list);
		}
	}

	public ConfTank getStaticTank(int tankId) {
		return tankMap.get(tankId);
	}

	public ConfSkill getStaticSkill(int skillId) {
		return skillMap.get(skillId);
	}
}
