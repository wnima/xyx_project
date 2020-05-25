package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.Account;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class AccountProvider extends DataProvider<Account> {

	private static final Logger logger = LoggerFactory.getLogger(AccountProvider.class);

	public static AccountProvider getInst() {
		return BeanManager.getBean(AccountProvider.class);
	}

	public AccountProvider() {
		super("");
	}

	@Override
	protected Class<Account[]> getClassType() {
		return Account[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected Account convertDBResult2Bean(ASObject o) {
		Account bean = new Account();
//		conf.setVip(o.getInt("vip"));
//		conf.setTopup(o.getInt("topup"));
//		conf.setBuyPower(o.getInt("buyPower"));
//		conf.setBuyArena(o.getInt("buyArena"));
//		conf.setWaitQue(o.getInt("waitQue"));
//		conf.setBuildQue(o.getInt("buildQue"));
//		conf.setResetDaily(o.getInt("resetDaily"));
//		conf.setBuyEquip(o.getInt("buyEquip"));
//		conf.setBuyPart(o.getInt("buyPart"));
//		conf.setSubPros(o.getInt("subPros"));
//		conf.setArmyCount(o.getInt("armyCount"));
//		conf.setWipe(o.getInt("wipe"));
//		conf.setPartProb(o.getInt("partProb"));
//		conf.setSpeedBuild(o.getInt("speedBuild"));
//		conf.setSpeedArmy(o.getInt("speedArmy"));
//		conf.setSpeedTank(o.getInt("speedTank"));
//		conf.setSpeedRefit(o.getInt("speedRefit"));
//		conf.setSpeedScience(o.getInt("speedScience"));
//		conf.setSpeedCollect(o.getInt("speedCollect"));
//		conf.setBuyMilitary(o.getInt("buyMilitary"));
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(Account bean) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	@Override
	protected void cronTask() {
		
	}

}
