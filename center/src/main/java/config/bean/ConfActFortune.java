package config.bean;

import java.util.List;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2015-11-30 上午11:17:36
 * @declare 活动奖励
 */

public class ConfActFortune implements IConfigBean{

	private int fortuneId;
	private int activityId;
	private int count;
	private int price;
	private int point;
	private String displayList;
	private List<List<Integer>> awardList;
	private String desc;

	public int getFortuneId() {
		return fortuneId;
	}

	public void setFortuneId(int fortuneId) {
		this.fortuneId = fortuneId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getDisplayList() {
		return displayList;
	}

	public void setDisplayList(String displayList) {
		this.displayList = displayList;
	}

	public List<List<Integer>> getAwardList() {
		return awardList;
	}

	public void setAwardList(List<List<Integer>> awardList) {
		this.awardList = awardList;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
