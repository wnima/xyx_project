package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfExplore;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfExploreProvider extends BaseProvider<ConfExplore> {

	public static ConfExploreProvider getInst() {
		return BeanManager.getBean(ConfExploreProvider.class);
	}

	public ConfExploreProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfExplore[]> getClassType() {
		return ConfExplore[].class;
	}

	@Override
	protected ConfExplore convertDBResult2Bean(ASObject o) {
		ConfExplore conf = new ConfExplore();
		conf.setExploreId(o.getInt("exploreId"));
		conf.setType(o.getInt("type"));
		conf.setDrop(JSON.parseObject(o.getString("drop"), List.class));
		conf.setForm(JSON.parseObject(o.getString("form"), List.class));
		conf.setHero(JSON.parseObject(o.getString("hero"), List.class));
		conf.setAttr(JSON.parseObject(o.getString("attr"), List.class));
		conf.setPreId(o.getInt("preId"));
		conf.setDropOne(JSON.parseObject(o.getString("dropOne"), List.class));
		conf.setWeight(o.getInt("weight"));
		conf.setDropMaterial(JSON.parseObject(o.getString("dropMaterial"), List.class));
		conf.setExp(o.getInt("exp"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfExplore conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
