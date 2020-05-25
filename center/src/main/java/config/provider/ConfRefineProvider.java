package config.provider;

import java.util.Iterator;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfRefine;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfRefineProvider extends BaseProvider<ConfRefine> {

	public static ConfRefineProvider getInst() {
		return BeanManager.getBean(ConfRefineProvider.class);
	}

	public ConfRefineProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfRefine[]> getClassType() {
		return ConfRefine[].class;
	}

	@Override
	protected ConfRefine convertDBResult2Bean(ASObject o) {
		ConfRefine conf = new ConfRefine();
		conf.setRefineId(o.getInt("refineId"));
		conf.setRefineName(o.getString("refineName"));
		conf.setRefineType(o.getInt("refineType"));
		conf.setBuildId(o.getInt("buildId"));
		conf.setType(o.getInt("type"));
		conf.setAttributeId(o.getInt("attributeId"));
		conf.setExp(o.getInt("exp"));
		conf.setCapacity(o.getInt("capacity"));
		conf.setProtect(o.getInt("protect"));
		conf.setSpeed(o.getInt("speed"));
		conf.setAddtion(o.getInt("addtion"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfRefine conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	public ConfRefine getRefineBuild(int buildingId) {
		Iterator<ConfRefine> it = getAllConfig().iterator();
		while (it.hasNext()) {
			ConfRefine next = it.next();
			if (next.getBuildId() == buildingId) {
				return next;
			}
		}
		return null;
	}

	public ConfRefine getRefineCapacity() {
		Iterator<ConfRefine> it = getAllConfig().iterator();
		while (it.hasNext()) {
			ConfRefine next = it.next();
			if (next.getCapacity() == 1) {
				return next;
			}
		}
		return null;
	}
}
