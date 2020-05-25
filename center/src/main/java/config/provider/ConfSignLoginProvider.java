package config.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.util.RandomHelper;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfSignLogin;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfSignLoginProvider extends BaseProvider<ConfSignLogin> {

	private Map<Integer, List<ConfSignLogin>> signLoginMap = new HashMap<Integer, List<ConfSignLogin>>();

	public static ConfSignLoginProvider getInst() {
		return BeanManager.getBean(ConfSignLoginProvider.class);
	}

	public ConfSignLoginProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfSignLogin[]> getClassType() {
		return ConfSignLogin[].class;
	}

	@Override
	protected ConfSignLogin convertDBResult2Bean(ASObject o) {
		ConfSignLogin conf = new ConfSignLogin();
		conf.setLoginId(o.getInt("loginId"));
		conf.setGrid(o.getInt("grid"));
		conf.setType(o.getInt("type"));
		conf.setItemId(o.getInt("itemId"));
		conf.setCount(o.getInt("count"));
		conf.setProbability(o.getInt("probability"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfSignLogin conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			int grid = e.getGrid();
			List<ConfSignLogin> loginList = signLoginMap.get(grid);
			if (loginList == null) {
				loginList = new ArrayList<ConfSignLogin>();
				signLoginMap.put(grid, loginList);
			}
			loginList.add(e);
		});
	}

	public ConfSignLogin getSignLoginByGrid(int grid) {
		List<ConfSignLogin> list = signLoginMap.get(grid);
		int seeds[] = { 0, 0 };
		for (ConfSignLogin e : list) {
			seeds[0] += e.getProbability();
		}
		seeds[0] = RandomHelper.randomInSize(seeds[0]);
		for (ConfSignLogin e : list) {
			seeds[1] += e.getProbability();
			if (seeds[0] <= seeds[1]) {
				return e;
			}
		}
		return null;
	}

}
