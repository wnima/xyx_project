package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfParty;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartyProvider extends BaseProvider<ConfParty> {

	public static ConfPartyProvider getInst() {
		return BeanManager.getBean(ConfPartyProvider.class);
	}

	public ConfPartyProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfParty[]> getClassType() {
		return ConfParty[].class;
	}

	@Override
	protected ConfParty convertDBResult2Bean(ASObject o) {
		ConfParty conf = new ConfParty();
		conf.setPartyLv(o.getInt("partyLv"));
		conf.setPartyNum(o.getInt("partyNum"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfParty conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	public int getLvNum(int lv) {
		ConfParty conf = getConfigById(lv);
		if (conf == null) {
			return 0;
		}
		return conf.getPartyNum();
	}
}
