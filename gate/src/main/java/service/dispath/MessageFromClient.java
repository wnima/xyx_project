package service.dispath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import define.AppId;
import io.netty.channel.ChannelHandlerContext;
import network.AbstractHandlers;
import network.NetClient;
import packet.CocoPacket;
import pb.LoginPb.LoginReq;
import protocol.c2s.RequestCode;
import service.GateApp;
import service.handler.agent.CocoAgent;
import service.handler.module.client.AccountModule;
import util.LogHelper;
import util.NettyUtil;
import util.Pair;

public class MessageFromClient extends AbstractHandlers {

	private static Logger logger = LoggerFactory.getLogger(MessageFromClient.class);

	@Override
	protected void registerAction() {
		AccountModule.getInst().registerModuleHandler(this);
		registerAction(RequestCode.PING.getValue(), this::actionPing, LoginReq.getDefaultInstance());
	}

	private void actionPing(ChannelHandlerContext client, CocoPacket packet, MessageHolder<MessageLite> message) {
		CocoAgent agent = NettyUtil.getAttribute(client, "agent");
		if (agent != null) {
//			agent.writeMessage(ResponseCode.PONG.getValue(), pb.build().toByteArray());
		}
	}

	@Override
	protected AppId getAppId() {
		return AppId.GATE;
	}

	@Override
	public void handPacket(ChannelHandlerContext client, CocoPacket packet) {
		RequestCode reqCode = packet.getReqCode();
		if (reqCode.getSendTo() != getAppId() && reqCode.getSendTo() != AppId.DONE_HERE) {
			getCenterClient().sendRequest(packet);
		} else {
			Pair<MessageLite, IActionHandler> messageAndHandler = actionHandlers.get(packet.getReqId());
			if (messageAndHandler == null) {
				if (reqCode != RequestCode.PING) {
				}
			} else {
				IActionHandler handler = messageAndHandler.getRight();
				MessageLite protoType = messageAndHandler.getLeft();
				if (handler != null) {
					MessageLite message = null;
					try {
						message = protoType == null ? null : protoType.getParserForType().parseFrom(packet.getBytes());
					} catch (InvalidProtocolBufferException e) {
						LogHelper.ERROR.error(e.getMessage(), e);
					}
					handler.doAction(client, packet, new MessageHolder<>(message));
				}
			}
		}
	}

	@Override
	protected NetClient getCenterClient() {
		return GateApp.getInst().getClient();
	}
}
