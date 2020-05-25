package data.bean;

public class Part {
	private int keyId;
	private int partId;
	private int upLv;
	private int refitLv;
	private int pos;
	private boolean locked;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getPartId() {
		return partId;
	}

	public void setPartId(int partId) {
		this.partId = partId;
	}

	public int getUpLv() {
		return upLv;
	}

	public void setUpLv(int upLv) {
		this.upLv = upLv;
	}

	public int getRefitLv() {
		return refitLv;
	}

	public void setRefitLv(int refitLv) {
		this.refitLv = refitLv;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @param keyId
	 * @param partId
	 * @param upLv
	 * @param refitLv
	 * @param pos
	 */
	public Part(int keyId, int partId, int upLv, int refitLv, int pos,boolean locked) {
		super();
		this.keyId = keyId;
		this.partId = partId;
		this.upLv = upLv;
		this.refitLv = refitLv;
		this.pos = pos;
		this.locked = locked;
	}

}
