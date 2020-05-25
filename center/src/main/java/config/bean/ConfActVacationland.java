package config.bean;

import java.util.List;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2015-11-30 上午11:17:36
 * @declare 活动奖励
 */

public class ConfActVacationland implements IConfigBean{

	private int landId;
	private int activityId;
	private int villageId;
	private int topup;
	private int price;
	private int onday;
	private List<List<Integer>> awardList;

	public int getLandId() {
		return landId;
	}

	public void setLandId(int landId) {
		this.landId = landId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getVillageId() {
		return villageId;
	}

	public void setVillageId(int villageId) {
		this.villageId = villageId;
	}

	public int getTopup() {
		return topup;
	}

	public void setTopup(int topup) {
		this.topup = topup;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getOnday() {
		return onday;
	}

	public void setOnday(int onday) {
		this.onday = onday;
	}

	public List<List<Integer>> getAwardList() {
		return awardList;
	}

	public void setAwardList(List<List<Integer>> awardList) {
		this.awardList = awardList;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
