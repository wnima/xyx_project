package com.game.dataMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.bean.ConfCost;

/**
 * done
 * 
 * @author Administrator
 *
 */
public class StaticCostDataMgr extends BaseDataMgr {

	private Map<Integer, List<ConfCost>> costMap = new HashMap<Integer, List<ConfCost>>();

	@Override
	public void init() {
//		List<StaticCost> costList = staticDataDao.selectCost();
//		for (StaticCost staticCost : costList) {
//			int costId = staticCost.getCostId();
//			List<StaticCost> costll = costMap.get(costId);
//			if (costll == null) {
//				costll = new ArrayList<StaticCost>();
//				costMap.put(costId, costll);
//			}
//			costll.add(staticCost);
//		}
	}

	public ConfCost getCost(int costId, int count, int number) {
		List<ConfCost> costList = costMap.get(costId);
		ConfCost rs = new ConfCost();
		int maxCount = 0;
		int costCount = 0;
		ConfCost maxCost = null;
		for (ConfCost staticCost : costList) {
			int temp = staticCost.getCount();
			if (temp > maxCount) {
				maxCount = temp;
				maxCost = staticCost;
			}
			if (temp >= count && temp < count + number) {
				rs.setPrice(rs.getPrice() + staticCost.getPrice());
				costCount++;
			}
		}
		if (maxCount != 0 && number - costCount > 0) {
			rs.setPrice(rs.getPrice() + maxCost.getPrice() * (number - costCount));
		}
		return rs;
	}

	public ConfCost getCost(int costId, int count) {
		List<ConfCost> costList = costMap.get(costId);
		for (ConfCost staticCost : costList) {
			if (staticCost.getCount() == count) {
				return staticCost;
			}
		}
		return costList.get(costList.size() - 1);
	}
}
