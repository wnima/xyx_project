package data.bean;

import data.DataBean;

public class Chapter implements DataBean {

	private long id;
	private long userId;
	private int combatId;
	private int starLv;
	private long passTime;
	private int createTime;
	private int gameTypeId;
	
	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public Chapter() {
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getCombatId() {
		return combatId;
	}

	public void setCombatId(int combatId) {
		this.combatId = combatId;
	}

	public int getStarLv() {
		return starLv;
	}

	public void setStarLv(int starLv) {
		this.starLv = starLv;
	}

	public long getPassTime() {
		return passTime;
	}

	public void setPassTime(long passTime) {
		this.passTime = passTime;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return starLv + "|" + passTime;
	}

}
