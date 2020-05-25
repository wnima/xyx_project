package service.handler.network.socket;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import network.AbstractHandlers;
import network.handler.ServerHandler;
import packet.CocoPacket;
import protocol.c2s.RequestCode;
import service.GateApp;
import service.handler.agent.AgentManager;
import service.handler.agent.CocoAgent;
import util.NettyUtil;
import util.PBUtilHelper;

public class GateNetHandler extends ServerHandler implements ChannelFutureListener {
	private static Logger logger = LoggerFactory.getLogger(GateNetHandler.class);

	// 快速进入游戏,手机账号密码登录,进入大厅
	private List<RequestCode> loginCode = Arrays.asList(RequestCode.LOGIN,  RequestCode.PING,  RequestCode.HOST);

	public GateNetHandler(AbstractHandlers handlers) {
		super(handlers);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		CocoAgent agent = new CocoAgent(0L, ctx);
		NettyUtil.setAttribute(ctx, CocoAgent.KEY, agent);

		logger.info("channelActive agentId:{}", agent.getSessionId());

		AgentManager.getInst().registerAgent(agent);

		// 维护中
//		if (ConditionManager.getInst().isSuspend()) {
//			agent.writeMessage(ResponseCode.SUSPEND.getValue(), PBHelper.pbInt(ConditionManager.getInst().getSecond()).toByteArray());
//		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
				logger.info("reader time out ==========================");
				ctx.channel().close();
			}
			if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
				logger.info("write time out ==========================");
				ctx.channel().close();
			}
		}
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
		if (msg instanceof CocoPacket == false) {
			logger.warn("the packet is not instance of coco packet ");
			return;
		}
		if (GateApp.getInst().isStop()) {
			logger.warn("server is stop , read nothing!");
			return;
		}

		CocoPacket packet = (CocoPacket) msg;
		CocoAgent agent = NettyUtil.getAttribute(ctx, "agent");
		logger.info("channelRead playerId:{} cmd:{} agentId:{}", agent.getPlayerId(), packet.getReqCode().getValue(), agent.getSessionId());

		// 维护则拦截掉所有的消息包,且提示正在维护
		int reqCode = packet.getReqCode().getValue();
//		if (ConditionManager.getInst().isSuspend() && reqCode != RequestCode.PING.getValue() && reqCode != RequestCode.HOST.getValue()) {
//			logger.info("gate is suspend :{}", ConditionManager.getInst().isSuspend());
//			agent.writeMessage(ResponseCode.SUSPEND.getValue(), PBHelper.pbInt(ConditionManager.getInst().getSecond()).toByteArray());
//			return;
//		}

		if (!agent.isValid() && !loginCode.contains(packet.getReqCode())) {
			logger.info("agent is valid and the req code is not login code:{}", packet.getReqCode());
			ctx.close();
			return;
		}

		packet.setPlayerId(agent.getPlayerId());
		handlers.handPacket(ctx, packet);
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
	}

}
