package network.handler.module.tank;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.domain.Player;
import com.game.fight.FightLogic;
import com.game.fight.domain.Fighter;
import com.game.util.LogHelper;
import com.game.util.PbHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfArenaAward;
import config.bean.ConfHero;
import config.bean.ConfProp;
import config.bean.ConfVip;
import config.provider.ConfHeroProvider;
import config.provider.ConfPropProvider;
import config.provider.ConfVipProvider;
import data.bean.Arena;
import data.bean.ArenaLog;
import data.bean.Equip;
import data.bean.Form;
import data.bean.Hero;
import data.bean.Lord;
import data.bean.Mail;
import data.bean.RptTank;
import define.AwardFrom;
import define.AwardType;
import define.FirstActType;
import define.FormType;
import define.GoldCost;
import define.MailType;
import define.PropId;
import define.SysChatId;
import define.TaskType;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.ArenaDataManager;
import manager.GlobalDataManager;
import manager.MailManager;
import manager.PartyDataManager;
import manager.PlayerDataManager;
import manager.TaskManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.CommonPb;
import pb.CommonPb.AwardPB;
import pb.CommonPb.Report;
import pb.CommonPb.RptAtkArena;
import pb.GamePb.ArenaAwardRs;
import pb.GamePb.BuyArenaCdRq;
import pb.GamePb.BuyArenaCdRs;
import pb.GamePb.BuyArenaRs;
import pb.GamePb.DoArenaRs;
import pb.GamePb.GetArenaRs;
import pb.GamePb.GetRankRs;
import pb.GamePb.InitArenaRq;
import pb.GamePb.InitArenaRs;
import pb.GamePb.UseScoreRs;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;

@Singleton
public class ArenaService implements IModuleMessageHandler {

