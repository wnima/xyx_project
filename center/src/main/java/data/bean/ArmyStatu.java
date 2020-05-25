package data.bean;

public class ArmyStatu {
	private long lordId;
	private int keyId;
	private int state;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	/**
	 * @param lordId
	 * @param keyId
	 * @param state
	 */
	public ArmyStatu(long lordId, int keyId, int state) {
		super();
		this.lordId = lordId;
		this.keyId = keyId;
		this.state = state;
	}

}
