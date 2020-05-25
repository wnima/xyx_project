package config.bean;

import config.IConfigBean;

public class ConfSlot implements IConfigBean{
	private int keyId;
	private int slotA;
	private int slotB;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getSlotA() {
		return slotA;
	}

	public void setSlotA(int slotA) {
		this.slotA = slotA;
	}

	public int getSlotB() {
		return slotB;
	}

	public void setSlotB(int slotB) {
		this.slotB = slotB;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
