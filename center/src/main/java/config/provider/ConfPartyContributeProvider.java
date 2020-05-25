package config.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPartyContribute;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartyContributeProvider extends BaseProvider<ConfPartyContribute> {

	private Map<Integer, Map<Integer, ConfPartyContribute>> contributeMap = new HashMap<Integer, Map<Integer, ConfPartyContribute>>();

	public static ConfPartyContributeProvider getInst() {
		return BeanManager.getBean(ConfPartyContributeProvider.class);
	}

	public ConfPartyContributeProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPartyContribute[]> getClassType() {
		return ConfPartyContribute[].class;
	}

	@Override
	protected ConfPartyContribute convertDBResult2Bean(ASObject o) {
		ConfPartyContribute conf = new ConfPartyContribute();
		conf.setKeyId(o.getInt("keyId"));
		conf.setType(o.getInt("type"));
		conf.setCount(o.getInt("count"));
		conf.setBuild(o.getInt("build"));
		conf.setPrice(o.getInt("price"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPartyContribute conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			int resourceId = e.getType();
			int count = e.getCount();
			Map<Integer, ConfPartyContribute> tempMap = contributeMap.get(resourceId);
			if (tempMap == null) {
				tempMap = new HashMap<Integer, ConfPartyContribute>();
				contributeMap.put(resourceId, tempMap);
			}
			tempMap.put(count, e);
		});
	}

	public ConfPartyContribute getStaticContribute(int resourceId, int count) {
		return contributeMap.get(resourceId).get(count);
	}

}
