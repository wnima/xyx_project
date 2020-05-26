package actor;

import timer.ActTimer;

public class CenterActorManager implements IActorManager {

	private static CenterActorManager inst = new CenterActorManager();

	private CenterActorManager() {
	}

	public static CenterActorManager getInst() {
		return inst;
	}

	private ActTimer dbTimer = null;
	private ActTimer logicTimer = null;

	private IActor tokenActor = null;
	private IActor dbCheckActor = null;
	private IActor dbFlushActor = null;
	private IActor lobbyActor = null;
	private IActor loginActor = null;
	private IActor deviceActor = null;
	private IActor rankActor = null;

	private ActorDispatcher dbActors = null;
	private ActorDispatcher mailActors = null;
	private ActorDispatcher phpDbActors = null;
	private ActorDispatcher dbLoadActors = null;
	private ActorDispatcher logicActors = null;
	private ActorDispatcher httpActors = null;

	public boolean stopWhenEmpty() {
		dbTimer.stop();
		logicTimer.stop();
		tokenActor.stopWhenEmpty();
		loginActor.stopWhenEmpty();
		deviceActor.stopWhenEmpty();
		rankActor.stopWhenEmpty();
		dbCheckActor.stopWhenEmpty();
		dbFlushActor.stopWhenEmpty();
		lobbyActor.stopWhenEmpty();
		dbLoadActors.stopWhenEmpty();
		dbActors.stopWhenEmpty();
		mailActors.stopWhenEmpty();
		phpDbActors.stopWhenEmpty();
		logicActors.stopWhenEmpty();
		httpActors.stopWhenEmpty();
		dbCheckActor.waitForStop();
		dbFlushActor.waitForStop();
		phpDbActors.waitForStop();
		dbActors.waitForStop();
		mailActors.waitForStop();
		loginActor.waitForStop();
		deviceActor.waitForStop();
		rankActor.waitForStop();
		dbLoadActors.waitForStop();
		logicActors.waitForStop();
		httpActors.waitForStop();
		lobbyActor.waitForStop();
		return true;
	}

	public boolean waitForStop() {
		return true;
	}

	public boolean start() {
		dbTimer = new ActTimer("db_timer");
		logicTimer = new ActTimer("logic_timer");
		dbTimer.start();
		logicTimer.start();
		tokenActor = new Actor("tokenActor");
		dbCheckActor = new Actor("db_check");
		dbFlushActor = new Actor("db_flush");
		lobbyActor = new Actor("lobby");
		loginActor = new Actor("loginActor");
		deviceActor = new Actor("deviceActor");
		rankActor = new Actor("rankActor");
		if (!tokenActor.start()) {
			return false;
		}
		if (!dbCheckActor.start()) {
			return false;
		}
		if (!dbFlushActor.start()) {
			return false;
		}
		if (!lobbyActor.start()) {
			return false;
		}
		if (!loginActor.start()) {
			return false;
		}
		if (!deviceActor.start()) {
			return false;
		}		
		if (!rankActor.start()) {
			return false;
		}
		dbActors = new ActorDispatcher(8, "db_save");
		if (!dbActors.start()) {
			return false;
		}
		mailActors = new ActorDispatcher(4, "mail_save");
		if (!mailActors.start()) {
			return false;
		}
		phpDbActors = new ActorDispatcher(8, "php_save");
		if (!phpDbActors.start()) {
			return false;
		}
		logicActors = new ActorDispatcher(8, "logic");
		if (!logicActors.start()) {
			return false;
		}
		dbLoadActors = new ActorDispatcher(4, "db_load");
		if (!dbLoadActors.start()) {
			return false;
		}
		httpActors = new ActorDispatcher(2, "http");
		if (!httpActors.start()) {
			return false;
		}

		return true;
	}

	@Override
	public String getStatus() {
		StringBuilder builder = new StringBuilder(256);
		builder.append(dbCheckActor.getThreadName() + "=" + dbCheckActor.getMaxQueueSize() + "\n");
		builder.append(dbFlushActor.getThreadName() + "=" + dbFlushActor.getMaxQueueSize() + "\n");
		builder.append(dbActors.getActorStatus() + "\n");
		builder.append(dbLoadActors.getActorStatus() + "\n");
		builder.append(logicActors.getActorStatus() + "\n");
		return builder.toString();
	}

	public static IActor getLogicActor(int playerId) {
		return getInst().logicActors.getActor(playerId);
	}

	public static ActTimer getLogicTimer() {
		return getInst().logicTimer;
	}

	public static ActTimer getDBTimer() {
		return getInst().dbTimer;
	}

	public static IActor getDbCheckActor() {
		return getInst().dbCheckActor;
	}
	
	public static IActor getDbFlushActor() {
		return getInst().dbFlushActor;
	}

	public static IActor getDbActor(int id) {
		return getInst().dbActors.getActor(id);
	}

	public static IActor getMailActor(int id) {
		return getInst().mailActors.getActor(id);
	}

	public static IActor getPhpDbActor(int id) {
		return getInst().phpDbActors.getActor(id);
	}

	public static IActor getLoadActor(int id) {
		return getInst().dbLoadActors.getActor(id);
	}

	public static IActor getLobbyActor() {
		return getInst().lobbyActor;
	}

	public static IActor getHttpActor(int id) {
		return getInst().httpActors.getActor(id);
	}

	public static IActor getLoginActor() {
		return getInst().loginActor;
	}

	public static IActor getDeviceActor() {
		return getInst().deviceActor;
	}

	public static IActor getTokenActor() {
		return getInst().tokenActor;
	}

	public static IActor getRankActor() {
		return getInst().rankActor;
	}
}
