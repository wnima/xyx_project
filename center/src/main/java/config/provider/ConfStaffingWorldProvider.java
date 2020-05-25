package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfStaffingWorld;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfStaffingWorldProvider extends BaseProvider<ConfStaffingWorld> {

	public static ConfStaffingWorldProvider getInst() {
		return BeanManager.getBean(ConfStaffingWorldProvider.class);
	}

	public ConfStaffingWorldProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfStaffingWorld[]> getClassType() {
		return ConfStaffingWorld[].class;
	}

	@Override
	protected ConfStaffingWorld convertDBResult2Bean(ASObject o) {
		ConfStaffingWorld conf = new ConfStaffingWorld();
		conf.setSumStaffing(o.getInt("sumStaffing"));
		conf.setHaust(o.getInt("haust"));
		conf.setWorldLv(o.getInt("worldLv"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfStaffingWorld conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	public ConfStaffingWorld calcWolrdLv(int totalLv) {
		ConfStaffingWorld staticStaffingWorld = null;
		int worldLv = 0;
		while (true) {
			if (worldLv >= 10) {
				break;
			}

			ConfStaffingWorld world = getConfigById(worldLv);
			if (totalLv >= world.getSumStaffing()) {
				worldLv++;
				staticStaffingWorld = world;
				continue;
			} else {
				break;
			}
		}

		return staticStaffingWorld;
	}
}
