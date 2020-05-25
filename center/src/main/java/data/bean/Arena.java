package data.bean;

import data.DataBean;

public class Arena implements DataBean {
	private int rank;
	private long lordId;
	private int score;
	private int count;
	private int lastRank;
	private int winCount;
	private int coldTime;
	private int arenaTime;
	private int awardTime;
	private int buyCount;
	private int fight;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getLastRank() {
		return lastRank;
	}

	public void setLastRank(int lastRank) {
		this.lastRank = lastRank;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getColdTime() {
		return coldTime;
	}

	public void setColdTime(int coldTime) {
		this.coldTime = coldTime;
	}

	public int getArenaTime() {
		return arenaTime;
	}

	public void setArenaTime(int arenaTime) {
		this.arenaTime = arenaTime;
	}

	public int getFight() {
		return fight;
	}

	public void setFight(int fight) {
		this.fight = fight;
	}

	public int getAwardTime() {
		return awardTime;
	}

	public void setAwardTime(int awardTime) {
		this.awardTime = awardTime;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public void setId(long id) {
	}

}
