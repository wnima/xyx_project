package network.handler.module.tank;
//package com.game.module;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//
//import com.game.constant.ActivityConst;
//import com.game.constant.AwardFrom;
//import com.game.constant.AwardType;
//import com.game.constant.GameError;
//import com.game.constant.GoldCost;
//import com.game.constant.ItemFrom;
//import com.game.constant.SysChatId;
//import com.game.dataMgr.StaticActivityDataMgr;
//import com.game.dataMgr.StaticAwardsDataMgr;
//import com.game.dataMgr.StaticPartDataMgr;
//import com.game.dataMgr.StaticPropDataMgr;
//import com.game.dataMgr.StaticTankDataMgr;
//import com.game.domain.ActivityBase;
//import com.game.domain.Member;
//import com.game.domain.PartyData;
//import com.game.domain.Player;
//import com.game.domain.UsualActivityData;
//import com.game.domain.p.ActPartyRank;
//import com.game.domain.p.ActPlayerRank;
//import com.game.domain.p.Activity;
//import com.game.domain.p.Cash;
//import com.game.domain.p.Chip;
//import com.game.domain.p.Equip;
//import com.game.domain.p.Lord;
//import com.game.domain.p.Part;
//import com.game.domain.p.Prop;
//import com.game.domain.s.StaticActAward;
//import com.game.domain.s.StaticActEverydayPay;
//import com.game.domain.s.StaticActExchange;
//import com.game.domain.s.StaticActFortune;
//import com.game.domain.s.StaticActGamble;
//import com.game.domain.s.StaticActGeneral;
//import com.game.domain.s.StaticActMecha;
//import com.game.domain.s.StaticActPartResolve;
//import com.game.domain.s.StaticActProfoto;
//import com.game.domain.s.StaticActRaffle;
//import com.game.domain.s.StaticActRank;
//import com.game.domain.s.StaticActRebate;
//import com.game.domain.s.StaticActTech;
//import com.game.domain.s.StaticActVacationland;
//import com.game.domain.s.StaticPart;
//import com.game.domain.s.StaticProp;
//import com.game.domain.s.StaticTank;
//import com.game.manager.ActivityDataManager;
//import com.game.manager.PartyDataManager;
//import com.game.manager.PlayerDataManager;
//import com.game.message.handler.ClientHandler;
//import pb.CommonPb;
//import pb.GamePb.AssembleMechaRq;
//import pb.GamePb.AssembleMechaRs;
//import pb.GamePb.BuyActVacationlandRq;
//import pb.GamePb.BuyActVacationlandRs;
//import pb.GamePb.DoActAmyRebateRq;
//import pb.GamePb.DoActAmyRebateRs;
//import pb.GamePb.DoActAmyfestivityRq;
//import pb.GamePb.DoActAmyfestivityRs;
//import pb.GamePb.DoActConsumeDialRq;
//import pb.GamePb.DoActConsumeDialRs;
//import pb.GamePb.DoActEDayPayRs;
//import pb.GamePb.DoActFortuneRq;
//import pb.GamePb.DoActFortuneRs;
//import pb.GamePb.DoActGambleRq;
//import pb.GamePb.DoActGambleRs;
//import pb.GamePb.DoActGeneralRq;
//import pb.GamePb.DoActGeneralRs;
//import pb.GamePb.DoActMechaRq;
//import pb.GamePb.DoActMechaRs;
//import pb.GamePb.DoActNewRaffleRq;
//import pb.GamePb.DoActNewRaffleRs;
//import pb.GamePb.DoActPartDialRq;
//import pb.GamePb.DoActPartDialRs;
//import pb.GamePb.DoActPartResolveRq;
//import pb.GamePb.DoActPartResolveRs;
//import pb.GamePb.DoActPayTurntableRq;
//import pb.GamePb.DoActPayTurntableRs;
//import pb.GamePb.DoActProfotoRs;
//import pb.GamePb.DoActTankRaffleRq;
//import pb.GamePb.DoActTankRaffleRs;
//import pb.GamePb.DoActTechRq;
//import pb.GamePb.DoActTechRs;
//import pb.GamePb.DoActVacationlandRq;
//import pb.GamePb.DoActVacationlandRs;
//import pb.GamePb.DoEquipCashRq;
//import pb.GamePb.DoEquipCashRs;
//import pb.GamePb.DoPartCashRq;
//import pb.GamePb.DoPartCashRs;
//import pb.GamePb.GetActAmyRebateRs;
//import pb.GamePb.GetActAmyfestivityRs;
//import pb.GamePb.GetActBeeRankRq;
//import pb.GamePb.GetActBeeRankRs;
//import pb.GamePb.GetActBeeRs;
//import pb.GamePb.GetActCarnivalRs;
//import pb.GamePb.GetActConsumeDialRankRs;
//import pb.GamePb.GetActConsumeDialRs;
//import pb.GamePb.GetActDestroyRankRs;
//import pb.GamePb.GetActDestroyRs;
//import pb.GamePb.GetActEDayPayRs;
//import pb.GamePb.GetActEquipCashRs;
//import pb.GamePb.GetActFortuneRankRs;
//import pb.GamePb.GetActFortuneRs;
//import pb.GamePb.GetActGambleRs;
//import pb.GamePb.GetActGeneralRankRs;
//import pb.GamePb.GetActGeneralRs;
//import pb.GamePb.GetActMechaRs;
//import pb.GamePb.GetActNewRaffleRs;
//import pb.GamePb.GetActPartCashRs;
//import pb.GamePb.GetActPartDialRankRs;
//import pb.GamePb.GetActPartDialRs;
//import pb.GamePb.GetActPartResolveRs;
//import pb.GamePb.GetActPartyDonateRankRs;
//import pb.GamePb.GetActPayTurntableRs;
//import pb.GamePb.GetActPrayRs;
//import pb.GamePb.GetActProfotoRs;
//import pb.GamePb.GetActTankRaffleRs;
//import pb.GamePb.GetActTechRs;
//import pb.GamePb.GetActVacationlandRs;
//import pb.GamePb.GetActionCenterRs;
//import pb.GamePb.GetPartyRankAwardRq;
//import pb.GamePb.GetPartyRankAwardRs;
//import pb.GamePb.GetRankAwardListRq;
//import pb.GamePb.GetRankAwardListRs;
//import pb.GamePb.GetRankAwardRq;
//import pb.GamePb.GetRankAwardRs;
//import pb.GamePb.LockNewRaffleRq;
//import pb.GamePb.LockNewRaffleRs;
//import pb.GamePb.RefshEquipCashRq;
//import pb.GamePb.RefshEquipCashRs;
//import pb.GamePb.RefshPartCashRq;
//import pb.GamePb.RefshPartCashRs;
//import pb.GamePb.UnfoldProfotoRs;
//import util.CompareEquipLv;
//import util.LogHelper;
//import util.PbHelper;
//import util.RandomHelper;
//import util.TimeHelper;
//
//public class ActionCenterModule {
//
////	@Autowired
////	private PlayerDataManager playerDataManager;
////
////	@Autowired
////	private PartyDataManager partyDataManager;
////
////	@Autowired
////	private ActivityDataManager activityDataManager;
////
////	@Autowired
////	private StaticActivityDataMgr staticActivityDataMgr;
////
////	@Autowired
////	private StaticTankDataMgr staticTankDataMgr;
////
////	@Autowired
////	private StaticPropDataMgr staticPropDataMgr;
////
////	@Autowired
////	private StaticAwardsDataMgr staticAwardsDataMgr;
////
////	@Autowired
////	private StaticPartDataMgr staticPartDataMgr;
////
////	@Autowired
////	private ChatService chatService;
////	
////	@Autowired
////	private PropService propService;
//	
//	
//
//	public void getActionCenterRq(ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		int platFlag = 1;// 默认为安卓玩家
//		int platNo = player.account.getPlatNo();
//		if (platNo == 94 || platNo == 95 || platNo > 500) {// IOS玩家
//			platFlag = 2;
//		}
//
//		Date now = new Date();
//		List<ActivityBase> list = staticActivityDataMgr.getActivityList();
//		GetActionCenterRs.Builder builder = GetActionCenterRs.newBuilder();
//		for (ActivityBase e : list) {
//			if (e.getActivityId() < 100 || e.getActivityId() >= 1000) {
//				continue;
//			}
//			int plat = e.getPlan().getPlat();
//			if (plat == 1 && platFlag == 2) {// 如果是安卓平台,IOS玩家不可见
//				continue;
//			} else if (plat == 2 && platFlag == 1) {// 如果是IOS平台,安卓玩家不可见
//				continue;
//			}
//			Date beginTime = e.getBeginTime();
//			Date endTime = e.getEndTime();
//			Date display = e.getDisplayTime();
//
//			int open = e.getBaseOpen();
//			if (open == ActivityConst.OPEN_CLOSE) {// 活动未开启
//				continue;
//			}
//
//			if (display == null) {
//				if (now.after(beginTime) && now.before(endTime)) {
//					builder.addActivity(PbHelper.createActivityPb(e, true, 0));
//				}
//			} else {
//				if (now.after(beginTime)) {
//					if (now.before(endTime)) {
//						builder.addActivity(PbHelper.createActivityPb(e, false, 0));
//					} else if (now.after(endTime) && now.before(display)) {
//						builder.addActivity(PbHelper.createActivityPb(e, true, 0));
//					}
//				}
//			}
//		}
//		handler.sendMsgToPlayer(GetActionCenterRs.ext, builder.build());
//	}
//
//	/**
//	 * 排行榜奖励列表
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void getRankAwardListRq(GetRankAwardListRq req, ClientHandler handler) {
//		int activityId = req.getActivityId();
//		int rankType = req.getRankType();
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(activityId);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(activityId);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, activityId);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		List<StaticActRank> srankList = staticActivityDataMgr.getActRankList(activityKeyId, rankType);
//		if (srankList == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//
//		GetRankAwardListRs.Builder builder = GetRankAwardListRs.newBuilder();
//		for (StaticActRank staticActRank : srankList) {
//			builder.addRankAward(PbHelper.createRankAwardPb(staticActRank));
//		}
//		int step = activityBase.getStep();
//		if (step == ActivityConst.OPEN_AWARD) {// 可领奖
//			builder.setOpen(true);
//		} else {// 不可领奖
//			builder.setOpen(false);
//		}
//		handler.sendMsgToPlayer(GetRankAwardListRs.ext, builder.build());
//	}
//
//	/**
//	 * 获取排名奖励
//	 * 
//	 * @param handler
//	 */
//	public void getRankAwardRq(GetRankAwardRq req, ClientHandler handler) {
//		int activityId = req.getActivityId();
//		int rankType = req.getRankType();
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(activityId);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		// 常规领取
//		int step = activityBase.getStep();// end-display之间才可领取排名奖励
//		if (step != 1) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(activityId);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, activityId);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		ActPlayerRank playerRank = activityData.getPlayerRank(rankType, lord.getLordId());
//		if (playerRank == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//
//		int rank = playerRank.getRank();
//		StaticActRank staticActRank = staticActivityDataMgr.getActRank(activityKeyId, rankType, rank);
//		if (staticActRank == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//
//		Integer status = activity.getStatusMap().get(rankType);
//		if (status != null && status.intValue() != 0) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_GOT);
//			return;
//		}
//
//		// 记录领奖状态
//		activity.getStatusMap().put(rankType, 1);
//
//		int serverId = player.account.getServerId();
//
//		GetRankAwardRs.Builder builder = GetRankAwardRs.newBuilder();
//		List<List<Integer>> awardList = staticActRank.getAwardList();
//		for (List<Integer> e : awardList) {
//			if (e.size() != 3) {
//				continue;
//			}
//			int type = e.get(0);
//			int id = e.get(1);
//			int count = e.get(2);
//			if (type == AwardType.EQUIP || type == AwardType.PART) {
//				for (int c = 0; c < count; c++) {
//					int itemkey = playerDataManager.addAward(player, type, id, 1, AwardFrom.ACT_RANK_AWARD);
//					builder.addAward(PbHelper.createAwardPb(type, id, 1, itemkey));
//				}
//			} else {
//				int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.ACT_RANK_AWARD);
//				builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//			}
//			LogHelper.logActivity(player.lord, activityId, 0, type, id, count, serverId);
//		}
//		handler.sendMsgToPlayer(GetRankAwardRs.ext, builder.build());
//	}
//
//	/**
//	 * 获取军团排名奖励
//	 * 
//	 * @param handler
//	 */
//	public void getPartyRankAwardRq(GetPartyRankAwardRq req, ClientHandler handler) {
//		int activityId = req.getActivityId();
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(activityId);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		// 常规领取
//		int step = activityBase.getStep();// end-display之间才可领取排名奖励
//		if (step != 1) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		PartyData partyData = partyDataManager.getPartyByLordId(handler.getRoleId());
//		if (partyData == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PARTY);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(activityId);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, activityId);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		ActPartyRank actPartyRank = activityData.getPartyRank(partyData.getPartyId());
//		if (actPartyRank == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//
//		int rank = actPartyRank.getRank();
//		StaticActRank staticActRank = staticActivityDataMgr.getActRank(activityKeyId, 0, rank);
//		if (staticActRank == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//
//		Integer status = activity.getStatusMap().get(0);
//		if (status != null && status.intValue() != 0) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_GOT);
//			return;
//		}
//
//		Member member = partyDataManager.getMemberById(handler.getRoleId());
//		Date endTime = activityBase.getEndTime();
//		int enterTime = member.getEnterTime();
//		if (enterTime != 0) {
//			Date enterDate = TimeHelper.getDate(enterTime);
//			if (enterDate != null && enterDate.after(endTime)) {
//				handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//				return;
//			}
//		}
//
//		// 记录领奖状态
//		activity.getStatusMap().put(0, 1);
//		int serverId = player.account.getServerId();
//		GetPartyRankAwardRs.Builder builder = GetPartyRankAwardRs.newBuilder();
//		List<List<Integer>> awardList = staticActRank.getAwardList();
//		for (List<Integer> e : awardList) {
//			if (e.size() != 3) {
//				continue;
//			}
//			int type = e.get(0);
//			int id = e.get(1);
//			int count = e.get(2);
//			if (type == AwardType.EQUIP || type == AwardType.PART) {
//				for (int c = 0; c < count; c++) {
//					int itemkey = playerDataManager.addAward(player, type, id, 1, AwardFrom.ACT_RANK_AWARD);
//					builder.addAward(PbHelper.createAwardPb(type, id, 1, itemkey));
//				}
//			} else {
//				int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.ACT_RANK_AWARD);
//				builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//			}
//			LogHelper.logActivity(player.lord, activityId, 0, type, id, count, serverId);
//		}
//		handler.sendMsgToPlayer(GetPartyRankAwardRs.ext, builder.build());
//	}
//
//	/**
//	 * Function机甲洪流
//	 * 
//	 * @param handler
//	 */
//	public void getActMechaRq(ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_MECHA);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_MECHA);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		GetActMechaRs.Builder builder = GetActMechaRs.newBuilder();
//		StaticActMecha single = staticActivityDataMgr.getMechaById(activityKeyId, 1);
//		StaticActMecha ten = staticActivityDataMgr.getMechaById(activityKeyId, 10);
//		if (single == null || ten == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//		}
//		int free = 0;
//		if (lord.getFreeMecha() != TimeHelper.getCurrentDay()) {
//			free = 1;
//		}
//		List<Long> statusList = activity.getStatusList();
//		int part1 = (int) statusList.get(0).longValue();
//		int part2 = (int) statusList.get(1).longValue();
//		int crit = (int) statusList.get(2).longValue();
//		crit = crit == 0 ? 1 : crit;
//		builder.setMechaSingle(PbHelper.createMechaPb(single, free, crit, part1));
//		builder.setMechaTen(PbHelper.createMechaPb(ten, 0, crit, part2));
//		handler.sendMsgToPlayer(GetActMechaRs.ext, builder.build());
//	}
//
//	/**
//	 * Function机甲洪流之抽取
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActMechaRq(DoActMechaRq req, ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_MECHA);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int mechaId = req.getMechaId();
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		StaticActMecha actMecha = staticActivityDataMgr.getMechaById(mechaId);
//		int cost = actMecha.getCost();
//		if (actMecha.getCount() == 1 && lord.getFreeMecha() != TimeHelper.getCurrentDay()) {
//			cost = 0;
//			lord.setFreeMecha(TimeHelper.getCurrentDay());
//		}
//		if (cost > player.lord.getGold()) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//		if (cost > 0) {
//			playerDataManager.subGold(lord, cost, GoldCost.ACTIVITY_MECHA);
//		}
//		int serverId = player.account.getServerId();
//		List<Long> statusList = activity.getStatusList();
//		int part1 = 0;
//		int part2 = 0;
//		long crit = (int) statusList.get(2).longValue();
//		crit = crit == 0 ? 1 : crit;
//		DoActMechaRs.Builder builder = DoActMechaRs.newBuilder();
//		for (int i = 0; i < actMecha.getCount(); i++) {
//			part1 += getNum(actMecha.getTank1PartList()) * crit;
//			part2 += getNum(actMecha.getTank2PartList()) * crit;
//			crit = getNum(actMecha.getCritList());
//		}
//		statusList.set(0, statusList.get(0) + part1);
//		statusList.set(1, statusList.get(1) + part2);
//		statusList.set(2, crit);
//		activity.setStatusList(statusList);
//		builder.setTwoInt(PbHelper.createTwoIntPb((int) statusList.get(0).longValue(), (int) statusList.get(1).longValue()));
//		builder.setCrit((int) crit);
//		handler.sendMsgToPlayer(DoActMechaRs.ext, builder.build());
//		LogHelper.logActivity(player.lord, ActivityConst.ACT_MECHA, cost, 0, 1, part1, serverId);
//		LogHelper.logActivity(player.lord, ActivityConst.ACT_MECHA, cost, 0, 2, part2, serverId);
//	}
//
//	private int getNum(List<List<Integer>> list) {
//		int random = RandomHelper.randomInSize(100);
//		int total = 0;
//		for (List<Integer> e : list) {
//			total += e.get(1);
//			if (random < total) {
//				return e.get(0);
//			}
//		}
//		return 4;
//	}
//
//	/**
//	 * Function机甲洪流之组装
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void assembleMechaRq(AssembleMechaRq req, ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_MECHA);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		int mechaId = req.getMechaId();
//
//		StaticActMecha staticActMecha = staticActivityDataMgr.getMechaById(mechaId);
//		if (staticActMecha == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		StaticTank staticTank = staticTankDataMgr.getStaticTank(staticActMecha.getTank());
//		if (staticTank == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int index = 0;
//		if (staticActMecha.getCount() == 10) {
//			index = 1;
//		}
//		List<Long> statusList = activity.getStatusList();
//		int part = (int) statusList.get(index).longValue();
//		int count = part / 20;
//		long reless = part % 20;
//		if (count <= 0) {
//			handler.sendErrorMsgToPlayer(GameError.PART_NOT_ENOUGH);
//			return;
//		}
//
//		statusList.set(index, reless);
//
//		int keyId = playerDataManager.addAward(player, AwardType.TANK, staticActMecha.getTank(), count, AwardFrom.ASSEMBLE_MECHA);
//
//		AssembleMechaRs.Builder builder = AssembleMechaRs.newBuilder();
//		builder.addAward(PbHelper.createAwardPb(AwardType.TANK, staticActMecha.getTank(), count, keyId));
//		handler.sendMsgToPlayer(AssembleMechaRs.ext, builder.build());
//
//		chatService.sendWorldChat(chatService.createSysChat(SysChatId.ASSEMBER_TANK, player.lord.getNick(), String.valueOf(staticActMecha.getTank())));
//	}
//
//	/**
//	 * Function建军节-返利环节
//	 * 
//	 * @param handler
//	 */
//	public void getActAmyRebate(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_AMY_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_AMY_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		List<Long> statusList = activity.getStatusList();
//		Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
//		for (Long e : statusList) {
//			int rebateId = (int) e.longValue();
//			if (rebateId == 0) {
//				continue;
//			}
//			StaticActRebate staticRebate = staticActivityDataMgr.getRebateById(rebateId);
//			if (staticRebate == null) {
//				continue;
//			}
//			Integer count = countMap.get(rebateId);
//			if (count == null) {
//				count = new Integer(0);
//			}
//			count++;
//			countMap.put(rebateId, count);
//		}
//
//		GetActAmyRebateRs.Builder builder = GetActAmyRebateRs.newBuilder();
//		Iterator<Entry<Integer, Integer>> it = countMap.entrySet().iterator();
//		while (it.hasNext()) {
//			Entry<Integer, Integer> next = it.next();
//			int rebateId = next.getKey();
//			int count = next.getValue();
//			builder.addAmyRebate(PbHelper.createAmyRebatePb(rebateId, count));
//		}
//		handler.sendMsgToPlayer(GetActAmyRebateRs.ext, builder.build());
//	}
//
//	/**
//	 * Function欢庆返利
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActAmyRebateRq(DoActAmyRebateRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_AMY_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_AMY_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		int rebateId = req.getRebateId();
//		StaticActRebate actRebate = staticActivityDataMgr.getRebateById(rebateId);
//		if (actRebate == null) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//
//		List<Long> statusList = activity.getStatusList();
//		if (statusList.size() < 1) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//		boolean flag = false;
//		Iterator<Long> it = statusList.iterator();
//		while (it.hasNext()) {
//			int id = (int) it.next().longValue();
//			if (rebateId == id) {
//				it.remove();
//				flag = true;
//				break;
//			}
//		}
//
//		if (!flag) {// 玩家身上不包含该奖励
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//
//		int money = actRebate.getMoney();
//		int rebate = actRebate.getRandomRebate();
//
//		DoActAmyRebateRs.Builder builder = DoActAmyRebateRs.newBuilder();
//		int addGold = money * rebate / 10;
//		if (addGold > 0) {
//			playerDataManager.addGold(lord, addGold, AwardFrom.AMY_REBATE);
//			builder.addAward(PbHelper.createAwardPb(AwardType.GOLD, 0, addGold));
//		}
//		builder.setGold(lord.getGold());
//
//		handler.sendMsgToPlayer(DoActAmyRebateRs.ext, builder.build());
//	}
//
//	/**
//	 * Function建军节-全服欢庆
//	 * 
//	 * @param handler
//	 */
//	public void getActAmyfestivityRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_AMY_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_AMY_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_AMY_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Map<Integer, Integer> statusMap = activity.getStatusMap();
//		GetActAmyfestivityRs.Builder builder = GetActAmyfestivityRs.newBuilder();
//		List<StaticActAward> condList = staticActivityDataMgr.getActAwardById(ActivityConst.ACT_AMY_ID);
//		for (StaticActAward e : condList) {
//			int keyId = e.getKeyId();
//			Integer status = statusMap.get(keyId);
//			if (status == null) {
//				status = 0;
//			}
//			builder.addActivityCond(PbHelper.createActivityCondPb(e, status));
//		}
//		builder.setState(activityData.getGoal());
//		handler.sendMsgToPlayer(GetActAmyfestivityRs.ext, builder.build());
//	}
//
//	/**
//	 * Function建军节-全服欢庆
//	 * 
//	 * @param handler
//	 */
//	public void doActAmyfestivityRq(DoActAmyfestivityRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_AMY_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		int keyId = req.getKeyId();
//		StaticActAward actAward = staticActivityDataMgr.getActAward(keyId);
//		if (actAward == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_AMY_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_AMY_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		// 欢庆值不足,则不可领奖
//		if (activityData.getGoal() < actAward.getCond()) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//
//		Map<Integer, Integer> statusMap = activity.getStatusMap();
//		Integer awardStatus = statusMap.get(keyId);
//		if (awardStatus != null && awardStatus == 1) {
//			handler.sendErrorMsgToPlayer(GameError.AWARD_HAD_GOT);
//			return;
//		}
//		activity.getStatusMap().put(keyId, 1);
//
//		DoActAmyfestivityRs.Builder builder = DoActAmyfestivityRs.newBuilder();
//		List<List<Integer>> awardList = actAward.getAwardList();
//		for (List<Integer> e : awardList) {
//			if (e.size() != 3) {
//				continue;
//			}
//			int type = e.get(0);
//			int itemId = e.get(1);
//			int count = e.get(2);
//			if (type == AwardType.EQUIP || type == AwardType.PART) {
//				for (int i = 0; i < count; i++) {
//					int itemkey = playerDataManager.addAward(player, type, itemId, 1, AwardFrom.ACTIVITY_AWARD);
//					builder.addAward(PbHelper.createAwardPb(type, itemId, 1, itemkey));
//				}
//			} else {
//				int itemkey = playerDataManager.addAward(player, type, itemId, count, AwardFrom.ACTIVITY_AWARD);
//				builder.addAward(PbHelper.createAwardPb(type, itemId, count, itemkey));
//			}
//		}
//
//		handler.sendMsgToPlayer(DoActAmyfestivityRs.ext, builder.build());
//	}
//
//	/**
//	 * 极限单兵-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActFortuneRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PAWN_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PAWN_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		GetActFortuneRs.Builder builder = GetActFortuneRs.newBuilder();
//		int day = TimeHelper.getCurrentDay();
//		if (lord.getPawn() != day) {
//			builder.setFree(1);
//		} else {
//			builder.setFree(0);
//		}
//		int activityKeyId = activityBase.getKeyId();
//
//		long score = activity.getStatusList().get(0);
//		List<StaticActFortune> condList = staticActivityDataMgr.getActFortuneList(activityKeyId);
//		for (StaticActFortune e : condList) {
//			builder.addFortune(PbHelper.createFortunePb(e));
//			builder.setDisplayList(e.getDisplayList());
//		}
//		builder.setScore((int) score);// 我的积分
//		handler.sendMsgToPlayer(GetActFortuneRs.ext, builder.build());
//	}
//
//	/**
//	 * 极限单兵-排行榜{前十名,其他均为未入榜}
//	 * 
//	 * @param handler
//	 */
//	public void getActFortuneRankRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PAWN_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int step = activityBase.getStep();
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_PAWN_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PAWN_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		// 我的积分
//		long score = activity.getStatusList().get(0);
//
//		GetActFortuneRankRs.Builder builder = GetActFortuneRankRs.newBuilder();
//		LinkedList<ActPlayerRank> rankList = activityData.getPlayerRanks(ActivityConst.TYPE_DEFAULT);
//		for (int i = 0; i < rankList.size() && i < ActivityConst.RANK_PAWN; i++) {
//			ActPlayerRank e = rankList.get(i);
//			long lordId = e.getLordId();
//			Player rankPlayer = playerDataManager.getPlayer(lordId);
//			if (rankPlayer != null && rankPlayer.lord != null) {
//				builder.addActPlayerRank(PbHelper.createActPlayerRank(e, rankPlayer.lord.getNick()));
//			}
//		}
//		builder.setStatus(0);
//		builder.setScore((int) score);
//		if (step == ActivityConst.OPEN_STEP) {
//			builder.setOpen(false);
//		} else if (step == ActivityConst.OPEN_AWARD) {
//			builder.setOpen(true);
//			Map<Integer, Integer> statusMap = activity.getStatusMap();
//			if (statusMap.containsKey(ActivityConst.TYPE_DEFAULT)) {
//				builder.setStatus(1);
//			}
//		}
//
//		List<StaticActRank> staticActRankList = staticActivityDataMgr.getActRankList(activityKeyId, ActivityConst.TYPE_DEFAULT);
//		if (staticActRankList != null) {
//			for (StaticActRank e : staticActRankList) {
//				builder.addRankAward(PbHelper.createRankAwardPb(e));
//			}
//		}
//		handler.sendMsgToPlayer(GetActFortuneRankRs.ext, builder.build());
//	}
//
//	/**
//	 * 极限单兵-抽取
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActFortuneRq(DoActFortuneRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PAWN_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int step = activityBase.getStep();
//		if (step != 0) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_PAWN_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PAWN_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int fortuneId = req.getFortuneId();
//		StaticActFortune staticActFortune = staticActivityDataMgr.getActFortune(fortuneId);
//		if (staticActFortune == null) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//
//		int day = TimeHelper.getCurrentDay();
//		if (lord.getPawn() != day && staticActFortune.getCount() == 1) {// 单抽免费次数
//			lord.setPawn(day);
//		} else {
//			int price = staticActFortune.getPrice();
//			if (lord.getGold() < price) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//			playerDataManager.subGold(lord, price, GoldCost.ACTIVITY_PAWN);
//		}
//
//		long score = activity.getStatusList().get(0);
//		score = score + staticActFortune.getPoint();
//		activity.getStatusList().set(0, score);
//		// 计算排名
//		if (score >= 500 && lord.getLevel() >= 10) {// 积分超过500才可进入排行
//			activityData.addPlayerRank(lord.getLordId(), score, ActivityConst.RANK_PAWN, ActivityConst.DESC);
//		}
//
//		DoActFortuneRs.Builder builder = DoActFortuneRs.newBuilder();
//
//		int serverId = player.account.getServerId();
//
//		// 发放奖励
//		int repeat = staticActFortune.getCount();
//		for (int i = 0; i < repeat; i++) {
//			List<Integer> list = staticActivityDataMgr.randomAwardList(staticActFortune.getAwardList());
//			if (list == null || list.size() < 3) {
//				continue;
//			}
//			int type = list.get(0);
//			int id = list.get(1);
//			int count = list.get(2);
//			int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.PAWN);
//			builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//
//			if (type == AwardType.CHIP) {
//				StaticPart staticPart = staticPartDataMgr.getStaticPart(id);
//				if (staticPart != null && staticPart.getQuality() >= 3) {
//					chatService.sendWorldChat(chatService.createSysChat(SysChatId.LUCK_ROUND, player.lord.getNick(), String.valueOf(id)));
//					LogHelper.logItem(lord, ItemFrom.PART_FORTUNE, serverId, type, id, count, String.valueOf(ActivityConst.ACT_PAWN_ID));
//				}
//			}
//		}
//
//		builder.setScore((int) score);// 我的积分
//		handler.sendMsgToPlayer(DoActFortuneRs.ext, builder.build());
//	}
//
//	/**
//	 * 勤劳致富-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActBeeRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_BEE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		UsualActivityData usualActivityData = activityDataManager.getUsualActivity(ActivityConst.ACT_BEE_ID);
//		if (usualActivityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_BEE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		GetActBeeRs.Builder builder = GetActBeeRs.newBuilder();
//		List<StaticActAward> condList = staticActivityDataMgr.getActAwardById(activityKeyId);
//		List<CommonPb.ActivityCond> stoneList = new ArrayList<>();
//		List<CommonPb.ActivityCond> ironList = new ArrayList<>();
//		List<CommonPb.ActivityCond> oilList = new ArrayList<>();
//		List<CommonPb.ActivityCond> copperList = new ArrayList<>();
//		List<CommonPb.ActivityCond> siliconList = new ArrayList<>();
//		for (StaticActAward e : condList) {
//			int keyId = e.getKeyId();
//			Integer status = activity.getStatusMap().get(keyId);
//			if (status == null) {
//				status = 0;
//			}
//			int sortId = e.getSortId();
//			if (sortId == 0) {// 铁
//				ironList.add(PbHelper.createActivityCondPb(e, status));
//			} else if (sortId == 1) {// 石油
//				oilList.add(PbHelper.createActivityCondPb(e, status));
//			} else if (sortId == 2) {// 铜
//				copperList.add(PbHelper.createActivityCondPb(e, status));
//			} else if (sortId == 3) {// 钛
//				siliconList.add(PbHelper.createActivityCondPb(e, status));
//			} else if (sortId == 4) {// 水晶
//				stoneList.add(PbHelper.createActivityCondPb(e, status));
//			}
//		}
//		List<Long> status = activity.getStatusList();
//		long collect = status.get(0);
//		int state = collect > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) collect;
//		builder.setIron(PbHelper.createCondStatePb(state, ironList));
//
//		collect = status.get(1);
//		state = collect > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) collect;
//		builder.setOil(PbHelper.createCondStatePb(state, oilList));
//
//		collect = status.get(2);
//		state = collect > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) collect;
//		builder.setCopper(PbHelper.createCondStatePb(state, copperList));
//
//		collect = status.get(3);
//		state = collect > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) collect;
//		builder.setSilicon(PbHelper.createCondStatePb(state, siliconList));
//
//		collect = status.get(4);
//		state = collect > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) collect;
//		builder.setStone(PbHelper.createCondStatePb(state, stoneList));
//
//		handler.sendMsgToPlayer(GetActBeeRs.ext, builder.build());
//	}
//
//	/**
//	 * 勤劳致富-排行榜{每榜的前十名,其他均为未入榜}
//	 * 
//	 * @param handler
//	 */
//	public void getActBeeRankRq(GetActBeeRankRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_BEE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int step = activityBase.getStep();
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_BEE_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_BEE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		List<StaticActRank> srankList = staticActivityDataMgr.getActRankList(activityKeyId, ActivityConst.TYPE_DEFAULT);
//		if (srankList == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		Map<Integer, Integer> statusMap = activity.getStatusMap();
//		GetActBeeRankRs.Builder builder = GetActBeeRankRs.newBuilder();
//		for (int index = 0; index < 5; index++) {
//
//			long collect = activity.getStatusList().get(index);// 我的采集量
//
//			LinkedList<ActPlayerRank> rankList = activityData.getPlayerRanks(index);// 排行榜
//			List<CommonPb.ActPlayerRank> playerRanks = new ArrayList<CommonPb.ActPlayerRank>();
//
//			for (int i = 0; i < rankList.size() && i < ActivityConst.RANK_BEE; i++) {// 榜单前十名
//				ActPlayerRank e = rankList.get(i);
//				long lordId = e.getLordId();
//				Player rankPlayer = playerDataManager.getPlayer(lordId);
//				if (rankPlayer == null || rankPlayer.lord == null) {
//					continue;
//				}
//
//				playerRanks.add(PbHelper.createActPlayerRank(e, rankPlayer.lord.getNick()));
//
//			}
//			if (statusMap.containsKey(index)) {// 奖励已领取
//				builder.addBeeRank(PbHelper.createBeeRankPb(index + 1, collect, 1, playerRanks));
//			} else {
//				builder.addBeeRank(PbHelper.createBeeRankPb(index + 1, collect, 0, playerRanks));
//			}
//		}
//
//		for (StaticActRank e : srankList) {
//			builder.addRankAward(PbHelper.createRankAwardPb(e));
//		}
//
//		if (step == ActivityConst.OPEN_CLOSE || step == ActivityConst.OPEN_STEP) {
//			builder.setOpen(false);
//		} else if (step == ActivityConst.OPEN_AWARD) {
//			builder.setOpen(true);
//		}
//		handler.sendMsgToPlayer(GetActBeeRankRs.ext, builder.build());
//	}
//
//	/**
//	 * 哈洛克宝藏
//	 * 
//	 * @param handler
//	 */
//	public void getActProfotoRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PROFOTO_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		StaticActProfoto staticActProfoto = staticActivityDataMgr.getActProfoto(activityKeyId);
//		if (staticActProfoto == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		GetActProfotoRs.Builder builder = GetActProfotoRs.newBuilder();
//
//		int preciousId = staticActProfoto.getPrecious();
//		Prop precious = player.props.get(preciousId);
//		if (precious == null) {
//			builder.setProfoto(PbHelper.createProfotoPb(preciousId, 0));
//		} else {
//			builder.setProfoto(PbHelper.createProfotoPb(preciousId, precious.getCount()));
//		}
//
//		int trustId = staticActProfoto.getTrust();
//		Prop trust = player.props.get(trustId);
//		if (trust == null) {
//			builder.setTrust(PbHelper.createProfotoPb(trustId, 0));
//		} else {
//			builder.setTrust(PbHelper.createProfotoPb(trustId, trust.getCount()));
//		}
//
//		int part1Id = staticActProfoto.getPart1();
//		Prop part1 = player.props.get(part1Id);
//		if (part1 == null) {
//			builder.addParts(PbHelper.createProfotoPb(part1Id, 0));
//		} else {
//			builder.addParts(PbHelper.createProfotoPb(part1Id, part1.getCount()));
//		}
//
//		int part2Id = staticActProfoto.getPart2();
//		Prop part2 = player.props.get(part2Id);
//		if (part2 == null) {
//			builder.addParts(PbHelper.createProfotoPb(part2Id, 0));
//		} else {
//			builder.addParts(PbHelper.createProfotoPb(part2Id, part2.getCount()));
//		}
//
//		int part3Id = staticActProfoto.getPart3();
//		Prop part3 = player.props.get(part3Id);
//		if (part3 == null) {
//			builder.addParts(PbHelper.createProfotoPb(part3Id, 0));
//		} else {
//			builder.addParts(PbHelper.createProfotoPb(part3Id, part3.getCount()));
//		}
//
//		int part4Id = staticActProfoto.getPart4();
//		Prop part4 = player.props.get(part4Id);
//		if (part4 == null) {
//			builder.addParts(PbHelper.createProfotoPb(part4Id, 0));
//		} else {
//			builder.addParts(PbHelper.createProfotoPb(part4Id, part4.getCount()));
//		}
//
//		handler.sendMsgToPlayer(GetActProfotoRs.ext, builder.build());
//	}
//
//	/**
//	 * 合成宝藏
//	 * 
//	 * @param handler
//	 */
//	public void doActProfotoRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PROFOTO_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		StaticActProfoto staticActProfoto = staticActivityDataMgr.getActProfoto(activityKeyId);
//		if (staticActProfoto == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		int part1Id = staticActProfoto.getPart1();
//		int part2Id = staticActProfoto.getPart2();
//		int part3Id = staticActProfoto.getPart3();
//		int part4Id = staticActProfoto.getPart4();
//		Prop part1 = player.props.get(part1Id);
//		Prop part2 = player.props.get(part2Id);
//		Prop part3 = player.props.get(part3Id);
//		Prop part4 = player.props.get(part4Id);
//		if (part1 == null || part1.getCount() < 1 || part2 == null || part2.getCount() < 1 || part3 == null || part3.getCount() < 1 || part4 == null
//				|| part4.getCount() < 1) {
//			handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
//			return;
//		}
//
//		DoActProfotoRs.Builder builder = DoActProfotoRs.newBuilder();
//
//		int preciousId = staticActProfoto.getPrecious();
//
//		playerDataManager.subProp(part1, 1);
//		playerDataManager.subProp(part2, 1);
//		playerDataManager.subProp(part3, 1);
//		playerDataManager.subProp(part4, 1);
//		int keyId = playerDataManager.addAward(player, AwardType.PROP, preciousId, 1, AwardFrom.ACTIVITY_AWARD);
//		builder.addAward(PbHelper.createAwardPb(AwardType.PROP, preciousId, 1, keyId));
//		builder.addParts(PbHelper.createProfotoPb(part1.getPropId(), part1.getCount()));
//		builder.addParts(PbHelper.createProfotoPb(part2.getPropId(), part2.getCount()));
//		builder.addParts(PbHelper.createProfotoPb(part3.getPropId(), part3.getCount()));
//		builder.addParts(PbHelper.createProfotoPb(part4.getPropId(), part4.getCount()));
//		handler.sendMsgToPlayer(DoActProfotoRs.ext, builder.build());
//	}
//
//	/**
//	 * 开启宝藏
//	 * 
//	 * @param handler
//	 */
//	public void unfoldProfotoRq(ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		StaticActProfoto staticActProfoto = staticActivityDataMgr.getActProfoto(105);
//		if (staticActProfoto == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		UnfoldProfotoRs.Builder builder = UnfoldProfotoRs.newBuilder();
//		int preciousId = staticActProfoto.getPrecious();
//		Prop precious = player.props.get(preciousId);
//		if (precious == null || precious.getCount() <= 0) {
//			handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
//			return;
//		}
//
//		StaticProp staticProp = staticPropDataMgr.getStaticProp(preciousId);
//		List<List<Integer>> effectValue = staticProp.getEffectValue();
//		if (effectValue == null || effectValue.isEmpty()) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		List<Integer> one = effectValue.get(0);
//		if (one.size() != 1) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		int trustId = staticActProfoto.getTrust();
//		int trustCount = 0;
//		Prop trust = player.props.get(trustId);
//		if (trust == null || trust.getCount() <= 0) {
//			if (lord.getGold() < 50) {
//				handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
//				return;
//			}
//			playerDataManager.subGold(lord, 50, GoldCost.ACTIVITY_PROFOTO);
//		} else {
//			playerDataManager.subProp(trust, 1);
//			trustCount = trust.getCount();
//		}
//		playerDataManager.subProp(precious, 1);
//
//		List<List<Integer>> awards = staticAwardsDataMgr.getAwards(one.get(0));
//
//		builder.addAllAward(playerDataManager.addAwardsBackPb(player, awards, AwardFrom.USE_PROP));
//		builder.setProfoto(PbHelper.createProfotoPb(precious.getPropId(), precious.getCount()));
//		builder.setTrust(PbHelper.createProfotoPb(trustId, trustCount));
//		handler.sendMsgToPlayer(UnfoldProfotoRs.ext, builder.build());
//
//		if (awards != null && !awards.isEmpty()) {
//			List<Integer> award = awards.get(0);
//			chatService.sendWorldChat(chatService.createSysChat(SysChatId.HALOKE_TREASURE, player.lord.getNick(), String.valueOf(award.get(0)),
//					String.valueOf(award.get(1))));
//		}
//
//	}
//
//	/**
//	 * 配件转盘-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActPartDialRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_DIAL_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PART_DIAL_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		GetActPartDialRs.Builder builder = GetActPartDialRs.newBuilder();
//		int monthAndDay = TimeHelper.getMonthAndDay(new Date());
//		int partDial = lord.getPartDial();
//		if (partDial / 100 != monthAndDay / 100) {
//			partDial = monthAndDay;
//		}
//
//		int useCount = partDial % 100;
//		int free = 0;
//		if (lord.getVip() > 0) {
//			free = 2 - useCount < 0 ? 0 : 2 - useCount;
//		} else {
//			free = 1 - useCount < 0 ? 0 : 1 - useCount;
//		}
//		builder.setFree(free);// 剩余次数
//
//		long score = activity.getStatusList().get(0);
//		List<StaticActFortune> condList = staticActivityDataMgr.getActFortuneList(ActivityConst.ACT_PART_DIAL_ID);
//		for (StaticActFortune e : condList) {
//			builder.addFortune(PbHelper.createFortunePb(e));
//			builder.setDisplayList(e.getDisplayList());
//		}
//		builder.setScore((int) score);// 我的积分
//		handler.sendMsgToPlayer(GetActPartDialRs.ext, builder.build());
//	}
//
//	/**
//	 * 配件转盘-排行榜{前十名,其他均为未入榜}
//	 * 
//	 * @param handler
//	 */
//	public void getActPartDialRankRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_DIAL_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int step = activityBase.getStep();
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_PART_DIAL_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PART_DIAL_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		// 我的积分
//		long score = activity.getStatusList().get(0);
//
//		GetActPartDialRankRs.Builder builder = GetActPartDialRankRs.newBuilder();
//		LinkedList<ActPlayerRank> rankList = activityData.getPlayerRanks(ActivityConst.TYPE_DEFAULT);
//		for (int i = 0; i < rankList.size() && i < ActivityConst.RANK_PAWN; i++) {
//			ActPlayerRank e = rankList.get(i);
//			long lordId = e.getLordId();
//			Player rankPlayer = playerDataManager.getPlayer(lordId);
//			if (rankPlayer != null && rankPlayer.lord != null) {
//				builder.addActPlayerRank(PbHelper.createActPlayerRank(e, rankPlayer.lord.getNick()));
//			}
//		}
//		builder.setStatus(0);
//		builder.setScore((int) score);
//		if (step == ActivityConst.OPEN_STEP) {
//			builder.setOpen(false);
//		} else if (step == ActivityConst.OPEN_AWARD) {
//			builder.setOpen(true);
//			Map<Integer, Integer> statusMap = activity.getStatusMap();
//			if (statusMap.containsKey(ActivityConst.TYPE_DEFAULT)) {
//				builder.setStatus(1);
//			}
//		}
//
//		List<StaticActRank> staticActRankList = staticActivityDataMgr.getActRankList(activityKeyId, ActivityConst.TYPE_DEFAULT);
//		if (staticActRankList != null) {
//			for (StaticActRank e : staticActRankList) {
//				builder.addRankAward(PbHelper.createRankAwardPb(e));
//			}
//		}
//		handler.sendMsgToPlayer(GetActPartDialRankRs.ext, builder.build());
//	}
//
//	/**
//	 * 配件转轴-抽取
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActPartDialRq(DoActPartDialRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_DIAL_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int step = activityBase.getStep();
//		if (step != 0) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_PART_DIAL_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PART_DIAL_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int fortuneId = req.getFortuneId();
//		StaticActFortune staticActFortune = staticActivityDataMgr.getActFortune(fortuneId);
//		if (staticActFortune == null) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//
//		int monthAndDay = TimeHelper.getMonthAndDay(new Date());
//		int partDial = lord.getPartDial();
//		if (partDial / 100 != monthAndDay / 100) {
//			partDial = monthAndDay;
//		}
//		int useCount = partDial % 100;
//		int free = 0;
//		if (lord.getVip() > 0) {
//			free = 2 - useCount < 0 ? 0 : 2 - useCount;
//		} else {
//			free = 1 - useCount < 0 ? 0 : 1 - useCount;
//		}
//
//		if (free > 0 && staticActFortune.getCount() == 1) {// 单抽免费次数
//			lord.setPartDial(partDial + 1);
//		} else {
//			int price = staticActFortune.getPrice();
//			if (lord.getGold() < price) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//			playerDataManager.subGold(lord, price, GoldCost.ACTIVITY_PAWN);
//		}
//
//		DoActPartDialRs.Builder builder = DoActPartDialRs.newBuilder();
//
//		// 发放奖励
//		int scoreAdd = 0;
//		int repeat = staticActFortune.getCount();
//		for (int i = 0; i < repeat; i++) {
//			List<Integer> list = staticActivityDataMgr.randomAwardList(staticActFortune.getAwardList());
//			if (list == null || list.size() < 5) {
//				continue;
//			}
//			int type = list.get(0);
//			int id = list.get(1);
//			int count = list.get(2);
//			int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.PART_DIAL);
//			scoreAdd += list.get(4);// 增加积分
//			builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//		}
//
//		long score = activity.getStatusList().get(0);
//		score += scoreAdd;
//		activity.getStatusList().set(0, score);
//		// 计算排名
//		if (score >= 250) {// 积分超过500才可进入排行
//			activityData.addPlayerRank(lord.getLordId(), score, ActivityConst.RANK_PART_DIAL, ActivityConst.DESC);
//		}
//
//		builder.setScore((int) score);// 我的积分
//		handler.sendMsgToPlayer(DoActPartDialRs.ext, builder.build());
//	}
//
//	/**
//	 * 坦克拉霸-主页面
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void getActTankRaffleRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_TANK_RAFFLE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_TANK_RAFFLE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		StaticActRaffle staticActRaffle = staticActivityDataMgr.getActRaffle(activityKeyId);
//		if (staticActRaffle == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		GetActTankRaffleRs.Builder builder = GetActTankRaffleRs.newBuilder();
//		int tankRaffle = lord.getTankRaffle();
//		int monthAndDay = TimeHelper.getMonthAndDay(new Date());
//		if (tankRaffle != monthAndDay) {
//			builder.setFree(1);
//		} else {
//			builder.setFree(0);
//		}
//		handler.sendMsgToPlayer(GetActTankRaffleRs.ext, builder.build());
//	}
//
//	/**
//	 * 坦克拉霸
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActTankRaffleRq(DoActTankRaffleRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_TANK_RAFFLE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_TANK_RAFFLE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		StaticActRaffle staticActRaffle = staticActivityDataMgr.getActRaffle(activityKeyId);
//		if (staticActRaffle == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int type = req.getType();
//		int price = 0;
//		int add = 1;
//		if (type == 1) {
//			int tankRaffle = lord.getTankRaffle();
//			int monthAndDay = TimeHelper.getMonthAndDay(new Date());
//			if (tankRaffle != monthAndDay) {// 免费一次抽取
//				lord.setTankRaffle(monthAndDay);
//			} else {
//				price = staticActRaffle.getPrice();
//			}
//		} else {
//			price = staticActRaffle.getTenPrice();
//			add = 10;
//		}
//		if (lord.getGold() < price) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//
//		if (price > 0) {
//			playerDataManager.subGold(lord, price, GoldCost.ACTIVITY_DAY_BUY);
//		}
//		int serverId = player.account.getServerId();
//		DoActTankRaffleRs.Builder builder = DoActTankRaffleRs.newBuilder();
//		int color[] = staticActivityDataMgr.getColor(staticActRaffle);
//		int count = staticActRaffle.getCount() * add;
//		int tankId = staticActRaffle.getTankList().get(color[0] - 1);
//		
//		int keyId = playerDataManager.addAward(player, AwardType.TANK, tankId, count, AwardFrom.ACTIVITY_AWARD);
//		
//		CommonPb.Award a = PbHelper.createAwardPb(AwardType.TANK, tankId, count, keyId);
//		builder.addAward(a);
//		
//		LogHelper.logActivity(lord, ActivityConst.ACT_TANK_RAFFLE_ID, price, AwardType.TANK, tankId, count, serverId);
//
//		for (int i = 0; i < 3; i++) {
//			builder.addColor(color[i + 1]);
//		}
//		handler.sendMsgToPlayer(DoActTankRaffleRs.ext, builder.build());
//		
//		// 加入活动获取奖励发送消息
//		propService.sendJoinActivityMsg(ActivityConst.ACT_TANK_RAFFLE_ID, player, Collections.singletonList(a));
//		
//	}
//	
//	
//		/**
//	 * 坦克拉霸-主页面·新
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void getActNewRaffleRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_NEW_RAFFLE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_NEW_RAFFLE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		StaticActRaffle staticActRaffle = staticActivityDataMgr.getActRaffle(activityKeyId);
//		if (staticActRaffle == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int lordTime = lord.getLockTime();
//		int currentDay = TimeHelper.getCurrentDay();
//		if (lordTime != currentDay) {
//			lord.setLockTankId(0);
//			lord.setLockTime(currentDay);
//		}
//
//		GetActNewRaffleRs.Builder builder = GetActNewRaffleRs.newBuilder();
//		int tankRaffle = lord.getTankRaffle();
//		int monthAndDay = TimeHelper.getMonthAndDay(new Date());
//		if (tankRaffle != monthAndDay) {
//			builder.setFree(1);
//		} else {
//			builder.setFree(0);
//		}
//		List<Integer> tankList = staticActRaffle.getTankList();
//		for (Integer e : tankList) {
//			builder.addTankId(e.intValue());
//		}
//
//		int lockId = lord.getLockTankId();
//		if (tankList.indexOf(lockId) < 0) {
//			lockId = 0;
//			lord.setLockTankId(0);
//		}
//
//		builder.setLockId(lord.getLockTankId());
//		handler.sendMsgToPlayer(GetActNewRaffleRs.ext, builder.build());
//	}
//
//	/**
//	 * 坦克拉霸·新
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActNewRaffleRq(DoActNewRaffleRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_NEW_RAFFLE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_NEW_RAFFLE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		StaticActRaffle staticActRaffle = staticActivityDataMgr.getActRaffle(activityKeyId);
//		if (staticActRaffle == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int type = req.getType();
//		int price = 0;
//		int add = 1;
//		if (type == 1) {
//			int tankRaffle = lord.getTankRaffle();
//			int monthAndDay = TimeHelper.getMonthAndDay(new Date());
//			if (tankRaffle != monthAndDay) {// 免费一次抽取
//				lord.setTankRaffle(monthAndDay);
//			} else {
//				if (lord.getLockTankId() != 0) {
//					price = staticActRaffle.getLockPrice();
//				} else {
//					price = staticActRaffle.getPrice();
//				}
//			}
//		} else {
//			if (lord.getLockTankId() != 0) {
//				price = staticActRaffle.getLockTenPrice();
//			} else {
//				price = staticActRaffle.getTenPrice();
//
//			}
//			add = 10;
//		}
//		if (lord.getGold() < price) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//
//		if (price > 0) {
//			playerDataManager.subGold(lord, price, GoldCost.ACTIVITY_DAY_BUY);
//		}
//
//		int lordTime = lord.getLockTime();
//		int currentDay = TimeHelper.getCurrentDay();
//		if (lordTime != currentDay) {
//			lord.setLockTankId(0);
//			lord.setLockTime(currentDay);
//		}
//
//		int serverId = player.account.getServerId();
//		DoActNewRaffleRs.Builder builder = DoActNewRaffleRs.newBuilder();
//		int color[] = staticActivityDataMgr.getColor(staticActRaffle);
//		int count = staticActRaffle.getCount() * add;
//		int tankId = staticActRaffle.getTankList().get(color[0] - 1);
//		if (price != 0 && lord.getLockTankId() != 0) {
//			tankId = lord.getLockTankId();
//		}
//		int keyId = playerDataManager.addAward(player, AwardType.TANK, tankId, count, AwardFrom.ACTIVITY_AWARD);
//		builder.addAward(PbHelper.createAwardPb(AwardType.TANK, tankId, count, keyId));
//		builder.setGold(lord.getGold());
//
//		LogHelper.logActivity(lord, ActivityConst.ACT_TANK_RAFFLE_ID, price, AwardType.TANK, tankId, count, serverId);
//
//		for (int i = 0; i < 3; i++) {
//			builder.addColor(color[i + 1]);
//		}
//		handler.sendMsgToPlayer(DoActNewRaffleRs.ext, builder.build());
//	}
//
//	/**
//	 * 坦克拉霸锁定·新
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void lockNewRaffleRq(LockNewRaffleRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_NEW_RAFFLE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_NEW_RAFFLE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		StaticActRaffle staticActRaffle = staticActivityDataMgr.getActRaffle(activityKeyId);
//		if (staticActRaffle == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int tankId = req.getTankId();
//		if (tankId != 0) {
//			List<Integer> tankList = staticActRaffle.getTankList();
//			if (tankList.indexOf(tankId) < 0) {
//				handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//				return;
//			}
//		}
//		lord.setLockTankId(tankId);
//		lord.setLockTime(TimeHelper.getCurrentDay());
//		LockNewRaffleRs.Builder builder = LockNewRaffleRs.newBuilder();
//		builder.setResult(true);
//
//		handler.sendMsgToPlayer(LockNewRaffleRs.ext, builder.build());
//	}
//
//	/**
//	 * 疯狂歼灭-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActDestroyRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_TANK_DESTORY_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		UsualActivityData usualActivityData = activityDataManager.getUsualActivity(ActivityConst.ACT_TANK_DESTORY_ID);
//		if (usualActivityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_TANK_DESTORY_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int nowDay = TimeHelper.getCurrentDay();
//
//		List<Long> statusList = activity.getStatusList();
//		GetActDestroyRs.Builder builder = GetActDestroyRs.newBuilder();
//		List<StaticActAward> condList = staticActivityDataMgr.getActAwardById(activityKeyId);
//		for (StaticActAward e : condList) {
//			int keyId = e.getKeyId();
//			Integer status = activity.getStatusMap().get(keyId);
//			if (status == null) {
//				status = 0;
//			}
//			int sortId = e.getSortId();
//			int state = (int) statusList.get(sortId).longValue();
//
//			if (activity.getEndTime() != nowDay && !e.getParam().trim().equals("0")) {// 清理歼灭数据{坦克,战车,火炮,火箭}
//				status = 0;
//				state = 0;
//			}
//			builder.addDestoryTank(PbHelper.createCondStatePb(state, e, status));
//		}
//		handler.sendMsgToPlayer(GetActDestroyRs.ext, builder.build());
//	}
//
//	/**
//	 * 疯狂歼灭-排行榜{前十名,其他均为未入榜}
//	 * 
//	 * @param handler
//	 */
//	public void getActDestroyRankRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_TANK_DESTORY_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		int step = activityBase.getStep();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_TANK_DESTORY_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_TANK_DESTORY_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		List<StaticActRank> srankList = staticActivityDataMgr.getActRankList(activityKeyId, ActivityConst.TYPE_DEFAULT);
//		if (srankList == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		// 我的积分
//		long score = activity.getStatusList().get(5);
//
//		GetActDestroyRankRs.Builder builder = GetActDestroyRankRs.newBuilder();
//		LinkedList<ActPlayerRank> rankList = activityData.getPlayerRanks(ActivityConst.TYPE_DEFAULT);
//		for (int i = 0; i < rankList.size() && i < ActivityConst.RANK_TANK_DESTORY; i++) {
//			ActPlayerRank e = rankList.get(i);
//			long lordId = e.getLordId();
//			Player rankPlayer = playerDataManager.getPlayer(lordId);
//			if (rankPlayer != null && rankPlayer.lord != null) {
//				builder.addActPlayerRank(PbHelper.createActPlayerRank(e, rankPlayer.lord.getNick()));
//			}
//		}
//		builder.setStatus(0);
//		builder.setScore((int) score);
//		if (step == ActivityConst.OPEN_STEP) {
//			builder.setOpen(false);
//		} else if (step == ActivityConst.OPEN_AWARD) {
//			builder.setOpen(true);
//			Map<Integer, Integer> statusMap = activity.getStatusMap();
//			if (statusMap.containsKey(ActivityConst.TYPE_DEFAULT)) {
//				builder.setStatus(1);
//			}
//		}
//
//		List<StaticActRank> staticActRankList = staticActivityDataMgr.getActRankList(activityKeyId, ActivityConst.TYPE_DEFAULT);
//		if (staticActRankList != null) {
//			for (StaticActRank e : staticActRankList) {
//				builder.addRankAward(PbHelper.createRankAwardPb(e));
//			}
//		}
//		handler.sendMsgToPlayer(GetActDestroyRankRs.ext, builder.build());
//	}
//
//	/**
//	 * 技术革新主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActTechRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_TECH_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		GetActTechRs.Builder builder = GetActTechRs.newBuilder();
//		Iterator<StaticActTech> it = staticActivityDataMgr.getActTechMap().values().iterator();
//		while (it.hasNext()) {
//			StaticActTech next = it.next();
//			builder.addTech(PbHelper.createTechPb(next));
//		}
//		handler.sendMsgToPlayer(GetActTechRs.ext, builder.build());
//	}
//
//	/**
//	 * 技术革新
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActTechRq(DoActTechRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_TECH_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		int techId = req.getTechId();
//		StaticActTech staticActTech = staticActivityDataMgr.getActTech(techId);
//		if (staticActTech == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int costPropId = staticActTech.getPropId();
//		int costCount = staticActTech.getCount();
//
//		Prop prop = player.props.get(costPropId);
//		if (prop == null || prop.getCount() < costCount) {
//			handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
//			return;
//		}
//
//		List<Integer> award = staticActivityDataMgr.getActTechAward(staticActTech);
//		if (award == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int serverId = player.account.getServerId();
//		prop.setCount(prop.getCount() - costCount);
//		DoActTechRs.Builder builder = DoActTechRs.newBuilder();
//		int type = award.get(0);
//		int itemId = award.get(1);
//		int count = award.get(2);
//		int keyId = playerDataManager.addAward(player, type, itemId, count, AwardFrom.ACTIVITY_TECH);
//		builder.addAward(PbHelper.createAwardPb(type, itemId, count, keyId));
//
//		LogHelper.logActivity(player.lord, ActivityConst.ACT_TECH_ID, 0, type, itemId, count, serverId);
//
//		handler.sendMsgToPlayer(DoActTechRs.ext, builder.build());
//	}
//
//	/**
//	 * 武将招募-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActGeneralRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_GENERAL_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_GENERAL_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//
//		GetActGeneralRs.Builder builder = GetActGeneralRs.newBuilder();
//
//		long score = activity.getStatusList().get(0);
//		long times = activity.getStatusList().get(1);
//		List<StaticActGeneral> condList = staticActivityDataMgr.getActGeneralList(activityKeyId);
//		for (StaticActGeneral e : condList) {
//			builder.addGeneral(PbHelper.createGeneralPb(e));
//			builder.setLuck(e.getRepeat());
//		}
//		builder.setScore((int) score);// 我的积分
//		builder.setCount((int) times);// 次数
//		handler.sendMsgToPlayer(GetActGeneralRs.ext, builder.build());
//	}
//
//	/**
//	 * 武将招募-抽取
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActGeneralRq(DoActGeneralRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_GENERAL_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		int step = activityBase.getStep();
//		if (step != ActivityConst.OPEN_STEP) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_GENERAL_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_GENERAL_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int generalId = req.getGeneralId();
//		StaticActGeneral staticActGeneral = staticActivityDataMgr.getActGeneral(generalId);
//		if (staticActGeneral == null) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//
//		int price = staticActGeneral.getPrice();
//		if (lord.getGold() < price) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//		playerDataManager.subGold(lord, price, GoldCost.ACTIVITY_GENERAL);
//
//		List<Long> statusList = activity.getStatusList();
//
//		long score = statusList.get(0);
//		long times = statusList.get(1);
//
//		int hotHero1 = (int) (times / staticActGeneral.getRepeat());
//
//		score = score + staticActGeneral.getPoint();
//		times = times + staticActGeneral.getCount();
//
//		statusList.set(0, score);
//		statusList.set(1, times);
//
//		int hotHero2 = (int) (times / staticActGeneral.getRepeat());
//
//		// 计算排名
//		if (score >= 800) {// 积分超过800才可进入排行
//			activityData.addPlayerRank(lord.getLordId(), score, ActivityConst.RANK_GENERAL, ActivityConst.DESC);
//		}
//		int serverId = player.account.getServerId();
//		DoActGeneralRs.Builder builder = DoActGeneralRs.newBuilder();
//
//		// 发放奖励
//		int repeat = staticActGeneral.getCount();
//
//		LogHelper.logActivity(lord, ActivityConst.ACT_GENERAL_ID, price, 0, 0, 0, serverId);
//
//		List<CommonPb.Award> awards = new ArrayList<CommonPb.Award>();
//		CommonPb.Award a;
//		if (hotHero1 != hotHero2) {// 出热门武将
//			repeat -= 1;
//			int keyId = playerDataManager.addAward(player, AwardType.HERO, staticActGeneral.getHeroId(), 1, AwardFrom.ACTIVITY_GENERAL);
//			a = PbHelper.createAwardPb(AwardType.HERO, staticActGeneral.getHeroId(), 1, keyId);
//			awards.add(a);
//			builder.addAward(a);
//			LogHelper.logActivity(lord, ActivityConst.ACT_GENERAL_ID, 0, AwardType.HERO, staticActGeneral.getHeroId(), 1, serverId);
//		}
//
//		for (int i = 0; i < repeat; i++) {
//			List<Integer> list = staticActivityDataMgr.randomAwardList(staticActGeneral.getAwardList());
//			if (list == null || list.size() < 3) {
//				continue;
//			}
//			int type = list.get(0);
//			int id = list.get(1);
//			int count = list.get(2);
//			int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.ACTIVITY_GENERAL);
//			a = PbHelper.createAwardPb(type, id, count, keyId);
//			awards.add(a);
//			builder.addAward(a);
//			LogHelper.logActivity(lord, ActivityConst.ACT_GENERAL_ID, 0, type, id, count, serverId);
//		}
//
//		builder.setScore((int) score);// 我的积分
//		builder.setCount((int) times);// 次数
//		handler.sendMsgToPlayer(DoActGeneralRs.ext, builder.build());
//		
//		// 发送加入活动消息
//		propService.sendJoinActivityMsg(ActivityConst.ACT_GENERAL_ID, player, awards);
//	}
//
//	/**
//	 * 招募武将-排行榜{前十名,其他均为未入榜}
//	 * 
//	 * @param handler
//	 */
//	public void getActGeneralRankRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_GENERAL_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int step = activityBase.getStep();
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_GENERAL_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_GENERAL_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		// 我的积分
//		long score = activity.getStatusList().get(0);
//
//		GetActGeneralRankRs.Builder builder = GetActGeneralRankRs.newBuilder();
//		LinkedList<ActPlayerRank> rankList = activityData.getPlayerRanks(ActivityConst.TYPE_DEFAULT);
//		for (int i = 0; i < rankList.size() && i < ActivityConst.RANK_GENERAL; i++) {
//			ActPlayerRank e = rankList.get(i);
//			long lordId = e.getLordId();
//			Player rankPlayer = playerDataManager.getPlayer(lordId);
//			if (rankPlayer != null && rankPlayer.lord != null) {
//				builder.addActPlayerRank(PbHelper.createActPlayerRank(e, rankPlayer.lord.getNick()));
//			}
//		}
//		builder.setStatus(0);
//		builder.setScore((int) score);
//		if (step == ActivityConst.OPEN_STEP) {
//			builder.setOpen(false);
//		} else if (step == ActivityConst.OPEN_AWARD) {
//			builder.setOpen(true);
//			Map<Integer, Integer> statusMap = activity.getStatusMap();
//			if (statusMap.containsKey(ActivityConst.TYPE_DEFAULT)) {
//				builder.setStatus(1);
//			}
//		}
//
//		List<StaticActRank> staticActRankList = staticActivityDataMgr.getActRankList(activityKeyId, ActivityConst.TYPE_DEFAULT);
//		if (staticActRankList != null) {
//			for (StaticActRank e : staticActRankList) {
//				builder.addRankAward(PbHelper.createRankAwardPb(e));
//			}
//		}
//		handler.sendMsgToPlayer(GetActGeneralRankRs.ext, builder.build());
//	}
//
//	/**
//	 * 每日充值-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActEDayPayRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_EDAY_PAY_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_EDAY_PAY_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		int dayiy = activityBase.getDayiy();
//		dayiy = dayiy % 4 == 0 ? 4 : dayiy % 4;
//
//		long status = activity.getStatusList().get(0);
//		StaticActEverydayPay staticEverydayPay = staticActivityDataMgr.getActEverydayPay(dayiy);
//		if (staticEverydayPay == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		GetActEDayPayRs.Builder builder = GetActEDayPayRs.newBuilder();
//		builder.setState((int) status);
//		builder.setGoldBoxId(staticEverydayPay.getBox1());
//		builder.setPropBoxId(staticEverydayPay.getBox2());
//		handler.sendMsgToPlayer(GetActEDayPayRs.ext, builder.build());
//	}
//
//	/**
//	 * 每日充值-领奖
//	 * 
//	 * @param handler
//	 */
//	public void doActEDayPayRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_EDAY_PAY_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_EDAY_PAY_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_EDAY_PAY_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		long status = activity.getStatusList().get(0);
//		if (status == 0) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//		if (status == 2) {
//			handler.sendErrorMsgToPlayer(GameError.AWARD_HAD_GOT);
//			return;
//		}
//
//		int dayiy = activityBase.getDayiy();
//		dayiy = dayiy % 4 == 0 ? 4 : dayiy % 4;
//
//		StaticActEverydayPay staticEverydayPay = staticActivityDataMgr.getActEverydayPay(dayiy);
//		if (staticEverydayPay == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		activity.getStatusList().set(0, (long) 2);// 领取奖励
//
//		DoActEDayPayRs.Builder builder = DoActEDayPayRs.newBuilder();
//
//		// 开启两个箱子
//		StaticProp goldBox = staticPropDataMgr.getStaticProp(staticEverydayPay.getBox1());
//		if (goldBox != null && goldBox.getEffectValue().size() > 0) {// 金币箱子
//			int awardId = goldBox.getEffectValue().get(0).get(0);
//			List<List<Integer>> awardList = staticAwardsDataMgr.getAwards(awardId);
//			for (List<Integer> e : awardList) {
//				if (e.size() < 3) {
//					continue;
//				}
//				int type = e.get(0);
//				int id = e.get(1);
//				int count = e.get(2);
//				playerDataManager.addAward(player, type, id, count, AwardFrom.EVERY_DAY_PAY);
//				builder.addAward(PbHelper.createAwardPb(type, id, count));
//			}
//		}
//		String params = activityData.getParams();
//
//		// 箱子特殊道具
//		boolean flag = false;
//		StaticProp propBox = staticPropDataMgr.getStaticProp(staticEverydayPay.getBox2());
//		if (propBox != null && propBox.getEffectValue().size() > 0) {
//			int awardId = propBox.getEffectValue().get(0).get(0);
//			List<List<Integer>> awardList = staticAwardsDataMgr.getAwards(awardId);
//			for (List<Integer> e : awardList) {
//				if (e.size() < 3) {
//					continue;
//				}
//				int type = e.get(0);
//				int id = e.get(1);
//				int count = e.get(2);
//
//				flag = staticActivityDataMgr.isSpecial(staticEverydayPay, type, id);
//				if (flag) {
//					flag = false;
//					String pp = new StringBuffer().append(type).append("_").append(id).toString();
//					if (params != null && !params.equals("")) {
//						int index = params.indexOf(pp);
//						if (index > -1) {// 该特殊道具被已被抽取
//							flag = true;
//						} else {// 未抽取到,则记录的特殊道具
//							pp = new StringBuffer().append(params).append(",").append(pp).toString();
//						}
//					}
//
//					if (!flag) {// 添加奖励
//						int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.EVERY_DAY_PAY);
//						builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//						activityData.setParams(pp);
//					}
//
//					flag = false;
//				} else {
//					int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.EVERY_DAY_PAY);
//					builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//
//				}
//			}
//		}
//
//		// 出了特殊道具,并且已被抽取掉
//		if (flag) {// 补一个道具
//			StaticProp fixBox = staticPropDataMgr.getStaticProp(staticEverydayPay.getBox3());
//			if (fixBox != null && fixBox.getEffectValue().size() > 0) {
//				int awardId = fixBox.getEffectValue().get(0).get(0);
//				List<List<Integer>> awardList = staticAwardsDataMgr.getAwards(awardId);
//				for (List<Integer> e : awardList) {
//					if (e.size() < 3) {
//						continue;
//					}
//					int type = e.get(0);
//					int id = e.get(1);
//					int count = e.get(2);
//					int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.EVERY_DAY_PAY);
//					builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//				}
//			}
//		}
//		handler.sendMsgToPlayer(DoActEDayPayRs.ext, builder.build());
//	}
//
//	/**
//	 * 消费转盘-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActConsumeDialRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_CONSUME_DIAL_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_CONSUME_DIAL_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		GetActConsumeDialRs.Builder builder = GetActConsumeDialRs.newBuilder();
//		int monthAndDay = TimeHelper.getMonthAndDay(new Date());
//		int consumeDial = lord.getConsumeDial();
//		if (consumeDial / 100 != monthAndDay / 100) {
//			consumeDial = monthAndDay;
//		}
//
//		int useCount = consumeDial % 100;
//		int free = 0;
//		if (lord.getVip() > 0) {
//			free = 2 - useCount < 0 ? 0 : 2 - useCount;
//		} else {
//			free = 1 - useCount < 0 ? 0 : 1 - useCount;
//		}
//
//		builder.setFree(free);// 剩余次数
//		long consume = activity.getStatusList().get(0);
//		builder.setCount((int) (consume / 199));
//
//		long score = activity.getStatusList().get(1);
//		List<StaticActFortune> condList = staticActivityDataMgr.getActFortuneList(ActivityConst.ACT_CONSUME_DIAL_ID);
//		for (StaticActFortune e : condList) {
//			builder.addFortune(PbHelper.createFortunePb(e));
//			builder.setDisplayList(e.getDisplayList());
//		}
//		builder.setScore((int) score);// 我的积分
//		handler.sendMsgToPlayer(GetActConsumeDialRs.ext, builder.build());
//	}
//
//	/**
//	 * 消费转盘-排行榜{前三十名,其他均为未入榜}
//	 * 
//	 * @param handler
//	 */
//	public void getActConsumeDialRankRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_CONSUME_DIAL_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int step = activityBase.getStep();
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_CONSUME_DIAL_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_CONSUME_DIAL_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		// 我的积分
//		long score = activity.getStatusList().get(1);
//
//		GetActConsumeDialRankRs.Builder builder = GetActConsumeDialRankRs.newBuilder();
//		LinkedList<ActPlayerRank> rankList = activityData.getPlayerRanks(ActivityConst.TYPE_DEFAULT);
//		for (int i = 0; i < rankList.size() && i < ActivityConst.RANK_PAWN; i++) {
//			ActPlayerRank e = rankList.get(i);
//			long lordId = e.getLordId();
//			Player rankPlayer = playerDataManager.getPlayer(lordId);
//			if (rankPlayer != null && rankPlayer.lord != null) {
//				builder.addActPlayerRank(PbHelper.createActPlayerRank(e, rankPlayer.lord.getNick()));
//			}
//		}
//		builder.setStatus(0);
//		builder.setScore((int) score);
//		if (step == ActivityConst.OPEN_STEP) {
//			builder.setOpen(false);
//		} else if (step == ActivityConst.OPEN_AWARD) {
//			builder.setOpen(true);
//			Map<Integer, Integer> statusMap = activity.getStatusMap();
//			if (statusMap.containsKey(ActivityConst.TYPE_DEFAULT)) {
//				builder.setStatus(1);
//			}
//		}
//
//		List<StaticActRank> staticActRankList = staticActivityDataMgr.getActRankList(activityKeyId, ActivityConst.TYPE_DEFAULT);
//		if (staticActRankList != null) {
//			for (StaticActRank e : staticActRankList) {
//				builder.addRankAward(PbHelper.createRankAwardPb(e));
//			}
//		}
//		handler.sendMsgToPlayer(GetActConsumeDialRankRs.ext, builder.build());
//	}
//
//	/**
//	 * 消费转轴-抽取
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActConsumeDialRq(DoActConsumeDialRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_CONSUME_DIAL_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int step = activityBase.getStep();
//		if (step != 0) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_CONSUME_DIAL_ID);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_CONSUME_DIAL_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int fortuneId = req.getFortuneId();
//		StaticActFortune staticActFortune = staticActivityDataMgr.getActFortune(fortuneId);
//		if (staticActFortune == null) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//
//		int monthAndDay = TimeHelper.getMonthAndDay(new Date());
//		int consumeDial = lord.getConsumeDial();
//		if (consumeDial / 100 != monthAndDay / 100) {
//			consumeDial = monthAndDay;
//		}
//		int useCount = consumeDial % 100;
//		int free = 0;
//		if (lord.getVip() > 0) {
//			free = 2 - useCount < 0 ? 0 : 2 - useCount;
//		} else {
//			free = 1 - useCount < 0 ? 0 : 1 - useCount;
//		}
//
//		if (free > 0 && staticActFortune.getCount() == 1) {// 单抽免费次数
//			lord.setConsumeDial(consumeDial + 1);
//		} else {
//			long consume = activity.getStatusList().get(0);
//			if (staticActFortune.getPrice() > consume) {
//				handler.sendErrorMsgToPlayer(GameError.SCORE_NOT_ENOUGH);
//				return;
//			}
//			activity.getStatusList().set(0, consume - staticActFortune.getPrice());
//		}
//
//		DoActConsumeDialRs.Builder builder = DoActConsumeDialRs.newBuilder();
//
//		// 发放奖励
//		int scoreAdd = 0;
//		int repeat = staticActFortune.getCount();
//		for (int i = 0; i < repeat; i++) {
//			List<Integer> list = staticActivityDataMgr.randomAwardList(staticActFortune.getAwardList());
//			if (list == null || list.size() < 5) {
//				continue;
//			}
//			int type = list.get(0);
//			int id = list.get(1);
//			int count = list.get(2);
//			int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.CONSUME_DIAL);
//			scoreAdd += list.get(4);// 增加积分
//			builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//		}
//
//		long score = activity.getStatusList().get(1);
//		score += scoreAdd;
//		activity.getStatusList().set(1, score);
//		// 计算排名
//		if (score >= 30) {// 积分超过500才可进入排行
//			activityData.addPlayerRank(lord.getLordId(), score, ActivityConst.RANK_CONSUME_DIAL, ActivityConst.DESC);
//		}
//
//		builder.setScore((int) score);// 我的积分
//		handler.sendMsgToPlayer(DoActConsumeDialRs.ext, builder.build());
//	}
//
//	/**
//	 * 度假胜地
//	 * 
//	 * @param handler
//	 */
//	public void getActVacationlandRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_VACATIONLAND_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_VACATIONLAND_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		GetActVacationlandRs.Builder builder = GetActVacationlandRs.newBuilder();
//
//		List<Long> statusList = activity.getStatusList();
//		int topup = (int) statusList.get(0).longValue();
//		int villageId = (int) statusList.get(1).longValue();
//		int buyDate = (int) statusList.get(2).longValue();// 购买时间
//		if (villageId != 0 && buyDate != 0) {// 是否已购买
//			int onday = TimeHelper.subDay(TimeHelper.getCurrentDay(), buyDate) + 3;
//			long state = statusList.get(onday).longValue();
//			if (state == 0) {
//				statusList.set(onday, 1L);
//			}
//		}
//
//		builder.setTopup(topup);
//		builder.setVillageId(villageId);
//
//		List<StaticActVacationland> vlist = staticActivityDataMgr.getVillageList();
//		for (StaticActVacationland e : vlist) {
//			builder.addVillage(PbHelper.createVillagePb(e));
//		}
//
//		Map<Integer, Integer> statusMap = activity.getStatusMap();
//		Map<Integer, StaticActVacationland> landMap = staticActivityDataMgr.getActVacationlandMap();
//		Iterator<StaticActVacationland> it = landMap.values().iterator();
//		while (it.hasNext()) {
//			StaticActVacationland next = it.next();
//			int landId = next.getLandId();
//			int theday = next.getOnday();
//			int state = (int) statusList.get(theday + 2).longValue();
//			if (statusMap.containsKey(landId)) {
//				builder.addVillageAward(PbHelper.createVillageAwardPb(next, villageId, state, 1));
//			} else {
//				builder.addVillageAward(PbHelper.createVillageAwardPb(next, villageId, state, 0));
//			}
//		}
//		handler.sendMsgToPlayer(GetActVacationlandRs.ext, builder.build());
//	}
//
//	/**
//	 * 度假胜地
//	 * 
//	 * @param handler
//	 */
//	public void buyActVacationlandRq(BuyActVacationlandRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_VACATIONLAND_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_VACATIONLAND_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		List<Long> statusList = activity.getStatusList();
//		int topup = (int) statusList.get(0).longValue();
//		int villageId = (int) statusList.get(1).longValue();
//		// int onday = (int) statusList.get(2).longValue();
//		if (villageId != 0) {
//			handler.sendErrorMsgToPlayer(GameError.BUY_ONLY_ONCE);
//			return;
//		}
//		int buyId = req.getVillageId();
//		StaticActVacationland buyLand = null;
//		List<StaticActVacationland> vlist = staticActivityDataMgr.getVillageList();
//		for (StaticActVacationland e : vlist) {
//			if (e.getVillageId() == buyId) {
//				buyLand = e;
//				break;
//			}
//		}
//		if (buyLand == null) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//		if (topup < buyLand.getTopup()) {
//			handler.sendErrorMsgToPlayer(GameError.TOPUP_NOT_ENOUGH);
//			return;
//		}
//
//		// 给特效
//		StaticProp staticProp = staticPropDataMgr.getStaticProp(buyId);
//		List<List<Integer>> effectValue = staticProp.getEffectValue();
//		if (effectValue == null || effectValue.isEmpty()) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		if (!playerDataManager.subGold(lord, buyLand.getPrice(), GoldCost.BUY_VACATION)) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//
//		for (List<Integer> one : effectValue) {
//			if (one.size() != 2) {
//				continue;
//			}
//			playerDataManager.addEffect(player, one.get(0), one.get(1));
//		}
//
//		int currentDay = TimeHelper.getCurrentDay();
//		statusList.set(1, (long) buyId);
//		statusList.set(2, (long) currentDay);
//		statusList.set(3, 1L);// 第一天已登陆
//
//		BuyActVacationlandRs.Builder builder = BuyActVacationlandRs.newBuilder();
//		handler.sendMsgToPlayer(BuyActVacationlandRs.ext, builder.build());
//	}
//
//	/**
//	 * 度假胜地-领奖
//	 * 
//	 * @param handler
//	 */
//	public void doActVacationlandRq(DoActVacationlandRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_VACATIONLAND_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_VACATIONLAND_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		List<Long> statusList = activity.getStatusList();
//		// int topup = (int) statusList.get(0).longValue();//充值
//		int villageId = (int) statusList.get(1).longValue();// 村庄ID
//		int buyDate = (int) statusList.get(2).longValue();// 购买日期
//		if (villageId == 0 || buyDate == 0) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//		int landId = req.getLandId();
//		StaticActVacationland village = staticActivityDataMgr.getVillage(landId);
//		if (village == null) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//
//		long state = statusList.get(2 + village.getOnday()).longValue();
//		if (state == 0) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//
//		if (activity.getStatusMap().containsKey(landId)) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_GOT);
//			return;
//		}
//		activity.getStatusMap().put(landId, 1);
//
//		DoActVacationlandRs.Builder builder = DoActVacationlandRs.newBuilder();
//		List<List<Integer>> awardList = village.getAwardList();
//		for (List<Integer> e : awardList) {
//			if (e.size() < 3) {
//				continue;
//			}
//			int type = e.get(0);
//			int id = e.get(1);
//			int count = e.get(2);
//			if (type == AwardType.EQUIP) {
//				for (int i = 0; i < count; i++) {
//					int keyId = playerDataManager.addAward(player, type, id, 1, AwardFrom.VACATION);
//					builder.addAward(PbHelper.createAwardPb(type, id, 1, keyId));
//				}
//			} else {
//				int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.VACATION);
//				builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//			}
//
//		}
//		handler.sendMsgToPlayer(DoActVacationlandRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * 获取配件兑换配方列表
//	 * 
//	 * @param handler
//	 */
//	public void getActPartCashRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_EXCHANGE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PART_EXCHANGE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		GetActPartCashRs.Builder builder = GetActPartCashRs.newBuilder();
//		List<StaticActExchange> exchangeList = staticActivityDataMgr.getActExchange(activityBase.getKeyId());
//		if (exchangeList == null || exchangeList.size() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int today = TimeHelper.getCurrentDay();
//		for (StaticActExchange e : exchangeList) {
//			int exchangeId = e.getExchangeId();
//			Cash cash = player.cashs.get(exchangeId);
//			if (cash == null || cash.getRefreshDate() != today) {
//				cash = activityDataManager.freshCash(player, cash, e, true);
//				cash.setRefreshDate(today);
//				player.cashs.put(exchangeId, cash);
//			}
//			builder.addCash(PbHelper.createCashPb(cash));
//		}
//		handler.sendMsgToPlayer(GetActPartCashRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * 配件配方兑换
//	 * 
//	 * @param handler
//	 */
//	public void doPartCashRq(DoPartCashRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_EXCHANGE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PART_EXCHANGE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int cashId = req.getCashId();
//		StaticActExchange actExchange = staticActivityDataMgr.getActExchange(activityBase.getKeyId(), cashId);
//		if (actExchange == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		int today = TimeHelper.getCurrentDay();
//		Cash cash = player.cashs.get(cashId);
//		if (cash == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		if (cash.getRefreshDate() != today) {
//			cash = activityDataManager.freshCash(player, cash, actExchange, true);
//			cash.setRefreshDate(today);
//			player.cashs.put(actExchange.getExchangeId(), cash);
//		}
//
//		if (cash.getState() <= 0) {
//			handler.sendErrorMsgToPlayer(GameError.COUNT_NOT_ENOUGH);
//			return;
//		}
//
//		// 判定材料是否足够
//		List<Part> partList = new ArrayList<Part>();
//		for (List<Integer> e : cash.getList()) {
//			int type = e.get(0);// 类型
//			int id = e.get(1);// ID
//			int count = e.get(2);// 数量
//			if (type == AwardType.PROP) {//
//				Prop prop = player.props.get(id);
//				if (prop == null || prop.getCount() < count) {
//					handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
//					return;
//				}
//			} else if (type == AwardType.GOLD) {
//				if (lord.getGold() < count) {
//					handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//					return;
//				}
//			} else if (type == AwardType.PART) {
//				Part part = playerDataManager.getMinLvPartById(player, id);
//				if (part == null) {
//					handler.sendErrorMsgToPlayer(GameError.NO_PART);
//					return;
//				}
//				partList.add(part);
//			} else if (type == AwardType.CHIP) {
//				Chip chip = player.chips.get(id);
//				if (chip == null || chip.getCount() < count) {
//					handler.sendErrorMsgToPlayer(GameError.CHIP_NOT_ENOUGH);
//					return;
//				}
//			} else if (type == AwardType.PART_MATERIAL) {
//				if (id == 1 && lord.getFitting() < count) {
//					handler.sendErrorMsgToPlayer(GameError.FIGHT_NOT_ENOUGH);
//					return;
//				} else if (id == 2 && lord.getMetal() < count) {
//					handler.sendErrorMsgToPlayer(GameError.METAL_NOT_ENOUGH);
//					return;
//				} else if (id == 3 && lord.getPlan() < count) {
//					handler.sendErrorMsgToPlayer(GameError.DRAW_NOT_ENOUGH);
//					return;
//				} else if (id == 4 && lord.getMineral() < count) {
//					handler.sendErrorMsgToPlayer(GameError.METAL_NOT_ENOUGH);
//					return;
//				} else if (id == 5 && lord.getTool() < count) {
//					handler.sendErrorMsgToPlayer(GameError.METAL_NOT_ENOUGH);
//					return;
//				} else if (id == 6 && lord.getDraw() < count) {
//					handler.sendErrorMsgToPlayer(GameError.DRAW_NOT_ENOUGH);
//					return;
//				}
//			} else {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//				return;
//			}
//		}
//
//		DoPartCashRs.Builder builder = DoPartCashRs.newBuilder();
//		for (List<Integer> e : cash.getList()) {
//			int type = e.get(0);
//			int id = e.get(1);
//			int count = e.get(2);
//			if (type == AwardType.PROP) {
//				Prop prop = player.props.get(id);
//				playerDataManager.subProp(prop, count);
//				builder.addCostList(PbHelper.createAwardPb(type, id, count, 0));
//			} else if (type == AwardType.GOLD) {
//				playerDataManager.subGold(lord, count, GoldCost.EXCHANGE_PART);
//				builder.addCostList(PbHelper.createAwardPb(type, id, count, 0));
//			} else if (type == AwardType.PART) {
//				continue;
//			} else if (type == AwardType.CHIP) {
//				Chip chip = player.chips.get(id);
//				if (chip != null) {
//					chip.setCount(chip.getCount() - count);
//				}
//				builder.addCostList(PbHelper.createAwardPb(type, id, count, 0));
//			} else if (type == AwardType.PART_MATERIAL) {
//				if (id == 1) {
//					playerDataManager.modifyFitting(lord, -count);
//				} else if (id == 2 && lord.getMetal() < count) {
//					playerDataManager.modifyMetal(lord, -count);
//				} else if (id == 3 && lord.getPlan() < count) {
//					playerDataManager.modifyPlan(lord, -count);
//				} else if (id == 4 && lord.getMineral() < count) {
//					playerDataManager.modifyMineral(lord, -count);
//				} else if (id == 5 && lord.getTool() < count) {
//					playerDataManager.modifyTool(lord, -count);
//				} else if (id == 6 && lord.getDraw() < count) {
//					playerDataManager.modifyDraw(lord, -count);
//				}
//				builder.addCostList(PbHelper.createAwardPb(type, id, count, 0));
//			}
//		}
//
//		for (Part part : partList) {
//			player.parts.get(0).remove(part.getKeyId());
//			builder.addCostList(PbHelper.createAwardPb(AwardType.PART, part.getPartId(), 1, part.getKeyId()));
//		}
//
//		int type = cash.getAwardList().get(0);
//		int id = cash.getAwardList().get(1);
//		int count = cash.getAwardList().get(2);
//		int awardKeyId = playerDataManager.addAward(player, type, id, count, AwardFrom.EXCHANGE_PART);
//
//		cash.setState(cash.getState() - 1);
//
//		builder.setAward(PbHelper.createAwardPb(type, id, count, awardKeyId));
//		handler.sendMsgToPlayer(DoPartCashRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * 刷新配件兑换配方
//	 * 
//	 * @param handler
//	 */
//	public void refshPartCashRq(RefshPartCashRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_EXCHANGE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PART_EXCHANGE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int cashId = req.getCashId();
//		StaticActExchange actExchange = staticActivityDataMgr.getActExchange(activityBase.getKeyId(), cashId);
//		if (actExchange == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int today = TimeHelper.getCurrentDay();
//		Cash cash = player.cashs.get(cashId);
//		if (cash == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		if (cash.getRefreshDate() != today) {
//			cash = activityDataManager.freshCash(player, cash, actExchange, true);
//			cash.setRefreshDate(today);
//			player.cashs.put(cashId, cash);
//		} else if (cash.getFree() > 0) {
//			cash = activityDataManager.freshCash(player, cash, actExchange, false);
//			cash.setFree(cash.getFree() - 1);
//		} else {
//			int price = actExchange.getPrice();
//			if (!playerDataManager.subGold(lord, price, GoldCost.REFRESH_PART_CASH)) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//			cash = activityDataManager.freshCash(player, cash, actExchange, false);
//		}
//
//		RefshPartCashRs.Builder builder = RefshPartCashRs.newBuilder();
//		builder.setCash(PbHelper.createCashPb(cash));
//		handler.sendMsgToPlayer(RefshPartCashRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * 获取装备兑换配方列表
//	 * 
//	 * @param handler
//	 */
//	public void getActEquipCashRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_EQUIP_EXCHANGE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_EQUIP_EXCHANGE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		GetActEquipCashRs.Builder builder = GetActEquipCashRs.newBuilder();
//		List<StaticActExchange> exchangeList = staticActivityDataMgr.getActExchange(activityBase.getKeyId());
//		if (exchangeList == null || exchangeList.size() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int today = TimeHelper.getCurrentDay();
//		for (StaticActExchange e : exchangeList) {
//			int exchangeId = e.getExchangeId();
//			Cash cash = player.cashs.get(exchangeId);
//			if (cash == null || cash.getRefreshDate() != today) {
//				cash = activityDataManager.freshCash(player, cash, e, true);
//				cash.setRefreshDate(today);
//				player.cashs.put(exchangeId, cash);
//			}
//			builder.addCash(PbHelper.createCashPb(cash));
//		}
//		handler.sendMsgToPlayer(GetActEquipCashRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * 装备配方兑换
//	 * 
//	 * @param handler
//	 */
//	public void doEquipCashRq(DoEquipCashRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_EQUIP_EXCHANGE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_EQUIP_EXCHANGE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int cashId = req.getCashId();
//		StaticActExchange actExchange = staticActivityDataMgr.getActExchange(activityBase.getKeyId(), cashId);
//		if (actExchange == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		int today = TimeHelper.getCurrentDay();
//		Cash cash = player.cashs.get(cashId);
//		if (cash == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		if (cash.getRefreshDate() != today) {
//			cash = activityDataManager.freshCash(player, cash, actExchange, true);
//			cash.setRefreshDate(today);
//		}
//
//		if (cash.getState() <= 0) {
//			handler.sendErrorMsgToPlayer(GameError.COUNT_NOT_ENOUGH);
//			return;
//		}
//
//		List<Equip> equipList = new ArrayList<Equip>();
//		for (List<Integer> e : cash.getList()) {
//			int type = e.get(0);// 类型
//			int id = e.get(1);// ID
//			int count = e.get(2);// 数量
//			if (type == AwardType.PROP) {//
//				Prop prop = player.props.get(id);
//				if (prop == null || prop.getCount() < count) {
//					handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
//					return;
//				}
//			} else if (type == AwardType.GOLD) {
//				if (lord.getGold() < count) {
//					handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//					return;
//				}
//			} else if (type == AwardType.EQUIP) {
//				List<Equip> costList = playerDataManager.getMinLvEquipById(player, id);
//				if (costList.size() < count) {
//					handler.sendErrorMsgToPlayer(GameError.NO_EQUIP);
//					return;
//				}
//				Collections.sort(costList, new CompareEquipLv());
//				for (int i = 0; i < count; i++) {
//					equipList.add(costList.get(i));
//				}
//			} else {
//				handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//				return;
//			}
//		}
//
//		DoEquipCashRs.Builder builder = DoEquipCashRs.newBuilder();
//		for (List<Integer> e : cash.getList()) {
//			int type = e.get(0);
//			int id = e.get(1);
//			int count = e.get(2);
//			if (type == AwardType.PROP) {
//				Prop prop = player.props.get(id);
//				playerDataManager.subProp(prop, count);
//				builder.addCostList(PbHelper.createAwardPb(type, id, count, 0));
//			} else if (type == AwardType.GOLD) {
//				playerDataManager.subGold(lord, count, GoldCost.EXCHANGE_EQUIP);
//				builder.addCostList(PbHelper.createAwardPb(type, id, count, 0));
//			} else if (type == AwardType.EQUIP) {
//				continue;
//			}
//		}
//
//		for (Equip equip : equipList) {
//			player.equips.get(0).remove(equip.getKeyId());
//			builder.addCostList(PbHelper.createAwardPb(AwardType.EQUIP, equip.getEquipId(), 1, equip.getKeyId()));
//		}
//
//		int type = cash.getAwardList().get(0);
//		int id = cash.getAwardList().get(1);
//		int count = cash.getAwardList().get(2);
//
//		cash.setState(cash.getState() - 1);
//
//		int awardKeyId = playerDataManager.addAward(player, type, id, count, AwardFrom.EXCHANGE_EQUIP);
//
//		builder.setAward(PbHelper.createAwardPb(type, id, count, awardKeyId));
//		handler.sendMsgToPlayer(DoEquipCashRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * 刷新装备兑换配方
//	 * 
//	 * @param handler
//	 */
//	public void refshEquipCashRq(RefshEquipCashRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_EQUIP_EXCHANGE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_EQUIP_EXCHANGE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int cashId = req.getCashId();
//		StaticActExchange actExchange = staticActivityDataMgr.getActExchange(activityBase.getKeyId(), cashId);
//		if (actExchange == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		int today = TimeHelper.getCurrentDay();
//		Cash cash = player.cashs.get(cashId);
//		if (cash == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		if (cash.getRefreshDate() != today) {
//			cash = activityDataManager.freshCash(player, cash, actExchange, true);
//			cash.setRefreshDate(today);
//		} else if (cash.getRefreshDate() == today && cash.getFree() > 0) {
//			cash = activityDataManager.freshCash(player, cash, actExchange, false);
//			cash.setFree(cash.getFree() - 1);
//		} else {
//			int price = actExchange.getPrice();
//			if (!playerDataManager.subGold(lord, price, GoldCost.REFRESH_EQUIP_CASH)) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//				return;
//			}
//			cash = activityDataManager.freshCash(player, cash, actExchange, false);
//		}
//
//		RefshEquipCashRs.Builder builder = RefshEquipCashRs.newBuilder();
//		builder.setCash(PbHelper.createCashPb(cash));
//		handler.sendMsgToPlayer(RefshEquipCashRs.ext, builder.build());
//	}
//
//	/**
//	 * 分解配件兑换-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActPartResolveRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_RESOLVE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PART_RESOLVE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		List<Long> statusList = activity.getStatusList();
//		long score = statusList.get(0).longValue();
//
//		GetActPartResolveRs.Builder builder = GetActPartResolveRs.newBuilder();
//		List<StaticActPartResolve> condList = staticActivityDataMgr.getActPartResolveList(activityKeyId);
//		for (StaticActPartResolve e : condList) {
//			builder.addPartResolve(PbHelper.createPartResolvePb(e));
//		}
//		builder.setState((int) score);
//		handler.sendMsgToPlayer(GetActPartResolveRs.ext, builder.build());
//	}
//
//	/**
//	 * 分解配件兑换
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActPartResolveRq(DoActPartResolveRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PART_RESOLVE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PART_RESOLVE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int resolveId = req.getResolveId();
//		int activityKeyId = activityBase.getKeyId();
//		List<Long> statusList = activity.getStatusList();
//		long score = statusList.get(0).longValue();
//		StaticActPartResolve staticActPartResolve = staticActivityDataMgr.getActPartResolve(activityKeyId, resolveId);
//		if (staticActPartResolve.getSlug() > score) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//		DoActPartResolveRs.Builder builder = DoActPartResolveRs.newBuilder();
//		statusList.set(0, score - staticActPartResolve.getSlug());
//		for (int i = 0; i < staticActPartResolve.getAwardList().size(); i++) {
//			List<Integer> elist = staticActPartResolve.getAwardList().get(0);
//			if (elist.size() < 3) {
//				continue;
//			}
//			int type = elist.get(0);
//			int id = elist.get(1);
//			int count = elist.get(2);
//			int keyId = playerDataManager.addAward(player, type, id, count, AwardFrom.PART_RESOLVE);
//			builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
//		}
//
//		handler.sendMsgToPlayer(DoActPartResolveRs.ext, builder.build());
//	}
//
//	/**
//	 * 下注赢金币-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActGambleRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_GAMBLE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_GAMBLE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		List<StaticActGamble> gambleList = staticActivityDataMgr.getActGambleList(activityKeyId);
//		if (gambleList == null || gambleList.size() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		List<Long> statusList = activity.getStatusList();
//		long topup = statusList.get(0).longValue();// 累计充值
//		long price = statusList.get(1).longValue();// 已抽到哪一档
//
//		GetActGambleRs.Builder builder = GetActGambleRs.newBuilder();
//		builder.setTopup((int) topup);
//		builder.setPrice((int) price);
//
//		int count = 0;
//		for (StaticActGamble e : gambleList) {
//			if (e.getTopup() <= topup && e.getPrice() > price) {
//				count++;
//			}
//			builder.addTopupGamble(PbHelper.createTopupGamblePb(e));
//		}
//		builder.setCount(count);
//		handler.sendMsgToPlayer(GetActGambleRs.ext, builder.build());
//	}
//
//	/**
//	 * 下注赢金币
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActGambleRq(DoActGambleRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_GAMBLE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_GAMBLE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//
//		List<Long> statusList = activity.getStatusList();
//		long topup = statusList.get(0).longValue();// 累计充值
//		long price = statusList.get(1).longValue();// 已抽到哪一档
//
//		StaticActGamble actGamble = staticActivityDataMgr.getActGamble(activityKeyId, (int) topup, (int) price);
//		if (actGamble == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//		if (!playerDataManager.subGold(lord, actGamble.getPrice(), GoldCost.TOPUP_GAMBLE)) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
//			return;
//		}
//
//		statusList.set(1, (long) actGamble.getPrice());
//
//		List<List<Integer>> awardList = actGamble.getAwardList();
//		int[] seed = new int[] { 0, 0, 0 };
//		for (List<Integer> e : awardList) {
//			seed[0] += e.get(3);
//		}
//		seed[0] = RandomHelper.randomInSize(seed[0]);
//		for (List<Integer> e : awardList) {
//			seed[1] += e.get(3);
//			if (seed[0] <= seed[1]) {
//				seed[2] = e.get(2);
//				break;
//			}
//		}
//		playerDataManager.addGold(lord, seed[2], AwardFrom.TOPUP_GAMBLE);
//		DoActGambleRs.Builder builder = DoActGambleRs.newBuilder();
//		builder.setGold(seed[2]);
//		handler.sendMsgToPlayer(DoActGambleRs.ext, builder.build());
//	}
//
//	/**
//	 * 充值转盘-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActPayTurntableRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PAY_TURNTABLE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PAY_TURNTABLE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		List<StaticActGamble> gambleList = staticActivityDataMgr.getActGambleList(activityKeyId);
//		if (gambleList == null || gambleList.size() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//
//		for (StaticActGamble e : gambleList) {
//
//			List<Long> statusList = activity.getStatusList();
//			long topup = statusList.get(0).longValue();// 累计充值
//			long count = statusList.get(1).longValue();// 已抽次数
//			int count1 = (int) (topup / e.getTopup() - count);
//
//			GetActPayTurntableRs.Builder builder = GetActPayTurntableRs.newBuilder();
//			builder.setTopup((int) topup);
//			builder.setCount(count1);
//			builder.setPaycount(e.getTopup());
//
//			builder.setTopupGamble(PbHelper.createTopupGamblePb(e));
//			handler.sendMsgToPlayer(GetActPayTurntableRs.ext, builder.build());
//			break;
//		}
//
//	}
//
//	/**
//	 * 充值转盘
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void doActPayTurntableRq(DoActPayTurntableRq req, ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_PAY_TURNTABLE_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_PAY_TURNTABLE_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int activityKeyId = activityBase.getKeyId();
//		List<StaticActGamble> gambleList = staticActivityDataMgr.getActGambleList(activityKeyId);
//		if (gambleList == null || gambleList.size() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_FINISH);
//			return;
//		}
//		StaticActGamble actGamble = gambleList.get(0);
//		List<Long> statusList = activity.getStatusList();
//		long topup = statusList.get(0).longValue();// 累计充值
//		long count = statusList.get(1).longValue();// 已抽次数
//		int count1 = (int) (topup / actGamble.getTopup() - count);
//		if (count1 < 1) {
//			handler.sendErrorMsgToPlayer(GameError.COUNT_NOT_ENOUGH);
//			return;
//		}
//
//		DoActPayTurntableRs.Builder builder = DoActPayTurntableRs.newBuilder();
//		List<List<Integer>> awardList = actGamble.getAwardList();
//		int[] seed = new int[] { 0, 0, 0 };
//		for (List<Integer> e : awardList) {
//			seed[0] += e.get(3);
//		}
//		seed[0] = RandomHelper.randomInSize(seed[0]);
//		for (List<Integer> e : awardList) {
//			seed[1] += e.get(3);
//			if (seed[0] <= seed[1]) {
//				int type = e.get(0);
//				int id = e.get(1);
//				int itemCount = e.get(2);
//				int keyId = playerDataManager.addAward(player, type, id, itemCount, AwardFrom.PAY_TURN_TABLE);
//				builder.addAward(PbHelper.createAwardPb(type, id, itemCount, keyId));
//				break;
//			}
//		}
//		statusList.set(1, count + 1);
//		handler.sendMsgToPlayer(DoActPayTurntableRs.ext, builder.build());
//	}
//
//	/**
//	 * 新春狂欢-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActCarnivalRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_SPRING_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_SPRING_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		int activityKeyId = activityBase.getKeyId();
//
//		List<StaticActAward> condList = staticActivityDataMgr.getActAwardById(activityKeyId);
//		if (condList == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		GetActCarnivalRs.Builder builder = GetActCarnivalRs.newBuilder();
//		// 头像
//		List<Long> statusList = activity.getStatusList();
//
//		for (StaticActAward e : condList) {
//			int sortId = e.getSortId();
//			int state = (int) statusList.get(sortId).longValue();
//			int keyId = e.getKeyId();
//			if (sortId == 0) {
//				if (activity.getStatusMap().containsKey(keyId)) {// 已领取奖励
//					builder.setPortrait(PbHelper.createCondStatePb(state, e, 1));
//				} else {
//					builder.setPortrait(PbHelper.createCondStatePb(state, e, 0));
//				}
//			} else if (sortId == 1) {// 首次充值
//				int currentDay = TimeHelper.getCurrentDay();
//				if (state != currentDay) {
//					builder.setPayFrist(PbHelper.createCondStatePb(0, e, 0));
//				} else {
//					if (activity.getStatusMap().containsKey(keyId)) {
//						builder.setPayFrist(PbHelper.createCondStatePb(1, e, 1));
//					} else {
//						builder.setPayFrist(PbHelper.createCondStatePb(1, e, 0));
//					}
//				}
//			} else if (sortId == 3) {
//				builder.setPortrait(PbHelper.createCondStatePb(state, e, 0));
//			}
//		}
//
//		handler.sendMsgToPlayer(GetActCarnivalRs.ext, builder.build());
//	}
//
//	/**
//	 * 新春狂欢-主页面
//	 * 
//	 * @param handler
//	 */
//	public void getActPray(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_SPRING_ID);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_SPRING_ID);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		int activityKeyId = activityBase.getKeyId();
//
//		List<StaticActAward> condList = staticActivityDataMgr.getActAwardById(activityKeyId);
//		if (condList == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
//			return;
//		}
//		GetActPrayRs.Builder builder = GetActPrayRs.newBuilder();
//		// 头像
//		List<Long> statusList = activity.getStatusList();
//
//		for (StaticActAward e : condList) {
////			int sortId = e.getSortId();
////			int state = (int) statusList.get(sortId).longValue();
////			int keyId = e.getKeyId();
////			if (sortId == 0) {
////				if (activity.getStatusMap().containsKey(keyId)) {// 已领取奖励
////					builder.setPortrait(PbHelper.createCondStatePb(state, e, 1));
////				} else {
////					builder.setPortrait(PbHelper.createCondStatePb(state, e, 0));
////				}
////			} else if (sortId == 1) {// 首次充值
////				int currentDay = TimeHelper.getCurrentDay();
////				if (state != currentDay) {
////					builder.setPayFrist(PbHelper.createCondStatePb(0, e, 0));
////				} else {
////					if (activity.getStatusMap().containsKey(keyId)) {
////						builder.setPayFrist(PbHelper.createCondStatePb(1, e, 1));
////					} else {
////						builder.setPayFrist(PbHelper.createCondStatePb(1, e, 0));
////					}
////				}
////			} else if (sortId == 3) {
////				builder.setPortrait(PbHelper.createCondStatePb(state, e, 0));
////			}
//		}
//
//		handler.sendMsgToPlayer(GetActPrayRs.ext, builder.build());
//	}
//	
//	/**
//	 * 火力全开-军团捐献排名
//	 * 
//	 * @param handler
//	 */
//	public void getActPartyDonateRankRq(ClientHandler handler) {
//		ActivityBase activityBase = staticActivityDataMgr.getActivityById(ActivityConst.ACT_FIRE_SHEET);
//		if (activityBase == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		int step = activityBase.getStep();
//		int activityKeyId = activityBase.getKeyId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//
//		UsualActivityData activityData = activityDataManager.getUsualActivity(ActivityConst.ACT_FIRE_SHEET);
//		if (activityData == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//
//		Activity activity = activityDataManager.getActivityInfo(player, ActivityConst.ACT_FIRE_SHEET);
//		if (activity == null) {
//			handler.sendErrorMsgToPlayer(GameError.ACTIVITY_NOT_OPEN);
//			return;
//		}
//		PartyData partyData = partyDataManager.getPartyByLordId(handler.getRoleId());
//		ActPartyRank partyRank = null;
//		int rank = 0;
//		int partyId = 0;
//
//		GetActPartyDonateRankRs.Builder builder = GetActPartyDonateRankRs.newBuilder();
//
//		LinkedList<ActPartyRank> rankList = activityData.getPartyRanks();
//
//		for (int i = 0; i < rankList.size() && i < ActivityConst.RANK_FIRE_SHEET; i++) {
//			ActPartyRank e = rankList.get(i);
//			int epartyId = e.getPartyId();
//			if (epartyId == partyId) {
//				rank = i + 1;
//			}
//
//			PartyData entity = partyDataManager.getParty(epartyId);
//			if (entity == null) {
//				continue;
//			}
//			String partyName = entity.getPartyName();
//			long fight = entity.getFight();
//
//			builder.addActPartyRank(PbHelper.createPartyRankPb(e, i + 1, partyName, fight));
//		}
//
//		if (partyData != null) {
//			partyId = partyData.getPartyId();
//			Long score = activityData.getPartyScore(partyId);
//			if (score == null) {
//				score = 0L;
//			}
//			partyRank = new ActPartyRank(partyId, 0, score);
//			builder.setParty(PbHelper.createPartyRankPb(partyRank, rank, partyData.getPartyName(), partyData.getFight()));
//		}
//
//		builder.setStatus(0);
//		if (step == ActivityConst.OPEN_STEP) {
//			builder.setOpen(false);
//		} else if (step == ActivityConst.OPEN_AWARD) {
//			builder.setOpen(true);
//			Map<Integer, Integer> statusMap = activity.getStatusMap();
//			if (statusMap.containsKey(ActivityConst.TYPE_DEFAULT)) {
//				builder.setStatus(1);
//			}
//		}
//
//		List<StaticActRank> staticActRankList = staticActivityDataMgr.getActRankList(activityKeyId, ActivityConst.TYPE_DEFAULT);
//		if (staticActRankList != null) {
//			for (StaticActRank e : staticActRankList) {
//				builder.addRankAward(PbHelper.createRankAwardPb(e));
//			}
//		}
//		handler.sendMsgToPlayer(GetActPartyDonateRankRs.ext, builder.build());
//	}
//}
