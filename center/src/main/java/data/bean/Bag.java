package data.bean;

import data.DataBean;

public class Bag implements DataBean {

	private long id;
	private long userId;
	private int propId;
	private int propNum;
	private int state;

	public Bag() {
	}

	public Bag(long userId, int propId, int propNum, int state) {
		this.userId = userId;
		this.propId = propId;
		this.propNum = propNum;
		this.state = state;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getPropId() {
		return propId;
	}

	public void setPropId(int propId) {
		this.propId = propId;
	}

	public int getPropNum() {
		return propNum;
	}

	public void setPropNum(int propNum) {
		this.propNum = propNum;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
