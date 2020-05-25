package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfIniLord;
import inject.BeanManager;
import util.ASObject;
import util.MapUtil;

@Singleton
public class ConfIniLordProvider extends BaseProvider<ConfIniLord> {

	public static ConfIniLordProvider getInst() {
		return BeanManager.getBean(ConfIniLordProvider.class);
	}

	public ConfIniLordProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfIniLord[]> getClassType() {
		return ConfIniLord[].class;
	}

	@Override
	protected ConfIniLord convertDBResult2Bean(ASObject o) {
		ConfIniLord conf = new ConfIniLord();
		conf.setKeyId(o.getInt("keyId"));
		conf.setLevel(o.getInt("level"));
		conf.setVip(o.getInt("vip"));
		conf.setGoldGive(o.getInt("goldGive"));
		conf.setRanks(o.getInt("ranks"));
		conf.setCommand(o.getInt("command"));
		conf.setFameLv(o.getInt("fameLv"));
		conf.setHonour(o.getInt("honour"));
		conf.setCombat(o.getInt("combat"));
		conf.setElite(o.getInt("elite"));
		conf.setPower(o.getInt("power"));
		conf.setNewState(o.getInt("newState"));
		conf.setProsMax(o.getInt("prosMax"));
		conf.setTanks(MapUtil.convert(o.getString("tanks"), Integer.class, Integer.class));
		conf.setStone(o.getInt("stone"));
		conf.setIron(o.getInt("iron"));
		conf.setOil(o.getInt("oil"));
		conf.setCopper(o.getInt("copper"));
		conf.setSilicon(o.getInt("silicon"));
		conf.setProps(MapUtil.convert(o.getString("props"), Integer.class, Integer.class));
		conf.setEquips(MapUtil.convert(o.getString("equips"), Integer.class, Integer.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfIniLord conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
