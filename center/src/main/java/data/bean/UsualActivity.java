package data.bean;

public class UsualActivity {

	private int activityId;
	private int goal;
	private int sortord;
	private String params;
	private byte[] playerRank;
	private byte[] partyRank;
	private byte[] addtion;
	private int activityTime;
	private int recordTime;

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public int getSortord() {
		return sortord;
	}

	public void setSortord(int sortord) {
		this.sortord = sortord;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public byte[] getPlayerRank() {
		return playerRank;
	}

	public void setPlayerRank(byte[] playerRank) {
		this.playerRank = playerRank;
	}

	public byte[] getPartyRank() {
		return partyRank;
	}

	public void setPartyRank(byte[] partyRank) {
		this.partyRank = partyRank;
	}

	public byte[] getAddtion() {
		return addtion;
	}

	public void setAddtion(byte[] addtion) {
		this.addtion = addtion;
	}

	public int getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(int activityTime) {
		this.activityTime = activityTime;
	}

	public int getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(int recordTime) {
		this.recordTime = recordTime;
	}

}
