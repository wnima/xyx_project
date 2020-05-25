package com.game.bossFight.domain;

import data.DataBean;

public class Boss implements DataBean {
	private int bossCreateTime;
	private int bossLv;
	private int bossHp;
	private int bossWhich;
	private int bossState;
	private long hurt;

	public int getBossCreateTime() {
		return bossCreateTime;
	}

	public void setBossCreateTime(int bossCreateTime) {
		this.bossCreateTime = bossCreateTime;
	}

	public int getBossLv() {
		return bossLv;
	}

	public void setBossLv(int bossLv) {
		this.bossLv = bossLv;
	}

	public int getBossHp() {
		return bossHp;
	}

	public void setBossHp(int bossHp) {
		this.bossHp = bossHp;
	}

	public int getBossWhich() {
		return bossWhich;
	}

	public void setBossWhich(int bossWhich) {
		this.bossWhich = bossWhich;
	}

	public int getBossState() {
		return bossState;
	}

	public void setBossState(int bossState) {
		this.bossState = bossState;
	}

	public long getHurt() {
		return hurt;
	}

	public void setHurt(long hurt) {
		this.hurt = hurt;

	}

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public void setId(long id) {
	}

}
