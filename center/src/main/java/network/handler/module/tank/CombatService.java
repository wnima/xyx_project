package network.handler.module.tank;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.domain.Player;
import com.game.fight.FightLogic;
import com.game.fight.domain.Fighter;
import com.game.fight.domain.Force;
import com.game.util.PbHelper;
import com.game.util.RandomHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfCombat;
import config.bean.ConfExplore;
import config.bean.ConfHero;
import config.bean.ConfSection;
import config.bean.ConfVip;
import config.provider.ConfCombatProvider;
import config.provider.ConfExploreProvider;
import config.provider.ConfHeroProvider;
import config.provider.ConfSectionProvider;
import config.provider.ConfVipProvider;
import data.bean.Award;
import data.bean.Combat;
import data.bean.Equip;
import data.bean.Extreme;
import data.bean.Form;
import data.bean.Hero;
import data.bean.Lord;
import data.bean.Part;
import data.bean.RptTank;
import data.bean.Tank;
import define.ActivityConst;
import define.AwardFrom;
import define.AwardType;
import define.FirstActType;
import define.GameError;
import define.GoldCost;
import define.MailType;
import define.PropId;
import define.TaskType;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.ExtremeDataManager;
import manager.MailManager;
import manager.PlayerDataManager;
import manager.TaskManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.CommonPb;
import pb.CommonPb.AtkExtreme;
import pb.CommonPb.AwardPB;
import pb.GamePb.BeginWipeRs;
import pb.GamePb.BuyExploreRq;
import pb.GamePb.BuyExploreRs;
import pb.GamePb.CombatBoxRq;
import pb.GamePb.CombatBoxRs;
import pb.GamePb.DoCombatRq;
import pb.GamePb.DoCombatRs;
import pb.GamePb.EndWipeRs;
import pb.GamePb.ExtremeRecordRq;
import pb.GamePb.ExtremeRecordRs;
import pb.GamePb.GetCombatRs;
import pb.GamePb.GetExtremeRq;
import pb.GamePb.GetExtremeRs;
import pb.GamePb.ResetExtrEprRs;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;

@Singleton
public class CombatService implements IModuleMessageHandler {

