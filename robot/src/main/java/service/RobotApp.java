package service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import actor.IActor;
import actor.IActorManager;
import actor.ICallback;
import actor.RobotActorManager;
import define.AppId;
import inject.BeanManager;
import inject.RobotBeanModule;
import logic.RobotManager;
import network.AbstractHandlers;
import service.handler.RobotClientHandler;
import service.handler.RobotServerHandler;
import service.handler.agent.AgentManager;
import service.handler.codec.RobotDecoder;
import service.handler.network.RobotNetServer;
import timer.ActTimer;

@Singleton
public class RobotApp extends BaseApp {
	private static Logger logger = LoggerFactory.getLogger(RobotApp.class);

	private RobotNetServer netServer;

	private static RobotApp instance = new RobotApp();

	private RobotApp() {
	}

	public static RobotApp getInst() {
		return instance;
	}

	@Override
	protected List<String> getRegistParam() {
		String host = props.getString("domain", "");
		int port = props.getInteger("robot.port", 9091);
		logger.info(" init robot app and  app bind the port {}", port);
		List<String> params = new ArrayList<String>();
		params.add(host);
		params.add(port + "");
		return params;
	}

	@Override
	protected void initServer() {
		int port = props.getInteger("robot.port", 9091);

		RobotActorManager.getInst().start();
		netServer = new RobotNetServer(port);
		netServer.setDecoder(new RobotDecoder());
		netServer.setHandler(new RobotServerHandler());
		netServer.start();
	}

	@Override
	protected String getServerName() {
		return "robot_server";
	}

	@Override
	protected void afterStart() {
		RobotManager.getInst().init();
	}

	@Override
	protected AbstractHandlers getClientHandler() {
		return new RobotClientHandler();
	}

	@Override
	protected AppId getAppId() {
		return AppId.CLIENT;
	}

	public int getGameGroupId() {
		return 0;
	}

	public static void main(String[] args) {
		BeanManager.init(new RobotBeanModule());
		RobotApp.getInst().start();
	}

	@Override
	protected IActor getCrontabActor() {
		return RobotActorManager.getPingActor();
	}

	@Override
	protected ActTimer getCrontabTimer() {
		return RobotActorManager.getTimer();
	}

	@Override
	protected IActorManager getActorManager() {
		return RobotActorManager.getInst();
	}

	@Override
	public void stop() {
		RobotActorManager.getInst().stopWhenEmpty();
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
