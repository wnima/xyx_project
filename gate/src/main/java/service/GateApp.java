
package service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import actor.GateActorManager;
import actor.IActor;
import actor.IActorManager;
import actor.ICallback;
import define.AppId;
import http.ResourceManager;
import inject.BeanManager;
import inject.GateBeanModule;
import network.AbstractHandlers;
import network.GateHttpServer;
import service.dispath.MessageFromClient;
import service.dispath.MessageFromServer;
import service.handler.agent.AgentManager;
import service.handler.codec.GateDecoder;
import service.server.GateNetServer;
import service.server.WebSocketServer;
import timer.ActTimer;

@Singleton
public class GateApp extends BaseApp {

	private static Logger logger = LoggerFactory.getLogger(GateApp.class);

	private GateNetServer netServer;
	private WebSocketServer wcServer;

	private int status = 0;
	private String version = "";
	public static int port;
	public static String phone_code = "";
	public static String verify_code = "";
	public static String wechat_id = "";
	public static String wechat_key = "";
	public static String wechat_pay = "";
	public static String pay_balance = "";
	public static String pay_cost = "";
	public static String wxCheckTokenUrl = "";
	public static String qqCheckTokenUrl = "";
	public static String qq_id = "";
	public static String qq_key = "";
	public static String APP_KEY = "";

	public static String tSDKGameId = "";
	public static String tSDKAppId = "";
	public static String tSDKAppKey = "";

	public static String payUrl = "";

	public static GateApp getInst() {
		return BeanManager.getBean(GateApp.class);
	}

	@Override
	protected List<String> getRegistParam() {
		String host = props.getString("domain", "");
		int port = props.getInteger("gate.port", 9091);
		logger.info("init gate app and  app bind the port {}", port);
		List<String> params = new ArrayList<String>();
		params.add(host);
		params.add(port + "");
		return params;
	}

	@Override
	protected void initServer() {
		port = props.getInteger("gate.port", 9091);
		version = props.getString("version");
		payUrl = props.getProperty("payUrl", "");
		wechat_id = props.getString("wechatAppId");
		wechat_key = props.getString("wechatKey");
		wechat_pay = props.getString("wechatPay");
		qq_id = props.getString("qq_id");
		qq_key = props.getString("qq_key");
		APP_KEY = props.getString("app_key");
		tSDKAppKey = props.getString("tSDKAppKey");
		GateActorManager.getInstance().start();

//		netServer = new GateNetServer(port);
//		netServer.setDecoder(new GateDecoder());
//		netServer.start();

//		// 纯HTTP服务器
		GateHttpServer httpServer = new GateHttpServer(props.getInteger("http.port", 8081));
		ResourceManager.getInst().registerAllHttpHandlr("service.handler.network.http");
		httpServer.start();
		logger.info("http server start");

		wcServer = new WebSocketServer(port);
		wcServer.setHandler(new MessageFromClient());
		wcServer.start();
		logger.info("websocket server start");

	}

	@Override
	protected void readDynamicProperties() {
		phone_code = dynamicProps.getString("phone_code");
		verify_code = dynamicProps.getString("verify_code");
		wxCheckTokenUrl = dynamicProps.getString("wx_check_token");
		qqCheckTokenUrl = dynamicProps.getString("qq_check_token");
		pay_balance = dynamicProps.getString("pay_balance");
		pay_cost = dynamicProps.getString("pay_cost");
	}

	@Override
	protected void registerCronTask() {
		GateActorManager.getTimer().register(8000, 8000, () -> AgentManager.getInst().onUpdate(), GateActorManager.getPingActor(), "update");
	}

	@Override
	protected String getServerName() {
		return "gate_server";
	}

	@Override
	protected void afterStart() {
	}

	@Override
	protected AbstractHandlers getClientHandler() {
		return new MessageFromServer();
	}

	public String getVersion() {
		return version;
	}

	@Override
	protected AppId getAppId() {
		return AppId.GATE;
	}

	public static void main(String[] args) {
		BeanManager.init(new GateBeanModule());
		GateApp.getInst().start();
	}

	@Override
	protected IActor getCrontabActor() {
		return GateActorManager.getPingActor();
	}

	@Override
	protected ActTimer getCrontabTimer() {
		return GateActorManager.getTimer();
	}

	@Override
	protected IActorManager getActorManager() {
		return GateActorManager.getInstance();
	}

	@Override
	public void stop() {
		AgentManager.getInst().closeAll();
		System.exit(0);
	}

	@Override
	protected ICallback getSessionCloseCallBack() {
		return (e) -> {
			AgentManager.getInst().closeAll();
		};
	}

	@Override
	protected void stopServer() {
	}
}
