package data.bean;

import config.bean.ConfPartyProp;

public class PartyProp {

	private int keyId;
	private int count;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public PartyProp() {
	}

	public PartyProp(ConfPartyProp staticProp) {
		this.keyId = staticProp.getKeyId();
	}
}
