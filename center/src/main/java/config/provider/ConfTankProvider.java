package config.provider;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.game.util.RandomHelper;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfTank;
import config.bean.ConfTask;
import config.bean.ConfTaskLive;
import inject.BeanManager;
import util.ASObject;
import util.MapUtil;

@Singleton
public class ConfTankProvider extends BaseProvider<ConfTank> {

	public static ConfTankProvider getInst() {
		return BeanManager.getBean(ConfTankProvider.class);
	}

	public ConfTankProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfTank[]> getClassType() {
		return ConfTank[].class;
	}

	@Override
	protected ConfTank convertDBResult2Bean(ASObject o) {
		ConfTank conf = new ConfTank();
		conf.setTankId(o.getInt("staffingId"));
		conf.setName(o.getString("name"));
		conf.setType(o.getInt("rank"));
		conf.setSubType(o.getInt("staffingLv"));
		conf.setGrade(o.getInt("countLimit"));
		conf.setCanBuild(o.getInt("countLimit"));
		conf.setBuildTime(o.getInt("countLimit"));
		conf.setLordLv(o.getInt("countLimit"));
		conf.setFactoryLv(o.getInt("countLimit"));
		conf.setDrawing(o.getInt("countLimit"));
		conf.setBook(o.getInt("countLimit"));
		conf.setIron(o.getInt("countLimit"));
		conf.setOil(o.getInt("countLimit"));
		conf.setCopper(o.getInt("countLimit"));
		conf.setSilicon(o.getInt("countLimit"));
		conf.setRepair(o.getInt("countLimit"));
		conf.setPayload(o.getInt("countLimit"));
		conf.setFight(o.getInt("countLimit"));
		conf.setAttackMode(o.getInt("countLimit"));
		conf.setAttack(o.getInt("countLimit"));
		conf.setHp(o.getInt("countLimit"));
		conf.setHit(o.getInt("countLimit"));
		conf.setDodge(o.getInt("countLimit"));
		conf.setCrit(o.getInt("countLimit"));
		conf.setCritDef(o.getInt("countLimit"));
		conf.setImpale(o.getInt("countLimit"));
		conf.setDefend(o.getInt("countLimit"));
		conf.setRestriction(MapUtil.convert(o.getString("restriction"), Integer.class, Integer.class));
		conf.setAura(JSON.parseObject(o.getString("aura"), List.class));
		conf.setCanRefit(o.getInt("countLimit"));
		conf.setRefitLv(o.getInt("countLimit"));
		conf.setRefitId(o.getInt("countLimit"));
		conf.setBuffs(JSON.parseObject(o.getString("buffs"), List.class));
		conf.setAttckFactor(o.getInt("countLimit"));
		conf.setHpFactor(o.getInt("countLimit"));
		conf.setHonorScore(o.getInt("countLimit"));
		conf.setImpaleFactor(o.getInt("countLimit"));
		conf.setWarScore(o.getInt("countLimit"));
		conf.setStaffingExp(o.getInt("countLimit"));
		conf.setHpFactor(o.getInt("countLimit"));
		conf.setHonorScore(o.getInt("countLimit"));
		conf.setImpaleFactor(o.getInt("countLimit"));
		conf.setWarScore(o.getInt("countLimit"));
		conf.setStaffingExp(o.getInt("countLimit"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfTank conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
