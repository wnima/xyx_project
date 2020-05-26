package config.bean;

import config.IConfigBean;

public class ConfChapter implements IConfigBean {

	private int id;
	private int chapterId;
	private int sectionId;
	private int maxGold;
	private int power;
	private int gameTypeId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public int getMaxGold() {
		return maxGold;
	}

	public void setMaxGold(int maxGold) {
		this.maxGold = maxGold;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}
	
	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id");
		sb.append(":");
		sb.append(id);
		sb.append(",");
		sb.append("chapterId");
		sb.append(":");
		sb.append(chapterId);
		sb.append(",");
		sb.append("sectionId");
		sb.append(":");
		sb.append(sectionId);
		sb.append(",");
		sb.append("maxGold");
		sb.append(":");
		sb.append(maxGold);
		sb.append(",");
		sb.append("power");
		sb.append(":");
		sb.append(power);
		sb.append(",");
		sb.append("gameType");
		sb.append(":");
		sb.append(gameTypeId);
		return sb.toString();
	}
}