	public static ArenaService getInst() {
		return BeanManager.getBean(ArenaService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	final static public int ARENA_LV = 15;

	/**
	 * 
	 * Method: getArena
	 * 
	 * @Description: 获取竞技场数据 @param handler @return void @throws
	 */
	public void getArena(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		GetArenaRs.Builder builder = GetArenaRs.newBuilder();
		if (player.lord.getLevel() >= ARENA_LV) {
			Arena arena = ArenaDataManager.getInst().enterArena(player.roleId);
			if (arena != null) {
				builder.setCount(arena.getCount());
				builder.setScore(arena.getScore());
				builder.setRank(arena.getRank());
				builder.setLastRank(arena.getLastRank());
				builder.setWinCount(arena.getWinCount());
				builder.setFight(arena.getFight());
				builder.setBuyCount(arena.getBuyCount());
				builder.setColdTime(arena.getColdTime());
				int nowDay = DateUtil.getToday();
				if (arena.getAwardTime() != nowDay) {
					builder.setAward(false);
				} else {
					builder.setAward(true);
				}

				List<Arena> list = ArenaDataManager.getInst().randomEnemy(arena.getRank());
				for (int i = 0; i < list.size(); i++) {
					Arena one = list.get(i);
					if (one == null) {
						LogHelper.ERROR_LOGGER.error("getArena get a null rank:" + player.roleId);
					}

					builder.addRankPlayer(PbHelper.createRankPlayer(one, PlayerDataManager.getInst().getPlayer(one.getLordId())));
				}

				int unRead = 0;
				Iterator<Mail> it = player.mails.values().iterator();
				while (it.hasNext()) {
					Mail mail = (Mail) it.next();
					if (mail.getType() == MailType.ARENA_MAIL && mail.getState() == MailType.STATE_UNREAD) {
						++unRead;
					}
				}
				builder.setUnread(unRead);
			}
		}

		Arena champion = ArenaDataManager.getInst().getArenaByRank(1);
		if (champion != null) {
			Player championPlayer = PlayerDataManager.getInst().getPlayer(champion.getLordId());
			if (championPlayer != null) {
				builder.setChampion(championPlayer.lord.getNick());
			}
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetArenaRs, builder.build());
	}

	/**
	 * 
	 * Method: initArena
	 * 
	 * @Description: 初始化玩家竞技场 @param req @param handler @return void @throws
	 */
	public void initArena(InitArenaRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player.lord.getLevel() < ARENA_LV) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		Arena arena = ArenaDataManager.getInst().getArena(player.roleId);
		if (arena != null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		CommonPb.Form form = req.getForm();
		Form attackForm = PbHelper.createForm(form);

		if (attackForm.getType() != FormType.ARENA) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		ConfHero staticHero = null;
		if (form.getCommander() > 0) {
			Hero hero = player.heros.get(form.getCommander());
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

		player.forms.put(FormType.ARENA, attackForm);

		int fight = FightService.getInst().calcFormFight(player, attackForm);
		arena = ArenaDataManager.getInst().addNew(player.roleId);
		arena.setFight(fight);

		InitArenaRs.Builder builder = InitArenaRs.newBuilder();
		builder.setRank(arena.getRank());
		builder.setCount(arena.getCount());
		builder.setFight(fight);
		List<Arena> list = ArenaDataManager.getInst().randomEnemy(arena.getRank());
		for (int i = 0; i < list.size(); i++) {
			Arena one = list.get(i);
			builder.addRankPlayer(PbHelper.createRankPlayer(one, PlayerDataManager.getInst().getPlayer(one.getLordId())));
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.InitArenaRs, builder.build());
	}

	private CommonPb.RptMan createRptMan(Player player, int hero, Map<Integer, RptTank> haust) {
		CommonPb.RptMan.Builder builder = CommonPb.RptMan.newBuilder();
		Lord lord = player.lord;
		builder.setName(lord.getNick());
		builder.setVip(lord.getVip());

		String party = PartyDataManager.getInst().getPartyNameByLordId(player.roleId);
		if (party != null) {
			builder.setParty(party);
		}

		if (hero != 0) {
			builder.setHero(hero);
		}

		if (haust != null) {
			Iterator<RptTank> it = haust.values().iterator();
			while (it.hasNext()) {
				builder.addTank(PbHelper.createRtpTankPb(it.next()));
			}
		}

		return builder.build();
	}

	private Report createAtkArenaReport(RptAtkArena rpt, int now) {
		Report.Builder report = Report.newBuilder();
		report.setAtkArena(rpt);
		report.setTime(now);
		return report.build();
	}

	private Report createDefArenaReport(RptAtkArena rpt, int now) {
		Report.Builder report = Report.newBuilder();
		report.setDefArena(rpt);
		report.setTime(now);
		return report.build();
	}

	private Report createGloabalArenaReport(RptAtkArena rpt, int now) {
		Report.Builder report = Report.newBuilder();
		report.setGlobalArena(rpt);
		report.setTime(now);
		return report.build();
	}

	/**
	 * 
	 * Method: doArena
	 * 
	 * @Description: 竞技场挑战 @param rank @param handler @return void @throws
	 */
	public void doArena(int rank, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player.lord.getLevel() < ARENA_LV) {
//			handler.sendErrorMsgToPlayer(GameError.LV_NOT_ENOUGH);
			return;
		}

		Map<Integer, Equip> store = player.equips.get(0);
		if (store.size() >= player.lord.getEquip()) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_EQUIP_STORE);
			return;
		}

		Arena arena = ArenaDataManager.getInst().enterArena(player.roleId);
		if (arena == null) {
//			handler.sendErrorMsgToPlayer(GameError.SERVER_EXCEPTION);
			return;
		}

		if (arena.getCount() <= 0) {
//			handler.sendErrorMsgToPlayer(GameError.ARENA_COUNT);
			return;
		}

		int now = DateUtil.getSecondTime();
		if (now < arena.getColdTime() + 10 * 60) {
//			handler.sendErrorMsgToPlayer(GameError.ARENA_CD);
			return;
		}

		Form form = player.forms.get(FormType.ARENA);
		if (form == null) {
//			handler.sendErrorMsgToPlayer(GameError.ARENA_FORM);
			return;
		}

		Arena targetArena = ArenaDataManager.getInst().getArenaByRank(rank);
		if (targetArena == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (targetArena.getLordId() == player.roleId) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player target = PlayerDataManager.getInst().getPlayer(targetArena.getLordId());
		if (target == null) {
//			handler.sendErrorMsgToPlayer(GameError.SERVER_EXCEPTION);
			return;
		}

		Form targetForm = target.forms.get(FormType.ARENA);
		if (targetForm == null) {
//			handler.sendErrorMsgToPlayer(GameError.ARENA_FORM);
			return;
		}

		Fighter attacker = FightService.getInst().createFighter(player, form, 3);
		Fighter defencer = FightService.getInst().createFighter(target, targetForm, 3);

		FightLogic fightLogic = new FightLogic(attacker, defencer, FirstActType.FISRT_VALUE_2, true);
		fightLogic.packForm(form, targetForm);
		fightLogic.fight();

		CommonPb.Record record = fightLogic.generateRecord();
		int result = (fightLogic.getWinState() == 1) ? 1 : -1;

		Map<Integer, RptTank> attackHaust = FightService.getInst().statisticHaustTank(attacker);
		Map<Integer, RptTank> defenceHaust = FightService.getInst().statisticHaustTank(defencer);

		RptAtkArena.Builder rpt = RptAtkArena.newBuilder();
		rpt.setFirst(fightLogic.attackerIsFirst());
		rpt.setAttacker(createRptMan(player, form.getCommander(), attackHaust));
		rpt.setDefencer(createRptMan(target, targetForm.getCommander(), defenceHaust));
		rpt.setRecord(record);

		DoArenaRs.Builder builder = DoArenaRs.newBuilder();
		builder.setRecord(record);
		builder.setForm(PbHelper.createFormPb(targetForm));
		builder.setResult(result);

		int scoreAward = 0;
		if (result == 1) {
			boolean aquirement = false;
			if (arena.getRank() > targetArena.getRank()) {
				ArenaDataManager.getInst().exchangeArena(arena, targetArena);
				if (arena.getRank() == 1) {
					aquirement = true;
					ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.ARENA_2, player.lord.getNick(), target.lord.getNick()));
				}
			}

			arena.setWinCount(arena.getWinCount() + 1);
			targetArena.setWinCount(0);

			scoreAward = scoreAward(arena.getWinCount());
			arena.setScore(arena.getScore() + scoreAward);

			// 竞技场宝箱
			PlayerDataManager.getInst().addProp(player, PropId.ARENA_BOX, 1);

			AwardPB box = PbHelper.createAwardPb(AwardType.PROP, PropId.ARENA_BOX, 1);
			AwardPB score = PbHelper.createAwardPb(AwardType.SCORE, 0, scoreAward);
			builder.addAward(box);
			builder.setScore(arena.getScore());

			rpt.addAward(box);
			rpt.addAward(score);
			rpt.setResult(true);

			RptAtkArena rptAtkArena = rpt.build();
			MailManager.getInst().sendArenaReportMail(player, createAtkArenaReport(rptAtkArena, now), MailType.MOLD_ARENA_3, now, target.lord.getNick());
			MailManager.getInst().sendArenaReportMail(target, createDefArenaReport(rptAtkArena, now), MailType.MOLD_ARENA_5, now, player.lord.getNick());

			if (arena.getRank() <= 10 || targetArena.getRank() <= 10) {
				GlobalDataManager.getInst().addGlobalReportMail(createGloabalArenaReport(rptAtkArena, now), MailType.MOLD_ARENA_2, now, player.lord.getNick(), target.lord.getNick());
			}

			if (!aquirement && arena.getWinCount() % 5 == 0) {
				ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.ARENA_1, player.lord.getNick(), String.valueOf(arena.getWinCount())));
			}