	public static CombatService getInst() {
		return BeanManager.getBean(CombatService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	/**
	 * 
	 * Method: getCombat
	 * 
	 * @Description: 客户端获取副本数据 @param handler @return void @throws
	 */
	public void getCombat(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		GetCombatRs.Builder builder = GetCombatRs.newBuilder();

		Iterator<Combat> it = player.combats.values().iterator();
		while (it.hasNext()) {
			builder.addCombat(PbHelper.createCombatPb(it.next()));
		}

		Iterator<Combat> exploreIt = player.explores.values().iterator();
		while (exploreIt.hasNext()) {
			builder.addExplore(PbHelper.createCombatPb(exploreIt.next()));
		}

		for (Map.Entry<Integer, Integer> entry : player.sections.entrySet()) {
			builder.addSection(PbHelper.createSectionPb(entry.getKey(), entry.getValue()));
		}

		builder.setCombatId(player.combatId);
		builder.setEquipEplrId(player.equipEplrId);
		builder.setPartEplrId(player.partEplrId);
		builder.setExtrEplrId(player.extrEplrId);
		builder.setMilitaryEplrId(player.militaryEplrId);
		builder.setTimePrlrId(player.timePrlrId);
		builder.setExtrMark(player.extrMark);
		if (player.wipeTime != 0) {
			builder.setWipeTime(player.wipeTime);
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetCombatRs, builder.build());
	}

	/**
	 * 
	 * Method: haustTank
	 * 
	 * @Description: 副本中的战损，扣除玩家的实际坦克数量 ，添加玩家的修理坦克 @param player @param
	 *               fighter @param ratio @return @return
	 *               Map<Integer,RptTank> @throws
	 */
	private Map<Integer, RptTank> haustTank(Player player, Fighter fighter, double ratio) {
		Map<Integer, RptTank> map = new HashMap<Integer, RptTank>();
		Map<Integer, Tank> tanks = player.tanks;
		int killed = 0;
		int tankId = 0;
		for (Force force : fighter.forces) {
			if (force != null) {
				killed = force.killed;
				if (killed > 0) {
					tankId = force.staticTank.getTankId();
					RptTank rptTank = map.get(tankId);
					if (rptTank != null) {
						rptTank.setCount(rptTank.getCount() + killed);
					} else {
						rptTank = new RptTank(tankId, killed);
						map.put(tankId, rptTank);
					}
				}
			}
		}

		Iterator<RptTank> it = map.values().iterator();
		while (it.hasNext()) {
			RptTank rptTank = (RptTank) it.next();
			killed = rptTank.getCount();
			int repair = (int) Math.ceil(ratio * killed);
			Tank tank = tanks.get(rptTank.getTankId());
			tank.setRest(tank.getRest() + repair);
			tank.setCount(tank.getCount() - killed);
		}

		return map;
	}

	/**
	 * 
	 * Method: doCombat
	 * 
	 * @Description: 挑战副本 @param req @param handler @return void @throws
	 */
	public void doCombat(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		boolean wipe = false;
		long playerId = packet.getPlayerId();
		DoCombatRq req = msg.get();

		if (req.hasField(DoCombatRq.getDescriptor().findFieldByName("wipe"))) {
			wipe = true;
		}
		int combatId = req.getCombatId();
		CommonPb.Form form = req.getForm();

		ConfCombat staticCombat = ConfCombatProvider.getInst().getConfigById(combatId);
		if (staticCombat == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		ConfSection staticSection = ConfSectionProvider.getInst().getConfigById(staticCombat.getSectionId());
		if (staticSection == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);

		if (wipe) {
			if (player.lord.getVip() < 3) {
//				handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
				return;
			}
		}

		if (combatId != 101) {
			if (player.combatId < staticCombat.getPreId()) {
//				handler.sendErrorMsgToPlayer(GameError.COMBAT_PASS_BEFORE);
				return;
			}
		}

		if (player.lord.getRanks() < staticSection.getRank()) {
//			handler.sendErrorMsgToPlayer(GameError.NO_RANKS);
			return;
		}

		if (player.lord.getPower() < 1) {
//			handler.sendErrorMsgToPlayer(GameError.NO_POWER);
			return;
		}

		Form attackForm = PbHelper.createForm(form);
		ConfHero staticHero = null;
		if (attackForm.getCommander() > 0) {
			Hero hero = player.heros.get(attackForm.getCommander());
			if (hero == null || hero.getCount() <= 0) {
//				handler.sendErrorMsgToPlayer(GameError.NO_HERO);
				return;
			}

			staticHero = ConfHeroProvider.getInst().getConfigById(hero.getHeroId());
			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			if (staticHero.getType() != 2) {
//				handler.sendErrorMsgToPlayer(GameError.NOT_HERO);
				return;
			}
		}

		int maxTankCount = PlayerDataManager.getInst().formTankCount(player, staticHero);

		if (!PlayerDataManager.getInst().checkTank(player, attackForm, maxTankCount)) {
//			handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
			return;
		}

		Fighter attacker = FightService.getInst().createFighter(player, attackForm, 1);
		Fighter npc = FightService.getInst().createFighter(staticCombat);

		FightLogic fightLogic = new FightLogic(attacker, npc, FirstActType.ATTACKER, !wipe);
		fightLogic.fight();

		Map<Integer, RptTank> haust = haustTank(player, attacker, 0.8);

		DoCombatRs.Builder builder = DoCombatRs.newBuilder();
		if (fightLogic.getWinState() == 1) {
			boolean firstPass = false;
			if (combatId > player.combatId) {
				firstPass = true;
				player.combatId = combatId;
			}

			int star = fightLogic.estimateStar();
			builder.addAllAward(dropCombatAward(player, staticCombat.getDrop()));
			Combat combat = player.combats.get(combatId);
			if (combat == null) {
				combat = new Combat(combatId, star);
				player.combats.put(combatId, combat);
			} else {
				if (star > combat.getStar()) {
					combat.setStar(star);
				}
			}

			if (firstPass) {
				firstAward(player, staticCombat.getFirstAward(), builder);
			}

			PlayerDataManager.getInst().subPower(player.lord, 1);
			int exp = (int) (staticCombat.getExp() * FightService.getInst().effectCombatExpAdd(player, staticHero));

			PlayerDataManager.getInst().addExp(player.lord, exp);
			builder.setExp(exp);
			builder.setResult(star);

//			activityDataManager.updActivity(player, ActivityConst.ACT_COMBAT, 1, 0);
			TaskManager.getInst().updTask(playerId, TaskType.COND_COMBAT, 1, combatId);
//			Prop prop = activityDataManager.combatCourse(staticCombat.getSectionId());
//			if (prop != null) {
//				int keyId = PlayerDataManager.getInst().addAward(player, AwardType.PROP, prop.getPropId(), prop.getCount(), AwardFrom.COMBAT_COURSE);
//				builder.addAward(PbHelper.createAwardPb(AwardType.PROP, prop.getPropId(), prop.getCount(), keyId));
//			}
//			Prop propEquip = activityDataManager.combatCash(ActivityConst.ACT_EQUIP_EXCHANGE_ID, staticCombat.getSectionId());
//			Prop propPart = activityDataManager.combatCash(ActivityConst.ACT_PART_EXCHANGE_ID, staticCombat.getSectionId());
//			if (propEquip != null) {
//				int keyId = PlayerDataManager.getInst().addAward(player, AwardType.PROP, propEquip.getPropId(), propEquip.getCount(), AwardFrom.EXCHANGE_EQUIP);
//				builder.addAward(PbHelper.createAwardPb(AwardType.PROP, propEquip.getPropId(), propEquip.getCount(), keyId));
//			}
//			if (propPart != null) {
//				int keyId = PlayerDataManager.getInst().addAward(player, AwardType.PROP, propPart.getPropId(), propPart.getCount(), AwardFrom.EXCHANGE_EQUIP);
//				builder.addAward(PbHelper.createAwardPb(AwardType.PROP, propPart.getPropId(), propPart.getCount(), keyId));
//			}
		} else {
			builder.setResult(-1);
		}

		Iterator<RptTank> it = haust.values().iterator();
		while (it.hasNext()) {
			builder.addHaust(PbHelper.createRtpTankPb(it.next()));
		}

		if (!wipe) {
			CommonPb.Record record = fightLogic.generateRecord();
			builder.setRecord(record);
		}

		TaskManager.getInst().updTask(playerId, TaskType.COND_ATTACK_COMBAT, 1, combatId);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.DoCombatRs, builder.build());
	}

	/**
	 * 
	 * Method: checkExploreLv
	 * 
	 * @Description: 检查探险开启等级 @param player @param type @return @return
	 *               boolean @throws
	 */
	private boolean checkExploreLv(Player player, int type) {
		if (type == 1) {// 装备探险
			if (player.lord.getLevel() < 1) {
				return false;
			}
		} else if (type == 2) {// 配件探险
			if (player.lord.getLevel() < 18) {
				return false;
			}
		} else if (type == 3) {// 极限探险
			if (player.lord.getLevel() < 35) {
				return false;
			}
		} else if (type == 5) {// 极限探险
			if (player.lord.getLevel() < 30) {
				return false;
			}
		} else {// 限时副本
			if (player.lord.getLevel() < 8) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * Method: cleanExplore
	 * 
	 * @Description: 每天重置次数 @param lord @return void @throws
	 */
	static public void cleanExplore(Lord lord) {
		int day = DateUtil.getToday();
		if (lord.getEplrTime() != day) {
			lord.setEquipEplr(0);
			lord.setPartEplr(0);
			lord.setMilitaryEplr(0);
			lord.setExtrEplr(0);
			lord.setTimeEplr(0);
			lord.setEquipBuy(0);
			lord.setPartBuy(0);
			lord.setMilitaryBuy(0);
			lord.setExtrReset(0);
			lord.setEplrTime(day);
		}
	}

	/**
	 * 
	 * Method: checkExploreCount
	 * 
	 * @Description: 检查探险次数 @param player @param type @return @return
	 *               boolean @throws
	 */
	private boolean checkExploreCount(Player player, int type) {
		Lord lord = player.lord;
		cleanExplore(lord);
		if (type == 1) {// 装备探险
//			int add = activityDataManager.freeLotEquip();
			int add = 0;
			if (lord.getEquipEplr() >= (5 + lord.getEquipBuy() * 5 + add)) {
				return false;
			}
		} else if (type == 2) {// 配件探险
//			int add = activityDataManager.freeLotPart();
			int add = 0;
			if (lord.getPartEplr() >= (5 + lord.getPartBuy() * 5 + add)) {
				return false;
			}
		} else if (type == 5) {// 军工探险
			int add = 0; // TODO 活动
			if (lord.getMilitaryEplr() >= (5 + lord.getMilitaryBuy() * 5 + add)) {
				return false;
			}
		} else if (type == 3) {// 极限探险
			if (lord.getExtrEplr() >= 3) {
				return false;
			}
		} else {// 限时副本
			if (lord.getTimeEplr() >= 5) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * Method: checkTime
	 * 
	 * @Description: 检查限时副本开启时间 @return @return boolean @throws
	 */
	private boolean checkTime() {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek != Calendar.TUESDAY && dayOfWeek != Calendar.THURSDAY && dayOfWeek != Calendar.SATURDAY) {
			return false;
		}

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 18) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * Method: doExplore
	 * 
	 * @Description: 探险副本 @param req @param handler @return void @throws
	 */
	public void doExplore(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		boolean wipe = false;
		long playerId = packet.getPlayerId();
		DoCombatRq req = msg.get();
		if (req.hasField(DoCombatRq.getDescriptor().findFieldByName("wipe"))) {
			wipe = true;
		}

		int combatId = req.getCombatId();
		CommonPb.Form form = req.getForm();

		ConfExplore staticExplore = ConfExploreProvider.getInst().getConfigById(combatId);
		if (staticExplore == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (wipe) {
			if (player.lord.getVip() < 3) {
//				handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
				return;
			}
		}

		if (!checkExploreLv(player, staticExplore.getType())) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		int exploreType = staticExplore.getType();
		if (exploreType == 1) {// 装备
			if (combatId != 101) {
				if (player.equipEplrId < staticExplore.getPreId()) {
//					handler.sendErrorMsgToPlayer(GameError.COMBAT_PASS_BEFORE);
					return;
				}
			}

			Map<Integer, Equip> store = player.equips.get(0);
			if (store.size() >= player.lord.getEquip()) {
//				handler.sendErrorMsgToPlayer(GameError.MAX_EQUIP_STORE);
				return;
			}
		} else if (exploreType == 2) {// 配件
			if (combatId != 201) {
				if (player.partEplrId < staticExplore.getPreId()) {
//					handler.sendErrorMsgToPlayer(GameError.COMBAT_PASS_BEFORE);
					return;
				}
			}

			Map<Integer, Part> storeMap = player.parts.get(0);
			if (storeMap.size() >= PlayerDataManager.PART_STORE_LIMIT) {
//				handler.sendErrorMsgToPlayer(GameError.MAX_PART_STORE);
				return;
			}
		} else if (exploreType == 3) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		} else if (exploreType == 4) {// 限时
			if (!checkTime()) {
//				handler.sendErrorMsgToPlayer(GameError.COMBAT_NOT_OPEN);
				return;
			}
			if (combatId != 401) {
				if (player.timePrlrId < staticExplore.getPreId()) {
//					handler.sendErrorMsgToPlayer(GameError.COMBAT_PASS_BEFORE);
					return;
				}
			}
		} else if (exploreType == 5) {// 军工副本
			if (combatId != 501) {
				if (player.militaryEplrId < staticExplore.getPreId()) {
//					handler.sendErrorMsgToPlayer(GameError.COMBAT_PASS_BEFORE);
					return;
				}
			}
		}

		if (!checkExploreCount(player, exploreType)) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EXPLORE_COUNT);
			return;
		}

		Form attackForm = PbHelper.createForm(form);

		ConfHero staticHero = null;
		if (attackForm.getCommander() > 0) {
			Hero hero = player.heros.get(attackForm.getCommander());
			if (hero == null || hero.getCount() <= 0) {
//				handler.sendErrorMsgToPlayer(GameError.NO_HERO);
				return;
			}

			staticHero = ConfHeroProvider.getInst().getConfigById(hero.getHeroId());
			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			if (staticHero.getType() != 2) {
//				handler.sendErrorMsgToPlayer(GameError.NOT_HERO);
				return;
			}
		}

		int maxTankCount = PlayerDataManager.getInst().formTankCount(player, staticHero);
		if (!PlayerDataManager.getInst().checkTank(player, attackForm, maxTankCount)) {
//			handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
			return;
		}

		Fighter attacker = FightService.getInst().createFighter(player, attackForm, 1);
		Fighter npc = FightService.getInst().createFighter(staticExplore);

		if (exploreType == 2) {// 配件探险
//			float partSupply = activityDataManager.partSupply(player);
//			if (partSupply != 1f) {
//				ConfBuff buff = new ConfBuff();
//				buff.setEffectType(4);
//				buff.setEffectValue(3000);
//				attacker.addAuraAttr(buff);
//			}
		}

		DoCombatRs.Builder builder = DoCombatRs.newBuilder();
		FightLogic fightLogic = new FightLogic(attacker, npc, FirstActType.ATTACKER, !wipe);
		fightLogic.fight();
		Map<Integer, RptTank> haust = haustTank(player, attacker, 0.8);

		if (fightLogic.getWinState() == 1) {
			int star = fightLogic.estimateStar();
			builder.addAllAward(dropAward(player, staticExplore.getDrop()));
			builder.addAllAward(dropOneAward(player, staticExplore.getDropOne(), staticExplore.getWeight()));
			builder.addAllAward(dropMaterial(player, staticExplore.getDropMaterial()));

			Combat combat = player.explores.get(combatId);
			if (combat == null) {
				combat = new Combat(combatId, star);
				player.explores.put(combatId, combat);
			} else {
				if (star > combat.getStar()) {
					combat.setStar(star);
				}
			}

			addExploreCount(player.lord, staticExplore.getType());

			PlayerDataManager.getInst().addExp(player.lord, (int) (staticExplore.getExp() * FightService.getInst().effectCombatExpAdd(player, staticHero)));

			if (exploreType == 1) {// 装备
				if (combatId > player.equipEplrId) {
					player.equipEplrId = combatId;
				}
			} else if (exploreType == 2) {// 配件
				if (combatId > player.partEplrId) {
					player.partEplrId = combatId;
				}
			} else if (exploreType == 4) {// 限时
				if (combatId > player.timePrlrId) {
					player.timePrlrId = combatId;
				}
			} else if (exploreType == 5) {// 军工
				if (combatId > player.militaryEplrId) {
					player.militaryEplrId = combatId;
				}
			}

			if (staticExplore.getExp() != 0) {
				int exp = (int) (staticExplore.getExp() * FightService.getInst().effectCombatExpAdd(player, staticHero));
				PlayerDataManager.getInst().addExp(player.lord, exp);
				builder.setExp(exp);
			}

			builder.setResult(star);
		} else {
			builder.setResult(-1);
		}

		Iterator<RptTank> it = haust.values().iterator();
		while (it.hasNext()) {
			builder.addHaust(PbHelper.createRtpTankPb(it.next()));
		}

		if (!wipe) {
			CommonPb.Record record = fightLogic.generateRecord();
			builder.setRecord(record);
		}

		if (exploreType == 1) {
			TaskManager.getInst().updTask(player, TaskType.COND_EQUIP_EPR, 1, null);
		} else if (exploreType == 2) {
			TaskManager.getInst().updTask(player, TaskType.COND_PART_EPR, 1, null);
		} else if (exploreType == 4) {
			TaskManager.getInst().updTask(player, TaskType.COND_LIMIT_COMBAT, 1, null);
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.DoCombatRs, builder.build());
	}

	/**
	 * 
	 * Method: doExtreme
	 * 
	 * @Description: 极限探险 @param req @param handler @return void @throws
	 */
	public void doExtreme(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		DoCombatRq req = msg.get();
		int combatId = req.getCombatId();
		CommonPb.Form form = req.getForm();

		ConfExplore staticExplore = ConfExploreProvider.getInst().getConfigById(combatId);
		if (staticExplore == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		if (staticExplore.getType() != 3) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);

		if (player.wipeTime != 0) {
//			handler.sendErrorMsgToPlayer(GameError.IN_WIPE);
			return;
		}

		Map<Integer, Equip> store = player.equips.get(0);
		if (store.size() >= player.lord.getEquip()) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_EQUIP_STORE);
			return;
		}

		if (!checkExploreLv(player, staticExplore.getType())) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		if (!checkExploreCount(player, staticExplore.getType())) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EXPLORE_COUNT);
			return;
		}

