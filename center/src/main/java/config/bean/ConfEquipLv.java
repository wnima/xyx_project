package config.bean;

import config.IConfigBean;

public class ConfEquipLv implements IConfigBean{
	private int quality;
	private int level;
	private int needExp;
	private int giveExp;

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNeedExp() {
		return needExp;
	}

	public void setNeedExp(int needExp) {
		this.needExp = needExp;
	}

	public int getGiveExp() {
		return giveExp;
	}

	public void setGiveExp(int giveExp) {
		this.giveExp = giveExp;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
