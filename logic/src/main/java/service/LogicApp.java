package service;

import java.io.File;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actor.IActor;
import actor.IActorManager;
import actor.LogicActorManager;
import database.DBManager;
import define.AppId;
import inject.BeanManager;
import inject.LogicBeanModule;
import loader.DataProvider;
import network.AbstractHandlers;
import service.handler.LogicHandler;
import timer.ActTimer;
import util.ClassUtil;

public class LogicApp extends BaseApp {

	private static Logger logger = LoggerFactory.getLogger(LogicApp.class);

	private static LogicApp instance = new LogicApp();

	private LogicApp() {

	}

	public static LogicApp getInst() {
		return instance;
	}

	private void initDataBase() {
		try {
			DBManager.touch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void readDynamicProperties() {
	}

	@Override
	protected void initServer() {
		initDataBase();
		DataProvider.app = this;
		DataProvider.init(ClassUtil.findAllClass("config.provider"));
		DataProvider.loadAll();
		LogicActorManager.getInstance().start();
		// initRabbitMQClient();
	}

	@Override
	protected void registerExtraCronTask() {

	}

	@Override
	protected void afterStart() {
		File reloadFile = new File("reload");
		while (true) {
			try {
				Thread.sleep(3 * 1000);
				// Thread.sleep(1 * 1000);
			} catch (Exception e) {
				// do nothing
			}
			checkReloadFile(reloadFile);
		}
	}

	@Override
	protected AbstractHandlers getClientHandler() {
		return new LogicHandler();
	}

	@Override
	protected AppId getAppId() {
		return AppId.LOGIC;
	}

	@Override
	protected String getServerName() {
		return "logic_server";
	}

	public static void main(String[] args) {
		BeanManager.init(new LogicBeanModule());
		LogicApp.getInst().start();
	}

	@Override
	protected IActor getCrontabActor() {
		return LogicActorManager.getLogicActor();
	}

	@Override
	protected ActTimer getCrontabTimer() {
		return LogicActorManager.getTimer();
	}

	@Override
	protected IActorManager getActorManager() {
		return LogicActorManager.getInstance();
	}

	@Override
	protected void stopServer() {
	}

	@Override
	public void stop() {
		logger.info("服务器正在关闭....");
		System.exit(0);
		logger.info("服务器正在成功....");
	}

}
