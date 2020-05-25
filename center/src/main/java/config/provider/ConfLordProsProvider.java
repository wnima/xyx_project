package config.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfLordPros;
import inject.BeanManager;
import util.ASObject;

/**
 * 繁荣度
 * 
 * @author Administrator
 *
 */
@Singleton
public class ConfLordProsProvider extends BaseProvider<ConfLordPros> {

	private Map<Integer, ConfLordPros> prosLvMap = new HashMap<Integer, ConfLordPros>();

	public static ConfLordProsProvider getInst() {
		return BeanManager.getBean(ConfLordProsProvider.class);
	}

	public ConfLordProsProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfLordPros[]> getClassType() {
		return ConfLordPros[].class;
	}

	@Override
	protected ConfLordPros convertDBResult2Bean(ASObject o) {
		ConfLordPros conf = new ConfLordPros();
		conf.setProsLv(o.getInt("prosLv"));
		conf.setProsExp(o.getInt("prosExp"));
		conf.setTankCount(o.getInt("tankCount"));
		conf.setStaffingAdd(o.getInt("staffingAdd"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfLordPros conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			prosLvMap.put(e.getProsLv(), e);
		});
	}

	public ConfLordPros getStaticProsLv(int pros) {
		ConfLordPros lv = null;
		for (ConfLordPros staticProsLv : getAllConfig()) {
			if (pros >= staticProsLv.getProsExp()) {
				lv = staticProsLv;
			} else
				break;
		}
		return lv;
	}

}
