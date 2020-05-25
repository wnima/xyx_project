package config.bean;

import config.IConfigBean;

public class ConfLordLv implements IConfigBean {
	private int lordLv;
	private int needExp;
	private int tankCount;
	private int blessExp;
	private int winPros;

	public int getLordLv() {
		return lordLv;
	}

	public void setLordLv(int lordLv) {
		this.lordLv = lordLv;
	}

	public int getNeedExp() {
		return needExp;
	}

	public void setNeedExp(int needExp) {
		this.needExp = needExp;
	}

	public int getTankCount() {
		return tankCount;
	}

	public void setTankCount(int tankCount) {
		this.tankCount = tankCount;
	}

	public int getBlessExp() {
		return blessExp;
	}

	public void setBlessExp(int blessExp) {
		this.blessExp = blessExp;
	}

	public int getWinPros() {
		return winPros;
	}

	public void setWinPros(int winPros) {
		this.winPros = winPros;
	}

	@Override
	public int getId() {
		return this.lordLv;
	}

}
