package com.game.dataMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.bean.ConfMail;
import config.bean.ConfMailPlat;

/**
 * done
 * @author Administrator
 *
 */
public class StaticMailDataMgr extends BaseDataMgr {

	private Map<Integer, ConfMail> mailMap = new HashMap<Integer, ConfMail>();

	private Map<Integer, List<ConfMailPlat>> mailPlatMap = new HashMap<Integer, List<ConfMailPlat>>();

	@Override
	public void init() {
//		mailMap = staticDataDao.selectMail();
//
//		List<StaticMailPlat> mailPlatList = staticDataDao.selectStaticMailPlat();
//		for (StaticMailPlat e : mailPlatList) {
//			List<StaticMailPlat> elist = mailPlatMap.get(e.getPlatNo());
//			if (elist == null) {
//				elist = new ArrayList<StaticMailPlat>();
//				mailPlatMap.put(e.getPlatNo(), elist);
//			}
//			elist.add(e);
//		}
	}

	public ConfMail getStaticMail(int moldId) {
		return mailMap.get(moldId);
	}

	public List<ConfMailPlat> getPlatMail(int platNo) {
		return mailPlatMap.get(platNo);
	}

}
