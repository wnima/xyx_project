package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfProp;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPropProvider extends BaseProvider<ConfProp> {

	public static ConfPropProvider getInst() {
		return BeanManager.getBean(ConfPropProvider.class);
	}

	public ConfPropProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfProp[]> getClassType() {
		return ConfProp[].class;
	}

	@Override
	protected ConfProp convertDBResult2Bean(ASObject o) {
		ConfProp conf = new ConfProp();
		conf.setPropId(o.getInt("propId"));
		conf.setPrice(o.getInt("price"));
		conf.setEffectType(o.getInt("effectType"));
		conf.setEffectValue(JSON.parseObject(o.getString("effectValue"), List.class));
		conf.setTag(o.getInt("tag"));
		conf.setColor(o.getInt("color"));
		conf.setCanBuy(o.getInt("canBuy"));
		conf.setCanBuild(o.getInt("canBuild"));
		conf.setStoneCost(o.getInt("stoneCost"));
		conf.setSkillBook(o.getInt("skillBook"));
		conf.setHeroChip(o.getInt("heroChip"));
		conf.setBuildTime(o.getInt("buildTime"));
		conf.setCanUse(o.getInt("canUse"));
		conf.setArenaScore(o.getInt("arenaScore"));
		conf.setPropName(o.getString("propName"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfProp conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
