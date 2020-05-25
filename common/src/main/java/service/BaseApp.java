package service;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actor.IActor;
import actor.IActorManager;
import actor.ICallback;
import database.DBManager;
import define.AppId;
import io.netty.channel.ChannelHandlerContext;
import network.AbstractHandlers;
import network.NetClient;
import packet.CocoPacket;
import protocol.c2s.RequestCode;
import timer.ActTimer;
import util.LogHelper;
import util.MiscUtil;
import util.XProperties;

public abstract class BaseApp implements Service {

	private static final Logger logger = LoggerFactory.getLogger(BaseApp.class);

	protected XProperties props;

	protected XProperties dynamicProps = null;

	protected abstract void initServer();

	protected void afterStart() {
		File stopFile = new File("stop");
		File reloadFile = new File("reload");
		while (true) {
			try {
				Thread.sleep(3 * 1000);
			} catch (Exception e) {
				LogHelper.ERROR.error(e.getMessage(), e);
			}
			checkStop(stopFile);
			checkReloadFile(reloadFile);
		}
	}

	protected void checkReloadFile(File reloadFile) {
		if (reloadFile != null && reloadFile.exists()) {
			try {
				reloadFile.delete();
				reloadDynamicProps();
			} catch (Exception e) {
				LogHelper.ERROR.error(e.getMessage(), e);
			}
		}
	}

	protected void checkStop(File stopFile) {
		if (stopFile != null && stopFile.exists()) {
			try {
				stopFile.delete();
				stopServer();
			} catch (Exception e) {
				LogHelper.ERROR.error(e.getMessage(), e);
			}
		}
	}

	protected abstract void stopServer();

	protected abstract AppId getAppId();

	protected NetClient client;

	protected ScheduledFuture<?> stopFuture;

	private long startTime = System.currentTimeMillis();

	private void initServerProps() {
		props = new XProperties();
		props.load(getConfDir() + "server.properties");
	}

	protected abstract String getServerName();

	public String getConfDir() {
		if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("win")) {
			// return System.getProperty("user.dir") + File.separator +
			// getServerName() + File.separator + "conf" + File.separator;
			return System.getProperty("user.dir") + File.separator + "conf" + File.separator;
		} else {
			return System.getProperty("user.dir") + File.separator + "conf" + File.separator;
		}
	}

	protected void registerCronTask() {
		logger.info("注册定时任务");
		ActTimer timer = getCrontabTimer();
		if (timer != null) {
			IActor actor = getCrontabActor();
			if (actor == null) {
				throw new RuntimeException(" the crontab actor is null ");
			}
			timer.register(60 * 1000, 60 * 1000, this::logMemory, actor, "memory_snapshot");
			if (getAppId() != AppId.CENTER && getAppId() != AppId.CLIENT) {
				timer.register(60 * 1000, 60 * 1000, () -> getClient().sendRequest(new CocoPacket(RequestCode.CENTER_SERVER_PING, null)), actor, "server ping ");
			}
			timer.register(2 * 60 * 1000, 2 * 60 * 1000, this::logDbPollState, actor, "dbPoll_snapshot");
			timer.register(2 * 60 * 1000, 2 * 60 * 1000, this::logActorStatus, actor, "actor_snapshot");
			registerExtraCronTask();
		}
	}

	private void logActorStatus() {
		IActorManager manger = getActorManager();
		if (manger != null) {
			logger.info(manger.getStatus());
		}
	}

	protected void reloadDynamicProps() {
		dynamicProps = new XProperties();
		dynamicProps.load(getConfDir() + "dynamic.properties");
		readDynamicProperties();
	}

	protected void readDynamicProperties() {

	}

	private void logDbPollState() {
		logger.info(DBManager.getDbPoolStatus());
	}

	protected void registerExtraCronTask() {
	}

	protected abstract IActor getCrontabActor();

	protected abstract ActTimer getCrontabTimer();

	protected abstract IActorManager getActorManager();

	protected List<String> getRegistParam() {
		return null;
	}

	protected ICallback getSessionCloseCallBack() {
		return null;
	}

	protected AbstractHandlers getClientHandler() {
		return null;
	}

	public long getStartTime() {
		return startTime;
	}

	public XProperties getProps() {
		return props;
	}

	public XProperties getDynamicProps() {
		return dynamicProps;
	}

	protected void registerToCenterServer() {
		if (getAppId() == AppId.CENTER || getAppId() == AppId.CLIENT) {
			// i'm center and needn't register to any server;
			return;
		}
		String host = props.getString("center.host");
		int port = props.getInteger("center.port", 9090);
		client = new NetClient(host, port);
		client.setAppId(getAppId());
		client.setRegistParam(getRegistParam());
		client.setOnSessionCloseCallBack(getSessionCloseCallBack());
		client.setRequestHandlers(getClientHandler());
		client.startConnect(e -> {

		});
	}

	protected void logMemory() {
		long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		long totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		logger.info("totalMemory : {}M maxMemory:{}M freeMemory:{}M usedMemory:{}M", totalMemory, maxMemory, freeMemory, totalMemory - freeMemory);
		// logger.info("maxMemory : {} MB", maxMemory);
		// logger.info("freeMemory : {} MB", freeMemory);
		// logger.info("usedMemory : {} MB", totalMemory - freeMemory);
	}

	public void start() {
		getActorManager().start();
		initServerProps();
		reloadDynamicProps();
		initServer();
		registerToCenterServer();
		registerCronTask();
		afterStart();
	}

	public NetClient getClient() {
		return client;
	}

	public ChannelHandlerContext getCtx() {
		return client.getCtx();
	}

	public abstract void stop();

	public void beginStop(int endTime) {
		long endTimeMs = endTime * 1000l;

		logger.info("服务器将于{}强制关闭!", MiscUtil.getDateStr_(endTimeMs));

		ActTimer timer = getCrontabTimer();
		if (timer != null) {
			IActor actor = getCrontabActor();
			if (actor == null) {
				throw new RuntimeException(" the crontab actor is null ");
			}
			cancelStopFuture();
			long delay = endTimeMs - System.currentTimeMillis();
			if (delay > 0) {
				logger.info("服务器将于0000{}强制关闭!", MiscUtil.getDateStr_(endTimeMs));
				stopFuture = timer.register(1000, delay, 1, () -> startStop(), actor, "stopServer");
			} else {
				logger.info("服务器将于1111{}强制关闭!", MiscUtil.getDateStr_(endTimeMs));
				startStop();
			}
		}
	}

	public void startStop() {
		logger.info("base stop!startStop");
		cancelStopFuture();
		stop();
	}

	protected void cancelStopFuture() {
		if (stopFuture != null) {
			stopFuture.cancel(true);
			stopFuture = null;
		}
	}

	public boolean isStop() {
		return stopFuture != null;
	}
}