		if (combatId != 301) {
			if (player.extrEplrId < staticExplore.getPreId()) {
//				handler.sendErrorMsgToPlayer(GameError.COMBAT_PASS_BEFORE);
				return;
			}
		}

		if (combatId <= player.extrEplrId) {
//			handler.sendErrorMsgToPlayer(GameError.HAD_PASS);
			return;
		}

		DoCombatRs.Builder builder = DoCombatRs.newBuilder();
		int result = -1;

		Form attackForm = PbHelper.createForm(form);
		ConfHero staticHero = null;
		if (attackForm.getCommander() > 0) {
			Hero hero = player.heros.get(attackForm.getCommander());
			if (hero == null || hero.getCount() <= 0) {
//				handler.sendErrorMsgToPlayer(GameError.NO_HERO);
				return;
			}

			staticHero = ConfHeroProvider.getInst().getConfigById(hero.getHeroId());
			if (staticHero == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
				return;
			}

			if (staticHero.getType() != 2) {
//				handler.sendErrorMsgToPlayer(GameError.NOT_HERO);
				return;
			}
		}

		int maxTankCount = PlayerDataManager.getInst().formTankCount(player, staticHero);
		if (!PlayerDataManager.getInst().checkTank(player, attackForm, maxTankCount)) {
//			handler.sendErrorMsgToPlayer(GameError.TANK_COUNT);
			return;
		}

