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
import service.LogicApp;
import util.LogHelper;
import util.Pair;

public class LogicHandler extends AbstractHandlers {
	private static Logger logger = LoggerFactory.getLogger(LogicHandler.class);

	@Override
	protected void registerAction() {
	}

	@Override
	public void handPacket(ChannelHandlerContext client, CocoPacket packet) {
		RequestCode reqCode = packet.getReqCode();
		if (reqCode.getSendTo() != getAppId()) {
			getCenterClient().sendRequest(packet);
		} else {
			Pair<MessageLite, IActionHandler> messageAndHandler = actionHandlers.get(packet.getReqId());
			if (messageAndHandler == null) {
				logger.debug(" the mssage hand is null and the req code is {}", reqCode);
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
	protected AppId getAppId() {
		return AppId.LOGIC;
	}

	@Override
	protected NetClient getCenterClient() {
		return LogicApp.getInst().getClient();
	}
}
