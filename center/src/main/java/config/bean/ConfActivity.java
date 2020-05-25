package config.bean;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2015-12-18 下午2:47:32
 * @declare
 */

public class ConfActivity implements IConfigBean{

	private int activityId;
	private String name;
	private int clean;
	private int podium;
	private int whole;

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getClean() {
		return clean;
	}

	public void setClean(int clean) {
		this.clean = clean;
	}

	public int getPodium() {
		return podium;
	}

	public void setPodium(int podium) {
		this.podium = podium;
	}

	public int getWhole() {
		return whole;
	}

	public void setWhole(int whole) {
		this.whole = whole;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
