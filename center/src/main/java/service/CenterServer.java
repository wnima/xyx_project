package service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import actor.CenterActorManager;
import actor.IActor;
import actor.IActorManager;
import config.BaseProvider;
import data.DataProvider;
import database.DBManager;
import define.AppId;
import http.ResourceManager;
import inject.BeanManager;
import inject.CenterBeanModule;
import manager.ChapterManager;
import mxw.PlayerManager;
import mxw.RankManager;
import network.NetServer;
import network.NettyHttpServer;
import network.ServerManager;
import network.ServerSession;
import network.codec.MessageDecoder;
import network.codec.MessageEncoder;
import network.handler.CenterMessageHandler;
import packet.CocoPacket;
import protocol.c2s.RequestCode;
import timer.ActTimer;
import util.ClassUtil;
import util.LogHelper;
import util.MiscUtil;
import util.PBUtilHelper;

@Singleton
public class CenterServer extends BaseApp {
	private static Logger logger = LoggerFactory.getLogger(CenterServer.class);

	private boolean stop = false;
	private String verify_code;
	private String ip_service;

	public static CenterServer getInst() {
		return BeanManager.getBean(CenterServer.class);
	}

	private NetServer server;

	private volatile boolean logicRegist = false;

	public boolean isStop() {
		return stop;
	}

	class ShutdownThread extends Thread {
		@Override
		public void run() {
			logger.info("ShutdownThread run!!!");
			startStop();
		}
	}

	@Override
	protected void initServer() {
		initDataBase();
		initStaticConfig();
		initDataProvider();
//		DataManager.getInst().init(200000, 20000);
//		DataManager.getInst().start();
		CenterActorManager.getInst().start();
		int serverPort = props.getInteger("center.port", 9090);
		logger.info(" the server port is {}", serverPort);
		server = new NetServer(serverPort);
		server.setDecoder(new MessageDecoder());
		server.setEncoder(new MessageEncoder());
		server.setHandler(new CenterMessageHandler());
		server.start();

		NettyHttpServer httpServer = new NettyHttpServer(props.getInteger("http.port", 8080));
		ResourceManager.getInst().registerAllHttpHandlr("network.handler.http");
		httpServer.start();

		logger.info("http server start");
		centerInit();
//		while (!logicRegist) {
//			try {
//				Thread.sleep(3000L);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		logger.info("监听关闭 addShutdownHook");
		Runtime.getRuntime().addShutdownHook(new ShutdownThread());
	}

	@Override
	protected void readDynamicProperties() {
		verify_code = dynamicProps.getString("verify_code");
		ip_service = dynamicProps.getString("ip_service");
	}

	private void initDataBase() {
		DBManager.setProps(props);// 游戏数据库
		DBManager.setDefaultDatabase("game_king");
//		DBManager.setConfigDatabase("config");
		try {
			DBManager.touch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void afterStart() {
		File stopFile = new File("stop");
		File reloadFile = new File("reload");
		while (true) {
			try {
				Thread.sleep(3 * 1000);
			} catch (Exception e) {
				// do nothing
			}
			checkStop(stopFile);
			checkReloadFile(reloadFile);
		}
	}

	@Override
	protected void registerExtraCronTask() {
		logger.info("启动定时任务");
		CenterActorManager.getLogicTimer().register(6000, 6000, () -> RankManager.getInst().calc(), CenterActorManager.getDbFlushActor(), "cleanDirty");
	}

	public void initStaticConfig() {
		BaseProvider.app = this;
		List<Class<?>> classList = ClassUtil.findAllClass("config.provider");
		logger.info("the classs list is {}", classList);
		BaseProvider.init(classList);
		BaseProvider.loadAll();
	}

	public void initDataProvider() {
		List<Class<?>> classList = ClassUtil.findAllClass("data.provider");
		logger.info("the classs list is {}", classList);
		DataProvider.init(classList);
		DataProvider.loadAll();
	}

	public void setLogicRegist(boolean logicRegist) {
		this.logicRegist = logicRegist;
	}

	public void sendStopServerRequest(ServerSession e, int endTime) {
		RequestCode req = null;
		switch (e.getAppId()) {
		case LOGIN:
			req = RequestCode.LOGIN_REMOVE_SERVER;
			break;
		case LOGIC:
			req = RequestCode.LOGIC_REMOVE_SERVER;
			break;
		case LOG:
			req = RequestCode.LOG_REMOVE_SERVER;
			break;
		case GATE:
			req = RequestCode.GATE_REMOVE_SERVER;
			break;
		default:
			break;
		}
		if (req == null) {
			return;
		}
		e.sendRequest(new CocoPacket(req, PBUtilHelper.pbInt(endTime)));
	}

	@Override
	protected void stopServer() {
		logger.info("stopServer CenterServer");
		stop = true;
		for (AppId appId : AppId.values()) {
			List<ServerSession> sessionList = ServerManager.getInst().getSessionList(appId);
			if (sessionList != null && sessionList.size() > 0) {// 指定时间后关闭
				sessionList.forEach(e -> {
					sendStopServerRequest(e, MiscUtil.getCurrentSeconds());
				});
			}
		}

		logger.info("CenterServer 等待任务进入队列");
		try {
			DataProvider.flushAll();// 所有数据写到数据库
			Thread.sleep(1000L);
			CenterActorManager.getInst().stopWhenEmpty();
			logger.info("CenterServer exit when actors success ");
		} catch (InterruptedException e) {
			logger.info(e.getMessage(), e);
		}
	}

	@Override
	protected IActor getCrontabActor() {
		return CenterActorManager.getDbCheckActor();
	}

	@Override
	protected ActTimer getCrontabTimer() {
		return CenterActorManager.getDBTimer();
	}

	@Override
	protected IActorManager getActorManager() {
		return CenterActorManager.getInst();
	}

	@Override
	protected String getServerName() {
		return "center_server";
	}

	@Override
	protected AppId getAppId() {
		return AppId.CENTER;
	}

	public static void main(String[] args) {
		BeanManager.init(new CenterBeanModule());
		CenterServer.getInst().start();
	}

	@Override
	public void stop() {
		try {
			stopServer();
		} catch (Exception e) {
			LogHelper.ERROR.error(e.getMessage(), e);
			// logger.error(e.getMessage(), e);
		}
		logger.info("stop center server");
	}

	public void centerInit() {
		PlayerManager.getInst().load();
		ChapterManager.getInst().load();
	}

	public String getVerify_code() {
		return verify_code;
	}

	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}

	public String getIp_service() {
		return ip_service;
	}

	public void setIp_service(String ip_service) {
		this.ip_service = ip_service;
	}

}
