package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfStaffingLv;
import data.bean.Lord;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfStaffingLvProvider extends BaseProvider<ConfStaffingLv> {

	public static ConfStaffingLvProvider getInst() {
		return BeanManager.getBean(ConfStaffingLvProvider.class);
	}

	public ConfStaffingLvProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfStaffingLv[]> getClassType() {
		return ConfStaffingLv[].class;
	}

	@Override
	protected ConfStaffingLv convertDBResult2Bean(ASObject o) {
		ConfStaffingLv conf = new ConfStaffingLv();
		conf.setStaffingLv(o.getInt("staffingLv"));
		conf.setExp(o.getInt("exp"));
		conf.setTotalExp(o.getInt("totalExp"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfStaffingLv conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	public boolean addStaffingExp(Lord lord, int add) {
		int lv = lord.getStaffingLv();

		boolean up = false;
		int exp = lord.getStaffingExp() + add;
		while (true) {
			if (lv >= 1000) {
				break;
			}

			ConfStaffingLv staticStaffingLv = getConfigById(lv + 1);
			if (exp >= staticStaffingLv.getExp()) {
				up = true;
				exp -= staticStaffingLv.getExp();
				lv++;
				continue;
			} else {
				break;
			}
		}

		lord.setStaffingLv(lv);
		lord.setStaffingExp(exp);
		return up;
	}

	public int getTotalExp(Lord lord) {
		if (lord.getStaffingLv() == 0) {
			return lord.getStaffingExp();
		}
		return getConfigById(lord.getStaffingLv()).getTotalExp() + lord.getStaffingExp();
	}

	public boolean subStaffingExp(Lord lord, int sub) {
		int lv = lord.getStaffingLv();
		boolean down = false;

		int exp = lord.getStaffingExp() - sub;
		while (true) {
			if (lv < 1) {
				break;
			}

			if (exp < 0) {
				ConfStaffingLv staticStaffingLv = getConfigById(lv);
				exp += staticStaffingLv.getExp();
				lv--;
				down = true;

				if (exp < 0) {
					continue;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		if (exp < 0) {
			exp = 0;
		}

		lord.setStaffingLv(lv);
		lord.setStaffingExp(exp);
		return down;
	}

}
