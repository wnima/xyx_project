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
import pb.LoginPb.LoginRsp;
import pb.ServerInternal.PBInterLoginRsp;
import pb.ServerInternal.PBInterQQLoginRsp;
import pb.util.PBInt;
import protocol.c2s.RequestCode;
import protocol.s2c.ResponseCode;
import service.GateApp;
import service.handler.agent.AgentManager;
import service.handler.agent.CocoAgent;
import util.LogHelper;
import util.NettyUtil;
import util.PBUtilHelper;
import util.Pair;

public class MessageFromServer extends AbstractHandlers {
	private static final Logger logger = LoggerFactory.getLogger(MessageFromServer.class);

	@Override
	protected void registerAction() {
		registerAction(RequestCode.PING.getValue(), this::actionPing);
		registerAction(RequestCode.GATE_REMOVE_SERVER.getValue(), this::actionRemoveServer, PBInt.getDefaultInstance());
		registerAction(RequestCode.INTERNAL_LOGIN_RSP.getValue(), this::actionLoginRsp, PBInterLoginRsp.getDefaultInstance());
		registerAction(RequestCode.INTERNAL_QQ_LOGIN_RSP.getValue(), this::actionQQLoginRsp, PBInterQQLoginRsp.getDefaultInstance());
	}

	/**
	 * 登陆成功
	 * 
	 * @param client
	 * @param packet
	 * @param message
	 */
	private void actionLoginRsp(ChannelHandlerContext client, CocoPacket packet, MessageHolder<MessageLite> message) {
		PBInterLoginRsp rsp = message.get();
		CocoAgent agent = AgentManager.getInst().getCocoAgent(rsp.getSessionId());
		if (agent == null) {// 连接被关闭
			logger.info("actionLoginRsp the session {} is null", rsp.getSessionId());
			return;
		}

		if (rsp.getUserId() != 0) {
			agent.setValid(true);
			agent.setPlayerId(rsp.getUserId());
			AgentManager.getInst().registerAgent(agent);
		}

		LoginRsp.Builder builder = LoginRsp.newBuilder();
		builder.setMsg(rsp.getMsg());
		builder.setUserId(rsp.getUserId());
		builder.setBan(rsp.getBan());
		builder.setBanMsg(rsp.getBanMsg());
		builder.setWhite(rsp.getWhite());

		logger.info("actionLoginRsp playerId:{} agentId:{}", rsp.getUserId(), rsp.getSessionId());
		agent.writeMessage(ResponseCode.LOGIN_RSP.getValue(), builder.build().toByteArray());
	}

	private void actionQQLoginRsp(ChannelHandlerContext client, CocoPacket packet, MessageHolder<MessageLite> message) {
		PBInterQQLoginRsp rsp = message.get();
		CocoAgent agent = AgentManager.getInst().getCocoAgent(rsp.getSessionId());
		if (agent == null) {// 连接被关闭
			logger.info("actionLoginRsp the session {} is null", rsp.getSessionId());
			return;
		}

		if (rsp.getUserId() != 0) {
			agent.setValid(true);
			agent.setPlayerId(rsp.getUserId());
			AgentManager.getInst().registerAgent(agent);
		}

		LoginRsp.Builder builder = LoginRsp.newBuilder();
		builder.setUserId(rsp.getUserId());
		builder.setBan(rsp.getBan());
		builder.setBanMsg(rsp.getBanMsg());
		builder.setWhite(rsp.getWhite());

		logger.info("actionQQLoginRsp playerId:{} agentId:{}", rsp.getUserId(), rsp.getSessionId());
		agent.writeMessage(ResponseCode.LOGIN_RSP.getValue(), builder.build().toByteArray());
	}

	private void actionRemoveServer(ChannelHandlerContext ioSession, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		PBInt req = message.get();
		AgentManager.getInst().closeAll();
		logger.info("game server top and stop time is {} =======", req.getValue());
		System.exit(0);
	}

	private void actionPing(ChannelHandlerContext ioSession, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		CocoAgent agent = NettyUtil.getAttribute(ioSession, "agent");
		if (agent == null) {
			logger.warn("un know agent send ping message ");
			return;
		}
		logger.info("actionPing");
		AgentManager.getInst().writeMessage(new CocoPacket(ResponseCode.PONG.getValue(), PBUtilHelper.pbLong(-1l)));
	}

	@Override
	protected AppId getAppId() {
		return AppId.GATE;
	}

	@Override
	protected NetClient getCenterClient() {
		return GateApp.getInst().getClient();
	}

	@Override
	public void handPacket(ChannelHandlerContext client, CocoPacket packet) {
		RequestCode reqCode = packet.getReqCode();
		if (reqCode == RequestCode.RESPONSE_TO_CLIIENT_CODE) {
			AgentManager.getInst().writeMessage(packet);
		} else {
			if (reqCode.getSendTo() != getAppId() && reqCode.getSendTo() != AppId.DONE_HERE) {
				getCenterClient().sendRequest(packet);
			} else {
				Pair<MessageLite, IActionHandler> messageAndHandler = actionHandlers.get(packet.getReqId());
				if (messageAndHandler == null) {
					AgentManager.getInst().writeMessage(packet);
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
	}
}
