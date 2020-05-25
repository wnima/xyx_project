package bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actor.RobotActorManager;
import io.netty.channel.ChannelHandlerContext;
import logic.RobotManager;
import network.AbstractHandlers;
import packet.CocoPacket;
import service.handler.RobotClientHandler;
import service.handler.robot.RobotClient;
import util.ChannelUtil;

public class Robot {

	private static Logger logger = LoggerFactory.getLogger(Robot.class);
	// 用户信息
	private int id;
	private long playerId;
	private String token = "921875cc-a61c-4bcb-8571-559c891acfed";
	private long coin;
	private String phone;
	private String passwd;
	private long ping;
	private long pingCount;

	private boolean valid;

	// 连接相关信息
	public String host;
	public int port;
	private String device;
	private AbstractHandlers abstractHandlers;
	private ChannelHandlerContext ctx;
	private RobotClient client;

	private List<PokerCard> cards = new ArrayList<PokerCard>();

	public Lock lock = new ReentrantLock();
	public Condition condition = lock.newCondition();
	private CocoPacket response;

	public Robot(String host, int port, int id, String device) {
		this.host = host;
		this.port = port;
		this.id = id;
		this.device = device;
		this.abstractHandlers = new RobotClientHandler();
	}

	public void connect() {
		client = new RobotClient(this);
		client.doConnect();
	}

	public void channelActive(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		ChannelUtil.setId(ctx, this.id);
		RobotManager.getInst().robotActive(ctx, this);
	}

	public void channelInactive(ChannelHandlerContext ctx) {
		// client.doConnect();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getCoin() {
		return coin;
	}

	public void setCoin(long coin) {
		this.coin = coin;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public long getPing() {
		return ping;
	}

	public void setPing(long ping) {
		this.ping = ping;
	}

	public long getPingCount() {
		return pingCount;
	}

	public void setPingCount(long pingCount) {
		this.pingCount = pingCount;
	}

	public AbstractHandlers getAbstractHandlers() {
		return abstractHandlers;
	}

	public void setAbstractHandlers(AbstractHandlers abstractHandlers) {
		this.abstractHandlers = abstractHandlers;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public void sendMsg(CocoPacket packet) {
		this.ctx.writeAndFlush(packet);
	}

	public CocoPacket synSendMsg(CocoPacket packet) {
		System.out.println("synSendMsg ：" + Thread.currentThread().getName());
		this.response = null;
		this.ctx.writeAndFlush(packet);
		try {
			lock.lock();
			System.out.println("synSendMsg 阻塞");
			condition.await(5, TimeUnit.SECONDS);// 这个地方柱塞了
			System.out.println("synSendMsg 疏通");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		System.out.println("synSendMsg 3");
		return response;
	}

	public void onResponse(CocoPacket response) {
		RobotActorManager.getReponseActor().put(() -> {
			System.out.println("onResponse ：" + Thread.currentThread().getName());
			try {
				System.out.println("onResponse 1");
				lock.lock();
				System.out.println("onResponse 2");
				this.response = response;
				condition.signal();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
			return null;
		});
	}

	public void setResponse(CocoPacket response) {
		this.response = response;
	}

	public CocoPacket getResponse() {
		return response;
	}

}
