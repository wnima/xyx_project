package com.game.dataMgr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.bean.ConfFunctionPlan;

public class StaticFunctionPlanDataMgr extends BaseDataMgr {


	private int dayiy;

	private Map<Integer, ConfFunctionPlan> planMap = new HashMap<Integer, ConfFunctionPlan>();

	@Override
	public void init() {
//		Date openTime = DateHelper.parseDate(serverSetting.getOpenTime());
//		this.dayiy = DateHelper.dayiy(openTime, new Date());
//
//		List<StaticFunctionPlan> list = staticDataDao.selectFunctionPlan();
//		for (StaticFunctionPlan plan : list) {
//			int funId = plan.getFunId();
//			String rules = plan.getRules().trim();
//			if (rules == null || rules.equals("")) {
//				continue;
//			}
//			if (rules.equals("*")) {
//				plan.setWhole(1);
//			} else if (rules.equals("0")) {
//				plan.setWhole(-1);
//			} else {
//				plan.setWhole(0);
//				String[] rule = rules.split(",");
//				for (int i = 0; i < rule.length; i++) {
//					if (rule[i].indexOf("-") < 0) {
//						continue;
//					}
//					String sids[] = rule[i].split("-");
//					if (sids.length < 2) {
//						continue;
//					}
//					int sidB = Integer.parseInt(sids[0].trim());
//					int sidE = Integer.parseInt(sids[1].trim());
//					for (int j = sidB; j <= sidE; j++) {
//						plan.getServerList().add(j);
//					}
//				}
//			}
//			planMap.put(funId, plan);
//		}
	}

	/**
	 * 过滤是否开启
	 * 
	 * @param functionName
	 * @return
	 */
	public boolean filter(int funId) {
//		if (!planMap.containsKey(funId)) {
//			return true;
//		}
//		StaticFunctionPlan plan = planMap.get(funId);
//		if (plan.getWhole() == -1) {
//			return false;
//		}
//		if (dayiy < plan.getDayiy()) {
//			return false;
//		}
//		if (plan.getWhole() == 1) {
//			return true;
//		}
//		int serverId = serverSetting.getServerID();
//		List<Integer> serverList = plan.getServerList();
//		if (serverList.contains(serverId)) {
//			return true;
//		}
		return false;
	}
}
