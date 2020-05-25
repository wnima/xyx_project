package data.bean;

public class Effect {
	private int effectId;
	private int endTime;

	public int getEffectId() {
		return effectId;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	/**      
	* @param effectId
	* @param endTime    
	*/
	public Effect(int effectId, int endTime) {
		super();
		this.effectId = effectId;
		this.endTime = endTime;
	}
	
	
}
