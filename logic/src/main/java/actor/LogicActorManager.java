package actor;

import timer.ActTimer;

public class LogicActorManager implements IActorManager {
	private static LogicActorManager ourInstance = new LogicActorManager();

	public static LogicActorManager getInstance() {
		return ourInstance;
	}

	private ActTimer logicTimer = null;

	private ActorDispatcher logicActors = null;

	private ActorDispatcher httpActors = null;

	private ActorDispatcher recordActors = null;

	private ActorDispatcher otherActors = null;

	private LogicActorManager() {
	}

	public boolean start() {
		logicTimer = new ActTimer("logic_timer");
		logicTimer.start();
		logicActors = new ActorDispatcher(2, "logic_actor_pool");
		if (!logicActors.start()) {
			return false;
		}
		httpActors = new ActorDispatcher(1, "http_pool");
		if (!httpActors.start()) {
			return false;
		}
		recordActors = new ActorDispatcher(2, "record");
		if (!recordActors.start()) {
			return false;
		}
		otherActors = new ActorDispatcher(1, "other");
		if (!otherActors.start()) {
			return false;
		}

		return true;
	}

	@Override
	public String getStatus() {
		StringBuilder builder = new StringBuilder(256);
		builder.append(recordActors.getActorStatus() + "\n");
		builder.append(logicActors.getActorStatus() + "\n");
		builder.append(httpActors.getActorStatus() + "\n");
		builder.append(otherActors.getActorStatus() + "\n");
		return builder.toString();
	}

	public static ActTimer getTimer() {
		return getInstance().logicTimer;
	}

	public static IActor getLogicActor() {
		return getInstance().logicActors.getActor(1);
	}

	public static IActor getHttpActor(int deskId) {
		return getInstance().httpActors.getActor(deskId);
	}

	public static IActor getOtherActor() {
		return getInstance().otherActors.getActor(1);
	}

}
