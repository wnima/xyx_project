package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPart;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartProvider extends BaseProvider<ConfPart> {

	public static ConfPartProvider getInst() {
		return BeanManager.getBean(ConfPartProvider.class);
	}

	public ConfPartProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPart[]> getClassType() {
		return ConfPart[].class;
	}

	@Override
	protected ConfPart convertDBResult2Bean(ASObject o) {
		ConfPart conf = new ConfPart();
		conf.setPartId(o.getInt("partId"));
		conf.setType(o.getInt("type"));
		conf.setQuality(o.getInt("quality"));
		conf.setAttr1(o.getInt("attr1"));
		conf.setAttr2(o.getInt("attr2"));
		conf.setA1(o.getInt("a1"));
		conf.setB1(o.getInt("b1"));
		conf.setA2(o.getInt("a2"));
		conf.setB2(o.getInt("b2"));
		conf.setChipCount(o.getInt("chipCount"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPart conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
