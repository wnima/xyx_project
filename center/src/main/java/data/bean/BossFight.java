package data.bean;

import data.DataBean;

public class BossFight implements DataBean {
	private long lordId;
	private long hurt;
	private int bless1;
	private int bless2;
	private int bless3;
	private int autoFight;
	private int attackTime;

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	public long getHurt() {
		return hurt;
	}

	public void setHurt(long hurt) {
		this.hurt = hurt;
	}

	public int getBless1() {
		return bless1;
	}

	public void setBless1(int bless1) {
		this.bless1 = bless1;
	}

	public int getBless2() {
		return bless2;
	}

	public void setBless2(int bless2) {
		this.bless2 = bless2;
	}

	public int getBless3() {
		return bless3;
	}

	public void setBless3(int bless3) {
		this.bless3 = bless3;
	}

	public int getAttackTime() {
		return attackTime;
	}

	public void setAttackTime(int attackTime) {
		this.attackTime = attackTime;
	}

	public int getAutoFight() {
		return autoFight;
	}

	public void setAutoFight(int autoFight) {
		this.autoFight = autoFight;
	}

	@Override
	public long getId() {
		return lordId;
	}

	@Override
	public void setId(long id) {
		this.lordId = id;
	}

}
