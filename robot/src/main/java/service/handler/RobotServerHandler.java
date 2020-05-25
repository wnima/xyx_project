package service.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import define.AppId;
import io.netty.channel.ChannelHandlerContext;
import network.AbstractHandlers;
import network.NetClient;
import packet.CocoPacket;
import protocol.c2s.RequestCode;
import protocol.s2c.ResponseCode;
import service.RobotApp;
import service.handler.agent.CocoAgent;
import util.NettyUtil;
import util.Pair;
import util.ShowInfo;

public class RobotServerHandler extends AbstractHandlers {
	private static Logger logger = LoggerFactory.getLogger(RobotServerHandler.class);

	@Override
	protected void registerAction() {
		registerAction(RequestCode.PING.getValue(), this::actionPing);
		// registerAction(RequestCode.ACCOUNT_LOGIN.getValue(),
		// this::actionAccountLogin, util.PBString.getDefaultInstance());
		// registerAction(RequestCode.ACCOUNT_GET_TOKEN.getValue(),
		// this::actionGetToken, util.PBString.getDefaultInstance());
		// registerAction(RequestCode.ACCOUNT_WX_LOGIN.getValue(),
		// this::actionWxLogin, AccountPb.PBWxLogin.getDefaultInstance());
		// registerAction(RequestCode.ACCOUNT_SMS_LOGIN.getValue(),
		// this::actionSmsLogin, AccountPb.PBSMSLogin.getDefaultInstance());
		// registerAction(RequestCode.ACCOUNT_PHONE_LOGIN.getValue(),
		// this::actionPhonePassLogin,
		// AccountPb.PBPhoneLogin.getDefaultInstance());
	}

	private void actionPing(ChannelHandlerContext client, CocoPacket packet, MessageHolder<MessageLite> message) {
		logger.info(" gate server ping ==============");
		CocoAgent agent = NettyUtil.getAttribute(client, "agent");
		if (agent != null) {
			agent.writeMessage(ResponseCode.PONG.getValue(), null);
		}
	}

	@Override
	protected AppId getAppId() {
		return AppId.CLIENT;
	}

	@Override
	public void handPacket(ChannelHandlerContext client, CocoPacket packet) {
		RequestCode reqCode = packet.getReqCode();
		if (reqCode.getSendTo() != getAppId()) {
			getCenterClient().sendRequest(packet);
		} else {
			Pair<MessageLite, IActionHandler> messageAndHandler = actionHandlers.get(packet.getReqId());
			if (messageAndHandler == null) {
				if (reqCode != RequestCode.PING) {
					ShowInfo.showIsNullToLog("Client", reqCode);
				}
			} else {
				IActionHandler handler = messageAndHandler.getRight();
				MessageLite protoType = messageAndHandler.getLeft();
				if (handler != null) {
					MessageLite message = null;
					try {
						message = protoType == null ? null : protoType.getParserForType().parseFrom(packet.getBytes());
						// LogUtil.msgLogger.info("player {} , request:{},
						// packet {}", new Object[]{packet.getPlayerId(),
						// reqCode, message});
					} catch (InvalidProtocolBufferException e) {
						logger.error("exception; {}", e);
					}
					handler.doAction(client, packet, new MessageHolder<>(message));
				}
			}
		}
	}

	@Override
	protected NetClient getCenterClient() {
		return RobotApp.getInst().getClient();
	}
}
