package com.game.dataMgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.bean.ConfPay;
import config.bean.ConfVip;

public class StaticVipDataMgr extends BaseDataMgr {

	// vip
	private Map<Integer, ConfVip> vipMap;

	private List<ConfVip> vipList;

	private List<ConfPay> payList;

	private List<ConfPay> payIosList;

	/**
	 * Overriding: init
	 * 
	 * @see com.game.dataMgr.BaseDataMgr#init()
	 */
	@Override
	public void init() {
//		initVip();
//		payList = staticDataDao.selectPay();
//		payIosList = staticDataDao.selectPayIos();
	}

	private void initVip() {
//		vipList = staticDataDao.selectVip();
//		vipMap = new HashMap<Integer, StaticVip>();
//
//		for (StaticVip staticVip : vipList) {
//			vipMap.put(staticVip.getVip(), staticVip);
//		}
	}

	public ConfVip getStaticVip(int vip) {
		return vipMap.get(vip);
	}

	public int calcVip(int topup) {
		ConfVip vip = null;
		for (ConfVip staticVip : vipList) {
			if (topup >= staticVip.getTopup()) {
				vip = staticVip;
			} else
				break;
		}
		if (vip != null) {
			return vip.getVip();
		}

		return 0;
	}

	/**
	 * 
	 * Method: getStaticPay
	 * 
	 * @Description: TODO
	 * @param topup
	 *            充值金币
	 * @return
	 * @return StaticPay
	 * @throws
	 */
	public int getExtraGold(int topup, boolean ios) {
		ConfPay category = null;
		if (ios) {
			for (ConfPay staticPay : payIosList) {
				if (topup >= staticPay.getTopup()) {
					category = staticPay;
				} else
					break;
			}
		} else {
			for (ConfPay staticPay : payList) {
				if (topup >= staticPay.getTopup()) {
					category = staticPay;
				} else
					break;
			}
		}

		if (category != null) {
			return category.getExtraGold() * topup / 1000;
		}

		return 0;
	}

}
