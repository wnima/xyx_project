package data.bean;

import data.DataBean;

public class DbGlobal implements DataBean {
	private int globalId;
	private int maxKey;
	private byte[] mails;
	private int warTime;
	private byte[] warRecord;
	private int warState;
	private byte[] winRank;
	private byte[] getWinRank;
	private int bossTime;
	private int bossLv;
	private int bossWhich;
	private int bossHp;
	private int bossState;
	private byte[] hurtRank;
	private byte[] getHurtRank;
	private String bossKiller;
	private String shop;
	private int shopTime;
	private byte[] scoreRank;
	private byte[] scorePartyRank;
	private int seniorWeek;
	private int seniorState;

	public int getGlobalId() {
		return globalId;
	}

	public void setGlobalId(int globalId) {
		this.globalId = globalId;
	}

	public byte[] getMails() {
		return mails;
	}

	public void setMails(byte[] mails) {
		this.mails = mails;
	}

	public int getMaxKey() {
		return maxKey;
	}

	public void setMaxKey(int maxKey) {
		this.maxKey = maxKey;
	}

	public int getWarTime() {
		return warTime;
	}

	public void setWarTime(int warTime) {
		this.warTime = warTime;
	}

	public byte[] getWarRecord() {
		return warRecord;
	}

	public void setWarRecord(byte[] warRecord) {
		this.warRecord = warRecord;
	}

	public int getWarState() {
		return warState;
	}

	public void setWarState(int warState) {
		this.warState = warState;
	}

	public byte[] getGetWinRank() {
		return getWinRank;
	}

	public void setGetWinRank(byte[] getWinRank) {
		this.getWinRank = getWinRank;
	}

	public byte[] getWinRank() {
		return winRank;
	}

	public void setWinRank(byte[] winRank) {
		this.winRank = winRank;
	}

	public int getBossLv() {
		return bossLv;
	}

	public void setBossLv(int bossLv) {
		this.bossLv = bossLv;
	}

	public int getBossWhich() {
		return bossWhich;
	}

	public void setBossWhich(int bossWhich) {
		this.bossWhich = bossWhich;
	}

	public int getBossHp() {
		return bossHp;
	}

	public void setBossHp(int bossHp) {
		this.bossHp = bossHp;
	}

	public byte[] getHurtRank() {
		return hurtRank;
	}

	public void setHurtRank(byte[] hurtRank) {
		this.hurtRank = hurtRank;
	}

	public byte[] getGetHurtRank() {
		return getHurtRank;
	}

	public void setGetHurtRank(byte[] getHurtRank) {
		this.getHurtRank = getHurtRank;
	}

	public int getBossTime() {
		return bossTime;
	}

	public void setBossTime(int bossTime) {
		this.bossTime = bossTime;
	}

	public int getBossState() {
		return bossState;
	}

	public void setBossState(int bossState) {
		this.bossState = bossState;
	}

	public String getBossKiller() {
		return bossKiller;
	}

	public void setBossKiller(String bossKiller) {
		this.bossKiller = bossKiller;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public int getShopTime() {
		return shopTime;
	}

	public void setShopTime(int shopTime) {
		this.shopTime = shopTime;
	}

	public byte[] getScoreRank() {
		return scoreRank;
	}

	public void setScoreRank(byte[] scoreRank) {
		this.scoreRank = scoreRank;
	}

	public byte[] getScorePartyRank() {
		return scorePartyRank;
	}

	public void setScorePartyRank(byte[] scorePartyRank) {
		this.scorePartyRank = scorePartyRank;
	}

	public int getSeniorWeek() {
		return seniorWeek;
	}

	public void setSeniorWeek(int seniorWeek) {
		this.seniorWeek = seniorWeek;
	}

	public int getSeniorState() {
		return seniorState;
	}

	public void setSeniorState(int seniorState) {
		this.seniorState = seniorState;
	}

	@Override
	public long getId() {
		return globalId;
	}

	@Override
	public void setId(long id) {
		this.globalId = (int) id;
	}
}
