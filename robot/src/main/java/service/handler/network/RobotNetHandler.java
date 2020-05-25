package service.handler.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import network.AbstractHandlers;
import network.handler.ServerHandler;
import packet.CocoPacket;
import pb.Common;
import protocol.c2s.RequestCode;
import service.RobotApp;
import service.handler.agent.AgentManager;
import service.handler.agent.CocoAgent;
import util.NettyUtil;

public class RobotNetHandler extends ServerHandler implements ChannelFutureListener {
	private static Logger logger = LoggerFactory.getLogger(RobotNetHandler.class);

	public RobotNetHandler(AbstractHandlers handlers) {
		super(handlers);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			logger.info(" user event trigger {}", evt);
			ctx.channel().close();
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channel in active  ============================= {}", ctx.channel().remoteAddress());
		CocoAgent agent = NettyUtil.getAttribute(ctx, "agent");
		if (agent != null) {
			if (AgentManager.getInst().removeAgent(agent.getPlayerId(), agent.getSessionId())) {
				RobotApp.getInst().getClient().sendRequest(new CocoPacket(RequestCode.CENTER_PLAYER_LOGOUT, null, agent.getPlayerId())); // 玩家退出游戏
			}
		}
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if (!future.isSuccess()) {
			CocoAgent agent = NettyUtil.getAttribute(future, "agent");
			if (agent != null) {
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("", cause);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof CocoPacket == false) {
			logger.warn(" the packet is not instance of coco packet ");
			return;
		}
		CocoPacket packet = (CocoPacket) msg;
		if (RequestCode.RESPONSE_TO_CLIIENT_CODE != packet.getReqCode()) {
			Common.PBGameEnterGame request = Common.PBGameEnterGame.parseFrom(packet.getBytes());
			logger.info("the request is {}", request);
		}

	}

}
