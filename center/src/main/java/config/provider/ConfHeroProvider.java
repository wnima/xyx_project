package config.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfHero;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfHeroProvider extends BaseProvider<ConfHero> {

	private Map<Integer, List<ConfHero>> starMapList = new HashMap<Integer, List<ConfHero>>();

	private Map<Integer, List<ConfHero>> starLvMapList = new HashMap<Integer, List<ConfHero>>();

	public static ConfHeroProvider getInst() {
		return BeanManager.getBean(ConfHeroProvider.class);
	}

	public ConfHeroProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfHero[]> getClassType() {
		return ConfHero[].class;
	}

	@Override
	protected ConfHero convertDBResult2Bean(ASObject o) {
		ConfHero conf = new ConfHero();
		conf.setHeroId(o.getInt("heroId"));
		conf.setHeroName(o.getString("heroName"));
		conf.setType(o.getInt("type"));
		conf.setStar(o.getInt("star"));
		conf.setLevel(o.getInt("level"));
		conf.setHeroAdditionId(o.getInt("heroAdditionId"));
		conf.setSkillId(o.getInt("skillId"));
		conf.setSkillValue(o.getInt("skillValue"));
		conf.setResolveId(o.getInt("resolveId"));
		conf.setSoul(o.getInt("soul"));
		conf.setCanup(o.getInt("canup"));
		conf.setMeta(JSON.parseObject(o.getString("meta"), List.class));
		conf.setAttr(JSON.parseObject(o.getString("attr"), List.class));
		conf.setTankCount(o.getInt("tankCount"));
		conf.setOrder(o.getInt("order"));
		conf.setCompound(o.getInt("compound"));
		conf.setProbability(o.getInt("probability"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfHero conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		for (ConfHero e : getAllConfig()) {
			int star = e.getStar();
			List<ConfHero> llList = starMapList.get(star);
			if (llList == null) {
				llList = new ArrayList<ConfHero>();
				starMapList.put(star, llList);
			}

			llList.add(e);

			if (e.getLevel() != 0 || e.getCompound() != 0) {
				continue;
			}
			List<ConfHero> llLvList = starLvMapList.get(star);
			if (llLvList == null) {
				llLvList = new ArrayList<ConfHero>();
				starLvMapList.put(star, llLvList);
			}
			llLvList.add(e);
		}
	}
	
	public List<ConfHero> getStarList(int star) {
		return starMapList.get(star);
	}

	public List<ConfHero> getStarListLv(int star) {
		return starLvMapList.get(star);
	}

	public int costSoul(int star) {
		if (star < 1 || star > 4) {
			return 0;
		}
		return starMapList.get(star).get(0).getSoul();
	}

}
