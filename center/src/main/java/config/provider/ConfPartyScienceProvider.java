package config.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPartyScience;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartyScienceProvider extends BaseProvider<ConfPartyScience> {

	private Map<Integer, Map<Integer, ConfPartyScience>> scienceMap = new HashMap<Integer, Map<Integer, ConfPartyScience>>();

	public static ConfPartyScienceProvider getInst() {
		return BeanManager.getBean(ConfPartyScienceProvider.class);
	}

	public ConfPartyScienceProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPartyScience[]> getClassType() {
		return ConfPartyScience[].class;
	}

	@Override
	protected ConfPartyScience convertDBResult2Bean(ASObject o) {
		ConfPartyScience conf = new ConfPartyScience();
		conf.setKeyId(o.getInt("keyId"));
		conf.setScienceId(o.getInt("scienceId"));
		conf.setScienceLv(o.getInt("scienceLv"));
		conf.setSchedule(o.getInt("schedule"));
		conf.setLockLv(o.getInt("lockLv"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPartyScience conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			int scienceId = e.getScienceId();
			int scienceLv = e.getScienceLv();
			Map<Integer, ConfPartyScience> tempMap = scienceMap.get(scienceId);
			if (tempMap == null) {
				tempMap = new HashMap<Integer, ConfPartyScience>();
				scienceMap.put(scienceId, tempMap);
			}
			tempMap.put(scienceLv, e);
		});
	}

	public ConfPartyScience getPartyScience(int scienceId, int level) {
		return scienceMap.get(scienceId).get(level);
	}

	public List<ConfPartyScience> getInitScience() {
		List<ConfPartyScience> rs = new ArrayList<ConfPartyScience>();
		Iterator<Map<Integer, ConfPartyScience>> it = scienceMap.values().iterator();
		while (it.hasNext()) {
			Map<Integer, ConfPartyScience> map = it.next();
			rs.add(map.get(0));
		}
		return rs;
	}

}
