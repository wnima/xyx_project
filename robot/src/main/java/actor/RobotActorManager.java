package actor;

import timer.ActTimer;

public class RobotActorManager implements IActorManager {

	private static RobotActorManager ourInstance = new RobotActorManager();

	public static RobotActorManager getInst() {
		return ourInstance;
	}

	private ActTimer robotTimer = null;

	private IActor pingActor = null;

	private IActor synResponseActor = null;

	private ActorDispatcher logicActors = null;

	private RobotActorManager() {
	}

	public boolean stopWhenEmpty() {
		robotTimer.stop();
		pingActor.stop();
		logicActors.stopWhenEmpty();
		logicActors.waitForStop();
		synResponseActor.stopWhenEmpty();
		synResponseActor.waitForStop();
		return true;
	}

	public boolean start() {
		robotTimer = new ActTimer("logic_timer");
		robotTimer.start();
		pingActor = new Actor("ping");
		if (!pingActor.start()) {
			return false;
		}
		synResponseActor = new Actor("response");
		if (!synResponseActor.start()) {
			return false;
		}

		logicActors = new ActorDispatcher(100, "logic");
		if (!logicActors.start()) {
			return false;
		}

		return true;
	}

	@Override
	public String getStatus() {
		StringBuilder builder = new StringBuilder(32);
		builder.append(pingActor.getThreadName() + "=" + pingActor.getMaxQueueSize() + "\n");
		return builder.toString();
	}

	public static ActTimer getTimer() {
		return getInst().robotTimer;
	}

	public static IActor getPingActor() {
		return getInst().pingActor;
	}

	public static IActor getLogicActor(int playerId) {
		return getInst().logicActors.getActor(playerId);
	}

	public static IActor getReponseActor() {
		return getInst().synResponseActor;
	}

}
