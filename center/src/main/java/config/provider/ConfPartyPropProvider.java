package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPartyProp;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartyPropProvider extends BaseProvider<ConfPartyProp> {

	public static ConfPartyPropProvider getInst() {
		return BeanManager.getBean(ConfPartyPropProvider.class);
	}

	public ConfPartyPropProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPartyProp[]> getClassType() {
		return ConfPartyProp[].class;
	}

	@Override
	protected ConfPartyProp convertDBResult2Bean(ASObject o) {
		ConfPartyProp conf = new ConfPartyProp();
		conf.setKeyId(o.getInt("keyId"));
		conf.setTreasure(o.getInt("treasure"));
		conf.setItemType(o.getInt("itemType"));
		conf.setItemId(o.getInt("itemId"));
		conf.setItemNum(o.getInt("itemNum"));
		conf.setCount(o.getInt("count"));
		conf.setPartyLv(o.getInt("partyLv"));
		conf.setContribute(o.getInt("contribute"));
		conf.setProbability(o.getInt("probability"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPartyProp conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}



}
