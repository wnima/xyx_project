package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfStaffing;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfStaffingProvider extends BaseProvider<ConfStaffing> {

	public static ConfStaffingProvider getInst() {
		return BeanManager.getBean(ConfStaffingProvider.class);
	}

	public ConfStaffingProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfStaffing[]> getClassType() {
		return ConfStaffing[].class;
	}

	@Override
	protected ConfStaffing convertDBResult2Bean(ASObject o) {
		ConfStaffing conf = new ConfStaffing();
		conf.setStaffingId(o.getInt("staffingId"));
		conf.setName(o.getString("name"));
		conf.setRank(o.getInt("rank"));
		conf.setStaffingLv(o.getInt("staffingLv"));
		conf.setCountLimit(o.getInt("countLimit"));
		conf.setAttr(JSON.parseObject(o.getString("attr"), List.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfStaffing conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	/**
	 * 
	 * Method: calcStaffing
	 * 
	 * @Description: 不考虑人数限制时，应该可以得到的称号 @param lv @param ranks @return @return
	 *               StaticStaffing @throws
	 */
	public ConfStaffing calcStaffing(int lv, int ranks) {
		ConfStaffing staticStaffing = null;
		int id = 1;
		while (id <= 11) {
			ConfStaffing staffing = getConfigById(id);
			if (lv >= staffing.getStaffingLv() && ranks >= staffing.getRank()) {
				staticStaffing = staffing;
				id++;
				continue;
			} else {
				break;
			}
		}

		return staticStaffing;
	}
}
