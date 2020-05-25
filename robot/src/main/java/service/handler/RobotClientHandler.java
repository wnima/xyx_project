package service.handler;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import actor.RobotActorManager;
import bean.PokerCard;
import bean.Robot;
import define.AppId;
import io.netty.channel.ChannelHandlerContext;
import logic.AccountManager;
import logic.ActManager;
import logic.BJManager;
import logic.GameManager;
import logic.RobotManager;
import logic.TgpdManager;
import network.AbstractHandlers;
import network.NetClient;
import packet.CocoPacket;
import pb.AccountPb.PBLoginSucc;
import pb.AccountPb.PBSmsCodeRs;
import pb.ActivityPb.ActivityList;
import pb.ActivityPb.PBActNormal;
import pb.ActivityPb.PBGotReward;
import pb.ActivityPb.PBMonthCard;
import pb.Common.PBGameEnterGameRs;
import pb.HallPb.PBActMManual;
import pb.HallPb.PBBeginSucc;
import pb.MpqzNiuniu.PBMPQZEnterGameRs;
import pb.PBBlackJ.PBBJRoomRs;
import pb.PBBlackJ.PBDiscard;
import pb.PBBlackJ.PBSynCalcResult;
import pb.PBBlackJ.PBSynChip;
import pb.PBBlackJ.PBSynPosOpt;
import pb.PBBlackJ.PBSynPosOptResult;
import pb.PBBlackJ.PBSynSit;
import pb.SlotPb.PBSlotDeskInfo;
import pb.SlotPb.PBSlotResult;
import pb.util.PBInt;
import pb.util.PBPairInt;
import pb.util.PBPairIntString;
import protocol.c2s.RequestCode;
import protocol.s2c.ResponseCode;
import service.RobotApp;
import service.handler.agent.AgentManager;
import service.handler.agent.CocoAgent;
import util.ChannelUtil;
import util.Pair;

public class RobotClientHandler extends AbstractHandlers {
	private static final Logger logger = LoggerFactory.getLogger(RobotClientHandler.class);

	@Override
	protected void registerAction() {
		// registerAction(RequestCode.PING.getValue(), this::actionPing);
		registerAction(ResponseCode.ACCOUNT_LOGIN_SUCC.getValue(), this::pbDeviceLogin, PBLoginSucc.getDefaultInstance());
		registerAction(ResponseCode.ENTER_HALL_RS.getValue(), this::pbBeginSucc, PBBeginSucc.getDefaultInstance());
		registerAction(ResponseCode.ACCOUNT_BIND_PHONE_SUCC.getValue(), this::pbBingPhoneSucc, PBPairIntString.getDefaultInstance());
		registerAction(ResponseCode.ENTER_GAME_ERROR.getValue(), this::pbEnterGame, PBGameEnterGameRs.getDefaultInstance());
		registerAction(ResponseCode.MPQZ_JOIN_GAME_RS.getValue(), this::pbGameInfo, PBMPQZEnterGameRs.getDefaultInstance());
		registerAction(ResponseCode.SUGAR_GAME_RS.getValue(), this::pbEnterTgpdDesk);
		registerAction(ResponseCode.SLOT_GEM_GAME_RS.getValue(), this::pbSlotGameInfo, PBSlotDeskInfo.getDefaultInstance());
		registerAction(ResponseCode.SLOT_GEM_CHIP.getValue(), this::pbSlotResult, PBSlotResult.getDefaultInstance());
		registerAction(ResponseCode.BJ_GAME_ROOM_RS.getValue(), this::bjGameRoomRs, PBBJRoomRs.getDefaultInstance());
		registerAction(ResponseCode.SYN_BJ_START.getValue(), this::bjSynGameStart, PBInt.getDefaultInstance());
		registerAction(ResponseCode.SYN_BJ_CHIP.getValue(), this::bjSynChip, PBSynChip.getDefaultInstance());
		registerAction(ResponseCode.SYN_BJ_DISCARD.getValue(), this::bjDiscard, PBDiscard.getDefaultInstance());
		registerAction(ResponseCode.SYN_BJ_POS_OPT.getValue(), this::bjSynPosOpt, PBSynPosOpt.getDefaultInstance());
		registerAction(ResponseCode.SYN_BJ_POS_OPT_RESULT.getValue(), this::bjSynPosOptResult, PBSynPosOptResult.getDefaultInstance());
		registerAction(ResponseCode.SYN_BJ_CALC_RESULT.getValue(), this::bjSynCalcResult, PBSynCalcResult.getDefaultInstance());
		registerAction(ResponseCode.SYN_BJ_SIT.getValue(), this::bjSynSit, PBSynSit.getDefaultInstance());
		registerAction(ResponseCode.SYN_BJ_UP.getValue(), this::bjSynUp, PBInt.getDefaultInstance());

		// 手机号
		registerAction(ResponseCode.PHONE_REGISTER.getValue(), this::phoneRegister, PBInt.getDefaultInstance());
		registerAction(ResponseCode.SMS_CODE.getValue(), this::smsCode, PBSmsCodeRs.getDefaultInstance());

		// 活动
		registerAction(ResponseCode.ACTIVITY_LIST.getValue(), this::activityList, ActivityList.getDefaultInstance());
		registerAction(ResponseCode.ACTIVITY_MAN_MANUAL.getValue(), this::actManManual, PBActMManual.getDefaultInstance());
		registerAction(ResponseCode.ACTIVITY_SIGN_WEEK.getValue(), this::actSignWeek, PBActNormal.getDefaultInstance());
		registerAction(ResponseCode.ACTIVITY_AWARD.getValue(), this::actAward, PBGotReward.getDefaultInstance());
		registerAction(ResponseCode.ACTIVITY_MONTH_CARD.getValue(), this::actMonthCard, PBMonthCard.getDefaultInstance());
	}