		Fighter attacker = FightService.getInst().createFighter(player, attackForm, 1);
		Fighter npc = FightService.getInst().createFighter(staticExplore);

		FightLogic fightLogic = new FightLogic(attacker, npc, FirstActType.ATTACKER, true);
		fightLogic.packForm(attackForm, PbHelper.createForm(staticExplore.getForm()));
		fightLogic.fight();

		CommonPb.Record record = fightLogic.generateRecord();
		builder.setRecord(record);

		if (fightLogic.getWinState() == 1) {
			result = fightLogic.estimateStar();
		}

		if (result > 0) {
			ExtremeDataManager.getInst().record(combatId, PbHelper.createAtkExtreme(PbHelper.createExtreme(player.lord.getNick(), player.lord.getLevel(), DateUtil.getSecondTime()), record));

			builder.addAllAward(dropAward(player, staticExplore.getDrop()));
			builder.addAllAward(dropOneAward(player, staticExplore.getDropOne(), staticExplore.getWeight()));
			builder.addAllAward(dropMaterial(player, staticExplore.getDropMaterial()));
			player.extrEplrId = combatId;
			if (combatId > player.extrMark) {
				player.extrMark = combatId;
//				RankDataManager.setExtreme(player);
			}

		} else {
			addExploreCount(player.lord, staticExplore.getType());
		}

