package network.handler.module.tank;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.game.domain.Player;
import com.game.util.PbHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfSign;
import config.bean.ConfSignLogin;
import config.provider.ConfSignLoginProvider;
import config.provider.ConfSignProvider;
import data.bean.Account;
import define.AwardFrom;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.PlayerDataManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.ActivityPb.AcceptEveLoginRs;
import pb.ActivityPb.EveLoginRq;
import pb.ActivityPb.EveLoginRs;
import pb.GamePb.GetSignRs;
import pb.GamePb.SignRq;
import pb.GamePb.SignRs;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;

@Singleton
public class SignService implements IModuleMessageHandler {

	public static SignService getInst() {
		return BeanManager.getBean(SignService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	/**
	 * Function:签到值：该值表示已领取标记
	 * 
	 * @param handler
	 */
	public void getSignRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		Account account = player.account;
		if (account == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		GetSignRs.Builder builder = GetSignRs.newBuilder();

		boolean display = false;// 客户端未用到这个字段
		// 当前第几天
		int logins = account.getLoginDays() > 30 ? 30 : account.getLoginDays();

		List<Integer> signs = player.signs;
		int size = signs.size();
		if (size == 0) {
			for (int i = 0; i < 30; i++) {
				signs.add(0);
			}
		}

		builder.setLogins(logins);
		Iterator<Integer> it = signs.iterator();
		while (it.hasNext()) {
			int sign = it.next();
			if (sign != 0) {
				builder.addSigns(1);// 已领取
			} else {
				builder.addSigns(0);// 未领取
				display = true;
			}
		}
		builder.setDisplay(display);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetSignRs, builder.build());
	}

	public void signRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		SignRq req = msg.get();
		int signId = req.getSignId();
		if (signId > 30 || signId < 1) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		Account account = player.account;
		if (account == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		int logins = account.getLoginDays();
		if (signId > logins) {
//			handler.sendErrorMsgToPlayer(GameError.UN_LOGIN);
			return;
		}
		SignRs.Builder builder = SignRs.newBuilder();
		List<Integer> signList = player.signs;
		int status = signList.get(signId - 1);
		if (status != 0) {
//			handler.sendErrorMsgToPlayer(GameError.AWARD_HAD_GOT);
			return;
		}
		ConfSign staticSign = ConfSignProvider.getInst().getConfigById(signId);
		if (staticSign == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}
		signList.set(signId - 1, 1);// 签到领奖
		List<List<Integer>> awardList = staticSign.getAwardList();
		for (List<Integer> e : awardList) {
			int type = e.get(0);
			int id = e.get(1);
			int count = e.get(2);
			int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.SIGN_AWARD);
			builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.SignRs, builder.build());
	}

	public void eveLoginRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		EveLoginRq req = msg.get();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		Account account = player.account;
		if (account == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		EveLoginRs.Builder builder = EveLoginRs.newBuilder();
		boolean display = true;

		List<Integer> signs = player.signs;
		Iterator<Integer> it = signs.iterator();
		while (it.hasNext()) {
			int sign = it.next();
			if (sign == 0) {
				display = false;// 有未领取的奖励
				break;
			}
		}

		int sign = player.getSignLogin();
		int monthAndDay = DateUtil.getMonthAndDay(new Date()) / 100;
		int signDate = sign / 1000000;
		if (signDate == monthAndDay) {
			builder.setAccept(true);
			builder.addLogins((sign % 1000000) / 10000);
			builder.addLogins((sign % 10000) / 100);
			builder.addLogins(sign % 100);
		} else {
			builder.setAccept(false);
			builder.addLogins(0);
			builder.addLogins(0);
			builder.addLogins(0);
		}
		builder.setDisplay(display);

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.EveLoginRs, builder.build());
	}

	int[] p = { 10000, 100, 1 };

	public void acceptEveLoginRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		Account account = player.account;
		if (account == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		boolean display = true;
		List<Integer> signs = player.signs;
		Iterator<Integer> it = signs.iterator();
		while (it.hasNext()) {
			int sign = it.next();
			if (sign == 0) {
				display = false;// 有未领取的奖励
				break;
			}
		}

		if (!display) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}

		int sign = player.getSignLogin();
		int monthAndDay = DateUtil.getMonthAndDay(new Date());
		int signDate = sign / 100000;
		if (signDate == monthAndDay) {
//			handler.sendErrorMsgToPlayer(GameError.HAD_ACCEPT);
			return;
		}
		sign = monthAndDay * 10000;
		AcceptEveLoginRs.Builder builder = AcceptEveLoginRs.newBuilder();
		for (int i = 0; i < 3; i++) {
			ConfSignLogin signLogin = ConfSignLoginProvider.getInst().getSignLoginByGrid(i + 1);
			int loginId = signLogin.getLoginId();
			int type = signLogin.getType();
			int id = signLogin.getItemId();
			int count = signLogin.getCount();
			sign += p[i] * signLogin.getLoginId();
			int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.SIGN_LOGIN);
			builder.addLogins(loginId);
			builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
		}
		player.setSignLogin(sign);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.AcceptEveLoginRs, builder.build());
	}

}
