package com.game.dataMgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.bean.ConfPart;
import config.bean.ConfPartRefit;
import config.bean.ConfPartUp;

/**
 * done
 * 
 * @author Administrator
 *
 */
public class StaticPartDataMgr extends BaseDataMgr {

	private Map<Integer, ConfPart> partMap;

	/**
	 * @Fields upMap : Map<partId, Map<lv, StaticPartUp>>
	 */
	private Map<Integer, Map<Integer, ConfPartUp>> upMap;

	/**
	 * @Fields refitMap : Map<quality, Map<lv, StaticPartUp>>
	 */
	private Map<Integer, Map<Integer, ConfPartRefit>> refitMap;

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
//		partMap = staticDataDao.selectPart();
//		initUp();
//		initRefit();
	}

	private void initUp() {
//		upMap = new HashMap<Integer, Map<Integer, StaticPartUp>>();
//		List<StaticPartUp> list = staticDataDao.selectPartUp();
//		for (StaticPartUp staticPartUp : list) {
//			Map<Integer, StaticPartUp> map = upMap.get(staticPartUp.getPartId());
//			if (map == null) {
//				map = new HashMap<>();
//				upMap.put(staticPartUp.getPartId(), map);
//			}
//
//			map.put(staticPartUp.getLv(), staticPartUp);
//		}
	}

	private void initRefit() {
//		refitMap = new HashMap<Integer, Map<Integer, StaticPartRefit>>();
//		List<StaticPartRefit> list = staticDataDao.selectPartRefit();
//		for (StaticPartRefit staticPartRefit : list) {
//			Map<Integer, StaticPartRefit> map = refitMap.get(staticPartRefit.getQuality());
//			if (map == null) {
//				map = new HashMap<>();
//				refitMap.put(staticPartRefit.getQuality(), map);
//			}
//
//			map.put(staticPartRefit.getLv(), staticPartRefit);
//		}
	}

	public ConfPart getStaticPart(int partId) {
		return partMap.get(partId);
	}

	public ConfPartUp getStaticPartUp(int partId, int upLv) {
		Map<Integer, ConfPartUp> map = upMap.get(partId);
		if (map != null) {
			return map.get(upLv);
		}
		return null;
	}

	public ConfPartRefit getStaticPartRefit(int quality, int refitLv) {
		Map<Integer, ConfPartRefit> map = refitMap.get(quality);
		if (map != null) {
			return map.get(refitLv);
		}
		return null;
	}
}
