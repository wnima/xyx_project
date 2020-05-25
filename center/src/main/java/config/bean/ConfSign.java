package config.bean;

import java.util.List;

import config.IConfigBean;


public class ConfSign implements IConfigBean {
	private int signId;
	private int signDay;
	private List<List<Integer>> awardList;
	private int vip;

	public int getSignId() {
		return signId;
	}

	public void setSignId(int signId) {
		this.signId = signId;
	}

	public int getSignDay() {
		return signDay;
	}

	public void setSignDay(int signDay) {
		this.signDay = signDay;
	}

	public List<List<Integer>> getAwardList() {
		return awardList;
	}

	public void setAwardList(List<List<Integer>> awardList) {
		this.awardList = awardList;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	@Override
	public int getId() {
		return signId;
	}

}
