package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfVip;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfVipProvider extends BaseProvider<ConfVip> {

	public static ConfVipProvider getInst() {
		return BeanManager.getBean(ConfVipProvider.class);
	}

	public ConfVipProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfVip[]> getClassType() {
		return ConfVip[].class;
	}

	@Override
	protected ConfVip convertDBResult2Bean(ASObject o) {
		ConfVip conf = new ConfVip();
		conf.setVip(o.getInt("vip"));
		conf.setTopup(o.getInt("topup"));
		conf.setBuyPower(o.getInt("buyPower"));
		conf.setBuyArena(o.getInt("buyArena"));
		conf.setWaitQue(o.getInt("waitQue"));
		conf.setBuildQue(o.getInt("buildQue"));
		conf.setResetDaily(o.getInt("resetDaily"));
		conf.setBuyEquip(o.getInt("buyEquip"));
		conf.setBuyPart(o.getInt("buyPart"));
		conf.setSubPros(o.getInt("subPros"));
		conf.setArmyCount(o.getInt("armyCount"));
		conf.setWipe(o.getInt("wipe"));
		conf.setPartProb(o.getInt("partProb"));
		conf.setSpeedBuild(o.getInt("speedBuild"));
		conf.setSpeedArmy(o.getInt("speedArmy"));
		conf.setSpeedTank(o.getInt("speedTank"));
		conf.setSpeedRefit(o.getInt("speedRefit"));
		conf.setSpeedScience(o.getInt("speedScience"));
		conf.setSpeedCollect(o.getInt("speedCollect"));
		conf.setBuyMilitary(o.getInt("buyMilitary"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfVip conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
