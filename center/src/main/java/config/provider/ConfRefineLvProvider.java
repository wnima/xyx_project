package config.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfRefineLv;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfRefineLvProvider extends BaseProvider<ConfRefineLv> {

	private Map<Integer, Map<Integer, ConfRefineLv>> refineLvMap = new HashMap<Integer, Map<Integer, ConfRefineLv>>();

	public static ConfRefineLvProvider getInst() {
		return BeanManager.getBean(ConfRefineLvProvider.class);
	}

	public ConfRefineLvProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfRefineLv[]> getClassType() {
		return ConfRefineLv[].class;
	}

	@Override
	protected ConfRefineLv convertDBResult2Bean(ASObject o) {
		ConfRefineLv conf = new ConfRefineLv();
		conf.setRefineId(o.getInt("refineId"));
		conf.setLevel(o.getInt("level"));
		conf.setDesc(o.getString("desc"));
		conf.setScienceLv(o.getInt("scienceLv"));
		conf.setFameLv(o.getInt("fameLv"));
		conf.setIronCost(o.getInt("ironCost"));
		conf.setOilCost(o.getInt("oilCost"));
		conf.setCopperCost(o.getInt("copperCost"));
		conf.setGoldCost(o.getInt("goldCost"));
		conf.setSilionCost(o.getInt("silionCost"));
		conf.setSpeed(o.getInt("speed"));
		conf.setUpTime(o.getInt("upTime"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfRefineLv conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			int refineId = e.getRefineId();
			int level = e.getLevel();
			Map<Integer, ConfRefineLv> levelMap = refineLvMap.get(refineId);
			if (levelMap == null) {
				levelMap = new HashMap<Integer, ConfRefineLv>();
				refineLvMap.put(refineId, levelMap);
			}
			levelMap.put(level, e);
		});
	}

	public ConfRefineLv getStaticRefineLv(int refineId, int level) {
		return refineLvMap.get(refineId).get(level);
	}

}