		TaskManager.getInst().updTask(player, TaskType.COND_EXTR_EPR, 1, null);
		builder.setResult(result);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.DoCombatRs, builder.build());
	}

	/**
	 * 
	 * Method: addExploreCount
	 * 
	 * @Description: 增加探险次数 @param lord @param type @return void @throws
	 */
	private void addExploreCount(Lord lord, int type) {
		if (type == 1) {// 装备探险
			lord.setEquipEplr(lord.getEquipEplr() + 1);
		} else if (type == 2) {// 配件探险
			lord.setPartEplr(lord.getPartEplr() + 1);
		} else if (type == 3) {
			lord.setExtrEplr(lord.getExtrEplr() + 1);
		} else if (type == 5) {
			lord.setMilitaryEplr(lord.getMilitaryEplr() + 1);
		} else {
			lord.setTimeEplr(lord.getTimeEplr() + 1);
		}
	}

	private List<AwardPB> dropAward(Player player, List<List<Integer>> drop) {
		List<AwardPB> awards = new ArrayList<>();
		if (drop != null && !drop.isEmpty()) {
			for (List<Integer> award : drop) {
				if (award.size() != 4) {
					continue;
				}

				int prob = award.get(3);
				if (RandomHelper.isHitRangeIn100(prob)) {
					int type = award.get(0);
					int id = award.get(1);
					int count = award.get(2);
					int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.COMBAT_DROP);
					awards.add(PbHelper.createAwardPb(type, id, count, keyId));
				}
			}
		}
		return awards;
	}

	private List<AwardPB> dropCombatAward(Player player, List<List<Integer>> drop) {
		List<AwardPB> awards = new ArrayList<>();
		if (drop != null && !drop.isEmpty()) {
			Map<Integer, List<List<Integer>>> dropMap = new HashMap<Integer, List<List<Integer>>>();
			int count = 0;
			for (List<Integer> award : drop) {
				if (award.size() != 6) {
					continue;
				}
				int level = award.get(5);
				if (player.lord.getLevel() < level) {
					continue;
				}
				int grid = award.get(4);
				if (grid > count) {
					count = grid;
				}
				List<List<Integer>> awardList = dropMap.get(grid);
				if (awardList == null) {
					awardList = new ArrayList<List<Integer>>();
					dropMap.put(grid, awardList);
				}
				awardList.add(award);
			}

			for (int i = 0; i < count; i++) {
				List<List<Integer>> awardList = dropMap.get(i + 1);
				if (awardList != null) {
					int seeds[] = { 0, 0 };
					for (List<Integer> e : awardList) {
						seeds[0] += e.get(3);
					}
					seeds[0] = RandomHelper.randomInSize(100);
					for (List<Integer> e : awardList) {
						seeds[1] += e.get(3);
						if (seeds[0] <= seeds[1]) {
							int itemType = e.get(0);
							int itemId = e.get(1);
							int itemCount = e.get(2);
							int keyId = PlayerDataManager.getInst().addAward(player, itemType, itemId, itemCount, AwardFrom.COMBAT_DROP);
							awards.add(PbHelper.createAwardPb(itemType, itemId, itemCount, keyId));
							break;
						}
					}
				}
			}
		}
		return awards;
	}

	private List<AwardPB> dropAward(List<List<Integer>> drop) {
		List<AwardPB> awards = new ArrayList<>();
		if (drop != null && !drop.isEmpty()) {
			for (List<Integer> award : drop) {
				if (award.size() != 4) {
					continue;
				}

				int prob = award.get(3);
				if (RandomHelper.isHitRangeIn100(prob)) {
					int type = award.get(0);
					int id = award.get(1);
					int count = award.get(2);
					awards.add(PbHelper.createAwardPb(type, id, count));
				}
			}
		}
		return awards;
	}

	private List<AwardPB> dropOneAward(Player player, List<List<Integer>> drop, int weight) {
		List<AwardPB> awards = new ArrayList<>();
		if (weight <= 0) {
			return awards;
		}
		if (drop != null && !drop.isEmpty()) {
			int prob = RandomHelper.randomInSize(weight);
			int accumulate = 0;
			for (List<Integer> award : drop) {
				int type = award.get(0);
				int id = award.get(1);
				int count = award.get(2);
				accumulate += award.get(3);
				if (prob < accumulate) {
					int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.COMBAT_DROP);
					awards.add(PbHelper.createAwardPb(type, id, count, keyId));
					break;
				}
			}
		}
		return awards;
	}

	private List<AwardPB> dropOneAward(List<List<Integer>> drop, int weight) {
		List<AwardPB> awards = new ArrayList<>();
		if (weight <= 0) {
			return awards;
		}
		if (drop != null && !drop.isEmpty()) {
			int prob = RandomHelper.randomInSize(weight);
			int accumulate = 0;
			for (List<Integer> award : drop) {
				int type = award.get(0);
				int id = award.get(1);
				int count = award.get(2);
				accumulate += award.get(3);
				if (prob < accumulate) {
					awards.add(PbHelper.createAwardPb(type, id, count));
					break;
				}
			}
		}
		return awards;
	}

	private List<AwardPB> dropMaterial(Player player, List<List<Integer>> drop) {
		List<AwardPB> awards = new ArrayList<>();
		if (drop != null && !drop.isEmpty()) {
			for (List<Integer> award : drop) {
				if (award.size() != 4) {
					continue;
				}

				int prob = award.get(3);
				if (RandomHelper.isHitRangeIn100(prob)) {
					int type = award.get(0);
					int id = award.get(1);
					int count = award.get(2);
					int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.COMBAT_DROP);
					awards.add(PbHelper.createAwardPb(type, id, count, keyId));
					break;
				}
			}
		}
		return awards;
	}

	private List<AwardPB> dropMaterial(List<List<Integer>> drop) {
		List<AwardPB> awards = new ArrayList<>();
		if (drop != null && !drop.isEmpty()) {
			for (List<Integer> award : drop) {
				if (award.size() != 4) {
					continue;
				}

				int prob = award.get(3);
				if (RandomHelper.isHitRangeIn100(prob)) {
					int type = award.get(0);
					int id = award.get(1);
					int count = award.get(2);
					awards.add(PbHelper.createAwardPb(type, id, count));
					break;
				}
			}
		}
		return awards;
	}

	private void firstAward(Player player, List<List<Integer>> drop, DoCombatRs.Builder builder) {
		if (drop != null && !drop.isEmpty()) {
			for (List<Integer> award : drop) {
				if (award.size() != 3) {
					continue;
				}

				int type = award.get(0);
				int id = award.get(1);
				int count = award.get(2);
				int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.COMBAT_FIRST);
				builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
			}
		}
	}

	final static private int[] BUY_COST = { 98, 148, 198, 248, 298, 298 };

	// final static private int[] VIP_LIMIT = { 1, 3, 5, 7, 9, 11 };

	private int buyEquipCount(Player player) {
		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(player.lord.getVip());
		if (staticVip != null) {
			return staticVip.getBuyEquip();
		}

		return 0;
	}

	private int buyPartCount(Player player) {
		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(player.lord.getVip());
		if (staticVip != null) {
			return staticVip.getBuyPart();
		}

		return 0;
	}

	private int buyMilitaryCount(Player player) {
		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(player.lord.getVip());
		if (staticVip != null) {
			return staticVip.getBuyMilitary();
		}

		return 0;
	}

	/**
	 * 
	 * Method: buyExplore
	 * 
	 * @Description: 购买探险副本次数 @param type @param handler @return void @throws
	 */
	public void buyExplore(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		BuyExploreRq req = msg.get();
		int type = req.getType();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Lord lord = player.lord;
		cleanExplore(lord);
		if (type == 1) {// 装备探险
			int count = lord.getEquipBuy();
			if (count >= buyEquipCount(player)) {
//				handler.sendErrorMsgToPlayer(GameError.MAX_BUY_COUNT);
				return;
			}

			// if (lord.getVip() < VIP_LIMIT[count]) {
			// handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
			// return;
			// }

			int cost = BUY_COST[count];

//			float discount = activityDataManager.discountActivity(ActivityConst.ACT_EQUIP_FEED, 0);
//			if (discount != 0f && discount != 100f) {
//				cost = (int) (cost * discount / 100f);
//			}

			if (lord.getGold() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}

			PlayerDataManager.getInst().subGold(lord, cost, GoldCost.BUY_EXPLORE);
			lord.setEquipBuy(count + 1);

			BuyExploreRs.Builder builder = BuyExploreRs.newBuilder();
			builder.setCount(lord.getEquipBuy());
			builder.setGold(lord.getGold());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.BuyExploreRs, builder.build());

		} else if (type == 2) {// 配件探险
			int count = lord.getPartBuy();
			if (count >= buyPartCount(player)) {
//				handler.sendErrorMsgToPlayer(GameError.MAX_BUY_COUNT);
				return;
			}

			// if (lord.getVip() < VIP_LIMIT[count]) {
			// handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
			// return;
			// }

			int cost = BUY_COST[count];
//			float costF = activityDataManager.partSupply(player);
//			cost *= costF;
			if (lord.getGold() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}

			PlayerDataManager.getInst().subGold(lord, cost, GoldCost.BUY_EXPLORE);
			lord.setPartBuy(count + 1);

			TaskManager.getInst().updTask(player, TaskType.COND_PART_EPR, 1, null);
			BuyExploreRs.Builder builder = BuyExploreRs.newBuilder();
			builder.setCount(lord.getPartBuy());
			builder.setGold(lord.getGold());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.BuyExploreRs, builder.build());
		} else if (type == 5) {// 军工探险
			int count = lord.getMilitaryBuy();
			if (count >= buyMilitaryCount(player)) {
//				handler.sendErrorMsgToPlayer(GameError.MAX_BUY_COUNT);
				return;
			}

			int cost = BUY_COST[count];

			if (lord.getGold() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}

			PlayerDataManager.getInst().subGold(lord, cost, GoldCost.BUY_MILITARY);
			lord.setMilitaryBuy(count + 1);

			BuyExploreRs.Builder builder = BuyExploreRs.newBuilder();
			builder.setCount(lord.getMilitaryBuy());
			builder.setGold(lord.getGold());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.BuyExploreRs, builder.build());
		}
	}

	public void resetExtrEpr(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Lord lord = player.lord;
		cleanExplore(lord);

		if (lord.getExtrReset() == 1) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (!PlayerDataManager.getInst().subPower(lord, 5)) {
//			handler.sendErrorMsgToPlayer(GameError.NO_POWER);
			return;
		}

		lord.setExtrReset(1);
		player.extrEplrId = 300;

		ResetExtrEprRs.Builder builder = ResetExtrEprRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.ResetExtrEprRs, builder.build());
	}

	private int boxPosValue(int which) {
		return 1 << (which - 1);
	}

	/**
	 * 
	 * Method: getCombatBox
	 * 
	 * @Description: 领取普通副本章节宝箱 @param sectionId @param which @param handler @return
	 *               void @throws
	 */
	private void getCombatBox(int sectionId, int which, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ConfSection staticSection = ConfSectionProvider.getInst().getConfigById(sectionId);
		if (staticSection == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(packet.getPlayerId());
		Integer boxStatu = player.sections.get(sectionId);
		if (boxStatu != null) {
			if ((boxStatu & boxPosValue(which)) != 0) {
//				handler.sendErrorMsgToPlayer(GameError.ALREADY_GET_BOX);
				return;
			}
		} else {
			boxStatu = 0;
		}

		int start = staticSection.getStartId();
		int end = staticSection.getEndId();

		int star = 0;
		for (int i = start; i <= end; i++) {
			Combat combat = player.combats.get(i);
			if (combat != null) {
				star += combat.getStar();
			}
		}

		CombatBoxRs.Builder builder = CombatBoxRs.newBuilder();
		boolean checked = false;

		if (sectionId != 1) {
			if (which == 1) {
				if (star >= 12) {
					builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox1(), AwardFrom.COMBAT_BOX));
					checked = true;
				}
			} else if (which == 2) {
				if (star >= 24) {
					builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox2(), AwardFrom.COMBAT_BOX));
					checked = true;
				}
			} else if (which == 3) {
				if (star >= 36) {
					builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox3(), AwardFrom.COMBAT_BOX));
					checked = true;
				}
			}
		} else {
			if (star >= 33 && which == 1) {
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox3(), AwardFrom.COMBAT_BOX));
				checked = true;
			}
		}

		if (checked) {
			player.sections.put(staticSection.getSectionId(), boxStatu | boxPosValue(which));
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.CombatBoxRs, builder.build());
		}
