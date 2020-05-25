package service.handler.agent;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import packet.PacketCreator;

public class CocoAgent {
	private static final Logger logger = LoggerFactory.getLogger(CocoAgent.class);
	public static final String KEY = "agent";
	private long playerId;
	private String openid;
	private int pgNo;
	private ChannelHandlerContext ctx;
	private String sessionId;
	private boolean valid;
	private int channel;
	private String ip;

	public CocoAgent(long playerId, ChannelHandlerContext ctx) {
		this.playerId = playerId;
		this.ctx = ctx;
		this.sessionId = UUID.randomUUID().toString();
		this.valid = false;
	}

	public String remoteIp() {
		if (this.ctx == null) {
			return "";
		}
		if (ip != null) {
			return ip;
		}
		InetSocketAddress address = (InetSocketAddress) (ctx.channel().remoteAddress());
		return address.getHostString();
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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public int getPgNo() {
		return pgNo;
	}

	public void setPgNo(int pgNo) {
		this.pgNo = pgNo;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public void writeMessage(int code, byte[] bytes) {
		ctx.writeAndFlush(PacketCreator.createCocos(code, bytes));
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

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