//			activityDataManager.updActivity(player, ActivityConst.ACT_CRAZY_ARENA, 1, 0);

		} else {
			scoreAward = 5;
			arena.setWinCount(0);
			arena.setColdTime(DateUtil.getSecondTime());
			arena.setScore(arena.getScore() + scoreAward);

			// 装备卡【50】
			int keyId = PlayerDataManager.getInst().addEquip(player, 701, 0).getKeyId();

			AwardPB equip = PbHelper.createAwardPb(AwardType.EQUIP, 701, 1, keyId);
			AwardPB score = PbHelper.createAwardPb(AwardType.SCORE, 0, scoreAward);

			builder.addAward(equip);
			builder.setScore(arena.getScore());
			builder.setColdTime(arena.getColdTime());

			rpt.addAward(equip);
			rpt.addAward(score);
			rpt.setResult(false);

			RptAtkArena rptAtkArena = rpt.build();
			MailManager.getInst().sendArenaReportMail(player, createAtkArenaReport(rptAtkArena, now), MailType.MOLD_ARENA_4, now, target.lord.getNick());
			MailManager.getInst().sendArenaReportMail(target, createDefArenaReport(rptAtkArena, now), MailType.MOLD_ARENA_6, now, player.lord.getNick());

			if (arena.getRank() <= 10 || targetArena.getRank() <= 10) {
				GlobalDataManager.getInst().addGlobalReportMail(createGloabalArenaReport(rptAtkArena, now), MailType.MOLD_ARENA_1, now, player.lord.getNick(), target.lord.getNick());
			}
		}

		arena.setCount(arena.getCount() - 1);
		TaskManager.getInst().updTask(player, TaskType.COND_ARENA, 1, null);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.DoArenaRs, builder.build());
	}

	/**
	 * 
	 * Method: buyArena
	 * 
	 * @Description: 购买竞技场次数 @param handler @return void @throws
	 */
	public void buyArena(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);

		Arena arena = ArenaDataManager.getInst().enterArena(player.roleId);
		if (arena == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (arena.getBuyCount() >= buyCount(player)) {
//			handler.sendErrorMsgToPlayer(GameError.MAX_BUY_ARENA);
			return;
		}

		int cost = 10 + arena.getBuyCount() * 2;
		cost = (cost > 100) ? 100 : cost;
		if (player.lord.getGold() < cost) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
			return;
		}

		PlayerDataManager.getInst().subGold(player.lord, cost, GoldCost.BUY_ARENA);
		arena.setCount(arena.getCount() + 1);
		arena.setBuyCount(arena.getBuyCount() + 1);

		BuyArenaRs.Builder builder = BuyArenaRs.newBuilder();
		builder.setCount(arena.getCount());
		builder.setGold(player.lord.getGold());
