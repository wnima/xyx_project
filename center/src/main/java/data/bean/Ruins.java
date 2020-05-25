package data.bean;

public class Ruins {
	private boolean isRuins;
	private long lordId;
	private String attackerName ="";

	public boolean isRuins() {
		return isRuins;
	}

	public void setRuins(boolean isRuins) {
		this.isRuins = isRuins;
	}

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	public String getAttackerName() {
		return attackerName;
	}

	public void setAttackerName(String attackerName) {
		this.attackerName = attackerName;
	}
	
}
