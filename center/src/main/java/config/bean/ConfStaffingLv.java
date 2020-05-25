package config.bean;

import config.IConfigBean;

public class ConfStaffingLv implements IConfigBean{
	private int staffingLv;
	private int exp;
	private int totalExp;

	public int getStaffingLv() {
		return staffingLv;
	}

	public void setStaffingLv(int staffingLv) {
		this.staffingLv = staffingLv;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getTotalExp() {
		return totalExp;
	}

	public void setTotalExp(int totalExp) {
		this.totalExp = totalExp;
	}

	@Override
	public int getId() {
		return staffingLv;
	}

}
