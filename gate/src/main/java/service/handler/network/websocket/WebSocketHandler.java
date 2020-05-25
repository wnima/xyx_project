package service.handler.network.websocket;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import network.AbstractHandlers;
import network.handler.ServerHandler;
import packet.CocoPacket;
import protocol.c2s.RequestCode;
import service.GateApp;
import service.handler.agent.AgentManager;
import service.handler.agent.CocoAgent;
import util.NettyUtil;
import util.PBUtilHelper;

public class WebSocketHandler extends ServerHandler implements ChannelFutureListener {

	private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

	private List<RequestCode> loginCode = Arrays.asList(RequestCode.LOGIN, RequestCode.QQ_LOGIN, RequestCode.PING, RequestCode.HOST);

	public WebSocketHandler(AbstractHandlers handlers) {
		super(handlers);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		CocoAgent agent = new CocoAgent(0L, ctx);
		NettyUtil.setAttribute(ctx, CocoAgent.KEY, agent);
		logger.info("channelActive agentId:{} ip:{}", agent.getSessionId(), agent.remoteIp());
		AgentManager.getInst().registerAgent(agent);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		CocoAgent agent = NettyUtil.getAttribute(ctx, CocoAgent.KEY);
		logger.info("channelInactive playerId:{} valid:{} sessionId:{} ip:{}", agent.getPlayerId(), agent.isValid(), agent.getSessionId(), agent.remoteIp());
		if (agent.isValid()) {
			GateApp.getInst().getClient().sendRequest(new CocoPacket(RequestCode.CENTER_PLAYER_LOGOUT.getValue(), PBUtilHelper.pbPairStringLong(agent.getSessionId(), agent.getPlayerId()).toByteArray(), agent.getPlayerId()));
		}

		AgentManager.getInst().removeSession(agent.getSessionId(), agent.getPlayerId());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof CocoPacket) {
			CocoPacket packet = (CocoPacket) msg;
			CocoAgent agent = NettyUtil.getAttribute(ctx, "agent");
			logger.info("channelRead playerId:{} cmd:{} agentId:{} ip:{}", agent.getPlayerId(), packet.getReqCode().getValue(), agent.getSessionId(), agent.remoteIp());

			if (!agent.isValid() && !loginCode.contains(packet.getReqCode())) {
				logger.info("agent is valid and the req code is not login code:{}", packet.getReqCode());
				ctx.close();
				return;
			}

			packet.setPlayerId(agent.getPlayerId());
			handlers.handPacket(ctx, packet);
		}
		if (msg instanceof String) {// string 消息的分发
			logger.info("recived string:{}", (String) msg);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
			System.out.println("websocket socket 握手成功。");
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
	}

}