//		else
//			handler.sendErrorMsgToPlayer(GameError.NO_STAR);

	}

	public void beginWipe(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player.lord.getVip() < 3) {
//			handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
			return;
		}

		Map<Integer, Equip> store = player.equips.get(0);
		if (store.size() >= player.lord.getEquip()) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_EQUIP_STORE);
			return;
		}

		if (!checkExploreLv(player, 3)) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		// if (!checkExploreCount(player, 3)) {
		// handler.sendErrorMsgToPlayer(GameError.NO_EXPLORE_COUNT);
		// return;
		// }

		if (player.extrEplrId == player.extrMark) {
//			handler.sendErrorMsgToPlayer(GameError.COMBAT_PASS_BEFORE);
		}

		if (player.wipeTime != 0) {
//			handler.sendErrorMsgToPlayer(GameError.IN_WIPE);
			return;
		}

		player.wipeTime = DateUtil.getSecondTime();

		TaskManager.getInst().updTask(player, TaskType.COND_EXTR_EPR, 1, null);

		BeginWipeRs.Builder builder = BeginWipeRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.BeginWipeRs, builder.build());
	}

	public void endWipe(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player.wipeTime == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NOT_IN_WIPE);
			return;
		}

		cleanExplore(player.lord);

		int now = DateUtil.getSecondTime();
		int count = (now - player.wipeTime) / 30;

		int beginExtr = player.extrEplrId + 1;
		int maxExtr = beginExtr + count;
		maxExtr = maxExtr > player.extrMark ? player.extrMark : maxExtr;

		player.wipeTime = 0;
		player.extrEplrId = maxExtr;

		EndWipeRs.Builder builder = EndWipeRs.newBuilder();
		for (int i = beginExtr; i < maxExtr; i++) {
			ConfExplore staticExplore = ConfExploreProvider.getInst().getConfigById(i);
			if (staticExplore != null) {
				builder.addAllAward(dropAward(player, staticExplore.getDrop()));
				builder.addAllAward(dropOneAward(player, staticExplore.getDropOne(), staticExplore.getWeight()));
				builder.addAllAward(dropMaterial(player, staticExplore.getDropMaterial()));
			}
		}

		builder.setCombatId(maxExtr);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.EndWipeRs, builder.build());
	}

	private void getEquipBox(int which, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ConfSection staticSection = ConfSectionProvider.getInst().getEquipSection();
		if (staticSection == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(packet.getPlayerId());

		Integer boxStatu = player.sections.get(staticSection.getSectionId());
		if (boxStatu != null) {
			if ((boxStatu & boxPosValue(which)) != 0) {
//				handler.sendErrorMsgToPlayer(GameError.ALREADY_GET_BOX);
				return;
			}
		} else {
			boxStatu = 0;
		}

		int star = 0;
		Iterator<Combat> it = player.explores.values().iterator();
		while (it.hasNext()) {
			Combat combat = (Combat) it.next();
			if (combat != null && combat.getCombatId() / 100 == 1) {
				star += combat.getStar();
			}
		}

		CombatBoxRs.Builder builder = CombatBoxRs.newBuilder();
		boolean checked = false;
		if (which == 1) {
			if (star >= 12) {
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox1(), AwardFrom.EQUIP_BOX));
				checked = true;
			}
		} else if (which == 2) {
			if (star >= 24) {
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox2(), AwardFrom.EQUIP_BOX));
				checked = true;
			}
		} else if (which == 3) {
			if (star >= 36) {
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox3(), AwardFrom.EQUIP_BOX));
				checked = true;
			}
		}

		if (checked) {
			player.sections.put(staticSection.getSectionId(), boxStatu | boxPosValue(which));
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.CombatBoxRs, builder.build());
		}
