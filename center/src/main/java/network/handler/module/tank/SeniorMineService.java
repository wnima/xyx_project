package network.handler.module.tank;
//package com.game.module;
//
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.game.constant.ArmyState;
//import com.game.constant.AwardFrom;
//import com.game.constant.AwardType;
//import com.game.constant.FirstActType;
//import com.game.constant.GameError;
//import com.game.constant.GoldCost;
//import com.game.constant.MailType;
//import com.game.constant.SeniorState;
//import com.game.constant.SysChatId;
//import com.game.dataMgr.StaticHeroDataMgr;
//import com.game.dataMgr.StaticLordDataMgr;
//import com.game.dataMgr.StaticPropDataMgr;
//import com.game.dataMgr.StaticVipDataMgr;
//import com.game.dataMgr.StaticWarAwardDataMgr;
//import com.game.dataMgr.StaticWorldDataMgr;
//import com.game.domain.PartyData;
//import com.game.domain.Player;
//import com.game.domain.SeniorPartyScoreRank;
//import com.game.domain.SeniorScoreRank;
//import com.game.domain.p.Army;
//import com.game.domain.p.ArmyStatu;
//import com.game.domain.p.Collect;
//import com.game.domain.p.Form;
//import com.game.domain.p.Grab;
//import com.game.domain.p.Guard;
//import com.game.domain.p.Hero;
//import com.game.domain.p.Lord;
//import com.game.domain.p.Mail;
//import com.game.domain.p.RptTank;
//import com.game.domain.p.Tank;
//import com.game.domain.s.StaticHero;
//import com.game.domain.s.StaticMine;
//import com.game.domain.s.StaticMineForm;
//import com.game.domain.s.StaticMineLv;
//import com.game.domain.s.StaticProp;
//import com.game.domain.s.StaticScout;
//import com.game.domain.s.StaticVip;
//import com.game.fight.FightLogic;
//import com.game.fight.domain.Fighter;
//import com.game.fight.domain.Force;
//import com.game.manager.ActivityDataManager;
//import com.game.manager.GlobalDataManager;
//import com.game.manager.PartyDataManager;
//import com.game.manager.PlayerDataManager;
//import com.game.manager.SeniorMineDataManager;
//import com.game.manager.StaffingDataManager;
//import com.game.message.handler.ClientHandler;
//import com.game.pb.CommonPb;
//import com.game.pb.CommonPb.Award;
//import com.game.pb.CommonPb.Report;
//import com.game.pb.CommonPb.RptAtkMine;
//import com.game.pb.CommonPb.RptScoutMine;
//import com.game.pb.GamePb.AtkSeniorMineRq;
//import com.game.pb.GamePb.AtkSeniorMineRs;
//import com.game.pb.GamePb.BuySeniorRs;
//import com.game.pb.GamePb.GetSeniorMapRs;
//import com.game.pb.GamePb.PartyScoreAwardRs;
//import com.game.pb.GamePb.ScoreAwardRs;
//import com.game.pb.GamePb.ScorePartyRankRs;
//import com.game.pb.GamePb.ScoreRankRs;
//import com.game.pb.GamePb.SctSeniorMineRs;
//import com.game.util.LogHelper;
//import com.game.util.PbHelper;
//import com.game.util.RandomHelper;
//import com.game.util.TimeHelper;
//import com.game.util.Turple;
//
//@Component
//public class SeniorMineService {
//
//	@Autowired
//	private SeniorMineDataManager mineDataManager;
//
//	@Autowired
//	private StaticWorldDataMgr staticWorldDataMgr;
//
//	@Autowired
//	private StaticHeroDataMgr staticHeroDataMgr;
//
//	@Autowired
//	private StaticVipDataMgr staticVipDataMgr;
//
//	@Autowired
//	private StaticPropDataMgr staticPropDataMgr;
//
//	@Autowired
//	private StaticLordDataMgr staticLordDataMgr;
//
//	@Autowired
//	private StaticWarAwardDataMgr staticWarAwardDataMgr;
//
//	@Autowired
//	private PlayerDataManager playerDataManager;
//
//	@Autowired
//	private PartyDataManager partyDataManager;
//
//	@Autowired
//	private StaffingDataManager staffingDataManager;
//
//	@Autowired
//	private ActivityDataManager activityDataManager;
//
//	@Autowired
//	private GlobalDataManager globalDataManager;
//
//	@Autowired
//	private FightService fightService;
//
//	@Autowired
//	private ChatService chatService;
//
//	private static int[] OCCUPA_SCORE = { 50, 52, 54, 56, 58, 60, 62, 64, 66, 68 };
//
//	private static int[] ROB_SCORE = { 50 * 3, 52 * 3, 54 * 3, 56 * 3, 58 * 3, 60 * 3, 62 * 3, 64 * 3, 66 * 3, 68 * 3 };
//
//	public void getSeniorMap(ClientHandler handler) {
//		if (!TimeHelper.isStaffingOpen()) {
//			handler.sendErrorMsgToPlayer(GameError.STAFFING_NOT_OPEN);
//			return;
//		}
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player.lord.getLevel() < 60) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//			return;
//		}
//
//		refreshSenior(player);
//
//		int playerPartyId = partyDataManager.getPartyId(player.roleId);
//
//		List<Guard> list;
//		Guard guard;
//		int partyId;
//		boolean sameParty;
//		Iterator<List<Guard>> it = mineDataManager.getGuardMap().values().iterator();
//		GetSeniorMapRs.Builder builder = GetSeniorMapRs.newBuilder();
//
//		while (it.hasNext()) {
//			list = it.next();
//			if (list != null && !list.isEmpty()) {
//				guard = list.get(0);
//				sameParty = false;
//				if (playerPartyId != 0) {
//					partyId = partyDataManager.getPartyId(guard.getPlayer().roleId);
//					if (partyId == playerPartyId) {
//						sameParty = true;
//					}
//				}
//
//				builder.addData(PbHelper.createSeniorMapDataPb(player, guard.getPlayer(), guard.getArmy(), sameParty));
//			}
//		}
//
//		builder.setCount(player.seniorCount);
//		builder.setLimit(5);
//		builder.setBuy(player.seniorBuy);
//		handler.sendMsgToPlayer(GetSeniorMapRs.ext, builder.build());
//	}
//
//	public void scout(int pos, ClientHandler handler) {
//		if (!TimeHelper.isStaffingOpen()) {
//			handler.sendErrorMsgToPlayer(GameError.STAFFING_NOT_OPEN);
//			return;
//		}
//
//		if (mineDataManager.getSeniorState() != SeniorState.START_STATE) {// 结算
//			handler.sendErrorMsgToPlayer(GameError.SENIOR_MINE_DAY);
//			return;
//		}
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player.lord.getLevel() < 60) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//			return;
//		}
//
//		scoutMine(player, pos, handler);
//	}
//
//	private void scoutMine(Player player, int pos, ClientHandler handler) {
//		StaticMine staticMine = mineDataManager.evaluatePos(pos);
//		if (staticMine != null) {
//			StaticScout staticScout = staticWorldDataMgr.getScout(staticMine.getLv());
//			long scountCost = staticScout.getScoutCost();
//			Lord lord = player.lord;
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
//
//			int product = staticWorldDataMgr.getStaticMineLv(staticMine.getLv()).getProduction();
//			int now = TimeHelper.getCurrentSecond();
//
//			Guard guard = mineDataManager.getMineGuard(pos);
//			if (guard != null) {// 有驻军
//				Army army = guard.getArmy();
//				if (army.getOccupy()) {
//					if (now < army.getEndTime() - army.getPeriod() + 1800) {
//						handler.sendErrorMsgToPlayer(GameError.ATTACK_FREE);
//						return;
//					}
//				}
//
//				rptMine.setForm(PbHelper.createFormPb(army.getForm()));
//				String partyName = partyDataManager.getPartyNameByLordId(guard.getPlayer().roleId);
//				if (partyName != null) {
//					rptMine.setParty(partyName);
//				}
//
//				rptMine.setFriend(guard.getPlayer().lord.getNick());
//				rptMine.setHarvest(playerDataManager.calcCollect(guard.getPlayer(), army, now, staticMine, product));
//			} else {// 无驻军
//				rptMine.setForm(PbHelper.createFormPb(mineDataManager.getMineForm(pos, staticMine.getLv()).getForm()));
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
//			Mail mail = playerDataManager.createReportMail(player, report.build(), MailType.MOLD_SENIOR_MINE_SCOUT, TimeHelper.getCurrentSecond(),
//					String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			playerDataManager.modifyStone(player.resource, -scountCost);
//
//			SctSeniorMineRs.Builder builder = SctSeniorMineRs.newBuilder();
//			builder.setMail(PbHelper.createMailPb(mail));
//			handler.sendMsgToPlayer(SctSeniorMineRs.ext, builder.build());
//		} else {
//			handler.sendErrorMsgToPlayer(GameError.EMPTY_POS);
//		}
//
//	}
//
//	private void refreshSenior(Player player) {
//		int day = TimeHelper.getCurrentDay();
//		if (day != player.seniorDay) {
//			player.seniorDay = day;
//			player.seniorCount = 5;
//			player.seniorBuy = 0;
//		}
//
//		// int week = TimeHelper.getCurrentWeek();
//		// if (week != player.seniorWeek) {
//		// player.seniorWeek = week;
//		// player.seniorScore = 0;
//		// player.seniorAward = 0;
//		// }
//	}
//
//	public void attack(AtkSeniorMineRq req, ClientHandler handler) {
//		if (!TimeHelper.isStaffingOpen()) {
//			handler.sendErrorMsgToPlayer(GameError.STAFFING_NOT_OPEN);
//			return;
//		}
//
//		if (mineDataManager.getSeniorState() != SeniorState.START_STATE) {// 结算
//			handler.sendErrorMsgToPlayer(GameError.SENIOR_MINE_DAY);
//			return;
//		}
//
//		int pos = req.getPos();
//		int type = req.getType();
//
//		Player attacker = playerDataManager.getPlayer(handler.getRoleId());
//		if (attacker == null) {
//			LogHelper.ERROR_LOGGER.error("attack nul!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + handler.getRoleId());
//		}
//
//		if (attacker.lord.getLevel() < 60) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//			return;
//		}
//		
//		if (attacker.lord.getPower() < 1) {
//			handler.sendErrorMsgToPlayer(GameError.NO_POWER);
//			return;
//		}
//		
//		
//
//		int armyCount = attacker.armys.size();
//		for (Army army : attacker.armys) {
//			if (army.getState() == ArmyState.WAR) {
//				armyCount -= 1;
//				break;
//			}
//		}
//
//		if (armyCount >= playerDataManager.armyCount(attacker)) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_ARMY_COUNT);
//			return;
//		}
//
//		refreshSenior(attacker);
//
//		StaticMine staticMine = mineDataManager.evaluatePos(pos);
//		if (staticMine != null) {// 打矿
//			Guard guard = mineDataManager.getMineGuard(pos);
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
//
//				if (type == 2) {
//					handler.sendErrorMsgToPlayer(GameError.SENIOR_ATTACK_1);
//					return;
//				}
//
//				if (attacker.seniorCount < 1) {
//					handler.sendErrorMsgToPlayer(GameError.NO_SENIOR_COUNT);
//					return;
//				}
//
//				Army guardArmy = guard.getArmy();
//				int now = TimeHelper.getCurrentSecond();
//				if (guardArmy.getOccupy()) {
//					if (now < guardArmy.getEndTime() - guardArmy.getPeriod() + 1800) {
//						handler.sendErrorMsgToPlayer(GameError.ATTACK_FREE);
//						return;
//					}
//				}
//
//				Form attackForm = PbHelper.createForm(req.getForm());
//				StaticHero staticHero = null;
//				Hero hero = null;
//				if (attackForm.getCommander() > 0) {
//					hero = attacker.heros.get(attackForm.getCommander());
//					if (hero == null || hero.getCount() <= 0) {
//						handler.sendErrorMsgToPlayer(GameError.NO_HERO);
//						return;
//					}
//
//					staticHero = staticHeroDataMgr.getStaticHero(hero.getHeroId());
//					if (staticHero == null) {
//						handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//						return;
//					}
//
//					if (staticHero.getType() != 2) {
//						handler.sendErrorMsgToPlayer(GameError.NOT_HERO);
//						return;
//					}
//				}
//
//				int maxTankCount = playerDataManager.formTankCount(attacker, staticHero);
//				if (!playerDataManager.checkAndSubTank(attacker, attackForm, maxTankCount)) {
//					handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
//					return;
//				}
//
//				if (hero != null) {
//					hero.setCount(hero.getCount() - 1);
//				}
//
//				Army army = new Army(attacker.maxKey(), pos, ArmyState.MARCH, attackForm, 0, now);
//				army.setSenior(true);
//				attacker.armys.add(army);
//
//				AtkSeniorMineRs.Builder builder = AtkSeniorMineRs.newBuilder();
//
//				if (fightMineGuard(attacker, army, staticMine, guard, now)) {
//					attacker.armys.remove(army);
//				} else {
//					builder.setArmy(PbHelper.createArmyPb(army));
//				}
//
//				attacker.seniorCount = attacker.seniorCount - 1;
//
//				playerDataManager.subPower(attacker.lord, 1);
//				
//				builder.setCount(attacker.seniorCount);
//				handler.sendMsgToPlayer(AtkSeniorMineRs.ext, builder.build());
//
//			} else {
//				if (type == 1) {
//					handler.sendErrorMsgToPlayer(GameError.SENIOR_ATTACK_2);
//					return;
//				}
//
//				Form attackForm = PbHelper.createForm(req.getForm());
//				StaticHero staticHero = null;
//				Hero hero = null;
//				if (attackForm.getCommander() > 0) {
//					hero = attacker.heros.get(attackForm.getCommander());
//					if (hero == null || hero.getCount() <= 0) {
//						handler.sendErrorMsgToPlayer(GameError.NO_HERO);
//						return;
//					}
//
//					staticHero = staticHeroDataMgr.getStaticHero(hero.getHeroId());
//					if (staticHero == null) {
//						handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//						return;
//					}
//
//					if (staticHero.getType() != 2) {
//						handler.sendErrorMsgToPlayer(GameError.NOT_HERO);
//						return;
//					}
//				}
//
//				int maxTankCount = playerDataManager.formTankCount(attacker, staticHero);
//				if (!playerDataManager.checkAndSubTank(attacker, attackForm, maxTankCount)) {
//					handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
//					return;
//				}
//
//				if (hero != null) {
//					hero.setCount(hero.getCount() - 1);
//				}
//
//				int now = TimeHelper.getCurrentSecond();
//				Army army = new Army(attacker.maxKey(), pos, ArmyState.MARCH, attackForm, 0, now);
//				army.setSenior(true);
//				army.setOccupy(true);
//				attacker.armys.add(army);
//
//				AtkSeniorMineRs.Builder builder = AtkSeniorMineRs.newBuilder();
//
//				if (fightMineNpc(attacker, army, staticMine, now)) {
//					attacker.armys.remove(army);
//				} else {
//					builder.setArmy(PbHelper.createArmyPb(army));
//				}
//				
//				playerDataManager.subPower(attacker.lord, 1);
//
//				builder.setCount(attacker.seniorCount);
//				handler.sendMsgToPlayer(AtkSeniorMineRs.ext, builder.build());
//			}
//
//		} else {
//			handler.sendErrorMsgToPlayer(GameError.EMPTY_POS);
//			return;
//		}
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
//
//			Tank tank = tanks.get(rptTank.getTankId());
//			// tank.setRest(tank.getRest() + repair);
//			tank.setCount(tank.getCount() + repair);
//		}
//
//		subForceToForm(fighter, form);
//		return map;
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
//
//		// 这里使用时改变了occupy的语义，这里是表示该部队不处于保护中了
//		army.setOccupy(false);
//	}
//
//	private void eliminateGuard(Guard guard) {
//		Player target = guard.getPlayer();
//		Army army = guard.getArmy();
//		target.armys.remove(army);
//		int heroId = army.getForm().getCommander();
//		if (heroId > 0) {
//			playerDataManager.addHero(target, heroId, 1);
//		}
//
//		mineDataManager.removeGuard(guard);
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
//		Fighter attacker = fightService.createFighter(player, army.getForm(), 3);
//		Fighter defencer = fightService.createFighter(guardPlayer, targetForm, 3);
//
//		FightLogic fightLogic = new FightLogic(attacker, defencer, FirstActType.FISRT_VALUE_1, true);
//		fightLogic.packForm(army.getForm(), targetForm);
//		fightLogic.fight();
//		CommonPb.Record record = fightLogic.generateRecord();
//
//		double worldRatio = 0.9;
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
//			int score = ROB_SCORE[(staticMine.getLv() - 62) / 2];
//			player.seniorScore += score;
//			mineDataManager.setScoreRank(player);
//			PartyData party = partyDataManager.getPartyByLordId(player.roleId);
//			if (party != null) {
//				party.setScore(party.getScore() + score);
//				mineDataManager.setPartyScoreRank(party);
//			}
//
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
//			Mail mail = playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_SENIOR_MINE_ATTACK, now,
//					String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			playerDataManager.sendReportMail(guardPlayer, createDefMineReport(rpt, now), MailType.MOLD_SENIOR_MINE_DEFEND, now, player.lord.getNick(),
//					String.valueOf(player.lord.getLevel()));
//			
//			// 分享战力top
//			chatService.shareChallengeFightRankTop5(player,guardPlayer, mail, result);
//
//			partyDataManager.addPartyTrend(13, guardPlayer, player, String.valueOf(param));// 军团军情
//
//			activityDataManager.profoto(player, staticMine.getLv());// 哈洛克宝藏活动
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
//			Mail mail = playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_SENIOR_MINE_ATTACK, now,
//					String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			playerDataManager.sendReportMail(guardPlayer, createDefMineReport(rpt, now), MailType.MOLD_SENIOR_MINE_DEFEND, now, player.lord.getNick(),
//					String.valueOf(player.lord.getLevel()));
//			
//			// 分享战力top
//			chatService.shareChallengeFightRankTop5(player, guardPlayer, mail, result);
//
//			partyDataManager.addPartyTrend(12, guardPlayer, player, null);
//
//			playerDataManager.synArmyToPlayer(guardPlayer, new ArmyStatu(guardPlayer.roleId, guard.getArmy().getKeyId(), 4));
//			return true;
//		} else {
//			rptAtkMine.setResult(false);
//
//			recollectArmy(guardPlayer, guard.getArmy(), now, staticMine, staticMineLv.getProduction(), get);
//
//			playerDataManager.retreatEnd(player, army);
//
//			RptAtkMine rpt = rptAtkMine.build();
//
//			playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_SENIOR_MINE_ATTACK, now,
//					String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			playerDataManager.sendReportMail(guardPlayer, createDefMineReport(rpt, now), MailType.MOLD_SENIOR_MINE_DEFEND, now, player.lord.getNick(),
//					String.valueOf(player.lord.getLevel()));
//
//			playerDataManager.synArmyToPlayer(guardPlayer, new ArmyStatu(guardPlayer.roleId, guard.getArmy().getKeyId(), 4));
//			return true;
//		}
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
//		mineDataManager.setGuard(new Guard(player, army));
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
//	private void backHero(Player player, Form form) {
//		if (form.getCommander() > 0) {
//			playerDataManager.addHero(player, form.getCommander(), 1);
//		}
//	}
//
//	// public void retreatEnd(Player player, Army army) {
//	// // 部队返回
//	// int[] p = army.getForm().p;
//	// int[] c = army.getForm().c;
//	// for (int i = 0; i < p.length; i++) {
//	// if (p[i] > 0 && c[i] > 0) {
//	// playerDataManager.addTank(player, p[i], c[i]);
//	// }
//	// }
//	// // 将领返回
//	// int heroId = army.getForm().getCommander();
//	// if (heroId > 0) {
//	// playerDataManager.addHero(player, heroId, 1);
//	// }
//	//
//	// // 加资源
//	// Grab grab = army.getGrab();
//	// if (grab != null) {
//	// playerDataManager.gainGrab(player, grab);
//	// StaticMine staticMine = mineDataManager.evaluatePos(army.getTarget());
//	// if (staticMine != null) {
//	// partyDataManager.collectMine(player.roleId, grab);
//	// activityDataManager.resourceCollect(player,
//	// ActivityConst.ACT_COLLECT_RESOURCE, grab);// 资源采集活动
//	// activityDataManager.beeCollect(player,
//	// ActivityConst.ACT_COLLECT_RESOURCE, grab);// 勤劳致富
//	// activityDataManager.amyRebate(player, 0, grab.rs);// 建军节欢庆
//	// }
//	// }
//	// }
//
//	private boolean fightMineNpc(Player player, Army army, StaticMine staticMine, int now) {
//		int pos = army.getTarget();
//		StaticMineForm staticMineForm = mineDataManager.getMineForm(pos, staticMine.getLv());
//
//		Fighter attacker = fightService.createFighter(player, army.getForm(), 3);
//		Fighter defencer = fightService.createFighter(staticMineForm);
//
//		FightLogic fightLogic = new FightLogic(attacker, defencer, FirstActType.ATTACKER, true);
//		fightLogic.packForm(army.getForm(), PbHelper.createForm(staticMineForm.getForm()));
//
//		fightLogic.fight();
//		CommonPb.Record record = fightLogic.generateRecord();
//
//		double worldRatio = 0.9;
//		Map<Integer, RptTank> attackHaust = haustArmyTank(player, attacker, army.getForm(), worldRatio);
//		Map<Integer, RptTank> defenceHaust = fightService.statisticHaustTank(defencer);
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
//			mineDataManager.resetMineForm(pos, staticMine.getLv());
//
//			int score = OCCUPA_SCORE[(staticMine.getLv() - 62) / 2];
//			player.seniorScore += score;
//			mineDataManager.setScoreRank(player);
//			PartyData party = partyDataManager.getPartyByLordId(player.roleId);
//			if (party != null) {
//				party.setScore(party.getScore() + score);
//				mineDataManager.setPartyScoreRank(party);
//			}
//
//			StaticMineLv staticMineLv = staticWorldDataMgr.getStaticMineLv(staticMine.getLv());
//			int heroId = army.getForm().getCommander();
//			StaticHero staticHero = null;
//			if (heroId != 0) {
//				staticHero = staticHeroDataMgr.getStaticHero(heroId);
//			}
//
//			int exp = (int) (staticMineLv.getExp() * fightService.effectMineExpAdd(player, staticHero));
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
//			playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_SENIOR_MINE_ATTACK, now,
//					String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			activityDataManager.profoto(player, staticMine.getLv());// 哈洛克宝藏活动
//			return false;
//		} else if (result == 2) {
//			backHero(player, army.getForm());
//			rptAtkMine.setResult(false);
//
//			RptAtkMine rpt = rptAtkMine.build();
//			playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_SENIOR_MINE_ATTACK, now,
//					String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			return true;
//		} else {
//			rptAtkMine.setResult(false);
//
//			playerDataManager.retreatEnd(player, army);
//			RptAtkMine rpt = rptAtkMine.build();
//			playerDataManager.sendReportMail(player, createAtkMineReport(rpt, now), MailType.MOLD_SENIOR_MINE_ATTACK, now,
//					String.valueOf(staticMine.getType()), String.valueOf(staticMine.getLv()));
//
//			return true;
//		}
//
//	}
//
//	public void scoreRank(ClientHandler handler) {
//		if (!TimeHelper.isStaffingOpen()) {
//			handler.sendErrorMsgToPlayer(GameError.STAFFING_NOT_OPEN);
//			return;
//		}
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player.lord.getLevel() < 60) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//			return;
//		}
//
//		ScoreRankRs.Builder builder = ScoreRankRs.newBuilder();
//		for (SeniorScoreRank one : mineDataManager.getScoreRankList()) {
//			Player target = playerDataManager.getPlayer(one.getLordId());
//			builder.addScoreRank(PbHelper.createScoreRankPb(target.lord.getNick(), one));
//		}
//
//		Turple<Integer, SeniorScoreRank> rank = mineDataManager.getScoreRank(player.roleId);
//		builder.setRank(rank.getA());
//		builder.setScore(player.seniorScore);
//
//		boolean canGet = false;
//		if (mineDataManager.getSeniorState() == SeniorState.END_STATE) {// 结算
//			if (rank.getA() > 0 && rank.getA() < 11) {
//				if (!rank.getB().getGet()) {
//					canGet = true;
//				}
//			}
//		}
//
//		builder.setCanGet(canGet);
//		handler.sendMsgToPlayer(ScoreRankRs.ext, builder.build());
//	}
//
//	public void scorePartyRank(ClientHandler handler) {
//		if (!TimeHelper.isStaffingOpen()) {
//			handler.sendErrorMsgToPlayer(GameError.STAFFING_NOT_OPEN);
//			return;
//		}
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player.lord.getLevel() < 60) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//			return;
//		}
//
//		ScorePartyRankRs.Builder builder = ScorePartyRankRs.newBuilder();
//		for (SeniorPartyScoreRank one : mineDataManager.getScorePartyRankList()) {
//			PartyData party = partyDataManager.getParty(one.getPartyId());
//			if (party != null) {
//				builder.addScoreRank(PbHelper.createScoreRankPb(party.getPartyName(), one));
//			}
//		}
//
//		boolean canGet = false;
//		int rankOrder = 0;
//		int score = 0;
//		PartyData partyData = partyDataManager.getPartyByLordId(player.roleId);
//		if (partyData != null) {
//			Turple<Integer, SeniorPartyScoreRank> rank = mineDataManager.getPartyScoreRank(partyData.getPartyId());
//			rankOrder = rank.getA();
//			score = partyData.getScore();
//			if (mineDataManager.getSeniorState() == SeniorState.END_STATE) {
//				if (rank.getA() > 0 && rank.getA() < 6) {
//					if (player.seniorAward == 0) {
//						canGet = true;
//					}
//				}
//			}
//		}
//
//		builder.setScore(score);
//		builder.setRank(rankOrder);
//		builder.setCanGet(canGet);
//		handler.sendMsgToPlayer(ScorePartyRankRs.ext, builder.build());
//	}
//
//	public void buySenior(ClientHandler handler) {
//		if (!TimeHelper.isStaffingOpen()) {
//			handler.sendErrorMsgToPlayer(GameError.STAFFING_NOT_OPEN);
//			return;
//		}
//
//		if (mineDataManager.getSeniorState() != SeniorState.START_STATE) {
//			handler.sendErrorMsgToPlayer(GameError.SENIOR_MINE_DAY);
//			return;
//		}
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player.lord.getLevel() < 60) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//			return;
//		}
//
//		refreshSenior(player);
//
//		int buyCount = player.seniorBuy;
//
//		int cost = 5 * (buyCount + 1);
//		if (player.lord.getGold() < cost) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//
//		playerDataManager.subGold(player.lord, cost, GoldCost.BUY_SENIOR);
//		player.seniorBuy++;
//		player.seniorCount++;
//
//		BuySeniorRs.Builder builder = BuySeniorRs.newBuilder();
//		builder.setCount(player.seniorCount);
//		builder.setGold(player.lord.getGold());
//		builder.setBuy(player.seniorBuy);
//		handler.sendMsgToPlayer(BuySeniorRs.ext, builder.build());
//	}
//
//	public void scoreAward(ClientHandler handler) {
//		if (!TimeHelper.isStaffingOpen()) {
//			handler.sendErrorMsgToPlayer(GameError.STAFFING_NOT_OPEN);
//			return;
//		}
//
//		if (mineDataManager.getSeniorState() != SeniorState.END_STATE) {
//			handler.sendErrorMsgToPlayer(GameError.SENIOR_MINE_NOT_END);
//			return;
//		}
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player.lord.getLevel() < 60) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//			return;
//		}
//
//		refreshSenior(player);
//
//		Turple<Integer, SeniorScoreRank> rank = mineDataManager.getScoreRank(player.roleId);
//
//		if (rank.getA() > 0 && rank.getA() < 11) {
//			if (rank.getB().getGet()) {
//				handler.sendErrorMsgToPlayer(GameError.ALREADY_GET_AWARD);
//				return;
//			}
//		} else {
//			handler.sendErrorMsgToPlayer(GameError.NOT_ON_SCORE_RANK);
//			return;
//		}
//
//		rank.getB().setGet(true);
//
//		ScoreAwardRs.Builder builder = ScoreAwardRs.newBuilder();
//
//		List<List<Integer>> awards = staticWarAwardDataMgr.getScoreAward(rank.getA());
//		builder.addAllAward(playerDataManager.addAwardsBackPb(player, awards, AwardFrom.SCORE_AWARD));
//		handler.sendMsgToPlayer(ScoreAwardRs.ext, builder.build());
//	}
//
//	public void partyScoreAward(ClientHandler handler) {
//		if (!TimeHelper.isStaffingOpen()) {
//			handler.sendErrorMsgToPlayer(GameError.STAFFING_NOT_OPEN);
//			return;
//		}
//
//		if (mineDataManager.getSeniorState() != SeniorState.END_STATE) {
//			handler.sendErrorMsgToPlayer(GameError.SENIOR_MINE_NOT_END);
//			return;
//		}
//
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player.lord.getLevel() < 60) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
//			return;
//		}
//
//		refreshSenior(player);
//
//		Turple<Integer, SeniorPartyScoreRank> rank;
//		PartyData partyData = partyDataManager.getPartyByLordId(player.roleId);
//		if (partyData != null) {
//			rank = mineDataManager.getPartyScoreRank(partyData.getPartyId());
//			if (mineDataManager.getSeniorState() == SeniorState.END_STATE) {
//				if (rank.getA() > 0 && rank.getA() < 6) {
//					if (player.seniorAward == 1) {
//						handler.sendErrorMsgToPlayer(GameError.ALREADY_GET_AWARD);
//						return;
//					}
//				} else {
//					handler.sendErrorMsgToPlayer(GameError.NOT_ON_SCORE_RANK);
//					return;
//				}
//			}
//		} else {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
//			return;
//		}
//
//		player.seniorAward = 1;
//
//		PartyScoreAwardRs.Builder builder = PartyScoreAwardRs.newBuilder();
//
//		List<List<Integer>> awards = staticWarAwardDataMgr.getScorePartyAward(rank.getA());
//		builder.addAllAward(playerDataManager.addAwardsBackPb(player, awards, AwardFrom.PARTY_SCORE_AWARD));
//		handler.sendMsgToPlayer(PartyScoreAwardRs.ext, builder.build());
//
//	}
//
//	private void addStaffing(Player player, Army army, int now) {
//		army.setStaffingTime(army.getStaffingTime() + TimeHelper.HALF_HOUR_S);
//		if (TimeHelper.isStaffingOpen() && now <= army.getEndTime()) {
//			StaticMine staticMine = mineDataManager.evaluatePos(army.getTarget());
//			int exp = staticWorldDataMgr.getStaticMineLv(staticMine.getLv()).getStaffingExp();
//			double ratio = staticLordDataMgr.getStaticProsLv(player.lord.getPros()).getStaffingAdd() / 100.0;
//			exp = (int) (exp * (1 + ratio));
//			playerDataManager.addStaffingExp(player, exp);
//		}
//	}
//
//	/**
//	 * 
//	 * Method: mineStaffingLogic
//	 * 
//	 * @Description: 军事地图半小时编制经验结算
//	 * @return void
//	 * @throws
//	 */
//	public void mineStaffingLogic() {
//		// Iterator<List<Guard>> it =
//		// mineDataManager.getGuardMap().values().iterator();
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
//
//		Iterator<List<Guard>> it = mineDataManager.getGuardMap().values().iterator();
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
//
//	public void clearSeniorRanking() {
//		Iterator<Player> iterator = playerDataManager.getPlayers().values().iterator();
//		while (iterator.hasNext()) {
//			Player player = iterator.next();
//			if (!player.isActive()) {
//				continue;
//			}
//
//			player.seniorAward = 0;
//			player.seniorScore = 0;
//		}
//
//		for (SeniorPartyScoreRank one : mineDataManager.getScorePartyRankList()) {
//			PartyData party = partyDataManager.getParty(one.getPartyId());
//			party.setScore(0);
//		}
//
//		mineDataManager.clearRank();
//
//	}
//
//	private void retreat() {
//		Iterator<List<Guard>> it = mineDataManager.getGuardMap().values().iterator();
//		// int now = TimeHelper.getCurrentSecond();
//		int state = 0;
//		Army army;
//		List<Guard> list;
//		while (it.hasNext()) {
//			list = it.next();
//			if (list != null && !list.isEmpty()) {
//				Guard guard = list.get(0);
//				army = guard.getArmy();
//				state = army.getState();
//				if (state == ArmyState.COLLECT) {
//					Player player = guard.getPlayer();
//
//					StaticMine staticMine = mineDataManager.evaluatePos(army.getTarget());
//					if (staticMine != null) {
//						long get = playerDataManager.calcCollect(player, army, TimeHelper.getCurrentSecond(), staticMine,
//								staticWorldDataMgr.getStaticMineLv(staticMine.getLv()).getProduction());
//						Grab grab = new Grab();
//						grab.rs[staticMine.getType() - 1] = get;
//						army.setGrab(grab);
//
//						mineDataManager.removeGuard(player, army);
//						playerDataManager.retreatEnd(player, army);
//						player.armys.remove(army);
//					} else {
//						mineDataManager.removeGuard(player, army);
//						playerDataManager.retreatEnd(player, army);
//						player.armys.remove(army);
//					}
//				}
//			}
//		}
//	}
//
//	public void seniorMineLogic() {
//		Calendar calendar = Calendar.getInstance();
//		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//		if (dayOfWeek == Calendar.SATURDAY) {
//
//			if (mineDataManager.getSeniorState() != SeniorState.START_STATE) {// 清除数据
//				mineDataManager.setSeniorState(SeniorState.START_STATE);
//				clearSeniorRanking();
//				retreat();
//			}
//		} else if (dayOfWeek == Calendar.MONDAY) {
//
//			if (mineDataManager.getSeniorState() != SeniorState.END_STATE) {// 结算
//				mineDataManager.setSeniorState(SeniorState.END_STATE);
//				mineDataManager.setSeniorWeek(TimeHelper.getCurrentWeek());
//				retreat();
//			}
//		}
//	}
//}
