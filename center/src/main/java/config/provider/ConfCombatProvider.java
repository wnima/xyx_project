package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfCombat;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfCombatProvider extends BaseProvider<ConfCombat> {

	public static ConfCombatProvider getInst() {
		return BeanManager.getBean(ConfCombatProvider.class);
	}

	public ConfCombatProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfCombat[]> getClassType() {
		return ConfCombat[].class;
	}

	@Override
	protected ConfCombat convertDBResult2Bean(ASObject o) {
		ConfCombat conf = new ConfCombat();
		conf.setCombatId(o.getInt("combatId"));
		conf.setSectionId(o.getInt("sectionId"));
		conf.setExp(o.getInt("exp"));
		conf.setDrop(JSON.parseObject(o.getString("drop"), List.class));
		conf.setFirstAward(JSON.parseObject(o.getString("firstAward"), List.class));
		conf.setForm(JSON.parseObject(o.getString("form"), List.class));
		conf.setAttr(JSON.parseObject(o.getString("attr"), List.class));
		conf.setPreId(o.getInt("preId"));
		conf.setHero(JSON.parseObject(o.getString("hero"), List.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfCombat conf) {
		ASObject o = new ASObject();
//		o.put("buildingId", conf.getBuildingId());
//		o.put("name", conf.getName());
//		o.put("type", conf.getType());
//		o.put("canUp", conf.getCanUp());
//		o.put("canDestory", conf.getCanDestory());
//		o.put("initLv", conf.getInitLv());
//		o.put("pros", conf.getPros());
//		o.put("canResource", conf.getCanResource());
		return o;
	}

	@Override
	public void init() {
	}

}
