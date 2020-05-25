package config.bean;

import config.IConfigBean;

public class ConfScout implements IConfigBean{
	private int lv;
	private int scoutCost;

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public int getScoutCost() {
		return scoutCost;
	}

	public void setScoutCost(int scoutCost) {
		this.scoutCost = scoutCost;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
