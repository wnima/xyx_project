package service.handler.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import packet.CocoPacket;
import packet.PacketCreator;

public class CocoAgent {
	private static final Logger logger = LoggerFactory.getLogger(CocoAgent.class);

	private long playerId;
	private ChannelHandlerContext ctx;
	private String sessionId;
	private boolean verifying;
	private boolean valid;

	public CocoAgent(long playerId, ChannelHandlerContext ctx, String sessionId) {
		this.playerId = playerId;
		this.ctx = ctx;
		this.sessionId = sessionId;
		this.valid = false;
		this.verifying = false;
	}

	public void setVerifying(boolean verifying) {
		this.verifying = verifying;
	}

	public boolean isVerifying() {
		return verifying;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public void writeMessage(int code, byte[] bytes) {
		ctx.writeAndFlush(PacketCreator.create(code, bytes));
	}

	public static void writeMessageEx(ChannelHandlerContext ctx, CocoPacket packet) {
		ctx.writeAndFlush(PacketCreator.create(packet.getReqId(), packet.getBytes()));
	}

	public void closeAgent() {
		logger.info("close agent and player id is {}", playerId);
		ctx.close();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
