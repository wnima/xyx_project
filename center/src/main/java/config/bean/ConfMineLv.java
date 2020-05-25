package config.bean;

import config.IConfigBean;

public class ConfMineLv implements IConfigBean{
	private int lv;
	private int production;
	private int exp;
	private int staffingExp;

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public int getProduction() {
		return production;
	}

	public void setProduction(int production) {
		this.production = production;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getStaffingExp() {
		return staffingExp;
	}

	public void setStaffingExp(int staffingExp) {
		this.staffingExp = staffingExp;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
