package data.bean;

import data.DataBean;

public class LordData implements DataBean {
	private long lordId;
	private byte[] roleData;
	private byte[] mail;
	private int combatId;
	private int equipEplrId;
	private int partEplrId;
	private int extrEplrId;
	private int militaryEplrId;
	private int extrMark;
	private int wipeTime;
	private int timePrlrId;
	private int signLogin;
	private int maxKey;
	private int seniorWeek;
	private int seniorDay;
	private int seniorCount;
	private int seniorScore;
	private int seniorAward;
	private int seniorBuy;

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	public byte[] getRoleData() {
		return roleData;
	}

	public void setRoleData(byte[] roleData) {
		this.roleData = roleData;
	}

	public byte[] getMail() {
		return mail;
	}

	public void setMail(byte[] mail) {
		this.mail = mail;
	}

	public int getCombatId() {
		return combatId;
	}

	public void setCombatId(int combatId) {
		this.combatId = combatId;
	}

	public int getEquipEplrId() {
		return equipEplrId;
	}

	public void setEquipEplrId(int equipEplrId) {
		this.equipEplrId = equipEplrId;
	}

	public int getPartEplrId() {
		return partEplrId;
	}

	public void setPartEplrId(int partEplrId) {
		this.partEplrId = partEplrId;
	}

	public int getExtrEplrId() {
		return extrEplrId;
	}

	public void setExtrEplrId(int extrEplrId) {
		this.extrEplrId = extrEplrId;
	}

	public int getExtrMark() {
		return extrMark;
	}

	public void setExtrMark(int extrMark) {
		this.extrMark = extrMark;
	}

	public int getWipeTime() {
		return wipeTime;
	}

	public void setWipeTime(int wipeTime) {
		this.wipeTime = wipeTime;
	}

	public int getTimePrlrId() {
		return timePrlrId;
	}

	public void setTimePrlrId(int timePrlrId) {
		this.timePrlrId = timePrlrId;
	}

	public int getSignLogin() {
		return signLogin;
	}

	public void setSignLogin(int signLogin) {
		this.signLogin = signLogin;
	}

	public int getMaxKey() {
		return maxKey;
	}

	public void setMaxKey(int maxKey) {
		this.maxKey = maxKey;
	}

	public int getSeniorDay() {
		return seniorDay;
	}

	public void setSeniorDay(int seniorDay) {
		this.seniorDay = seniorDay;
	}

	public int getSeniorCount() {
		return seniorCount;
	}

	public void setSeniorCount(int seniorCount) {
		this.seniorCount = seniorCount;
	}

	public int getSeniorScore() {
		return seniorScore;
	}

	public void setSeniorScore(int seniorScore) {
		this.seniorScore = seniorScore;
	}

	public int getSeniorAward() {
		return seniorAward;
	}

	public void setSeniorAward(int seniorAward) {
		this.seniorAward = seniorAward;
	}

	public int getSeniorBuy() {
		return seniorBuy;
	}

	public void setSeniorBuy(int seniorBuy) {
		this.seniorBuy = seniorBuy;
	}

	public int getSeniorWeek() {
		return seniorWeek;
	}

	public void setSeniorWeek(int seniorWeek) {
		this.seniorWeek = seniorWeek;
	}

	public int getMilitaryEplrId() {
		return militaryEplrId;
	}

	public void setMilitaryEplrId(int militaryEplrId) {
		this.militaryEplrId = militaryEplrId;
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
