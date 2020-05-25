package config.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfEquipLv;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfEquipLvProvider extends BaseProvider<ConfEquipLv> {

	private Map<Integer, Map<Integer, ConfEquipLv>> levelMap = new HashMap<Integer, Map<Integer, ConfEquipLv>>();

	public static ConfEquipLvProvider getInst() {
		return BeanManager.getBean(ConfEquipLvProvider.class);
	}

	public ConfEquipLvProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfEquipLv[]> getClassType() {
		return ConfEquipLv[].class;
	}

	@Override
	protected ConfEquipLv convertDBResult2Bean(ASObject o) {
		ConfEquipLv conf = new ConfEquipLv();
		conf.setQuality(o.getInt("quality"));
		conf.setLevel(o.getInt("level"));
		conf.setNeedExp(o.getInt("needExp"));
		conf.setGiveExp(o.getInt("giveExp"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfEquipLv conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			Map<Integer, ConfEquipLv> qualityMap = levelMap.get(e.getQuality());
			if (qualityMap == null) {
				qualityMap = new HashMap<>();
				levelMap.put(e.getQuality(), qualityMap);
			}
			qualityMap.put(e.getLevel(), e);
		});
	}

	public ConfEquipLv getConfEquipLv(int quality, int lv) {
		Map<Integer, ConfEquipLv> map = levelMap.get(quality);
		if (map != null) {
			return map.get(lv);
		}

		return null;
	}

}
