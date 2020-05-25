package com.game.dataMgr;

import java.util.List;
import java.util.Map;

import com.game.util.RandomHelper;

import config.bean.ConfMine;
import config.bean.ConfMineForm;
import config.bean.ConfMineLv;
import config.bean.ConfScout;
import config.bean.ConfSlot;

public class StaticWorldDataMgr extends BaseDataMgr {

	private Map<Integer, ConfMine> mineMap;

	private Map<Integer, ConfMine> seniorMineMap;

	private Map<Integer, ConfScout> scoutMap;

	private Map<Integer, ConfMineLv> mineLvMap;

	private List<ConfSlot> slots;

	// MAP<LV, LIST>
	private Map<Integer, List<ConfMineForm>> formMap;

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
		// mineMap = staticDataDao.selectMine();
		// seniorMineMap = staticDataDao.selectMineSenior();
		// scoutMap = staticDataDao.selectScout();
		// mineLvMap = staticDataDao.selectMineLv();
		// slots = staticDataDao.selectSlot();
		// initForm();
	}

	private void initForm() {
//		formMap = new HashMap<Integer, List<ConfMineForm>>();
//		List<StaticMineForm> list = staticDataDao.selectMineForm();
//		for (StaticMineForm staticMineForm : list) {
//			// checkForm(staticMineForm);
//			List<StaticMineForm> one = formMap.get(staticMineForm.getLv());
//			if (one == null) {
//				one = new ArrayList<>();
//				formMap.put(staticMineForm.getLv(), one);
//			}
//
//			one.add(staticMineForm);
//		}
	}

	public boolean checkForm(ConfMineForm staticMineForm) {
		int formCount = 0;
		for (List<Integer> e : staticMineForm.getForm()) {
			if (!e.isEmpty()) {
				formCount++;
			}
		}

		int attrCount = 0;
		for (List<Integer> e : staticMineForm.getAttr()) {
			if (!e.isEmpty()) {
				attrCount++;
			}
		}

		if (formCount != attrCount) {
			System.err.println("check StaticMineForm " + staticMineForm.getKeyId() + " |" + formCount + " " + attrCount);
			return false;
		}

		return true;
	}

	public ConfMine getMine(int pos) {
		return mineMap.get(pos);
	}

	public ConfMine getSeniorMine(int pos) {
		return seniorMineMap.get(pos);
	}

	public ConfScout getScout(int lv) {
		return scoutMap.get(lv);
	}

	public ConfMineForm randomForm(int lv) {
		List<ConfMineForm> one = formMap.get(lv);
		return one.get(RandomHelper.randomInSize(one.size()));
	}

	public ConfMineLv getStaticMineLv(int mineLv) {
		return mineLvMap.get(mineLv);
	}

	/**
	 * 
	 * Method: getSlot
	 * 
	 * @Description: 根据地图上的玩家数量，分配新进入地图玩家的slot(0 ~ 399) @param
	 * playerNumber @return @return int @throws
	 */
	public int getSlot(int playerNumber) {
		int index = playerNumber / 400;
		if (index > 199) {
			return RandomHelper.randomInSize(400);
		} else {
			// return 125;
			ConfSlot staticSlot = slots.get(index);
			if (playerNumber % 2 == 0) {
				return staticSlot.getSlotA();
			} else {
				return staticSlot.getSlotB();
			}
		}
	}
}
