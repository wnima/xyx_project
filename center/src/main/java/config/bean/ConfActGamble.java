package config.bean;

import java.util.List;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2016-3-15 上午11:17:36
 * @declare 活动奖励
 */

public class ConfActGamble implements IConfigBean{

	private int gambleId;
	private int activityId;
	private int topup;
	private int price;
	private List<List<Integer>> awardList;

	public int getGambleId() {
		return gambleId;
	}

	public void setGambleId(int gambleId) {
		this.gambleId = gambleId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
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