//		else
//			handler.sendErrorMsgToPlayer(GameError.NO_STAR);
	}

	private void getMilitaryBox(int which, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ConfSection staticSection = ConfSectionProvider.getInst().getMilitarySection();
		if (staticSection == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(packet.getPlayerId());
		Integer boxStatu = player.sections.get(staticSection.getSectionId());
		if (boxStatu != null) {
			if ((boxStatu & boxPosValue(which)) != 0) {
//				handler.sendErrorMsgToPlayer(GameError.ALREADY_GET_BOX);
				return;
			}
		} else {
			boxStatu = 0;
		}

		int star = 0;
		Iterator<Combat> it = player.explores.values().iterator();
		while (it.hasNext()) {
			Combat combat = (Combat) it.next();
			if (combat != null && combat.getCombatId() / 100 == 5) {
				star += combat.getStar();
			}
		}

		CombatBoxRs.Builder builder = CombatBoxRs.newBuilder();

		boolean checked = false;
		if (which == 1) {
			if (star >= 12) {
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox1(), AwardFrom.PART_BOX));
				checked = true;
			}
		} else if (which == 2) {
			if (star >= 24) {
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox2(), AwardFrom.PART_BOX));
				checked = true;
			}
		} else if (which == 3) {
			if (star >= 36) {
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox3(), AwardFrom.PART_BOX));
				checked = true;
			}
		}

		if (checked) {
			player.sections.put(staticSection.getSectionId(), boxStatu | boxPosValue(which));
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.CombatBoxRs, builder.build());
		}
