package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfScout;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfScoutProvider extends BaseProvider<ConfScout> {

	public static ConfScoutProvider getInst() {
		return BeanManager.getBean(ConfScoutProvider.class);
	}

	public ConfScoutProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfScout[]> getClassType() {
		return ConfScout[].class;
	}

	@Override
	protected ConfScout convertDBResult2Bean(ASObject o) {
		ConfScout conf = new ConfScout();
		conf.setLv(o.getInt("lv"));
		conf.setScoutCost(o.getInt("scoutCost"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfScout conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
