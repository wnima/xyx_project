package config.bean;

import config.IConfigBean;

/**
 * 繁荣度
 * 
 * @author Administrator
 *
 */
public class ConfLordPros implements IConfigBean {
	private int prosLv;
	private int prosExp;
	private int tankCount;
	private int staffingAdd;

	public int getProsLv() {
		return prosLv;
	}

	public void setProsLv(int prosLv) {
		this.prosLv = prosLv;
	}

	public int getProsExp() {
		return prosExp;
	}

	public void setProsExp(int prosExp) {
		this.prosExp = prosExp;
	}

	public int getTankCount() {
		return tankCount;
	}

	public void setTankCount(int tankCount) {
		this.tankCount = tankCount;
	}

	public int getStaffingAdd() {
		return staffingAdd;
	}

	public void setStaffingAdd(int staffingAdd) {
		this.staffingAdd = staffingAdd;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
