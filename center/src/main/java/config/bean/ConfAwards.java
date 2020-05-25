package config.bean;

import java.util.List;

import config.IConfigBean;

/**
 * 
 * @author ChenKui
 * 
 */
public class ConfAwards implements IConfigBean {
	private int awardId;
	private List<List<Integer>> awardList;
	private int count;
	private int repeat;
	private int weight;
	private int comefrom;
	private int displayCount;
	private List<List<Integer>> displayList;

	public int getAwardId() {
		return awardId;
	}

	public void setAwardId(int awardId) {
		this.awardId = awardId;
	}

	public List<List<Integer>> getAwardList() {
		return awardList;
	}

	public void setAwardList(List<List<Integer>> awardList) {
		this.awardList = awardList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getComefrom() {
		return comefrom;
	}

	public void setComefrom(int comefrom) {
		this.comefrom = comefrom;
	}

	public int getDisplayCount() {
		return displayCount;
	}

	public void setDisplayCount(int displayCount) {
		this.displayCount = displayCount;
	}

	public List<List<Integer>> getDisplayList() {
		return displayList;
	}

	public void setDisplayList(List<List<Integer>> displayList) {
		this.displayList = displayList;
	}

	@Override
	public int getId() {
		return this.awardId;
	}

}
