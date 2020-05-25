package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPartyCombat;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartyCombatProvider extends BaseProvider<ConfPartyCombat> {

	public static ConfPartyCombatProvider getInst() {
		return BeanManager.getBean(ConfPartyCombatProvider.class);
	}

	public ConfPartyCombatProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPartyCombat[]> getClassType() {
		return ConfPartyCombat[].class;
	}

	@Override
	protected ConfPartyCombat convertDBResult2Bean(ASObject o) {
		ConfPartyCombat conf = new ConfPartyCombat();
		conf.setCombatId(o.getInt("combatId"));
		conf.setSectionId(o.getInt("sectionId"));
		conf.setName(o.getString("name"));
		conf.setExp(o.getInt("exp"));
		conf.setContribute(o.getInt("contribute"));
		conf.setDrop(JSON.parseObject(o.getString("drop"), List.class));
		conf.setLastAward(JSON.parseObject(o.getString("lastAward"), List.class));
		conf.setForm(JSON.parseObject(o.getString("form"), List.class));
		conf.setAttr(JSON.parseObject(o.getString("attr"), List.class));
		conf.setBox(JSON.parseObject(o.getString("box"), List.class));
		conf.setTotalTank(o.getInt("totalTank"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPartyCombat conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
