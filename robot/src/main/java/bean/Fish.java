package bean;

public class Fish {

	private int id;
	private int fishConfId;
	private int routeId;
	private int fishType;
	private int fishSpeed;
	private long deadTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFishConfId() {
		return fishConfId;
	}

	public void setFishConfId(int fishConfId) {
		this.fishConfId = fishConfId;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getFishType() {
		return fishType;
	}

	public void setFishType(int fishType) {
		this.fishType = fishType;
	}

	public int getFishSpeed() {
		return fishSpeed;
	}

	public void setFishSpeed(int fishSpeed) {
		this.fishSpeed = fishSpeed;
	}

	public long getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

}
