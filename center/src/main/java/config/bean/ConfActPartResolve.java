package config.bean;

import java.util.List;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2016-2-29 下午4:33:45
 * @declare
 */

public class ConfActPartResolve implements IConfigBean{

	private int resolveId;
	private int activityId;
	private int slug;
	private List<List<Integer>> awardList;
	private List<List<Integer>> resolveList;

	public int getResolveId() {
		return resolveId;
	}

	public void setResolveId(int resolveId) {
		this.resolveId = resolveId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getSlug() {
		return slug;
	}

	public void setSlug(int slug) {
		this.slug = slug;
	}

	public List<List<Integer>> getAwardList() {
		return awardList;
	}

	public void setAwardList(List<List<Integer>> awardList) {
		this.awardList = awardList;
	}

	public List<List<Integer>> getResolveList() {
		return resolveList;
	}

	public void setResolveList(List<List<Integer>> resolveList) {
		this.resolveList = resolveList;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
