package config.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfCost;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfCostProvider extends BaseProvider<ConfCost> {

	private Map<Integer, List<ConfCost>> costMap = new HashMap<Integer, List<ConfCost>>();

	public static ConfCostProvider getInst() {
		return BeanManager.getBean(ConfCostProvider.class);
	}

	public ConfCostProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfCost[]> getClassType() {
		return ConfCost[].class;
	}

	@Override
	protected ConfCost convertDBResult2Bean(ASObject o) {
		ConfCost conf = new ConfCost();
		conf.setCostId(o.getInt("costId"));
		conf.setCount(o.getInt("count"));
		conf.setType(o.getInt("type"));
		conf.setPrice(o.getInt("price"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfCost conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			int costId = e.getCostId();
			List<ConfCost> costll = costMap.get(costId);
			if (costll == null) {
				costll = new ArrayList<ConfCost>();
				costMap.put(costId, costll);
			}
			costll.add(e);
		});
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
