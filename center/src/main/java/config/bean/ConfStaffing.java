package config.bean;

import java.util.List;

import config.IConfigBean;

public class ConfStaffing implements IConfigBean {
	private int staffingId;
	private String name;
	private int rank;
	private int staffingLv;
	private int countLimit;
	private List<List<Integer>> attr;

	public int getStaffingId() {
		return staffingId;
	}

	public void setStaffingId(int staffingId) {
		this.staffingId = staffingId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getStaffingLv() {
		return staffingLv;
	}

	public void setStaffingLv(int staffingLv) {
		this.staffingLv = staffingLv;
	}

	public int getCountLimit() {
		return countLimit;
	}

	public void setCountLimit(int countLimit) {
		this.countLimit = countLimit;
	}

	public List<List<Integer>> getAttr() {
		return attr;
	}

	public void setAttr(List<List<Integer>> attr) {
		this.attr = attr;
	}

	@Override
	public int getId() {
		return this.staffingId;
	}

}
