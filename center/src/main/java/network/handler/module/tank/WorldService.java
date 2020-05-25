//package network.handler.module.tank;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//
//import com.game.dataMgr.StaticHeroDataMgr;
//import com.game.dataMgr.StaticLordDataMgr;
//import com.game.dataMgr.StaticVipDataMgr;
//import com.game.dataMgr.StaticWorldDataMgr;
//import com.game.domain.PartyData;
//import com.game.domain.Player;
//import com.game.fight.FightLogic;
//import com.game.fight.domain.Fighter;
//import com.game.fight.domain.Force;
//import com.game.util.LogHelper;
//import com.game.util.PbHelper;
//import com.game.util.RandomHelper;
//import com.game.util.Turple;
//import com.google.inject.Singleton;
//
//import data.bean.Army;
//import data.bean.ArmyStatu;
//import data.bean.Award;
//import data.bean.Collect;
//import data.bean.Form;
//import data.bean.Grab;
//import data.bean.Guard;
//import data.bean.Hero;
//import data.bean.Lord;
//import data.bean.Mail;
//import data.bean.March;
//import data.bean.PartyScience;
//import data.bean.Prop;
//import data.bean.RptTank;
//import data.bean.Science;
//import data.bean.Tank;
//import define.ActivityConst;
//import define.ArmyState;
//import define.AwardFrom;
//import define.AwardType;
//import define.EffectType;
//import define.FirstActType;
//import define.GameError;
//import define.GoldCost;
//import define.MailType;
//import define.PartyType;
//import define.PropId;
//import define.ScienceId;
//import define.SeniorState;
//import define.SysChatId;
//import define.TaskType;
//import domain.Member;
//import inject.BeanManager;
//import manager.PartyDataManager;
//import manager.PlayerDataManager;
//import manager.WorldDataManager;
//import network.AbstractHandlers;
//import network.IModuleMessageHandler;
//import network.handler.ClientHandler;
//import pb.CommonPb;
//import pb.CommonPb.Report;
//import pb.CommonPb.RptAtkHome;
//import pb.CommonPb.RptAtkMine;
//import pb.CommonPb.RptScoutHome;
//import pb.CommonPb.RptScoutMine;
//import pb.GamePb.AttackPosRq;
//import pb.GamePb.AttackPosRs;
//import pb.GamePb.GetAidRs;
//import pb.GamePb.GetInvasionRs;
//import pb.GamePb.GetMapRs;
//import pb.GamePb.GuardPosRq;
//import pb.GamePb.GuardPosRs;
//import pb.GamePb.MoveHomeRq;
//import pb.GamePb.MoveHomeRs;
//import pb.GamePb.RetreatAidRq;
//import pb.GamePb.RetreatAidRs;
//import pb.GamePb.RetreatRs;
//import pb.GamePb.ScoutPosRs;
//import pb.GamePb.SetGuardRq;
//import pb.GamePb.SetGuardRs;
//import pb.GamePb.SpeedQueRq;
//import pb.GamePb.SpeedQueRs;
//
//@Singleton
//public class WorldService implements IModuleMessageHandler{
//	
//	
//	private WorldDataManager worldDataManager = WorldDataManager;
//
//	@Autowired
//	private PlayerDataManager playerDataManager;
//
//	@Autowired
//	private ActivityDataManager activityDataManager;
//
//	@Autowired
//	private StaticHeroDataMgr staticHeroDataMgr;
//
//	@Autowired
//	private StaticWorldDataMgr staticWorldDataMgr;
//
//	@Autowired
//	private StaticPropDataMgr staticPropDataMgr;
//
//	@Autowired
//	private StaticLordDataMgr staticLordDataMgr;
//
//	@Autowired
//	private StaticVipDataMgr staticVipDataMgr;
//
//	@Autowired
//	private PartyDataManager partyDataManager;
//
//	@Autowired
//	private StaffingDataManager staffingDataManager;
//
//
//
//	public static SignService getInst() {
//		return BeanManager.getBean(SignService.class);
//	}
//
//	@Override
//	public void registerModuleHandler(AbstractHandlers handler) {
//	}
//
//	// @Autowired
//	// private ServerSetting serverSetting;
//
//	/**
//	 * 
//	 * Method: getMap
//	 * 
//	 * @Description: 获取区域玩家数据 @param index @param handler @return void @throws
//	 */
//	public void getMap(int area, ClientHandler handler) {
//
//		GetMapRs.Builder builder = GetMapRs.newBuilder();
//		List<Player> list = worldDataManager.getMap(area);
//		Player p;
//		for (int i = 0; i < list.size(); i++) {
//			p = list.get(i);
//			String party = partyDataManager.getPartyNameByLordId(p.roleId);
//			builder.addData(PbHelper.createMapDataPb(p, party));
//		}
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		int partyId = partyDataManager.getPartyId(player.roleId);
//
//		// System.out.println("party mine player partyId:" + partyId);
//		if (partyId != 0) {
//			// 矿上的驻军
//			List<Guard> guards = worldDataManager.getAreaMineGuard(area);
//			Guard guard;
//			int id = 0;
//			for (int i = 0; i < guards.size(); i++) {
//				guard = guards.get(i);
//				id = partyDataManager.getPartyId(guard.getPlayer().roleId);
//				// System.out.println("party mine guard partyId:" + id);
//				if (partyId == id) {
//					builder.addPartyMine(PbHelper.createPartyMinePb(guard.getPlayer().lord.getNick(), guard.getArmy().getTarget()));
//				}
//			}
//		}
//
//		handler.sendMsgToPlayer(GetMapRs.ext, builder.build());
//	}
//
//	public void recalcArmyMarch(Player player) {
//		int state;
//		int period;
//		int originPeriod;
//		Player targetPlayer;
//		for (Army army : player.armys) {
//			state = army.getState();
//			if (state == ArmyState.MARCH) {
//				originPeriod = army.getPeriod();
//				period = marchTime(player, army.getTarget());
//				if (originPeriod != period) {
//					army.setEndTime(army.getEndTime() - army.getPeriod() + period);
//					army.setPeriod(period);
//
//					playerDataManager.synArmyToPlayer(player, new ArmyStatu(player.roleId, army.getKeyId(), 4));
//
//					targetPlayer = worldDataManager.getPosData(army.getTarget());
//					if (targetPlayer != null) {
//						playerDataManager.synArmyToPlayer(targetPlayer, new ArmyStatu(player.roleId, army.getKeyId(), 4));
//					}
//				}
//			} else if (state == ArmyState.RETREAT) {
//				originPeriod = army.getPeriod();
//				period = marchTime(player, army.getTarget());
//				if (originPeriod != period) {
//					army.setEndTime(army.getEndTime() - army.getPeriod() + period);
//					army.setPeriod(period);
//
//					playerDataManager.synArmyToPlayer(player, new ArmyStatu(player.roleId, army.getKeyId(), 4));
//				}
//			} else if (state == ArmyState.AID) {
//				originPeriod = army.getPeriod();
//				period = partyMarchTime(player, army.getTarget());
//				if (originPeriod != period) {
//					army.setEndTime(army.getEndTime() - army.getPeriod() + period);
//					army.setPeriod(period);
//
//					playerDataManager.synArmyToPlayer(player, new ArmyStatu(player.roleId, army.getKeyId(), 4));
//
//					targetPlayer = worldDataManager.getPosData(army.getTarget());
//					if (targetPlayer != null) {
//						playerDataManager.synArmyToPlayer(targetPlayer, new ArmyStatu(player.roleId, army.getKeyId(), 4));
//					}
//				}
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * Method: marchTime
//	 * 
//	 * @Description: 世界地图行军时间 @param player @param pos @return @return int @throws
//	 */
//	private int marchTime(Player player, int pos) {
//		Turple<Integer, Integer> selfXy = WorldDataManager.reducePos(player.lord.getPos());
//		Turple<Integer, Integer> targetXy = WorldDataManager.reducePos(pos);
//		// int time = 180 + Math.abs(selfXy.getA() - targetXy.getA()) +
//		// Math.abs(selfXy.getB() - targetXy.getB());
//
//		// int time = 180;
//		// if (selfXy.getA() == targetXy.getA()) {// 横坐标相同
//		// time += Math.round(Math.abs(selfXy.getB() - targetXy.getB()) * 7.5);
//		// } else if (selfXy.getB() == targetXy.getB()) {// 纵坐标相同
//		// time += Math.round(Math.abs(selfXy.getA() - targetXy.getA()) * 7.5);
//		// } else {
//		// time += Math.round((Math.abs(selfXy.getA() - targetXy.getA()) +
//		// Math.abs(selfXy.getB() - selfXy.getA() + targetXy.getA() -
//		// targetXy.getB())) * 7.5);
//		// }
//
//		int k = 1;
//		if (selfXy.getA() != targetXy.getA()) {
//			if ((selfXy.getB() - targetXy.getB()) / (float) (selfXy.getA() - targetXy.getA()) < 0) {
//				k = -1;
//			}
//		}
//
//		int time = 180 + Math.round((Math.abs(selfXy.getA() - targetXy.getA()) + Math.abs(selfXy.getB() - k * selfXy.getA() + k * targetXy.getA() - targetXy.getB())) * 7.5f);
//
//		float factor = 1;
//
//		// 引擎强化科技
//		Science science = player.sciences.get(ScienceId.ENGINE);
//		if (science != null) {
//			factor += (science.getScienceLv() * 5 / 100.0f);
//		}
//
//		if (player.effects.containsKey(EffectType.MARCH_SPEED)) {// 世界地图行军速度提升100%
//			factor += 1;
//		} else if (player.effects.containsKey(EffectType.MARCH_SPEED_SUPER)) {
//			factor += 1.5;
//		}
//
//		StaticVip staticVip = staticVipDataMgr.getStaticVip(player.lord.getVip());
//		if (staticVip != null) {
//			factor += (staticVip.getSpeedArmy() / 100.0f);
//		}
//
//		time = (int) (time / factor);
//
//		if (time < 1) {
//			time = 1;
//		}
//		return time;
//	}
//
//	public static void main(String[] args) {
//		int x1 = 441;
//		int y1 = 312;
//		int x2 = 119;
//		int y2 = 595;
//
//		int k = 1;
//		if (x1 != x2) {
//			if ((y1 - y2) / (float) (x1 - x2) < 0) {
//				k = -1;
//			}
//		}
//
//		int step = 0;
//
//		step = (Math.abs(x1 - x2) + Math.abs(y1 - k * x1 + k * x2 - y2));
//
//		int time = 180 + Math.round(step * 7.5f);
//		System.out.println("step: " + step + " time: " + time);
//	}
//
//	/**
//	 * 
//	 * Method: partyMarchTime
//	 * 
//	 * @Description: 军团驻军行军时间 @param player @param pos @return @return int @throws
//	 */
//	public int partyMarchTime(Player player, int pos) {
//		Turple<Integer, Integer> selfXy = WorldDataManager.reducePos(player.lord.getPos());
//		Turple<Integer, Integer> targetXy = WorldDataManager.reducePos(pos);
//		int k = 1;
//		if (selfXy.getA() != targetXy.getA()) {
//			if ((selfXy.getB() - targetXy.getB()) / (float) (selfXy.getA() - targetXy.getA()) < 0) {
//				k = -1;
//			}
//		}
//
//		int time = 180 + Math.round((Math.abs(selfXy.getA() - targetXy.getA()) + Math.abs(selfXy.getB() - k * selfXy.getA() + k * targetXy.getA() - targetXy.getB())) * 7.5f);
//
//		float factor = 1;
//
//		// 引擎强化科技
//		Science science1 = player.sciences.get(ScienceId.ENGINE);
//		if (science1 != null) {
//			factor += (science1.getScienceLv() * 5 / 100.0f);
//		}
//
//		// 帮派科技，火线支援
//		Map<Integer, PartyScience> sciences = partyDataManager.getScience(player);
//		if (sciences != null) {
//			PartyScience science2 = sciences.get(ScienceId.PARTY_MARCH_TIME);
//			if (science2 != null) {
//				factor += (science2.getScienceLv() * 10 / 100.0f);
//			}
//		}
//
//		if (player.effects.containsKey(EffectType.MARCH_SPEED)) {// 世界地图行军速度提升100%
//			factor += 1;
//		} else if (player.effects.containsKey(EffectType.MARCH_SPEED_SUPER)) {
//			factor += 1.5;
//		}
//
//		StaticVip staticVip = staticVipDataMgr.getStaticVip(player.lord.getVip());
//		if (staticVip != null) {
//			factor += (staticVip.getSpeedArmy() / 100.0f);
//		}
//
//		time = (int) (time / factor);
//
//		if (time < 1) {
//			time = 1;
//		}
//		return time;
//	}
//
//	// private int calcHarvest(Army army, int prodction) {
//	// return (int) ((TimeHelper.getCurrentSecond() - army.getEndTime() +
//	// army.getPeriod()) * prodction / (float) TimeHelper.HOUR_S);
//	// }
//
//	private long grabMax(long v, long protect) {
//		if (v < protect) {
//			return 0;
//		}
//
//		long max = v - protect;
//		v = v / 10;
//
//		return ((v > max) ? max : v);
//	}
//
//	private CommonPb.RptMan createRptMan(Player player, int hero, Map<Integer, RptTank> haust, int prosAdd) {
//		CommonPb.RptMan.Builder builder = CommonPb.RptMan.newBuilder();
//		Lord lord = player.lord;
//		builder.setPos(lord.getPos());
//		builder.setName(lord.getNick());
//		builder.setVip(lord.getVip());
//		builder.setPros(lord.getPros());
//		builder.setProsMax(lord.getProsMax());
//
//		String party = partyDataManager.getPartyNameByLordId(player.roleId);
//		if (party != null) {
//			builder.setParty(party);
//		}
//
//		if (hero != 0) {
//			builder.setHero(hero);
//		}
//
//		if (prosAdd != 0) {
//			builder.setProsAdd(prosAdd);
//		}
//
//		if (haust != null) {
//			Iterator<RptTank> it = haust.values().iterator();
//			while (it.hasNext()) {
//				builder.addTank(PbHelper.createRtpTankPb(it.next()));
//			}
//		}
//
//		return builder.build();
//	}
//
//	private CommonPb.RptMine createRptMine(int pos, StaticMine staticMine, Player guard, int hero, Map<Integer, RptTank> haust) {
//		CommonPb.RptMine.Builder builder = CommonPb.RptMine.newBuilder();
//		Lord lord = guard.lord;
//		builder.setPos(pos);
//		builder.setMine(staticMine.getType());
//		builder.setLv(staticMine.getLv());
//		builder.setName(lord.getNick());
//		builder.setVip(lord.getVip());
//		String party = partyDataManager.getPartyNameByLordId(guard.roleId);
//		if (party != null) {
//			builder.setParty(party);
//		}
//
//		if (hero != 0) {
//			builder.setHero(hero);
//		}
//
//		if (haust != null) {
//			Iterator<RptTank> it = haust.values().iterator();
//			while (it.hasNext()) {
//				builder.addTank(PbHelper.createRtpTankPb(it.next()));
//			}
//		}
//
//		return builder.build();
//	}
//
//	private CommonPb.RptMine createRptMine(int pos, StaticMine staticMine, Map<Integer, RptTank> haust) {
//		CommonPb.RptMine.Builder builder = CommonPb.RptMine.newBuilder();
//		builder.setMine(staticMine.getType());
//		builder.setLv(staticMine.getLv());
//		builder.setPos(pos);
//
//		if (haust != null) {
//			Iterator<RptTank> it = haust.values().iterator();
//			while (it.hasNext()) {
//				builder.addTank(PbHelper.createRtpTankPb(it.next()));
//			}
//		}
//
//		return builder.build();
//	}
//
//	private Grab calcGrab(Player target, long load) {
//		Grab grab = new Grab();
//		long protect = playerDataManager.calcProtect(target);
//		long stone = grabMax(target.resource.getStone(), protect);
//		long iron = grabMax(target.resource.getIron(), protect);
//		long silicon = grabMax(target.resource.getSilicon(), protect);
//		long copper = grabMax(target.resource.getCopper(), protect);
//		long oil = grabMax(target.resource.getOil(), protect);
//		long total = stone + iron + silicon + copper + oil;
//		if (load < total) {
//			stone = (long) (load * stone / (double) total);
//			iron = (long) (load * iron / (double) total);
//			silicon = (long) (load * silicon / (double) total);
//			copper = (long) (load * copper / (double) total);
//			oil = (long) (load * oil / (double) total);
//		}
//
//		grab.rs[0] = iron;
//		grab.rs[1] = oil;
//		grab.rs[2] = copper;
//		grab.rs[3] = silicon;
//		grab.rs[4] = stone;
//
//		return grab;
//	}
//
//	private Grab calcMaxGrab(Player target) {
//		Grab grab = new Grab();
//		long protect = playerDataManager.calcProtect(target);
//		long stone = grabMax(target.resource.getStone(), protect);
//		long iron = grabMax(target.resource.getIron(), protect);
//		long silicon = grabMax(target.resource.getSilicon(), protect);
//		long copper = grabMax(target.resource.getCopper(), protect);
//		long oil = grabMax(target.resource.getOil(), protect);
//		grab.rs[0] = iron;
//		grab.rs[1] = oil;
//		grab.rs[2] = copper;
//		grab.rs[3] = silicon;
//		grab.rs[4] = stone;
//		return grab;
//	}
//
//	// private void changeArmyState(Player player, Army army, int state, int
//	// now, int period) {
//	// if (state == ArmyState.RETREAT) {
//	// army.setState(state);
//	// army.setPeriod(period);
//	// army.setEndTime(now + period);
//	// worldDataManager.removeMarch(player, army);
//	// } else if (state == ArmyState.COLLECT) {
//	// army.setState(state);
//	// army.setPeriod(period);
//	// army.setEndTime(now + period);
//	// worldDataManager.removeMarch(player, army);
//	// worldDataManager.setGuard(new Guard(player, army));
//	// }
//	// }
//
//	private void retreatArmy(Player player, Army army, int now) {
//		int marchTime = marchTime(player, army.getTarget());
//		army.setState(ArmyState.RETREAT);
//		army.setPeriod(marchTime);
//		army.setEndTime(now + marchTime);
//	}
//
//	private void retreatAidArmy(Player player, Army army, int now) {
//		// int marchTime = partyMarchTime(player, army.getTarget());
//		int marchTime = marchTime(player, army.getTarget());
//		army.setState(ArmyState.RETREAT);
//		army.setPeriod(marchTime);
//		army.setEndTime(now + marchTime);
//	}
//
//	public void retreatAllGuard(Player player) {
//		int now = TimeHelper.getCurrentSecond();
//		int pos = player.lord.getPos();
//
//		List<Guard> list = worldDataManager.getGuard(pos);
//		if (list != null) {
//			Army army = null;
//			Player target = null;
//			for (int i = 0; i < list.size(); i++) {
//				Guard guard = list.get(i);
//				target = guard.getPlayer();
//				army = guard.getArmy();
//				worldDataManager.removeGuard(target, army);
//
//				retreatAidArmy(player, army, now);
//
//				playerDataManager.sendNormalMail(target, MailType.MOLD_RETREAT, now, player.lord.getNick());
//				playerDataManager.synArmyToPlayer(target, new ArmyStatu(target.roleId, army.getKeyId(), 2));
//			}
//		}
//
//		for (Army e : player.armys) {
//			int state = e.getState();
//			if (state == ArmyState.GUARD || state == ArmyState.WAIT) {// 召回驻防
//				worldDataManager.removeGuard(player, e);
//				retreatArmy(player, e, TimeHelper.getCurrentSecond());
//				Player target = worldDataManager.getPosData(e.getTarget());
//				if (target != null) {
//					playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, e.getKeyId(), 2));
//				}
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * Method: collectArmy
//	 * 
//	 * @Description: 部队开始采集 @param player @param army @param now @param
//	 * staticMine @param collect @param get @return void @throws
//	 */
//	private void collectArmy(Player player, Army army, int now, StaticMine staticMine, int collect, long get) {
//		long load = playerDataManager.calcLoad(player, army.getForm());
//		long loadFree = load;
//		if (get > 0) {
//			Grab grab = new Grab();
//			if (load > get) {
//				loadFree -= get;
//				grab.rs[staticMine.getType() - 1] = get;
//			} else {
//				grab.rs[staticMine.getType() - 1] = loadFree;
//				loadFree = 0;
//			}
//
//			army.setGrab(grab);
//		}
//
//		int speedAdd = 0;
//		StaticVip staticVip = staticVipDataMgr.getStaticVip(player.lord.getVip());
//		if (staticVip != null) {
//			speedAdd += staticVip.getSpeedCollect();
//		}
//
//		int heroId = army.getForm().getCommander();
//		StaticHero staticHero = staticHeroDataMgr.getStaticHero(heroId);
//		if (staticHero != null && staticHero.getSkillId() == 5) {
//			speedAdd += staticHero.getSkillValue();
//		}
//
//		collect = (int) (collect * (1 + speedAdd / 100.0f));
//
//		int collectTime = (int) (loadFree / (collect / (double) TimeHelper.HOUR_S));
//		army.setState(ArmyState.COLLECT);
//		army.setPeriod(collectTime);
//		army.setEndTime(now + collectTime);
//		army.setStaffingTime(now + TimeHelper.HALF_HOUR_S);
//
//		Collect c = new Collect();
//		c.speed = speedAdd;
//		c.load = load;
//		army.setCollect(c);
//
//		worldDataManager.setGuard(new Guard(player, army));
//	}
//
//	private void recollectArmy(Player player, Army army, int now, StaticMine staticMine, int collect, long get) {
//		long load = playerDataManager.calcLoad(player, army.getForm());
//		// long grab = get;
//		Grab grab = army.getGrab();
//		if (grab == null) {
//			grab = new Grab();
//			army.setGrab(grab);
//		}
//
//		if (get > load) {
//			grab.rs[staticMine.getType() - 1] = load;
//		} else {
//			grab.rs[staticMine.getType() - 1] = get;
//		}
//
//		int speedAdd = 0;
//		Collect c = army.getCollect();
//		if (c != null) {
//			c.load = load;
//			speedAdd = c.speed;
//		} else {
//			StaticVip staticVip = staticVipDataMgr.getStaticVip(player.lord.getVip());
//			if (staticVip != null) {
//				speedAdd += staticVip.getSpeedCollect();
//			}
//
//			int heroId = army.getForm().getCommander();
//			StaticHero staticHero = staticHeroDataMgr.getStaticHero(heroId);
//			if (staticHero != null && staticHero.getSkillId() == 5) {
//				speedAdd += staticHero.getSkillValue();
//			}
//
//			c = new Collect();
//			c.speed = speedAdd;
//			c.load = load;
//			army.setCollect(c);
//		}
//
//		long loadFree = 0;
//		if (get >= load) {// 已经获取的资源 大于 当前负载
//			loadFree = 0;
//		} else {
//			loadFree = load - get;
//		}
//
//		collect = (int) (collect * (1 + speedAdd / 100.0f));
//
//		int collectTime = (int) (loadFree / (collect / (double) TimeHelper.HOUR_S));
//		army.setState(ArmyState.COLLECT);
//		army.setPeriod(collectTime);
//		army.setEndTime(now + collectTime);
//	}
//
//	public void retreat(int keyId, ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		boolean find = false;
//
//		Army army = null;
//		for (Army e : player.armys) {
//			if (e.getKeyId() == keyId) {
//				find = true;
//				army = e;
//				break;
//			}
//		}
//
//		if (!find) {
//			handler.sendErrorMsgToPlayer(GameError.NO_ARMY);
//			return;
//		}
//
//		int state = army.getState();
//		if (state == ArmyState.RETREAT || state == ArmyState.MARCH) {
//			handler.sendErrorMsgToPlayer(GameError.IN_MARCH);
//			return;
//		}
//
//		if (state == ArmyState.COLLECT) {
//			if (!army.getSenior()) {
//				StaticMine staticMine = worldDataManager.evaluatePos(army.getTarget());
//				if (staticMine != null) {
//					long get = playerDataManager.calcCollect(player, army, TimeHelper.getCurrentSecond(), staticMine, staticWorldDataMgr.getStaticMineLv(staticMine.getLv()).getProduction());
//					Grab grab = new Grab();
//					grab.rs[staticMine.getType() - 1] = get;
//					army.setGrab(grab);
//
//					worldDataManager.removeGuard(player, army);
//					retreatArmy(player, army, TimeHelper.getCurrentSecond());
//				} else {
//					// handler.sendErrorMsgToPlayer(GameError.IN_MARCH);
//					// return;
//					worldDataManager.removeGuard(player, army);
//					retreatArmy(player, army, TimeHelper.getCurrentSecond());
//				}
//			} else {
//				StaticMine staticMine = SeniorMineDataManager.getInst().evaluatePos(army.getTarget());
//				if (staticMine != null) {
//					long get = playerDataManager.calcCollect(player, army, TimeHelper.getCurrentSecond(), staticMine, staticWorldDataMgr.getStaticMineLv(staticMine.getLv()).getProduction());
//					Grab grab = new Grab();
//					grab.rs[staticMine.getType() - 1] = get;
//					army.setGrab(grab);
//
//					SeniorMineDataManager.getInst().removeGuard(player, army);
//					playerDataManager.retreatEnd(player, army);
//					player.armys.remove(army);
//				} else {
//					SeniorMineDataManager.getInst().removeGuard(player, army);
//					playerDataManager.retreatEnd(player, army);
//					player.armys.remove(army);
//				}
//			}
//		} else if (state == ArmyState.GUARD || state == ArmyState.WAIT) {// 召回驻防
//			worldDataManager.removeGuard(player, army);
//			retreatArmy(player, army, TimeHelper.getCurrentSecond());
//			Player target = worldDataManager.getPosData(army.getTarget());
//			if (target != null) {
//				playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, army.getKeyId(), 2));
//			}
//		}
//
//		RetreatRs.Builder builder = RetreatRs.newBuilder();
//		handler.sendMsgToPlayer(RetreatRs.ext, builder.build());
//	}
//
//	public void retreatAid(RetreatAidRq req, ClientHandler handler) {
//		long targetId = req.getLordId();
//		int keyId = req.getKeyId();
//		Player target = playerDataManager.getPlayer(targetId);
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		int pos = player.lord.getPos();
//
//		boolean find = false;
//		Army army = null;
//		for (Army e : target.armys) {
//			if (e.getKeyId() == keyId && e.getTarget() == pos) {
//				find = true;
//				army = e;
//				break;
//			}
//		}
//
//		if (!find) {
//			handler.sendErrorMsgToPlayer(GameError.NO_ARMY);
//			return;
//		}
//
//		int state = army.getState();
//		if (state != ArmyState.WAIT && state != ArmyState.GUARD) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
//			return;
//		}
//
//		worldDataManager.removeGuard(target, army);
//
//		int now = TimeHelper.getCurrentSecond();
//		retreatAidArmy(target, army, now);
//
//		playerDataManager.sendNormalMail(target, MailType.MOLD_RETREAT, now, player.lord.getNick());
//		playerDataManager.synArmyToPlayer(target, new ArmyStatu(target.roleId, army.getKeyId(), 2));
//
//		RetreatAidRs.Builder builder = RetreatAidRs.newBuilder();
//		handler.sendMsgToPlayer(RetreatAidRs.ext, builder.build());
//	}
//
//	public void guardPos(GuardPosRq req, ClientHandler handler) {
//		int pos = req.getPos();
//		Member member = partyDataManager.getMemberById(handler.getRoleId());
//		if (member == null || member.getPartyId() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
//			return;
//		}
//
//		int partyId = member.getPartyId();
//		PartyData partyData = partyDataManager.getParty(partyId);
//		if (partyData == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
//			return;
//		}
//
//		partyDataManager.refreshMember(member);
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//
//		// GameServer.GAME_LOGGER.error("getAid roleId:" + handler.getRoleId());
//
//		// if (player.armys.size() >= armyCount(player)) {
//		// handler.sendErrorMsgToPlayer(GameError.MAX_ARMY_COUNT);
//		// return;
//		// }
//
//		int armyCount = player.armys.size();
//		for (Army army : player.armys) {
//			if (army.getState() == ArmyState.WAR) {
//				armyCount -= 1;
//				break;
//			}
//		}
//		if (armyCount >= playerDataManager.armyCount(player)) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_ARMY_COUNT);
//			return;
//		}
//
//		Player target = worldDataManager.getPosData(pos);
//		if (target == null) {
//			handler.sendErrorMsgToPlayer(GameError.EMPTY_POS);
//			return;
//		}
//
//		if (player.roleId == target.roleId) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
//			return;
//		}
//
//		if (!partyDataManager.isSameParty(player.roleId, target.roleId)) {
//			handler.sendErrorMsgToPlayer(GameError.NOT_SAME_PARTY);
//			return;
//		}
//
//		Form attackForm = PbHelper.createForm(req.getForm());
//		StaticHero staticHero = null;
//		Hero hero = null;
//		if (attackForm.getCommander() > 0) {
//			hero = player.heros.get(attackForm.getCommander());
//			if (hero == null || hero.getCount() <= 0) {
//				handler.sendErrorMsgToPlayer(GameError.NO_HERO);
//				return;
//			}
//
//			staticHero = staticHeroDataMgr.getStaticHero(hero.getHeroId());
//			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//				return;
//			}
//
//			if (staticHero.getType() != 2) {
//				handler.sendErrorMsgToPlayer(GameError.NOT_HERO);
//				return;
//			}
//		}
//
//		int maxTankCount = playerDataManager.formTankCount(player, staticHero);
//		if (!playerDataManager.checkAndSubTank(player, attackForm, maxTankCount)) {
//			handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
//			return;
//		}
//
//		if (hero != null) {
//			hero.setCount(hero.getCount() - 1);
//		}
//
//		int marchTime = partyMarchTime(player, pos);
//		int now = TimeHelper.getCurrentSecond();
//		Army army = new Army(player.maxKey(), pos, ArmyState.AID, attackForm, marchTime, now + marchTime);
//		player.armys.add(army);
//		March march = new March(player, army);
//		worldDataManager.addMarch(march);
//
//		PartyDataManager.doPartyLivelyTask(partyData, member, PartyType.TASK_ARMY);
//		playerDataManager.updTask(player, TaskType.COND_PARTY_GUARD, 1, null);
//
//		GuardPosRs.Builder builder = GuardPosRs.newBuilder();
//		builder.setArmy(PbHelper.createArmyPb(army));
//		handler.sendMsgToPlayer(GuardPosRs.ext, builder.build());
//
//		if (target != null) {
//			playerDataManager.synInvasionToPlayer(target, march);
//		}
//	}
//
//	public void setGuard(SetGuardRq req, ClientHandler handler) {
//		long lordId = req.getLordId();
//		int keyId = req.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//
//		if (lordId == 0) {// 撤防
//			Guard guard = worldDataManager.getHomeGuard(player.lord.getPos());
//			if (guard == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_ARMY);
//				return;
//			}
//
//			guard.getArmy().setState(ArmyState.WAIT);
//		} else {// 上防
//			List<Guard> list = worldDataManager.getGuard(player.lord.getPos());
//			boolean find = false;
//			boolean hadOld = false;
//			Guard oldGuard = null;
//			if (list != null) {
//				for (int i = 0; i < list.size(); i++) {
//					oldGuard = list.get(i);
//					if (oldGuard.getArmy().getState() == ArmyState.GUARD) {
//						oldGuard.getArmy().setState(ArmyState.WAIT);
//						hadOld = true;
//					}
//				}
//
//				for (int i = 0; i < list.size(); i++) {
//					Guard guard = list.get(i);
//					if (guard.getPlayer().roleId == lordId && guard.getArmy().getKeyId() == keyId && guard.getArmy().getState() == ArmyState.WAIT) {
//						guard.getArmy().setState(ArmyState.GUARD);
//						find = true;
//					}
//				}
//			}
//
//			if (!find) {
//				if (hadOld) {
//					oldGuard.getArmy().setState(ArmyState.GUARD);
//				}
//				handler.sendErrorMsgToPlayer(GameError.NO_ARMY);
//				return;
//			}
//		}
//
//		SetGuardRs.Builder builder = SetGuardRs.newBuilder();
//		handler.sendMsgToPlayer(SetGuardRs.ext, builder.build());
//	}
//
//	public void moveHome(MoveHomeRq req, ClientHandler handler) {
//		int type = req.getType();
//		int pos = -1;
//		if (type == 1) {// 金币
//			if (!req.hasPos()) {
//				handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//				return;
//			}
//
//			pos = req.getPos();
//
//			if (!worldDataManager.isValidPos(pos)) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_POS);
//				return;
//			}
//
//			Player player = playerDataManager.getPlayer(handler.getRoleId());
//			if (player.lord.getGold() < 88) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//
//			Player target = worldDataManager.getPosData(pos);
//			if (target != null) {
//				handler.sendErrorMsgToPlayer(GameError.POS_NOT_EMPTY);
//				return;
//			}
//
//			StaticMine staticMine = worldDataManager.evaluatePos(pos);
//			if (staticMine != null) {
//				handler.sendErrorMsgToPlayer(GameError.POS_NOT_EMPTY);
//				return;
//			}
//
//			playerDataManager.subGold(player.lord, 88, GoldCost.MOVE_HOME);
//
//			int oldPos = player.lord.getPos();
//			List<Guard> list = worldDataManager.getGuard(oldPos);
//			if (list != null) {
//				for (int i = 0; i < list.size(); i++) {
//					Guard guard = list.get(i);
//					guard.getArmy().setTarget(pos);
//					worldDataManager.setGuard(guard);
//				}
//			}
//
//			worldDataManager.removeGuard(oldPos);
//			worldDataManager.removePos(oldPos);
//
//			player.lord.setPos(pos);
//			worldDataManager.putPlayer(player);
//
//			MoveHomeRs.Builder builder = MoveHomeRs.newBuilder();
//			builder.setPos(pos);
//			builder.setGold(player.lord.getGold());
//			handler.sendMsgToPlayer(MoveHomeRs.ext, builder.build());
//		} else if (type == 2) {// 2.道具(随机)
//			Player player = playerDataManager.getPlayer(handler.getRoleId());
//			Prop prop = player.props.get(PropId.MOVE_HOME_2);
//			if (prop == null || prop.getCount() < 1) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//
//			playerDataManager.subProp(prop, 1);
//
//			while (true) {
//				pos = RandomHelper.randomInSize(600 * 600);
//				Player target = worldDataManager.getPosData(pos);
//				if (target != null) {
//					continue;
//				}
//
//				StaticMine staticMine = worldDataManager.evaluatePos(pos);
//				if (staticMine != null) {
//					continue;
//				}
//
//				if (!worldDataManager.isValidPos(pos)) {
//					continue;
//				}
//
//				int oldPos = player.lord.getPos();
//				List<Guard> list = worldDataManager.getGuard(oldPos);
//				if (list != null) {
//					for (int i = 0; i < list.size(); i++) {
//						Guard guard = list.get(i);
//						guard.getArmy().setTarget(pos);
//						worldDataManager.setGuard(guard);
//					}
//				}
//
//				worldDataManager.removeGuard(oldPos);
//				worldDataManager.removePos(oldPos);
//
//				player.lord.setPos(pos);
//				worldDataManager.putPlayer(player);
//				break;
//			}
//
//			MoveHomeRs.Builder builder = MoveHomeRs.newBuilder();
//			builder.setPos(pos);
//			builder.setGold(player.lord.getGold());
//			handler.sendMsgToPlayer(MoveHomeRs.ext, builder.build());
//		} else if (type == 3) {// 3.道具(定点)
//			if (!req.hasPos()) {
//				handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//				return;
//			}
//
//			pos = req.getPos();
//
//			if (!worldDataManager.isValidPos(pos)) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_POS);
//				return;
//			}
//
//			Player player = playerDataManager.getPlayer(handler.getRoleId());
//			Prop prop = player.props.get(PropId.MOVE_HOME_1);
//			if (prop == null || prop.getCount() < 1) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//
//			Player target = worldDataManager.getPosData(pos);
//			if (target != null) {
//				handler.sendErrorMsgToPlayer(GameError.POS_NOT_EMPTY);
//				return;
//			}
//
//			StaticMine staticMine = worldDataManager.evaluatePos(pos);
//			if (staticMine != null) {
//				handler.sendErrorMsgToPlayer(GameError.POS_NOT_EMPTY);
//				return;
//			}
//
//			playerDataManager.subProp(prop, 1);
//
//			int oldPos = player.lord.getPos();
//			List<Guard> list = worldDataManager.getGuard(oldPos);
//			if (list != null) {
//				for (int i = 0; i < list.size(); i++) {
//					Guard guard = list.get(i);
//					guard.getArmy().setTarget(pos);
//					worldDataManager.setGuard(guard);
//				}
//			}
//
//			worldDataManager.removeGuard(oldPos);
//			worldDataManager.removePos(oldPos);
//
//			player.lord.setPos(pos);
//			worldDataManager.putPlayer(player);
//
//			MoveHomeRs.Builder builder = MoveHomeRs.newBuilder();
//			builder.setPos(pos);
//			builder.setGold(player.lord.getGold());
//			handler.sendMsgToPlayer(MoveHomeRs.ext, builder.build());
//		}
//	}
//
//	private void scoutMine(Player player, int pos, ClientHandler handler) {
//		StaticMine staticMine = worldDataManager.evaluatePos(pos);
//		if (staticMine != null) {
//			Lord lord = player.lord;
//			StaticScout staticScout = staticWorldDataMgr.getScout(staticMine.getLv());
//			long scountCost = staticScout.getScoutCost();
//			if (lord.getScountDate() != TimeHelper.getCurrentDay()) {
//				lord.setScountDate(TimeHelper.getCurrentDay());
//				lord.setScount(0);
//			}
//			int scount = lord.getScount() + 1;
//			if (scount > SeniorState.SCOUNT_MAX) {
//				scountCost = scountCost + scountCost * (long) (Math.pow(scount - SeniorState.SCOUNT_MAX, 5));
//			}
//			if (player.resource.getStone() < scountCost) {
//				handler.sendErrorMsgToPlayer(GameError.STONE_NOT_ENOUGH);
//				return;
//			}
//			lord.setScount(scount);
//			RptScoutMine.Builder rptMine = RptScoutMine.newBuilder();
//			playerDataManager.modifyStone(player.resource, -scountCost);
//
//			int product = staticWorldDataMgr.getStaticMineLv(staticMine.getLv()).getProduction();
//			int now = TimeHelper.getCurrentSecond();
//			Guard guard = worldDataManager.getMineGuard(pos);
//			if (guard != null) {// 有驻军
//				rptMine.setForm(PbHelper.createFormPb(guard.getArmy().getForm()));
//				String partyName = partyDataManager.getPartyNameByLordId(guard.getPlayer().roleId);
//				if (partyName != null) {
//					rptMine.setParty(partyName);
//				}
//
//				rptMine.setFriend(guard.getPlayer().lord.getNick());
//				rptMine.setHarvest(playerDataManager.calcCollect(guard.getPlayer(), guard.getArmy(), now, staticMine, product));
//			} else {// 无驻军
//				// rptMine.setForm(PbHelper.createFormPb(worldDataManager.getMineForm(pos,
//				// staticMine.getLv())));
//				rptMine.setForm(PbHelper.createFormPb(worldDataManager.getMineForm(pos, staticMine.getLv()).getForm()));
//			}
//
//			rptMine.setPos(pos);
//			rptMine.setLv(staticMine.getLv());
//			rptMine.setProduct(product);
//			rptMine.setMine(staticMine.getType());
//
//			Report.Builder report = Report.newBuilder();
//			report.setScoutMine(rptMine);
//			report.setTime(now);
//
//			Mail mail = playerDataManager.createReportMail(player, report.build(), MailType.MOLD_SCOUT_MINE, TimeHelper.getCurrentSecond(), String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			ScoutPosRs.Builder builder = ScoutPosRs.newBuilder();
//			builder.setMail(PbHelper.createMailPb(mail));
//			handler.sendMsgToPlayer(ScoutPosRs.ext, builder.build());
//		} else {
//			handler.sendErrorMsgToPlayer(GameError.EMPTY_POS);
//		}
//
//	}
//
//	private void scoutHome(Player player, Player target, ClientHandler handler) {
//		if (target.effects.containsKey(EffectType.ATTACK_FREE)) {
//			handler.sendErrorMsgToPlayer(GameError.ATTACK_FREE);
//			return;
//		}
//
//		Lord lord = target.lord;
//		StaticScout staticScout = staticWorldDataMgr.getScout(lord.getLevel());
//		long scountCost = staticScout.getScoutCost();
//		if (player.lord.getScountDate() != TimeHelper.getCurrentDay()) {
//			player.lord.setScountDate(TimeHelper.getCurrentDay());
//			player.lord.setScount(0);
//		}
//		int scount = player.lord.getScount() + 1;
//		if (scount > SeniorState.SCOUNT_MAX) {
//			scountCost = scountCost + scountCost * (long) (Math.pow(scount - SeniorState.SCOUNT_MAX, 5));
//		}
//		if (player.resource.getStone() < scountCost) {
//			handler.sendErrorMsgToPlayer(GameError.STONE_NOT_ENOUGH);
//			return;
//		}
//		player.lord.setScount(scount);
//		playerDataManager.modifyStone(player.resource, -scountCost);
//
//		RptScoutHome.Builder rptHome = RptScoutHome.newBuilder();
//		rptHome.setPos(lord.getPos());
//		rptHome.setLv(lord.getLevel());
//		rptHome.setName(lord.getNick());
//		rptHome.setPros(lord.getPros());
//		rptHome.setProsMax(lord.getProsMax());
//		String party = partyDataManager.getPartyNameByLordId(target.roleId);
//		if (party != null) {
//			rptHome.setParty(party);
//		}
//
//		Guard guard = worldDataManager.getHomeGuard(lord.getPos());
//		if (guard != null) {// 有驻军
//			// GameServer.ERROR_LOGGER.error("Guard player:" +
//			// guard.getPlayer().lord.getNick() + "|pos:" +
//			// guard.getPlayer().lord.getPos());
//			rptHome.setFriend(guard.getPlayer().lord.getNick());
//			rptHome.setForm(PbHelper.createFormPb(guard.getArmy().getForm()));
//		} else {
//			Form targetForm = playerDataManager.getHomeDefendForm(target);
//			if (targetForm != null) {
//				rptHome.setForm(PbHelper.createFormPb(targetForm));
//			}
//		}
//
//		int now = TimeHelper.getCurrentSecond();
//		Grab grab = calcMaxGrab(target);
//		rptHome.setGrab(PbHelper.createGrabPb(grab));
//
//		Report.Builder report = Report.newBuilder();
//		report.setScoutHome(rptHome);
//		report.setTime(now);
//
//		Mail mail = playerDataManager.createReportMail(player, report.build(), MailType.MOLD_SCOUT_PLAYER, now, lord.getNick(), String.valueOf(lord.getLevel()));
//
//		ScoutPosRs.Builder builder = ScoutPosRs.newBuilder();
//		builder.setMail(PbHelper.createMailPb(mail));
//		handler.sendMsgToPlayer(ScoutPosRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: scout
//	 * 
//	 * @Description: 侦查 @param pos @param handler @return void @throws
//	 */
//	public void scoutPos(int pos, ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//
//		Player target = worldDataManager.getPosData(pos);
//		if (target != null) {
//			scoutHome(player, target, handler);
//		} else {
//			scoutMine(player, pos, handler);
//		}
//
//	}
//
//	// private int armyCount(Player player) {
//	// StaticVip staticVip =
//	// staticVipDataMgr.getStaticVip(player.lord.getVip());
//	// if (staticVip != null) {
//	// return staticVip.getArmyCount();
//	// }
//	// return 3;
//	// }
//
//	public void attackPos(AttackPosRq req, ClientHandler handler) {
//		int pos = req.getPos();
//		Player attacker = playerDataManager.getPlayer(handler.getRoleId());
//		if (attacker == null) {
//			LogHelper.ERROR_LOGGER.error("attack nul!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + handler.getRoleId());
//		}
//
//		if (attacker.lord.getPower() < 1) {
//			handler.sendErrorMsgToPlayer(GameError.NO_POWER);
//			return;
//		}
//
//		if (attacker.lord.getLevel() < 3) {
//			handler.sendErrorMsgToPlayer(GameError.LEFT_ONE_MEMBER);
//			return;
//		}
//
//		int armyCount = attacker.armys.size();
//		for (Army army : attacker.armys) {
//			if (army.getState() == ArmyState.WAR) {
//				armyCount -= 1;
//				break;
//			}
//		}
//		if (armyCount >= playerDataManager.armyCount(attacker)) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_ARMY_COUNT);
//			return;
//		}
//
//		Player defencer = null;
//		StaticMine staticMine = worldDataManager.evaluatePos(pos);
//		if (staticMine != null) {// 打矿
//			Guard guard = worldDataManager.getMineGuard(pos);
//			if (guard != null) {// 有驻军
//				Player guarder = guard.getPlayer();
//				if (attacker == guarder) {
//					handler.sendErrorMsgToPlayer(GameError.IN_COLLECT);
//					return;
//				} else {
//					if (partyDataManager.isSameParty(attacker.roleId, guarder.roleId)) {
//						handler.sendErrorMsgToPlayer(GameError.IN_SAME_PARTY);
//						return;
//					}
//				}
//			}
//
//			playerDataManager.updTask(attacker, TaskType.COND_ATTACK_RESOURCE, 1, staticMine.getLv());
//		} else {// 打人
//			defencer = worldDataManager.getPosData(pos);
//			if (defencer == null) {
//				handler.sendErrorMsgToPlayer(GameError.EMPTY_POS);
//				return;
//			}
//
//			if (attacker.roleId == defencer.roleId) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
//				return;
//			}
//
//			if (partyDataManager.isSameParty(attacker.roleId, defencer.roleId)) {
//				handler.sendErrorMsgToPlayer(GameError.IN_SAME_PARTY);
//				return;
//			}
//
//			if (defencer.effects.containsKey(EffectType.ATTACK_FREE)) {
//				handler.sendErrorMsgToPlayer(GameError.ATTACK_FREE);
//				return;
//			}
//
//			playerDataManager.updTask(attacker, TaskType.COND_ATTACK_PLAYER, 1, null);
//		}
//
//		Form attackForm = PbHelper.createForm(req.getForm());
//		StaticHero staticHero = null;
//		Hero hero = null;
//		if (attackForm.getCommander() > 0) {
//			hero = attacker.heros.get(attackForm.getCommander());
//			if (hero == null || hero.getCount() <= 0) {
//				handler.sendErrorMsgToPlayer(GameError.NO_HERO);
//				return;
//			}
//
//			staticHero = staticHeroDataMgr.getStaticHero(hero.getHeroId());
//			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//				return;
//			}
//
//			if (staticHero.getType() != 2) {
//				handler.sendErrorMsgToPlayer(GameError.NOT_HERO);
//				return;
//			}
//		}
//
//		int maxTankCount = playerDataManager.formTankCount(attacker, staticHero);
//		if (!playerDataManager.checkAndSubTank(attacker, attackForm, maxTankCount)) {
//			handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
//			return;
//		}
//
//		playerDataManager.subPower(attacker.lord, 1);
//
//		if (hero != null) {
//			hero.setCount(hero.getCount() - 1);
//		}
//
//		int marchTime = marchTime(attacker, pos);
//		int now = TimeHelper.getCurrentSecond();
//		Army army = new Army(attacker.maxKey(), pos, ArmyState.MARCH, attackForm, marchTime, now + marchTime);
//		attacker.armys.add(army);
//
//		March march = new March(attacker, army);
//		worldDataManager.addMarch(march);
//
//		if (staticMine == null) {
//			playerDataManager.clearAttackFree(attacker);
//		}
//
//		AttackPosRs.Builder builder = AttackPosRs.newBuilder();
//		builder.setArmy(PbHelper.createArmyPb(army));
//		handler.sendMsgToPlayer(AttackPosRs.ext, builder.build());
//		if (staticMine == null) {
//			activityDataManager.updActivity(attacker, ActivityConst.ACT_ATTACK, 1, 0);
//		}
//
//		if (defencer != null) {
//			playerDataManager.synInvasionToPlayer(defencer, march);
//		}
//	}
//
//	// private void haustMineTank(Form form, Fighter fighter) {
//	// int killed = 0;
//	// for (int i = 0; i < fighter.forces.length; i++) {
//	// Force force = fighter.forces[i];
//	// if (force != null) {
//	// killed = force.killed;
//	// if (killed > 0) {
//	// form.c[i] = form.c[i] - killed;
//	// }
//	// }
//	// }
//	// }
//
//	// private boolean fightMineNpc(Player player, Army army, StaticMine
//	// staticMine, int now) {
//	// int pos = army.getTarget();
//	//
//	// Form mineForm = worldDataManager.getMineForm(pos, staticMine.getLv());
//	// StaticMineForm staticMineForm = worldDataManager.getStaticMineForm(pos);
//	//
//	// Fighter attacker = FightService.getInst().createFighter(player, army.getForm(), 3);
//	// Fighter defencer = FightService.getInst().createFighter(staticMineForm);
//	//
//	// FightLogic fightLogic = new FightLogic(attacker, defencer,
//	// FirstActType.ATTACKER, true);
//	// fightLogic.packForm(army.getForm(), mineForm);
//	//
//	// fightLogic.fight();
//	// CommonPb.Record record = fightLogic.generateRecord();
//	//
//	// Map<Integer, RptTank> attackHaust = haustArmyTank(player, attacker,
//	// army.getForm(), 0.8);
//	// Map<Integer, RptTank> defenceHaust =
//	// FightService.getInst().statisticHaustTank(defencer);
//	//
//	// RptAtkMine.Builder rptAtkMine = RptAtkMine.newBuilder();
//	// rptAtkMine.setFirst(fightLogic.attackerIsFirst());
//	// rptAtkMine.setHonour(0);
//	// rptAtkMine.setAttacker(createRptMan(player,
//	// army.getForm().getCommander(), attackHaust, 0));
//	// rptAtkMine.setDefencer(createRptMine(pos, staticMine, defenceHaust));
//	// rptAtkMine.setRecord(record);
//	// int result = fightLogic.getWinState();
//	//
//	// activityDataManager.tankDestory(player, defenceHaust);// 疯狂歼灭坦克
//	//
//	// if (result == 1) {// 攻方胜利
//	// worldDataManager.resetMineForm(pos, staticMine.getLv());
//	//
//	// StaticMineLv staticMineLv =
//	// staticWorldDataMgr.getStaticMineLv(staticMine.getLv());
//	// int heroId = army.getForm().getCommander();
//	// StaticHero staticHero = null;
//	// if (heroId != 0) {
//	// staticHero = staticHeroDataMgr.getStaticHero(heroId);
//	// }
//	//
//	// int exp = (int) (staticMineLv.getExp() *
//	// FightService.getInst().effectMineExpAdd(player, staticHero));
//	// playerDataManager.addExp(player.lord, exp);
//	//
//	// Award award = mineDropOneAward(player, staticMine.getDropOne());
//	//
//	// collectArmy(player, army, now, staticMine, staticMineLv.getProduction(),
//	// 0);
//	//
//	// rptAtkMine.setResult(true);
//	// rptAtkMine.addAward(PbHelper.createAwardPb(AwardType.EXP, 0, exp));
//	// if (award != null) {
//	// StaticProp staticProp = staticPropDataMgr.getStaticProp(award.getId());
//	// if (staticProp != null && staticProp.getColor() >= 4) {
//	// chatService.sendWorldChat(chatService.createSysChat(SysChatId.ATTACK_MINE,
//	// player.lord.getNick(), staticProp.getPropName()));
//	// }
//	//
//	// rptAtkMine.addAward(award);
//	// }
//	//
//	// RptAtkMine rpt = rptAtkMine.build();
//	// playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now),
//	// MailType.MOLD_ATTACK_MINE, now, String.valueOf(staticMine.getType()),
//	// String.valueOf(staticMine.getLv()));
//	// playerDataManager.updTask(player, TaskType.COND_WIN_RESOURCE, 1,
//	// staticMine.getLv());
//	//
//	// activityDataManager.profoto(player, staticMine.getLv());// 哈洛克宝藏活动
//	// return false;
//	// } else if (result == 2) {
//	// haustMineTank(mineForm, defencer);
//	// backHero(player, army.getForm());
//	// rptAtkMine.setResult(false);
//	//
//	// RptAtkMine rpt = rptAtkMine.build();
//	// playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now),
//	// MailType.MOLD_ATTACK_MINE, now, String.valueOf(staticMine.getType()),
//	// String.valueOf(staticMine.getLv()));
//	//
//	// return true;
//	// } else {
//	// haustMineTank(mineForm, defencer);
//	//
//	// rptAtkMine.setResult(false);
//	// retreatArmy(player, army, now);
//	// RptAtkMine rpt = rptAtkMine.build();
//	// playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now),
//	// MailType.MOLD_ATTACK_MINE, now, String.valueOf(staticMine.getType()),
//	// String.valueOf(staticMine.getLv()));
//	//
//	// return false;
//	// }
//	//
//	// }
//
//	private boolean fightMineNpc(Player player, Army army, StaticMine staticMine, int now) {
//		int pos = army.getTarget();
//		StaticMineForm staticMineForm = worldDataManager.getMineForm(pos, staticMine.getLv());
//
//		Fighter attacker = FightService.getInst().createFighter(player, army.getForm(), 3);
//		Fighter defencer = FightService.getInst().createFighter(staticMineForm);
//
//		FightLogic fightLogic = new FightLogic(attacker, defencer, FirstActType.ATTACKER, true);
//		fightLogic.packForm(army.getForm(), PbHelper.createForm(staticMineForm.getForm()));
//
//		fightLogic.fight();
//		CommonPb.Record record = fightLogic.generateRecord();
//
//		double worldRatio = staffingDataManager.getWorldRatio();
//		Map<Integer, RptTank> attackHaust = haustArmyTank(player, attacker, army.getForm(), worldRatio);
//		Map<Integer, RptTank> defenceHaust = FightService.getInst().statisticHaustTank(defencer);
//
//		// worldDataManager.resetMineForm(pos, staticMine.getLv());
//
//		RptAtkMine.Builder rptAtkMine = RptAtkMine.newBuilder();
//		rptAtkMine.setFirst(fightLogic.attackerIsFirst());
//		rptAtkMine.setHonour(0);
//		rptAtkMine.setAttacker(createRptMan(player, army.getForm().getCommander(), attackHaust, 0));
//		rptAtkMine.setDefencer(createRptMine(pos, staticMine, defenceHaust));
//		rptAtkMine.setRecord(record);
//		int result = fightLogic.getWinState();
//
//		activityDataManager.tankDestory(player, defenceHaust, false);// 疯狂歼灭坦克
//
//		if (result == 1) {// 攻方胜利
//			worldDataManager.resetMineForm(pos, staticMine.getLv());
//
//			StaticMineLv staticMineLv = staticWorldDataMgr.getStaticMineLv(staticMine.getLv());
//			int heroId = army.getForm().getCommander();
//			StaticHero staticHero = null;
//			if (heroId != 0) {
//				staticHero = staticHeroDataMgr.getStaticHero(heroId);
//			}
//
//			int exp = (int) (staticMineLv.getExp() * FightService.getInst().effectMineExpAdd(player, staticHero));
//			playerDataManager.addExp(player.lord, exp);
//
//			Award award = mineDropOneAward(player, staticMine.getDropOne());
//
//			collectArmy(player, army, now, staticMine, staticMineLv.getProduction(), 0);
//
//			rptAtkMine.setResult(true);
//			rptAtkMine.addAward(PbHelper.createAwardPb(AwardType.EXP, 0, exp));
//			if (award != null) {
//				StaticProp staticProp = staticPropDataMgr.getStaticProp(award.getId());
//				if (staticProp != null && staticProp.getColor() >= 4) {
//					chatService.sendWorldChat(chatService.createSysChat(SysChatId.ATTACK_MINE, player.lord.getNick(), staticProp.getPropName()));
//				}
//
//				rptAtkMine.addAward(award);
//			}
//
//			RptAtkMine rpt = rptAtkMine.build();
//			playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_ATTACK_MINE, now, String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//			playerDataManager.updTask(player, TaskType.COND_WIN_RESOURCE, 1, staticMine.getLv());
//
//			activityDataManager.profoto(player, staticMine.getLv());// 哈洛克宝藏活动
//			return false;
//		} else if (result == 2) {
//			backHero(player, army.getForm());
//			rptAtkMine.setResult(false);
//
//			RptAtkMine rpt = rptAtkMine.build();
//			playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_ATTACK_MINE, now, String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			return true;
//		} else {
//			// haustMineTank(mineForm, defencer);
//
//			rptAtkMine.setResult(false);
//
//			retreatArmy(player, army, now);
//			RptAtkMine rpt = rptAtkMine.build();
//			playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_ATTACK_MINE, now, String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			return false;
//		}
//
//	}
//
//	// 消灭敌方部队
//	// private void eliminateArmy(Player target, Army army) {
//	// target.armys.remove(army);
//	// int heroId = army.getForm().getCommander();
//	// if (heroId > 0) {
//	// playerDataManager.addHero(target, heroId, 1);
//	// }
//	// worldDataManager.removeGuard(guard);
//	// }
//
//	private void eliminateGuard(Guard guard) {
//		Player target = guard.getPlayer();
//		Army army = guard.getArmy();
//		target.armys.remove(army);
//		int heroId = army.getForm().getCommander();
//		if (heroId > 0) {
//			playerDataManager.addHero(target, heroId, 1);
//		}
//		worldDataManager.removeGuard(guard);
//	}
//
//	private void backHero(Player player, Form form) {
//		if (form.getCommander() > 0) {
//			playerDataManager.addHero(player, form.getCommander(), 1);
//		}
//	}
//
//	private Award mineDropOneAward(Player player, List<List<Integer>> drop) {
//		if (drop != null && !drop.isEmpty()) {
//			for (List<Integer> award : drop) {
//				if (award.size() != 4) {
//					continue;
//				}
//
//				int prob = award.get(3);
//				int revelry[] = activityDataManager.revelry();
//				prob += revelry[2];
//
//				if (RandomHelper.isHitRangeIn100(prob)) {
//					int type = award.get(0);
//					int id = award.get(1);
//					int count = award.get(2);
//					int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.FIGHT_MINE);
//					return PbHelper.createAwardPb(type, id, count, keyId);
//				}
//			}
//		}
//		return null;
//	}
//
//	private boolean fightMineGuard(Player player, Army army, StaticMine staticMine, Guard guard, int now) {
//		int pos = army.getTarget();
//		Player guardPlayer = guard.getPlayer();
//		Form targetForm = guard.getArmy().getForm();
//
//		StaticMineLv staticMineLv = staticWorldDataMgr.getStaticMineLv(staticMine.getLv());
//		long get = playerDataManager.calcCollect(guardPlayer, guard.getArmy(), now, staticMine, staticMineLv.getProduction());
//
//		Fighter attacker = FightService.getInst().createFighter(player, army.getForm(), 3);
//		Fighter defencer = FightService.getInst().createFighter(guardPlayer, targetForm, 3);
//
//		FightLogic fightLogic = new FightLogic(attacker, defencer, FirstActType.FISRT_VALUE_1, true);
//		fightLogic.packForm(army.getForm(), targetForm);
//		fightLogic.fight();
//		CommonPb.Record record = fightLogic.generateRecord();
//
//		double worldRatio = staffingDataManager.getWorldRatio();
//		Map<Integer, RptTank> attackHaust = haustArmyTank(player, attacker, army.getForm(), worldRatio);
//		Map<Integer, RptTank> defenceHaust = haustArmyTank(guardPlayer, defencer, targetForm, worldRatio);
//
//		activityDataManager.tankDestory(player, defenceHaust, true);// 疯狂歼灭坦克
//		activityDataManager.tankDestory(guardPlayer, attackHaust, true);// 疯狂歼灭坦克
//
//		int result = fightLogic.getWinState();
//
//		int honor = playerDataManager.calcHonor(attackHaust, defenceHaust, worldRatio);
//		if (honor > 0) {
//			if (result == 1) {
//				honor = playerDataManager.giveHonor(player, guardPlayer, honor);
//			} else {
//				honor = playerDataManager.giveHonor(guardPlayer, player, honor);
//			}
//		}
//
//		RptAtkMine.Builder rptAtkMine = RptAtkMine.newBuilder();
//		rptAtkMine.setFirst(fightLogic.attackerIsFirst());
//		rptAtkMine.setHonour(honor);
//		rptAtkMine.setAttacker(createRptMan(player, army.getForm().getCommander(), attackHaust, 0));
//		rptAtkMine.setDefencer(createRptMine(pos, staticMine, guardPlayer, targetForm.getCommander(), defenceHaust));
//		rptAtkMine.setRecord(record);
//
//		if (result == 1) {// 攻方胜利
//			int staffingExp = playerDataManager.calcStaffingExp(defenceHaust, worldRatio);
//			if (staffingExp > 0) {
//				staffingExp = playerDataManager.giveStaffingExp(player, guardPlayer, staffingExp);
//			}
//
//			rptAtkMine.setStaffingExp(staffingExp);
//
//			eliminateGuard(guard);
//			collectArmy(player, army, now, staticMine, staticMineLv.getProduction(), get);
//
//			rptAtkMine.setResult(true);
//			long param = 0;
//			if (army.getGrab() != null) {
//				rptAtkMine.setGrab(PbHelper.createGrabPb(army.getGrab()));
//				param = army.getGrab().rs[0] + army.getGrab().rs[1] + army.getGrab().rs[2] + army.getGrab().rs[3] + army.getGrab().rs[4];
//			}
//
//			RptAtkMine rpt = rptAtkMine.build();
//			Mail mail = playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_ATTACK_MINE, now, String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			playerDataManager.sendReportMail(guardPlayer, createDefMineReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//			partyDataManager.addPartyTrend(13, guardPlayer, player, String.valueOf(param));// 军团军情
//
//			playerDataManager.updTask(player, TaskType.COND_WIN_RESOURCE, 1, staticMine.getLv());// 战胜世界资源点
//			activityDataManager.profoto(player, staticMine.getLv());// 哈洛克宝藏活动
//
//			// 分享战力top
//			chatService.shareChallengeFightRankTop5(player, guardPlayer, mail, result);
//
//			playerDataManager.synArmyToPlayer(guardPlayer, new ArmyStatu(guardPlayer.roleId, guard.getArmy().getKeyId(), 3));
//			return false;
//		} else if (result == 2) {
//			rptAtkMine.setResult(false);
//			backHero(player, army.getForm());
//
//			recollectArmy(guardPlayer, guard.getArmy(), now, staticMine, staticMineLv.getProduction(), get);
//
//			RptAtkMine rpt = rptAtkMine.build();
//			Mail mail = playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_ATTACK_MINE, now, String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			playerDataManager.sendReportMail(guardPlayer, createDefMineReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//			partyDataManager.addPartyTrend(12, guardPlayer, player, null);
//
//			// 分享战力top
//			chatService.shareChallengeFightRankTop5(player, guardPlayer, mail, result);
//
//			playerDataManager.synArmyToPlayer(guardPlayer, new ArmyStatu(guardPlayer.roleId, guard.getArmy().getKeyId(), 4));
//			return true;
//		} else {
//			rptAtkMine.setResult(false);
//
//			retreatArmy(player, army, now);
//
//			recollectArmy(guardPlayer, guard.getArmy(), now, staticMine, staticMineLv.getProduction(), get);
//
//			RptAtkMine rpt = rptAtkMine.build();
//
//			playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_ATTACK_MINE, now, String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			playerDataManager.sendReportMail(guardPlayer, createDefMineReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//			playerDataManager.synArmyToPlayer(guardPlayer, new ArmyStatu(guardPlayer.roleId, guard.getArmy().getKeyId(), 4));
//			return false;
//		}
//	}
//
//	/**
//	 * 
//	 * Method: fightMine
//	 * 
//	 * @Description: 攻击矿 @param player @param army @param staticMine @return @return
//	 * boolean @throws
//	 */
//	private boolean fightMine(Player player, Army army, StaticMine staticMine, int now) {
//		int pos = army.getTarget();
//		Guard guard = worldDataManager.getMineGuard(pos);
//		if (guard != null) // 有驻军
//			if (guard.getPlayer() != player) {
//				if (partyDataManager.isSameParty(player.roleId, guard.getPlayer().roleId)) {
//					playerDataManager.sendNormalMail(player, MailType.MOLD_HOLD, now, String.valueOf(staticMine.getType()), String.valueOf(guard.getPlayer().lord.getNick()));
//					retreatArmy(player, army, now);
//					return false;
//				}
//
//				return fightMineGuard(player, army, staticMine, guard, now);
//			} else {
//				playerDataManager.sendNormalMail(player, MailType.MOLD_HOLD, now, String.valueOf(staticMine.getType()), String.valueOf(player.lord.getNick()));
//				retreatArmy(player, army, now);
//				return false;
//			}
//		else
//			return fightMineNpc(player, army, staticMine, now);
//	}
//
//	public void subForceToForm(Fighter fighter, Form form) {
//		int[] c = form.c;
//		for (int i = 0; i < c.length; i++) {
//			Force force = fighter.forces[i];
//			if (force != null) {
//				form.c[i] = force.count;
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * Method: haustArmyTank
//	 * 
//	 * @Description: 出征部队的战损 @param player @param fighter @param
//	 * ratio @return @return Map<Integer,RptTank> @throws
//	 */
//	private Map<Integer, RptTank> haustArmyTank(Player player, Fighter fighter, Form form, double ratio) {
//		Map<Integer, RptTank> map = new HashMap<Integer, RptTank>();
//		Map<Integer, Tank> tanks = player.tanks;
//		int killed = 0;
//		int tankId = 0;
//		for (Force force : fighter.forces) {
//			if (force != null) {
//				killed = force.killed;
//				if (killed > 0) {
//					tankId = force.staticTank.getTankId();
//					RptTank rptTank = map.get(tankId);
//					if (rptTank != null) {
//						rptTank.setCount(rptTank.getCount() + killed);
//					} else {
//						rptTank = new RptTank(tankId, killed);
//						map.put(tankId, rptTank);
//					}
//				}
//			}
//		}
//
//		Iterator<RptTank> it = map.values().iterator();
//		while (it.hasNext()) {
//			RptTank rptTank = (RptTank) it.next();
//			killed = rptTank.getCount();
//			int repair = (int) Math.ceil(ratio * killed);
//			Tank tank = tanks.get(rptTank.getTankId());
//			tank.setRest(tank.getRest() + repair);
//		}
//
//		subForceToForm(fighter, form);
//		return map;
//	}
//
//	/**
//	 * 
//	 * Method: haustHomeTank
//	 * 
//	 * @Description: 基地防守的战损 @param player @param fighter @param
//	 * ratio @return @return Map<Integer,RptTank> @throws
//	 */
//	private Map<Integer, RptTank> haustHomeTank(Player player, Fighter fighter, double ratio) {
//		Map<Integer, RptTank> map = new HashMap<Integer, RptTank>();
//		Map<Integer, Tank> tanks = player.tanks;
//		int killed = 0;
//		int tankId = 0;
//		int alive = 0;
//		for (Force force : fighter.forces) {
//			if (force != null) {
//				killed = force.killed;
//				alive = force.count;
//				tankId = force.staticTank.getTankId();
//
//				if (killed > 0) {
//					RptTank rptTank = map.get(tankId);
//					if (rptTank != null) {
//						rptTank.setCount(rptTank.getCount() + killed);
//					} else {
//						rptTank = new RptTank(tankId, killed);
//						map.put(tankId, rptTank);
//					}
//				}
//
//				if (alive > 0) {
//					Tank tank = tanks.get(tankId);
//					tank.setCount(tank.getCount() + alive);
//				}
//			}
//		}
//
//		Iterator<RptTank> it = map.values().iterator();
//		while (it.hasNext()) {
//			RptTank rptTank = (RptTank) it.next();
//			killed = rptTank.getCount();
//			int repair = (int) Math.ceil(ratio * killed);
//			Tank tank = tanks.get(rptTank.getTankId());
//			tank.setRest(tank.getRest() + repair);
//		}
//
//		return map;
//	}
//
//	private int[] calcWinPros(Player attacker, int heroId) {
//		int[] v = new int[2];
//		StaticLordLv staticLordLv = staticLordDataMgr.getStaticLordLv(attacker.lord.getLevel());
//		v[0] = v[1] = staticLordLv.getWinPros();
//
//		StaticHero staticHero = staticHeroDataMgr.getStaticHero(heroId);
//		if (staticHero != null && staticHero.getSkillId() == 7) {
//			v[1] += staticHero.getSkillValue();
//		}
//
//		StaticVip staticVip = staticVipDataMgr.getStaticVip(attacker.lord.getVip());
//		if (staticVip != null) {
//			v[1] += staticVip.getSubPros();
//		}
//
//		if (playerDataManager.isRuins(attacker)) {
//			v[0] /= 2;
//		}
//
//		return v;
//	}
//
//	private boolean fightHomePlayer(Player player, Army army, Player target, int now) {
//		boolean state = false;
//		boolean targetRuins = playerDataManager.isRuins(target);
//
//		Form targetForm = playerDataManager.createHomeDefendForm(target);
//		if (targetForm == null) {// 没有设置防守阵型，直接胜利
//			long load = playerDataManager.calcLoad(player, army.getForm());
//			Grab grab = calcGrab(target, load);
//			playerDataManager.undergoGrab(target, grab);
//			army.setGrab(grab);
//
//			retreatArmy(player, army, now);
//
//			int[] pros = calcWinPros(player, army.getForm().getCommander());
//			winPros(player, target, pros);
//
//			RptAtkHome.Builder rptAtkHome = RptAtkHome.newBuilder();
//			rptAtkHome.setResult(true);
//			rptAtkHome.setFirst(true);
//			rptAtkHome.setHonour(0);
//			rptAtkHome.setAttacker(createRptMan(player, army.getForm().getCommander(), null, pros[0]));
//			rptAtkHome.setDefencer(createRptMan(target, 0, null, -pros[1]));
//			rptAtkHome.setGrab(PbHelper.createGrabPb(grab));
//
//			RptAtkHome rpt = rptAtkHome.build();
//			Mail mail = playerDataManager.sendReportMail(player, createAtkHomeReport(rpt, now), MailType.MOLD_ATTACK_PLAYER, now, target.lord.getNick(), String.valueOf(target.lord.getLevel()));
//			long param = grab.rs[0] + grab.rs[1] + grab.rs[2] + grab.rs[3] + grab.rs[4];
//			partyDataManager.addPartyTrend(13, target, player, String.valueOf(param));
//
//			playerDataManager.sendReportMail(target, createDefHomeReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//			// 分享战力top
//			chatService.shareChallengeFightRankTop5(player, target, mail, 1);
//
//			playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, army.getKeyId(), 1));
//		} else {
//			Fighter attacker = FightService.getInst().createFighter(player, army.getForm(), 3);
//			Fighter defencer = FightService.getInst().createFighter(target, targetForm, 3);
//
//			FightLogic fightLogic = new FightLogic(attacker, defencer, FirstActType.FISRT_VALUE_1, true);
//			fightLogic.packForm(army.getForm(), targetForm);
//
//			fightLogic.fight();
//			CommonPb.Record record = fightLogic.generateRecord();
//
//			double worldRatio = staffingDataManager.getWorldRatio();
//			Map<Integer, RptTank> attackHaust = haustArmyTank(player, attacker, army.getForm(), worldRatio);
//			Map<Integer, RptTank> defenceHaust = haustHomeTank(target, defencer, worldRatio);
//
//			activityDataManager.tankDestory(player, defenceHaust, true);// 疯狂歼灭坦克
//			activityDataManager.tankDestory(target, attackHaust, true);// 疯狂歼灭坦克
//
//			int[] pros;
//			int result = fightLogic.getWinState();
//			if (result == 1) {
//				pros = calcWinPros(player, army.getForm().getCommander());
//				winPros(player, target, pros);
//				// honor = playerDataManager.giveHonor(player, target, honor);
//			} else {
//				pros = new int[] { 0, 0 };
//				// honor = playerDataManager.giveHonor(target, player, honor);
//			}
//
//			int honor = playerDataManager.calcHonor(attackHaust, defenceHaust, worldRatio);
//			if (honor > 0) {
//				if (result == 1) {
//					honor = playerDataManager.giveHonor(player, target, honor);
//				} else {
//					honor = playerDataManager.giveHonor(target, player, honor);
//				}
//			}
//
//			RptAtkHome.Builder rptAtkHome = RptAtkHome.newBuilder();
//			rptAtkHome.setFirst(fightLogic.attackerIsFirst());
//			rptAtkHome.setHonour(honor);
//			rptAtkHome.setAttacker(createRptMan(player, army.getForm().getCommander(), attackHaust, pros[0]));
//			rptAtkHome.setDefencer(createRptMan(target, targetForm.getCommander(), defenceHaust, -pros[1]));
//			rptAtkHome.setRecord(record);
//
//			if (result == 1) {// 攻方胜利
//				long load = playerDataManager.calcLoad(player, army.getForm());
//				Grab grab = calcGrab(target, load);
//				playerDataManager.undergoGrab(target, grab);
//				army.setGrab(grab);
//				retreatArmy(player, army, now);
//
//				rptAtkHome.setResult(true);
//				rptAtkHome.setGrab(PbHelper.createGrabPb(grab));
//
//				RptAtkHome rpt = rptAtkHome.build();
//				Mail mail = playerDataManager.sendReportMail(player, createAtkHomeReport(rpt, now), MailType.MOLD_ATTACK_PLAYER, now, target.lord.getNick(), String.valueOf(target.lord.getLevel()));
//
//				playerDataManager.sendReportMail(target, createDefHomeReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//				long param = grab.rs[0] + grab.rs[1] + grab.rs[2] + grab.rs[3] + grab.rs[4];
//				partyDataManager.addPartyTrend(13, target, player, String.valueOf(param));
//
//				// 分享战力top
//				chatService.shareChallengeFightRankTop5(player, target, mail, result);
//
//				playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, army.getKeyId(), 1));
//			} else if (result == 2) {
//				rptAtkHome.setResult(false);
//				backHero(player, army.getForm());
//
//				RptAtkHome rpt = rptAtkHome.build();
//				Mail mail = playerDataManager.sendReportMail(player, createAtkHomeReport(rpt, now), MailType.MOLD_ATTACK_PLAYER, now, target.lord.getNick(), String.valueOf(target.lord.getLevel()));
//
//				playerDataManager.sendReportMail(target, createDefHomeReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//				partyDataManager.addPartyTrend(12, target, player, null);
//
//				// 分享战力top
//				chatService.shareChallengeFightRankTop5(player, target, mail, result);
//
//				playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, army.getKeyId(), 1));
//				state = true;
//			} else {
//				rptAtkHome.setResult(false);
//
//				retreatArmy(player, army, now);
//
//				RptAtkHome rpt = rptAtkHome.build();
//				Mail mail = playerDataManager.sendReportMail(player, createAtkHomeReport(rpt, now), MailType.MOLD_ATTACK_PLAYER, now, target.lord.getNick(), String.valueOf(target.lord.getLevel()));
//
//				playerDataManager.sendReportMail(target, createDefHomeReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//				// 分享战力top
//				chatService.shareChallengeFightRankTop5(player, target, mail, result);
//
//				playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, army.getKeyId(), 1));
//			}
//		}
//
//		if (!targetRuins) {
//			if (playerDataManager.isRuins(target)) {
//				playerDataManager.sendNormalMail(target, MailType.MOLD_RUINS, now, player.lord.getNick());
//			}
//		}
//
//		return state;
//	}
//
//	private Report createAtkHomeReport(RptAtkHome rpt, int now) {
//		Report.Builder report = Report.newBuilder();
//		report.setAtkHome(rpt);
//		report.setTime(now);
//		return report.build();
//	}
//
//	private Report createDefHomeReport(RptAtkHome rpt, int now) {
//		Report.Builder report = Report.newBuilder();
//		report.setDefHome(rpt);
//		report.setTime(now);
//		return report.build();
//	}
//
//	private Report createAtkMineReport(RptAtkMine rpt, int now) {
//		Report.Builder report = Report.newBuilder();
//		report.setAtkMine(rpt);
//		report.setTime(now);
//		return report.build();
//	}
//
//	private Report createDefMineReport(RptAtkMine rpt, int now) {
//		Report.Builder report = Report.newBuilder();
//		report.setDefMine(rpt);
//		report.setTime(now);
//		return report.build();
//	}
//
//	private void winPros(Player attacker, Player defencer, int[] v) {
//		playerDataManager.addPros(attacker, v[0]);
//		playerDataManager.subProsByAttack(defencer, v[1], attacker);
//	}
//
//	private boolean fightHomeGuard(Player player, Army army, Player target, Guard guard, int now) {
//		boolean state = false;
//		boolean targetRuins = playerDataManager.isRuins(target);
//
//		Player guardPlayer = guard.getPlayer();
//		Form targetForm = guard.getArmy().getForm();
//
//		Fighter attacker = FightService.getInst().createFighter(player, army.getForm(), 3);
//		Fighter defencer = FightService.getInst().createFighter(guardPlayer, targetForm, 3);
//
//		FightLogic fightLogic = new FightLogic(attacker, defencer, FirstActType.FISRT_VALUE_1, true);
//		fightLogic.packForm(army.getForm(), targetForm);
//		fightLogic.fight();
//		CommonPb.Record record = fightLogic.generateRecord();
//
//		double worldRatio = staffingDataManager.getWorldRatio();
//		Map<Integer, RptTank> attackHaust = haustArmyTank(player, attacker, army.getForm(), worldRatio);
//		Map<Integer, RptTank> defenceHaust = haustArmyTank(guardPlayer, defencer, targetForm, worldRatio);
//
//		activityDataManager.tankDestory(player, defenceHaust, true);// 疯狂歼灭坦克
//		activityDataManager.tankDestory(target, attackHaust, true);// 疯狂歼灭坦克
//
//		int[] pros;
//		int result = fightLogic.getWinState();
//		if (result == 1) {
//			pros = calcWinPros(player, army.getForm().getCommander());
//			winPros(player, target, pros);
//		} else {
//			pros = new int[] { 0, 0 };
//		}
//
//		RptAtkHome.Builder rptAtkHome = RptAtkHome.newBuilder();
//		rptAtkHome.setFirst(fightLogic.attackerIsFirst());
//		rptAtkHome.setHonour(0);
//		rptAtkHome.setFriend(guardPlayer.lord.getNick());
//		rptAtkHome.setAttacker(createRptMan(player, army.getForm().getCommander(), attackHaust, pros[0]));
//		rptAtkHome.setDefencer(createRptMan(target, targetForm.getCommander(), defenceHaust, -pros[1]));
//		rptAtkHome.setRecord(record);
//
//		if (result == 1) {// 攻方胜利
//			long load = playerDataManager.calcLoad(player, army.getForm());
//			Grab grab = calcGrab(target, load);
//			playerDataManager.undergoGrab(target, grab);
//			army.setGrab(grab);
//			retreatArmy(player, army, now);
//
//			rptAtkHome.setResult(true);
//			rptAtkHome.setGrab(PbHelper.createGrabPb(grab));
//
//			eliminateGuard(guard);
//
//			RptAtkHome rpt = rptAtkHome.build();
//			Mail mail = playerDataManager.sendReportMail(player, createAtkHomeReport(rpt, now), MailType.MOLD_ATTACK_PLAYER, now, target.lord.getNick(), String.valueOf(target.lord.getLevel()));
//
//			playerDataManager.sendReportMail(target, createDefHomeReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//			playerDataManager.sendReportMail(guardPlayer, createDefHomeReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//			// 分享战力top
//			chatService.shareChallengeFightRankTop5(player, guardPlayer, mail, result);
//
//			playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, army.getKeyId(), 1));
//			ArmyStatu guardStatu = new ArmyStatu(guardPlayer.roleId, guard.getArmy().getKeyId(), 3);
//			playerDataManager.synArmyToPlayer(target, guardStatu);
//			playerDataManager.synArmyToPlayer(guardPlayer, guardStatu);
//		} else if (result == 2) {
//			rptAtkHome.setResult(false);
//			backHero(player, army.getForm());
//
//			RptAtkHome rpt = rptAtkHome.build();
//			Mail mail = playerDataManager.sendReportMail(player, createAtkHomeReport(rpt, now), MailType.MOLD_ATTACK_PLAYER, now, target.lord.getNick(), String.valueOf(target.lord.getLevel()));
//
//			playerDataManager.sendReportMail(target, createDefHomeReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//			playerDataManager.sendReportMail(guardPlayer, createDefHomeReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//			// 分享战力top
//			chatService.shareChallengeFightRankTop5(player, guardPlayer, mail, result);
//
//			playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, army.getKeyId(), 1));
//			ArmyStatu guardStatu = new ArmyStatu(guardPlayer.roleId, guard.getArmy().getKeyId(), 4);
//			playerDataManager.synArmyToPlayer(target, guardStatu);
//			playerDataManager.synArmyToPlayer(guardPlayer, guardStatu);
//			state = true;
//		} else {
//			rptAtkHome.setResult(false);
//
//			retreatArmy(player, army, now);
//			playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, army.getKeyId(), 1));
//
//			RptAtkHome rpt = rptAtkHome.build();
//			playerDataManager.sendReportMail(player, createAtkHomeReport(rpt, now), MailType.MOLD_ATTACK_PLAYER, now, target.lord.getNick(), String.valueOf(target.lord.getLevel()));
//
//			playerDataManager.sendReportMail(target, createDefHomeReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//			playerDataManager.sendReportMail(guardPlayer, createDefHomeReport(rpt, now), MailType.MOLD_DEFEND, now, player.lord.getNick(), String.valueOf(player.lord.getLevel()));
//
//			playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, army.getKeyId(), 1));
//			ArmyStatu armyStatu = new ArmyStatu(guardPlayer.roleId, guard.getArmy().getKeyId(), 4);
//			playerDataManager.synArmyToPlayer(target, armyStatu);
//			playerDataManager.synArmyToPlayer(guardPlayer, armyStatu);
//		}
//
//		if (!targetRuins) {
//			if (playerDataManager.isRuins(target)) {
//				playerDataManager.sendNormalMail(target, MailType.MOLD_RUINS, now, player.lord.getNick());
//			}
//		}
//
//		return state;
//	}
//
//	/**
//	 * 
//	 * Method: fightPlayer
//	 * 
//	 * @Description: 攻击玩家 @param player @param army @return @return boolean @throws
//	 */
//	private boolean fightHome(Player player, Army army, Player target, int now) {
//		if (player.roleId == target.roleId) {// 直接返回
//			retreatArmy(player, army, now);
//			playerDataManager.sendNormalMail(player, MailType.MOLD_RETREAT, now, player.lord.getNick());
//			return false;
//		}
//
//		if (partyDataManager.isSameParty(player.roleId, target.roleId)) {
//			retreatArmy(player, army, now);
//			playerDataManager.sendNormalMail(player, MailType.MOLD_RETREAT, now, target.lord.getNick());
//			return false;
//		}
//
//		int pos = army.getTarget();
//		// Player target = worldDataManager.getPosData(pos);
//		// if (target == null || player.roleId == target.roleId) {// 直接返回
//		// retreatArmy(player, army, now);
//		// return false;
//		// }
//
//		if (target.effects.containsKey(EffectType.ATTACK_FREE)) {
//			retreatArmy(player, army, now);
//			playerDataManager.sendNormalMail(player, MailType.MOLD_FREE_ATTACK, now, target.lord.getNick());
//			return false;
//		}
//
//		Guard guard = worldDataManager.getHomeGuard(pos);
//		if (guard != null) // 有驻军
//			return fightHomeGuard(player, army, target, guard, now);
//		else
//			return fightHomePlayer(player, army, target, now);
//	}
//
//	/**
//	 * 
//	 * Method: getInvasion
//	 * 
//	 * @Description: 进军数据 @param handler @return void @throws
//	 */
//	public void getInvasion(ClientHandler handler) {
//		// GameServer.GAME_LOGGER.error("getInvasion roleId:" +
//		// handler.getRoleId());
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		List<March> all = new ArrayList<March>();
//		List<March> list = worldDataManager.getMarch(player.lord.getPos());
//		if (list != null) {
//			all.addAll(list);
//		}
//
//		// for (Army army : player.armys) {
//		// if (army.getState() == ArmyState.COLLECT) {
//		// List<March> list = worldDataManager.getMarch(army.getTarget());
//		// if (list != null) {
//		// all.addAll(list);
//		// }
//		// }
//		// }
//
//		GetInvasionRs.Builder builder = GetInvasionRs.newBuilder();
//		for (int i = 0; i < all.size(); i++) {
//			builder.addInvasion(PbHelper.createInvasionPb(all.get(i)));
//		}
//
//		handler.sendMsgToPlayer(GetInvasionRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: getAid
//	 * 
//	 * @Description: 盟友驻军数据 @param handler @return void @throws
//	 */
//	public void getAid(ClientHandler handler) {
//		// GameServer.GAME_LOGGER.error("getAid roleId:" + handler.getRoleId());
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		List<Guard> list = worldDataManager.getGuard(player.lord.getPos());
//
//		GetAidRs.Builder builder = GetAidRs.newBuilder();
//		if (list != null) {
//			for (int i = 0; i < list.size(); i++) {
//				builder.addAid(PbHelper.createAidPb(list.get(i)));
//			}
//		}
//
//		handler.sendMsgToPlayer(GetAidRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: speedArmy
//	 * 
//	 * @Description: 加速行军 @param req @param handler @return void @throws
//	 */
//	public void speedArmy(SpeedQueRq req, ClientHandler handler) {
//		int keyId = req.getKeyId();
//		// int cost = req.getCost();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		List<Army> list = player.armys;
//		Army army = null;
//		boolean find = false;
//
//		for (Army e : list) {
//			if (e.getKeyId() == keyId) {
//				army = e;
//				find = true;
//				break;
//			}
//		}
//
//		if (!find) {
//			handler.sendErrorMsgToPlayer(GameError.NO_ARMY);
//			return;
//		}
//
//		if (ArmyState.MARCH != army.getState() && ArmyState.RETREAT != army.getState() && ArmyState.AID != army.getState()) {
//			handler.sendErrorMsgToPlayer(GameError.NOT_IN_MARCH);
//			return;
//		}
//
//		int now = TimeHelper.getCurrentSecond();
//		SpeedQueRs.Builder builder = SpeedQueRs.newBuilder();
//
//		int leftTime = army.getEndTime() - now;
//		if (leftTime <= 0) {
//			leftTime = 1;
//		}
//
//		int sub = (int) Math.ceil(leftTime / 60.0);
//		Lord lord = player.lord;
//		if (lord.getGold() < sub) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//		playerDataManager.subGold(lord, sub, GoldCost.SPEED_BUILD);
//		army.setEndTime(now);
//
//		dealWorldAction(player, now);
//		builder.setGold(lord.getGold());
//		handler.sendMsgToPlayer(SpeedQueRs.ext, builder.build());
//		return;
//
//	}
//
//	private boolean marchEnd(Player player, Army army, int now) {
//		worldDataManager.removeMarch(player, army);
//		int pos = army.getTarget();
//		Player target = worldDataManager.getPosData(pos);
//		if (target != null) {// 攻击地图上的玩家
//			return fightHome(player, army, target, now);
//		} else {// 攻击矿
//			StaticMine staticMine = worldDataManager.evaluatePos(pos);
//			if (staticMine == null) {
//				retreatArmy(player, army, now);
//				playerDataManager.sendNormalMail(player, MailType.MOLD_TARGET_GONE, now, String.valueOf(pos));
//				return false;
//			}
//
//			return fightMine(player, army, staticMine, now);
//		}
//	}
//
//	private void aidEnd(Player player, Army army, int now) {
//		worldDataManager.removeMarch(player, army);
//		int pos = army.getTarget();
//		Player target = worldDataManager.getPosData(pos);
//		if (target != null) {// 地图上的玩家
//			if (player.roleId == target.roleId) {// 直接返回
//				retreatAidArmy(player, army, now);
//				playerDataManager.sendNormalMail(player, MailType.MOLD_RETREAT, now, player.lord.getNick());
//				return;
//			}
//
//			if (partyDataManager.isSameParty(player.roleId, target.roleId)) {
//				army.setState(ArmyState.WAIT);
//				worldDataManager.setGuard(new Guard(player, army));
//				playerDataManager.sendNormalMail(target, MailType.MOLD_GUARD, now, player.lord.getNick());
//
//				playerDataManager.synArmyToPlayer(target, new ArmyStatu(player.roleId, army.getKeyId(), 1));
//				return;
//			} else {
//				retreatAidArmy(player, army, now);
//				playerDataManager.sendNormalMail(player, MailType.MOLD_RETREAT, now, target.lord.getNick());
//				return;
//			}
//		} else {
//			retreatAidArmy(player, army, now);
//			playerDataManager.sendNormalMail(player, MailType.MOLD_AID_GONE, now, String.valueOf(pos));
//			return;
//		}
//	}
//
//	// private void sendTargetGoneMail(Player player, int pos) {
//	// playerDataManager.addReportMail(player, MailType.MOLD_TARGET_GONE,
//	// String.valueOf(pos));
//	// }
//
//	private void retreatEnd(Player player, Army army) {
//		// 部队返回
//		int[] p = army.getForm().p;
//		int[] c = army.getForm().c;
//		for (int i = 0; i < p.length; i++) {
//			if (p[i] > 0 && c[i] > 0) {
//				playerDataManager.addTank(player, p[i], c[i]);
//			}
//		}
//		// 将领返回
//		int heroId = army.getForm().getCommander();
//		if (heroId > 0) {
//			playerDataManager.addHero(player, heroId, 1);
//		}
//
//		// 加资源
//		Grab grab = army.getGrab();
//		if (grab != null) {
//			playerDataManager.gainGrab(player, grab);
//			StaticMine staticMine = worldDataManager.evaluatePos(army.getTarget());
//			if (staticMine != null) {
//				partyDataManager.collectMine(player.roleId, grab);
//				activityDataManager.resourceCollect(player, ActivityConst.ACT_COLLECT_RESOURCE, grab);// 资源采集活动
//				activityDataManager.beeCollect(player, ActivityConst.ACT_COLLECT_RESOURCE, grab);// 勤劳致富
//				activityDataManager.amyRebate(player, 0, grab.rs);// 建军节欢庆
//			}
//		}
//	}
//
//	private void addStaffing(Player player, Army army, int now) {
//		army.setStaffingTime(army.getStaffingTime() + TimeHelper.HALF_HOUR_S);
//		if (TimeHelper.isStaffingOpen() && now <= army.getEndTime()) {
//			StaticMine staticMine = worldDataManager.evaluatePos(army.getTarget());
//			int exp = staticWorldDataMgr.getStaticMineLv(staticMine.getLv()).getStaffingExp();
//			double ratio = staticLordDataMgr.getStaticProsLv(player.lord.getPros()).getStaffingAdd() / 100.0;
//			exp = (int) (exp * (1 + ratio));
//			playerDataManager.addStaffingExp(player, exp);
//		}
//	}
//
//	private void dealWorldAction(Player player, int now) {
//		List<Army> list = player.armys;
//		Iterator<Army> it = list.iterator();
//
//		int state = 0;
//		Army army;
//		while (it.hasNext()) {
//			army = it.next();
//			state = army.getState();
//			if (now >= army.getEndTime()) {
//				if (state == ArmyState.MARCH) {// 行军结束
//					if (marchEnd(player, army, now)) {// 部队被灭
//						it.remove();
//					}
//				} else if (state == ArmyState.AID) {// 援军行军结束
//					aidEnd(player, army, now);
//				} else if (state == ArmyState.RETREAT) {// 返回结束
//					retreatEnd(player, army);
//					it.remove();
//				}
//			}
//
//		}
//	}
//
//	public void worldTimerLogic() {
//		Iterator<Player> iterator = playerDataManager.getPlayers().values().iterator();
//		int now = TimeHelper.getCurrentSecond();
//		while (iterator.hasNext()) {
//			Player player = iterator.next();
//			if (!player.isActive() || player.armys.isEmpty()) {
//				continue;
//			}
//			dealWorldAction(player, now);
//		}
//	}
//
//	/**
//	 * 
//	 * Method: mineStaffingLogic
//	 * 
//	 * @Description: 世界地图半小时编制经验结算 @return void @throws
//	 */
//	public void mineStaffingLogic() {
//		// Iterator<List<Guard>> it =
//		// worldDataManager.getGuardMap().values().iterator();
//		// int now = TimeHelper.getCurrentSecond();
//		// int state = 0;
//		// Army army;
//		// List<Guard> list;
//		// while (it.hasNext()) {
//		// list = it.next();
//		// if (list != null && !list.isEmpty()) {
//		// Guard guard = list.get(0);
//		// army = guard.getArmy();
//		// state = army.getState();
//		// if (state == ArmyState.COLLECT && now >= army.getStaffingTime()) {
//		// addStaffing(guard.getPlayer(), army, now);
//		// }
//		// }
//		// }
//		Iterator<List<Guard>> it = worldDataManager.getGuardMap().values().iterator();
//		int now = TimeHelper.getCurrentSecond();
//		int state = 0;
//		Army army;
//		List<Guard> list;
//		while (it.hasNext()) {
//			list = it.next();
//			if (list != null && !list.isEmpty()) {
//				Guard guard = list.get(0);
//				army = guard.getArmy();
//				state = army.getState();
//				if (state == ArmyState.COLLECT && army.getStaffingTime() != 0 && now >= army.getStaffingTime()) {
//					addStaffing(guard.getPlayer(), army, now);
//				}
//			}
//		}
//
//	}
//}
