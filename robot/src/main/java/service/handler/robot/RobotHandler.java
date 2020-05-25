package service.handler.robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bean.Robot;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import packet.CocoPacket;
import protocol.SynProtocol;

public class RobotHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(RobotHandler.class);

	private Robot robot;

	public RobotHandler(Robot robot) {
		this.robot = robot;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		robot.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelInactive ：{}", Thread.currentThread().getName());
		robot.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof CocoPacket == false) {
			logger.debug(" the message is not  coco packet ");
			return;
		}
		logger.info("channelRead ：{}", Thread.currentThread().getName());
		CocoPacket coco = (CocoPacket) msg;
		// 是否是同步消息
		if (SynProtocol.isSynReq(coco.getRealReqId())) {
			robot.onResponse(coco);
		} else {
			robot.getAbstractHandlers().handPacket(ctx, coco);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(" error : {}", cause);
	}
}
