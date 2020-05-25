package config.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPartUp;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartUpProvider extends BaseProvider<ConfPartUp> {

	private Map<Integer, Map<Integer, ConfPartUp>> upMap = new HashMap<Integer, Map<Integer, ConfPartUp>>();

	public static ConfPartUpProvider getInst() {
		return BeanManager.getBean(ConfPartUpProvider.class);
	}

	public ConfPartUpProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPartUp[]> getClassType() {
		return ConfPartUp[].class;
	}

	@Override
	protected ConfPartUp convertDBResult2Bean(ASObject o) {
		ConfPartUp conf = new ConfPartUp();
		conf.setPartId(o.getInt("partId"));
		conf.setLv(o.getInt("lv"));
		conf.setProb(o.getInt("prob"));
		conf.setStone(o.getInt("stone"));
		conf.setStoneExplode(o.getInt("stoneExplode"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPartUp conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			Map<Integer, ConfPartUp> map = upMap.get(e.getPartId());
			if (map == null) {
				map = new HashMap<>();
				upMap.put(e.getPartId(), map);
			}

			map.put(e.getLv(), e);
		});
	}

	public ConfPartUp getStaticPartUp(int partId, int upLv) {
		Map<Integer, ConfPartUp> map = upMap.get(partId);
		if (map != null) {
			return map.get(upLv);
		}
		return null;
	}
}
