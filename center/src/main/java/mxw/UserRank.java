package mxw;

public class UserRank {

	private long userId;
	private int combatId;
	private int starLv;
	private long passTime;
	private int createTime;

	public UserRank(long userId, int starLv) {
		this.userId = userId;
		this.starLv = starLv;
	}

	public UserRank(long userId, int starLv, long passTime, int createTime) {
		this.userId = userId;
		this.starLv = starLv;
		this.passTime = passTime;
		this.createTime = createTime;
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
	public String toString() {
		return userId + "|" + starLv + "|" + passTime + "|" + createTime;
	}

}
