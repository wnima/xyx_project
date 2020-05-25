package config.bean;

import config.IConfigBean;

public class ConfStaffingWorld implements IConfigBean{
	private int worldLv;
	private int sumStaffing;
	private int haust;

	public int getSumStaffing() {
		return sumStaffing;
	}

	public void setSumStaffing(int sumStaffing) {
		this.sumStaffing = sumStaffing;
	}

	public int getHaust() {
		return haust;
	}

	public void setHaust(int haust) {
		this.haust = haust;
	}

	public int getWorldLv() {
		return worldLv;
	}

	public void setWorldLv(int worldLv) {
		this.worldLv = worldLv;
	}

	@Override
	public int getId() {
		return worldLv;
	}

}
