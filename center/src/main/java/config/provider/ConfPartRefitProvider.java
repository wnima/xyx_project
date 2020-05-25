package config.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPartRefit;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartRefitProvider extends BaseProvider<ConfPartRefit> {

	private Map<Integer, Map<Integer, ConfPartRefit>> refitMap = new HashMap<Integer, Map<Integer, ConfPartRefit>>();

	public static ConfPartRefitProvider getInst() {
		return BeanManager.getBean(ConfPartRefitProvider.class);
	}

	public ConfPartRefitProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPartRefit[]> getClassType() {
		return ConfPartRefit[].class;
	}

	@Override
	protected ConfPartRefit convertDBResult2Bean(ASObject o) {
		ConfPartRefit conf = new ConfPartRefit();
		conf.setQuality(o.getInt("quality"));
		conf.setLv(o.getInt("lv"));
		conf.setFitting(o.getInt("fitting"));
		conf.setPlan(o.getInt("plan"));
		conf.setMineral(o.getInt("mineral"));
		conf.setTool(o.getInt("tool"));
		conf.setFittingExplode(o.getInt("fittingExplode"));
		conf.setPlanExplode(o.getInt("planExplode"));
		conf.setMineralExplode(o.getInt("mineralExplode"));
		conf.setToolExplode(o.getInt("toolExplode"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPartRefit conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			Map<Integer, ConfPartRefit> map = refitMap.get(e.getQuality());
			if (map == null) {
				map = new HashMap<>();
				refitMap.put(e.getQuality(), map);
			}

			map.put(e.getLv(), e);
		});
	}

	public ConfPartRefit getStaticPartRefit(int quality, int refitLv) {
		Map<Integer, ConfPartRefit> map = refitMap.get(quality);
		if (map != null) {
			return map.get(refitLv);
		}
		return null;
	}
}
