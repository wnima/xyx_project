package com.game.dataMgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.bean.ConfEquip;
import config.bean.ConfEquipLv;
import data.bean.Equip;

/**
 * done
 * 
 * @author Administrator
 *
 */
public class StaticEquipDataMgr extends BaseDataMgr {

	private Map<Integer, ConfEquip> equipMap;

	/**
	 * @Fields levelMap : Map<quality, Map<level, StaticEquipLv>>
	 */
	private Map<Integer, Map<Integer, ConfEquipLv>> levelMap;

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
//		equipMap = staticDataDao.selectEquip();
		initLevel();
	}

	private void initLevel() {
//		levelMap = new HashMap<Integer, Map<Integer, StaticEquipLv>>();
//		List<StaticEquipLv> list = staticDataDao.selectEquipLv();
//		for (StaticEquipLv staticEquipLv : list) {
//			Map<Integer, StaticEquipLv> qualityMap = levelMap.get(staticEquipLv.getQuality());
//			if (qualityMap == null) {
//				qualityMap = new HashMap<>();
//				levelMap.put(staticEquipLv.getQuality(), qualityMap);
//			}
//
//			qualityMap.put(staticEquipLv.getLevel(), staticEquipLv);
//		}
	}

	public ConfEquip getStaticEquip(int equipId) {
		return equipMap.get(equipId);
	}

	public ConfEquipLv getStaticEquipLv(int quality, int lv) {
		Map<Integer, ConfEquipLv> map = levelMap.get(quality);
		if (map != null) {
			return map.get(lv);
		}

		return null;
	}

	public boolean addEquipExp(Equip equip, int add) {
		ConfEquip staticEquip = equipMap.get(equip.getEquipId());
		int quality = staticEquip.getQuality();
		int oriLv = equip.getLv();
		int lv = oriLv;
		int curExp = equip.getExp() + add;
		while (true) {
			ConfEquipLv staticEquipLv = getStaticEquipLv(quality, lv + 1);
			if (staticEquipLv == null) {
				break;
			}

			if (curExp >= staticEquipLv.getNeedExp()) {
				lv++;
				curExp -= staticEquipLv.getNeedExp();
			} else {
				break;
			}
		}

		equip.setLv(lv);
		equip.setExp(curExp);
		return oriLv != lv;
	}
}
