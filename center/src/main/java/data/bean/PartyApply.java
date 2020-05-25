package data.bean;

public class PartyApply {

	private long lordId;
	private int applyDate;

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	public void setApplyDate(int applyDate) {
		this.applyDate = applyDate;
	}

	public int getApplyDate() {
		return applyDate;
	}

	public PartyApply() {
	}

	public PartyApply(long lordId, int today) {
		this.lordId = lordId;
		this.applyDate = today;
	}

}
