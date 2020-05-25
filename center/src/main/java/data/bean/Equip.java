package data.bean;

public class Equip {
	private int keyId;
	private int equipId;
	private int lv;
	private int exp;
	private int pos;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	/**
	 * @param keyId
	 * @param equipId
	 * @param lv
	 * @param exp
	 * @param pos
	 */
	public Equip(int keyId, int equipId, int lv, int exp, int pos) {
		super();
		this.keyId = keyId;
		this.equipId = equipId;
		this.lv = lv;
		this.exp = exp;
		this.pos = pos;
	}

}