//		handler.sendMsgToPlayer(BuyArenaRs.ext, builder.build());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.BuyArenaRs, builder.build());
	}

	/**
	 * 
	 * Method: useScore
	 * 
	 * @Description: 竞技场积分兑换 @param propId @param handler @return void @throws
	 */
	public void useScore(int propId, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Arena arena = ArenaDataManager.getInst().enterArena(player.roleId);
		if (arena == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		ConfProp staticProp = ConfPropProvider.getInst().getConfigById(propId);
		if (staticProp == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		if (staticProp.getArenaScore() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (arena.getScore() < staticProp.getArenaScore()) {
//			handler.sendErrorMsgToPlayer(GameError.ARENA_SCORE);
			return;
		}

		arena.setScore(arena.getScore() - staticProp.getArenaScore());

		if (propId == PropId.TOOL) {//
			PlayerDataManager.getInst().addPartMaterial(player, 5, 1);
		} else {
			PlayerDataManager.getInst().addAward(player, AwardType.PROP, propId, 1, AwardFrom.ARENA_SCORE);
		}

		UseScoreRs.Builder builder = UseScoreRs.newBuilder();
		builder.setScore(arena.getScore());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.UseScoreRs, builder.build());
	}

	/**
	 * 
	 * Method: arenaAward
	 * 
	 * @Description: 领取排名奖励 @param handler @return void @throws
	 */
	public void arenaAward(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Arena arena = ArenaDataManager.getInst().enterArena(player.roleId);
		if (arena == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		if (arena.getLastRank() > 500) {
//			handler.sendErrorMsgToPlayer(GameError.RANK_NOT_ENOUGH);
			return;
		}

		int nowDay = DateUtil.getToday();
		if (arena.getAwardTime() == nowDay) {
//			handler.sendErrorMsgToPlayer(GameError.ALREADY_ARENA_AWARD);
			return;
		}

		ConfArenaAward staticArenaAward = ArenaDataManager.getInst().getRankAward(arena.getLastRank());
		if (staticArenaAward == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		arena.setAwardTime(nowDay);

		ArenaAwardRs.Builder builder = ArenaAwardRs.newBuilder();
		builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, staticArenaAward.getAward(), AwardFrom.ARENA_RANK));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.ArenaAwardRs, builder.build());
	}

	private int buyCount(Player player) {
		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(player.lord.getVip());
		if (staticVip != null) {
			return staticVip.getBuyArena();
		}

		return 1;
	}

	private int scoreAward(int winCount) {
		if (winCount < 6) {
			return 10;
		} else if (winCount < 11) {
			return 12;
		} else if (winCount < 21) {
			return 14;
		} else if (winCount < 51) {
			return 16;
		} else if (winCount < 101) {
			return 18;
		} else {
			return 20;
		}
	}

	public void getRankData(int page, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		GetRankRs.Builder builder = GetRankRs.newBuilder();
		int begin = (page - 1) * 20 + 1;
		int end = page * 20 + 1;
		for (int i = begin; i < end; i++) {
			Arena arena = ArenaDataManager.getInst().getArenaByRank(i);
			if (arena != null) {
				Lord lord = PlayerDataManager.getInst().getPlayer(arena.getLordId()).lord;
				if (lord != null) {
					builder.addRankData(PbHelper.createRankData(lord.getNick(), lord.getLevel(), arena.getFight()));
				}
			}
		}

		Arena arena = ArenaDataManager.getInst().getArena(playerId);
		int rank = 0;
		if (arena != null) {
			rank = arena.getRank();
		}
		builder.setRank(rank);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetRankRs, builder.build());
	}

	public void buyArenaCd(BuyArenaCdRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Arena arena = ArenaDataManager.getInst().enterArena(player.roleId);
		if (arena == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		int now = DateUtil.getSecondTime();
//		if (now < arena.getColdTime() + 10 * 60) {
//			handler.sendErrorMsgToPlayer(GameError.ARENA_CD);
//			return;
//		}

		int leftTime = arena.getColdTime() + 10 * 60 - now;
		if (leftTime > 0) {
			int cost = (int) Math.ceil(leftTime / 60.0);
			if (player.lord.getGold() < cost) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}

			PlayerDataManager.getInst().subGold(player.lord, cost, GoldCost.ARENA_CD);
			arena.setColdTime(now - 60 * 10);
		}

		BuyArenaCdRs.Builder builder = BuyArenaCdRs.newBuilder();
		builder.setGold(player.lord.getGold());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.BuyArenaCdRs, builder.build());
	}

	/**
	 * 
	 * Method: arenaTimerLogic
	 * 
	 * @Description: 竞技场定时器逻辑 @return void @throws
	 */
	public void arenaTimerLogic() {
		ArenaLog serverLog = ArenaDataManager.getInst().getArenaLog();
		int nowDay = DateUtil.getToday();
		if (nowDay != serverLog.getArenaTime()) {
			dayRank(nowDay);
//			activityService.activityTimeLogic();
			ArenaDataManager.getInst().setArenaLog(new ArenaLog(nowDay, 0));
			ArenaDataManager.getInst().flushArenaLog();
		}
	}

	private void dayRank(int awardDay) {
		Iterator<Arena> it = ArenaDataManager.getInst().getRankMap().values().iterator();
		int rank;
		Arena arena = null;
		while (it.hasNext()) {
			arena = (Arena) it.next();
			rank = arena.getRank();
			arena.setLastRank(rank);
		}
	}

}
