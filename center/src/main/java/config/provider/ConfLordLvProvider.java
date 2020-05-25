package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfLordLv;
import data.bean.Lord;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfLordLvProvider extends BaseProvider<ConfLordLv> {

	public static ConfLordLvProvider getInst() {
		return BeanManager.getBean(ConfLordLvProvider.class);
	}

	public ConfLordLvProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfLordLv[]> getClassType() {
		return ConfLordLv[].class;
	}

	@Override
	protected ConfLordLv convertDBResult2Bean(ASObject o) {
		ConfLordLv conf = new ConfLordLv();
		conf.setLordLv(o.getInt("lordLv"));
		conf.setNeedExp(o.getInt("needExp"));
		conf.setTankCount(o.getInt("tankCount"));
		conf.setBlessExp(o.getInt("blessExp"));
		conf.setWinPros(o.getInt("winPros"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfLordLv conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	public boolean addExp(Lord lord, int add) {
		int lv = lord.getLevel();

		boolean up = false;
		int exp = lord.getExp() + add;
		while (true) {
			if (lv >= 80) {
				break;
			}

			ConfLordLv staticLordLv = getConfigById(lv + 1);
			if (exp >= staticLordLv.getNeedExp()) {
				up = true;
				exp -= staticLordLv.getNeedExp();
				lv++;
				continue;
			} else {
				break;
			}
		}

		lord.setLevel(lv);
		lord.setExp(exp);
		return up;
	}
}
