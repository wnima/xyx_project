package config.bean;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2016-01-23 上午11:17:36
 * @declare 活动奖励
 */

public class ConfActDestory implements IConfigBean{

	private int destoryId;
	private int activityId;
	private int tankId;
	private int score;

	public int getDestoryId() {
		return destoryId;
	}

	public void setDestoryId(int destoryId) {
		this.destoryId = destoryId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getTankId() {
		return tankId;
	}

	public void setTankId(int tankId) {
		this.tankId = tankId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
