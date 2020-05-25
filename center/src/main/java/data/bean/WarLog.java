package data.bean;

public class WarLog {
	private int keyId;
	private int warTime;
	private int state;
	private int partyCount;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getWarTime() {
		return warTime;
	}

	public void setWarTime(int warTime) {
		this.warTime = warTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getPartyCount() {
		return partyCount;
	}

	public void setPartyCount(int partyCount) {
		this.partyCount = partyCount;
	}

}