	private void pbDeviceLogin(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			PBLoginSucc res = message.get();
			int playerId = res.getRoleId();
			String token = res.getAccessToken();
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				logger.info("pbDeviceLogin error: roleId:{} token:{}", res.getRoleId(), res.getAccessToken());
				return null;
			}
			robot.setPlayerId(playerId);
			robot.setToken(token);
			robot.setValid(true);
			ChannelUtil.setUserId(ctx, playerId);
			logger.info("pbDeviceLoginSucc: roleId:{} token:{}", res.getRoleId(), res.getAccessToken());
			AccountManager.begin(robot);
			return null;
		});
	}

	private void pbBeginSucc(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			PBBeginSucc res = message.get();
			int state = res.getState();
			if (state != 1) {
				logger.info("pbBeginError: robotId:{} state:{}", id, state);
				return null;
			}

			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}

			robot.setCoin(res.getRole().getGold());
			logger.info("pbBeginSucc: robotId:{} token:{}", id, state);

			// 进入游戏
			// GameManager.enterGame(robot, 37, 371);
//			GameManager.actList(robot);
			ActManager.actGrilLove(robot);
			return null;
		});
	}

	private void pbEnterGame(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			PBGameEnterGameRs res = message.get();
			int state = res.getCode();
			if (state != 200) {
				logger.info("pbEnterGameError : robotId:{} code:{} errorString:{}", id, state, res.getError());
				return null;
			}
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("pbEnterGame: robotId:{} ", id);
			GameManager.gameInfo(robot);
			return null;
		});
	}

	private void pbGameInfo(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			PBMPQZEnterGameRs res = message.get();
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("pbGameInfo: robotId:{} bankId:{} state:{} time:{}", id, res.getBanker(), res.getDeskState(), res.getDeskTime());
			return null;
		});
	}

	private void pbSlotGameInfo(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			PBSlotDeskInfo res = message.get();
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			List<PBPairInt> list = res.getResultList();
			for (int i = 0; i < 3; i++) {
				PBPairInt p1 = list.get(i);
				PBPairInt p2 = list.get(i + 3);
				PBPairInt p3 = list.get(i + 6);
				PBPairInt p4 = list.get(i + 9);
				PBPairInt p5 = list.get(i + 12);
				logger.info("{} | {} | {} | {} | {}", p1.getValue(), p2.getValue(), p3.getValue(), p4.getValue(), p5.getValue());
			}
			GameManager.slotChip(robot);
			return null;
		});
	}

	private void pbSlotResult(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			PBSlotResult res = message.get();
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			// logger.info("slotChip chip:10000 award:{} coin:{} free:{}",
			// res.getBonus(), res.getUsermoney(), res.getFreeSpinsCount());
			// List<PBPairInt> list = res.getResultList();
			// for (int i = 0; i < 3; i++) {
			// PBPairInt p1 = list.get(i);
			// PBPairInt p2 = list.get(i + 3);
			// PBPairInt p3 = list.get(i + 6);
			// PBPairInt p4 = list.get(i + 9);
			// PBPairInt p5 = list.get(i + 12);
			// logger.info("{} | {} | {} | {} | {}", p1.getValue(),
			// p2.getValue(), p3.getValue(), p4.getValue(), p5.getValue());
			// }
			return null;
		});
	}

	private void bjGameRoomRs(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBBJRoomRs res = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("bj room:{}", res);
			BJManager.getInst().bjSit(robot);
			return null;
		});
	}

	private void bjSynGameStart(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBInt res = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("bjSynGameStart robotId:{} second:{}", id, res.getValue());
			BJManager.getInst().bjChip(robot);
			return null;
		});
	}

	private void bjSynChip(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBSynChip res = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("bjSynChip robotId:{} body:{}", id, res);
			return null;
		});
	}

	private void bjDiscard(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBDiscard res = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("bjFristDiscard robotId:{} 可否保险:{} body:{}", id, res.getProtect(), res);
			BJManager.getInst().bjDiscard(robot, res);
			if (!res.getProtect()) {
				return null;
			}

			// 上保险
			Optional<PBPairInt> opt = res.getSafesList().stream().filter(e -> e.getKey() == 1 && e.getValue() == 1).findFirst();
			if (opt.isPresent()) {
				BJManager.getInst().bjSafe(robot);
			}
			return null;
		});
	}

	private void bjSynPosOpt(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBSynPosOpt res = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			logger.info("bjSynPosOpt robotId:{} pos:{} order:{} second:{}", id, res.getPos(), res.getOrder(), res.getSecond());
			if (robot == null) {
				return null;
			}
			if (res.getPos() == 1) {
				BJManager.getInst().addCard(robot, null);
			}
			return null;
		});
	}

	private void bjSynPosOptResult(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBSynPosOptResult rs = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			PokerCard card = null;
			if (rs.hasCard() && rs.getCard() != 0) {
				card = PokerCard.getByValue(rs.getCard());
			}

			String result = "正常";
			if (rs.getOpt() == 1) {
				result = "要牌";
			} else if (rs.getOpt() == 2) {
				result = "分牌";
			} else if (rs.getOpt() == 4) {
				result = "加倍";
			} else if (rs.getOpt() == 3) {
				result = "上保险";
			} else if (rs.getOpt() == 6) {
				result = "停牌";
			} else if (rs.getOpt() == 5) {
				result = "爆牌";
			}

			logger.info("recived bjSynPosOptResult result:{} pos:{} order:{} opt:{} coin:{} card:{}", result, rs.getPos(), rs.getOrder(), rs.getOpt(), rs.getCoin(), card);
			return null;
		});

	}

	private void bjSynCalcResult(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBSynCalcResult res = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("bjSynCalcResult robotId:{} body:{}", id, res);
			return null;
		});
	}

	private void bjSynSit(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBSynSit res = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("bjSynSit robotId:{} body:{}", id, res);
			return null;
		});
	}

	private void bjSynUp(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBInt res = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("bjSynUp robotId:{} body:{}", id, res);
			BJManager.getInst().reset(robot);
			BJManager.getInst().bjSit(robot);
			return null;
		});
	}

	private void phoneRegister(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBInt res = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("phoneRegister robotId:{} body:{}", id, res);
			return null;
		});
	}

	private void smsCode(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBSmsCodeRs res = message.get();
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("phoneRegister robotId:{} body:{}", id, res);
			return null;
		});
	}

	private void activityList(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			GameManager.actMonthCard(robot);
			// GameManager.manManual(robot);
			// GameManager.signWeek(robot);
			return null;
		});
	}

	private void actManManual(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			GameManager.actReward(robot, 2, 2003);
			// GameManager.manExe(robot);
			return null;
		});
	}

	private void actSignWeek(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			GameManager.actReward(robot, 1, 1005);
			return null;
		});
	}

	private void actAward(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			GameManager.manManual(robot);
			return null;
		});
	}

	private void actMonthCard(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		PBMonthCard rp = message.get();

		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			logger.info("actMonthCard: robotId:{} playerId:{}", id, robot.getPlayerId());

			return null;
		});
	}

	private void pbEnterTgpdDesk(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot == null) {
				return null;
			}
			TgpdManager.getInst().playerChip(robot);
			logger.info("pbEnterTgpdDesk: robotId:{} playerId:{}", id, robot.getPlayerId());
			return null;
		});
	}

	private void pbBingPhoneSucc(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		int id = ChannelUtil.getId(ctx);
		RobotActorManager.getLogicActor(id).put(() -> {
			PBPairIntString res = message.get();
			int state = res.getKey();
			if (state != 200) {
				logger.info("pbBingPhoneError: robotId:{} code:{} errorString:{}", id, state, res.getValue());
				return null;
			}
			Robot robot = RobotManager.getInst().getRobot(id);
			if (robot != null) {
				robot.setPhone("15871490051");
				robot.setPasswd("123456");
			}
			logger.info("pbBingPhoneSucc: robotId:{} phone:{}", id, robot.getPhone());
			return null;
		});
	}

	public void actionPlayerEnterDesk(ChannelHandlerContext ioSession, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		CocoAgent agent = AgentManager.getInst().getCocoAgent(packet.getPlayerId());
		logger.info(" client message handler ping =================");
		if (agent == null) {
			logger.warn(" agent is not in this gate ======== ");
			return;
		}
		logger.info("set agent valid player id {}", agent.getPlayerId());
		agent.setValid(true);
	}

	@Override
	protected AppId getAppId() {
		return AppId.GATE;
	}

	@Override
	protected NetClient getCenterClient() {
		return RobotApp.getInst().getClient();
	}

	@Override
	public void handPacket(ChannelHandlerContext client, CocoPacket packet) {
		try {
			RequestCode reqCode = packet.getReqCode();
			Pair<MessageLite, IActionHandler> messageAndHandler = actionHandlers.get(packet.getReqId());
			if (messageAndHandler == null) {
				return;
			}

			IActionHandler handler = messageAndHandler.getRight();
			if (handler == null) {
				return;
			}
			MessageLite protoType = messageAndHandler.getLeft();
			MessageLite message = protoType == null ? null : protoType.getParserForType().parseFrom(packet.getBytes());
			handler.doAction(client, packet, new MessageHolder<>(message));
		} catch (InvalidProtocolBufferException e) {
			logger.error("exception; {}", e);
		}
	}
}
