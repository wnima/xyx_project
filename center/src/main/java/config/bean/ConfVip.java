package config.bean;

import config.IConfigBean;

public class ConfVip implements IConfigBean {
	private int vip;
	private int topup;
	private int buyPower;
	private int buyArena;
	private int buyEquip;
	private int buyPart;
	private int buyMilitary;
	private int waitQue;
	private int buildQue;
	private int resetDaily;

	private int subPros;
	private int armyCount;
	private int wipe;
	private int partProb;
	private int speedBuild;
	private int speedArmy;
	private int speedTank;
	private int speedRefit;
	private int speedScience;
	private int speedCollect;

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getTopup() {
		return topup;
	}

	public void setTopup(int topup) {
		this.topup = topup;
	}

	public int getBuyPower() {
		return buyPower;
	}

	public void setBuyPower(int buyPower) {
		this.buyPower = buyPower;
	}

	public int getBuyArena() {
		return buyArena;
	}

	public void setBuyArena(int buyArena) {
		this.buyArena = buyArena;
	}

	public int getWaitQue() {
		return waitQue;
	}

	public void setWaitQue(int waitQue) {
		this.waitQue = waitQue;
	}

	public int getBuildQue() {
		return buildQue;
	}

	public void setBuildQue(int buildQue) {
		this.buildQue = buildQue;
	}

	public int getResetDaily() {
		return resetDaily;
	}

	public void setResetDaily(int resetDaily) {
		this.resetDaily = resetDaily;
	}

	public int getBuyEquip() {
		return buyEquip;
	}

	public void setBuyEquip(int buyEquip) {
		this.buyEquip = buyEquip;
	}

	public int getBuyPart() {
		return buyPart;
	}

	public void setBuyPart(int buyPart) {
		this.buyPart = buyPart;
	}

	public int getSubPros() {
		return subPros;
	}

	public void setSubPros(int subPros) {
		this.subPros = subPros;
	}

	public int getArmyCount() {
		return armyCount;
	}

	public void setArmyCount(int armyCount) {
		this.armyCount = armyCount;
	}

	public int getWipe() {
		return wipe;
	}

	public void setWipe(int wipe) {
		this.wipe = wipe;
	}

	public int getPartProb() {
		return partProb;
	}

	public void setPartProb(int partProb) {
		this.partProb = partProb;
	}

	public int getSpeedBuild() {
		return speedBuild;
	}

	public void setSpeedBuild(int speedBuild) {
		this.speedBuild = speedBuild;
	}

	public int getSpeedArmy() {
		return speedArmy;
	}

	public void setSpeedArmy(int speedArmy) {
		this.speedArmy = speedArmy;
	}

	public int getSpeedTank() {
		return speedTank;
	}

	public void setSpeedTank(int speedTank) {
		this.speedTank = speedTank;
	}

	public int getSpeedRefit() {
		return speedRefit;
	}

	public void setSpeedRefit(int speedRefit) {
		this.speedRefit = speedRefit;
	}

	public int getSpeedScience() {
		return speedScience;
	}

	public void setSpeedScience(int speedScience) {
		this.speedScience = speedScience;
	}

	public int getSpeedCollect() {
		return speedCollect;
	}

	public void setSpeedCollect(int speedCollect) {
		this.speedCollect = speedCollect;
	}

	public int getBuyMilitary() {
		return buyMilitary;
	}

	public void setBuyMilitary(int buyMilitary) {
		this.buyMilitary = buyMilitary;
	}

	@Override
	public int getId() {
		return vip;
	}

}
