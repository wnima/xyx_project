package com.game.dataMgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.bean.ConfLordCommand;
import config.bean.ConfLordLv;
import config.bean.ConfLordPros;
import config.bean.ConfLordRank;
import data.bean.Lord;

/**
 * done
 * 
 * @author Administrator
 *
 */
public class StaticLordDataMgr extends BaseDataMgr {

	// 指挥官等级
	private Map<Integer, ConfLordLv> lordLvMap;

	// 统帅等级
	private Map<Integer, ConfLordCommand> commandLvMap;

	// 繁荣度
	private List<ConfLordPros> prosLvList;
	private Map<Integer, ConfLordPros> prosLvMap;

	// 军衔
	private Map<Integer, ConfLordRank> rankMap;

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
//		lordLvMap = staticDataDao.selectLordLv();
//		commandLvMap = staticDataDao.selectLordCommand();
//		prosLvList = staticDataDao.selectLordPros();
//		rankMap = staticDataDao.selectLordRank();
//		initProsLvMap();
	}

	private void initProsLvMap() {
		prosLvMap = new HashMap<Integer, ConfLordPros>();
		for (ConfLordPros staticProsLv : prosLvList) {
			prosLvMap.put(staticProsLv.getProsLv(), staticProsLv);
		}
	}

	public ConfLordLv getStaticLordLv(int lv) {
		return lordLvMap.get(lv);
	}

	public ConfLordCommand getStaticCommandLv(int lv) {
		return commandLvMap.get(lv);
	}

	public ConfLordPros getStaticProsLv(int pros) {
		ConfLordPros lv = null;
		for (ConfLordPros staticProsLv : prosLvList) {
			if (pros >= staticProsLv.getProsExp()) {
				lv = staticProsLv;
			} else
				break;
		}
		return lv;
	}

	public ConfLordRank getStaticLordRank(int rankId) {
		return rankMap.get(rankId);
	}

	public boolean addExp(Lord lord, int add) {
		int lv = lord.getLevel();

		boolean up = false;
		int exp = lord.getExp() + add;
		while (true) {
			if (lv >= 80) {
				break;
			}

			ConfLordLv staticLordLv = lordLvMap.get(lv + 1);
			if (exp >= staticLordLv.getNeedExp()) {
				up = true;
				exp -= staticLordLv.getNeedExp();
				lv++;
				continue;
			} else {
				break;
			}
		}

		lord.setLevel(lv);
		lord.setExp(exp);
		return up;
	}

}
