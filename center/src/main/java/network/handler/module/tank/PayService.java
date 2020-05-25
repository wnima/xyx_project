package network.handler.module.tank;
//package com.game.module;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.game.constant.ActivityConst;
//import com.game.constant.AwardFrom;
//import com.game.constant.MailType;
//import com.game.constant.SysChatId;
//import com.game.dataMgr.StaticActivityDataMgr;
//import com.game.dataMgr.StaticVipDataMgr;
//import com.game.domain.Player;
//import com.game.domain.p.Lord;
//import com.game.domain.s.StaticActAward;
//import com.game.manager.ActivityDataManager;
//import com.game.manager.PlayerDataManager;
//import com.game.message.handler.DealType;
//import com.game.message.handler.ServerHandler;
//import com.game.pb.BasePb.Base;
//import com.game.pb.CommonPb.Award;
//import com.game.pb.InnerPb.PayBackRq;
//import com.game.pb.InnerPb.PayConfirmRq;
//import com.game.server.GameServer;
//import com.game.server.ICommand;
//import com.game.util.DateHelper;
//import com.game.util.LogHelper;
//import com.game.util.PbHelper;
//import com.game.util.TimeHelper;
//
///**
// * @ClassName: PayService
// * @Description: TODO
// * @author ZhangJun
// * @date 2015年11月7日 上午11:55:08
// * 
// */
//@Service
//public class PayService {
//	@Autowired
//	private PlayerDataManager playerDataManager;
//
//	@Autowired
//	private StaticVipDataMgr staticVipDataMgr;
//
//	@Autowired
//	private ActivityDataManager activityDataManager;
//
//	@Autowired
//	private StaticActivityDataMgr staticActivityDataMgr;
//
//	@Autowired
//	private WorldService worldService;
//	
//	@Autowired
//	private ChatService chatService;
//
//	public void payBackRq(final PayBackRq req, final ServerHandler handler) {
//		GameServer.getInstance().mainLogicServer.addCommand(new ICommand() {
//			@Override
//			public void action() {
//				// TODO Auto-generated method stub
//
//				long roleId = req.getRoleId();
//				Player player = playerDataManager.getPlayer(roleId);
//				payLogic(req, player);
//			}
//		}, DealType.MAIN);
//
//	}
//
//	public boolean addPayGold(Player target, int topup, int extraGold, String serialId, AwardFrom from) {
//		if (topup <= 0) {
//			return false;
//		}
//
//		boolean upVip = false;
//		Lord lord = target.lord;
//
//		lord.setGold(lord.getGold() + topup + extraGold);
//		lord.setGoldGive(lord.getGoldGive() + topup + extraGold);
//		lord.setTopup(lord.getTopup() + topup);
//		int vip = staticVipDataMgr.calcVip(lord.getTopup());
//		if (vip > lord.getVip()) {
//			lord.setVip(vip);
//			if(vip>0) {
//				chatService.sendWorldChat(chatService.createSysChat(SysChatId.BECOME_VIP, lord.getNick(),""+vip));
//			}
//			upVip = true;
//		}
//
//		LogHelper.logGoldGive(lord, topup + extraGold, from);
//		playerDataManager.synGoldToPlayer(target, topup + extraGold, topup, serialId);
//
//		if (upVip) {
//			worldService.recalcArmyMarch(target);
//		}
//		return true;
//	}
//
//	private void firstPayAward(Player player) {
//		StaticActAward award = staticActivityDataMgr.getActAwardById(ActivityConst.ACT_PAY_FIRST).get(0);
//		List<Award> awards = new ArrayList<>();
//		List<List<Integer>> awardList = award.getAwardList();
//		for (List<Integer> e : awardList) {
//			if (e.size() != 3) {
//				continue;
//			}
//
//			int type = e.get(0);
//			int itemId = e.get(1);
//			int count = e.get(2);
//			awards.add(PbHelper.createAwardPb(type, itemId, count));
//		}
//
//		playerDataManager.sendAttachMail(player, awards, MailType.MOLD_FIRST_PAY, TimeHelper.getCurrentSecond());
//	}
//
//	public boolean payLogic(final PayBackRq req, Player player) {
//		int topup = req.getAmount() * 10;
//		int extraGold;
//		int platNo = req.getPlatNo();
//
//		if (platNo == 94 || platNo == 95) {
//			extraGold = staticVipDataMgr.getExtraGold(topup, true);
//		} else {
//			extraGold = staticVipDataMgr.getExtraGold(topup, false);
//		}
//
//		if (player == null) {
//			return false;
//		}
//
//		int originTopup = player.lord.getTopup();
//		if (originTopup == 0) {
//			extraGold += topup;
//		}
//
//		if (addPayGold(player, topup, extraGold, req.getSerialId(), AwardFrom.PAY)) {
//			playerDataManager.sendNormalMail(player, MailType.MOLD_PAY_DONE, TimeHelper.getCurrentSecond(), String.valueOf(topup + extraGold),
//					String.valueOf(topup), String.valueOf(extraGold));
//
//			if (originTopup == 0) {
//				firstPayAward(player);
//			}
//
//			activityDataManager.updActivity(player, ActivityConst.ACT_RED_GIFT, topup, 0);
//			activityDataManager.updActivity(player, ActivityConst.ACT_PAY_EVERYDAY, topup, 0);
//			activityDataManager.updActivity(player, ActivityConst.ACT_DAY_PAY, topup, 0);
//			activityDataManager.payContinue(player, topup);
//			activityDataManager.payFoison(player, topup);
//			activityDataManager.amyRebate(player, req.getAmount(), null);
//			activityDataManager.reFirstPay(player, topup);
//			activityDataManager.giftPay(player, topup);
//			activityDataManager.payContu4(player, topup);
//			activityDataManager.payEveryday(player, topup);
//			activityDataManager.payVacationland(player, topup);
//			activityDataManager.payGamble(player, topup);
//			activityDataManager.payTrunTable(player, topup);
//
//			PayConfirmRq.Builder builder = PayConfirmRq.newBuilder();
//			builder.setPlatNo(req.getPlatNo());
//			builder.setOrderId(req.getOrderId());
//			builder.setAddGold(topup + extraGold);
//
//			Base.Builder baseBuilder = PbHelper.createRqBase(PayConfirmRq.EXT_FIELD_NUMBER, null, PayConfirmRq.ext, builder.build());
//			GameServer.getInstance().sendMsgToPublic(baseBuilder);
//
//			LogHelper.logPay(player.lord, player.account, req.getServerId(), req.getOrderId(), req.getSerialId(), req.getAmount(),
//					DateHelper.formatDateMiniTime(new Date()));
//			return true;
//		}
//
//		return false;
//	}
//}