//		else
//			handler.sendErrorMsgToPlayer(GameError.NO_STAR);
	}

	private void getPartBox(int which, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ConfSection staticSection = ConfSectionProvider.getInst().getPartSection();
		if (staticSection == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(packet.getPlayerId());
		Integer boxStatu = player.sections.get(staticSection.getSectionId());
		if (boxStatu != null) {
			if ((boxStatu & boxPosValue(which)) != 0) {
//				handler.sendErrorMsgToPlayer(GameError.ALREADY_GET_BOX);
				return;
			}
		} else {
			boxStatu = 0;
		}

		int star = 0;
		Iterator<Combat> it = player.explores.values().iterator();
		while (it.hasNext()) {
			Combat combat = (Combat) it.next();
			if (combat != null && combat.getCombatId() / 100 == 2) {
				star += combat.getStar();
			}
		}

		CombatBoxRs.Builder builder = CombatBoxRs.newBuilder();

		boolean checked = false;
		if (which == 1) {
			if (star >= 12) {
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox1(), AwardFrom.PART_BOX));
				checked = true;
			}
		} else if (which == 2) {
			if (star >= 24) {
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox2(), AwardFrom.PART_BOX));
				checked = true;
			}
		} else if (which == 3) {
			if (star >= 36) {
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticSection.getBox3(), AwardFrom.PART_BOX));
				checked = true;
			}
		}

		if (checked) {
			player.sections.put(staticSection.getSectionId(), boxStatu | boxPosValue(which));
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.CombatBoxRs, builder.build());
		}
//		else
//			handler.sendErrorMsgToPlayer(GameError.NO_STAR);
	}

	private void exchangeTimeBox(int which, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int cost = 0;
		int awardId = 0;

		if (which == 1) {
			cost = 10;
			awardId = PropId.HUANGBAO_1;
		} else if (which == 2) {
			cost = 20;
			awardId = PropId.HUANGBAO_2;
		} else if (which == 3) {
			cost = 40;
			awardId = PropId.HUANGBAO_3;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player.lord.getHuangbao() < cost) {
//			handler.sendErrorMsgToPlayer(GameError.HUANGBAO_NOT_ENOUGH);
			return;
		}

		PlayerDataManager.getInst().subHuangbao(player.lord, cost);

		PlayerDataManager.getInst().addAward(player, AwardType.PROP, awardId, 1, AwardFrom.HUANGBAO_BOX);
		CombatBoxRs.Builder builder = CombatBoxRs.newBuilder();
		builder.addAward(PbHelper.createAwardPb(AwardType.PROP, awardId, 1));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.CombatBoxRs, builder.build());
	}

	/**
	 * 
	 * Method: combatBox
	 * 
	 * @Description: 领取副本星级宝箱 @param req @param handler @return void @throws
	 */
	public void combatBox(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		CombatBoxRq req = msg.get();
		int type = req.getType();
		int id = req.getId();
		int which = req.getWhich();
		if (which < 0 || which > 3) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (type == 1) {
			getCombatBox(id, which, ctx, packet, msg);
		} else if (type == 2) {
			getEquipBox(which, ctx, packet, msg);
		} else if (type == 3) {
			getPartBox(which, ctx, packet, msg);
		} else if (type == 4) {
			exchangeTimeBox(which, ctx, packet, msg);
		} else if (type == 6) {
			getMilitaryBox(which, ctx, packet, msg);
		}
	}

	public void getExtreme(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		GetExtremeRq req = msg.get();
		int extremeId = req.getExtremeId();
		Extreme extreme = ExtremeDataManager.getInst().getExtreme(extremeId);
		if (extreme == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		GetExtremeRs.Builder builder = GetExtremeRs.newBuilder();
		AtkExtreme first = extreme.getFirst1();
		if (first != null) {
			builder.setFirst(first.getAttacker());
		}

		for (AtkExtreme e : extreme.getLast3()) {
			builder.addLast3(e.getAttacker());
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetExtremeRs, builder.build());
	}

	public void extremeRecord(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		ExtremeRecordRq req = msg.get();
		int extremeId = req.getExtremeId();
		int which = req.getWhich();
		Extreme extreme = ExtremeDataManager.getInst().getExtreme(extremeId);
		if (extreme == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		if (which < 0) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		ExtremeRecordRs.Builder builder = ExtremeRecordRs.newBuilder();
		if (which == 0) {
			if (extreme.getFirst1() != null) {
				builder.setRecord(extreme.getFirst1().getRecord());
			}
		} else {
			int index = 0;
			for (AtkExtreme e : extreme.getLast3()) {
				index++;
				if (which == index) {
					builder.setRecord(e.getRecord());
				}
			}
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.ExtremeRecordRs, builder.build());
	}

	public void checkWipe(Player player, int now) {
		int count = (now - player.wipeTime) / 30;
		int beginExtr = player.extrEplrId + 1;
		int maxExtr = beginExtr + count;
		if (maxExtr >= player.extrMark) {
			List<AwardPB> awards = new ArrayList<>();
			for (int i = beginExtr; i <= player.extrMark; i++) {
				ConfExplore staticExplore = ConfExploreProvider.getInst().getConfigById(i);
				if (staticExplore != null) {
					awards.addAll(dropAward(staticExplore.getDrop()));
					awards.addAll(dropOneAward(staticExplore.getDropOne(), staticExplore.getWeight()));
					awards.addAll(dropMaterial(staticExplore.getDropMaterial()));
				}
			}

			player.wipeTime = 0;
			player.extrEplrId = player.extrMark;
			MailManager.getInst().sendAttachMail(player, awards, MailType.MOLD_WIPE, now, String.valueOf(count + 1));
		}
	}
}
