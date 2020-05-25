package data.bean;

public class BuildQue {
	private int keyId;
	private int buildingId;
	private int pos;
	private int period;
	private int endTime;

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	/**      
	* @param keyId
	* @param buildingId
	* @param pos
	* @param period
	* @param endTime    
	*/
	public BuildQue(int keyId, int buildingId, int pos, int period, int endTime) {
		super();
		this.keyId = keyId;
		this.buildingId = buildingId;
		this.pos = pos;
		this.period = period;
		this.endTime = endTime;
	}

	
}
