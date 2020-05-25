package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfMineLv;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfMineLvProvider extends BaseProvider<ConfMineLv> {

	public static ConfMineLvProvider getInst() {
		return BeanManager.getBean(ConfMineLvProvider.class);
	}

	public ConfMineLvProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfMineLv[]> getClassType() {
		return ConfMineLv[].class;
	}

	@Override
	protected ConfMineLv convertDBResult2Bean(ASObject o) {
		ConfMineLv conf = new ConfMineLv();
		conf.setLv(o.getInt("lv"));
		conf.setProduction(o.getInt("production"));
		conf.setExp(o.getInt("exp"));
		conf.setStaffingExp(o.getInt("staffingExp"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfMineLv conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
