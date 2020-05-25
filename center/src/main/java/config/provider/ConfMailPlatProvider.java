package config.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfMailPlat;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfMailPlatProvider extends BaseProvider<ConfMailPlat> {

	private Map<Integer, List<ConfMailPlat>> mailPlatMap = new HashMap<Integer, List<ConfMailPlat>>();

	public static ConfMailPlatProvider getInst() {
		return BeanManager.getBean(ConfMailPlatProvider.class);
	}

	public ConfMailPlatProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfMailPlat[]> getClassType() {
		return ConfMailPlat[].class;
	}

	@Override
	protected ConfMailPlat convertDBResult2Bean(ASObject o) {
		ConfMailPlat conf = new ConfMailPlat();
		conf.setPlatNo(o.getInt("platNo"));
		conf.setMailId(o.getInt("mailId"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfMailPlat conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			List<ConfMailPlat> elist = mailPlatMap.get(e.getPlatNo());
			if (elist == null) {
				elist = new ArrayList<ConfMailPlat>();
				mailPlatMap.put(e.getPlatNo(), elist);
			}
			elist.add(e);
		});
	}

}
