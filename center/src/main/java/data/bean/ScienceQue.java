package data.bean;

public class ScienceQue {

	private int keyId;
	private int scienceId;
	private int period;
	private int state;
	private int endTime;
	public int getKeyId() {
		return keyId;
	}
	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}
	public int getScienceId() {
		return scienceId;
	}
	public void setScienceId(int scienceId) {
		this.scienceId = scienceId;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public ScienceQue(){
	}
	
	public ScienceQue(int keyId,int scienceId,int period,int state,int endTime){
		this.keyId= keyId;
		this.scienceId = scienceId;
		this.period = period;
		this.state = state;
		this.endTime = endTime;
	}
	
}
