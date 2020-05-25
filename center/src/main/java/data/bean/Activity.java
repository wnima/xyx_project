package data.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.domain.ActivityBase;

import config.bean.ConfActivity;
import define.ActivityConst;
import util.DateUtil;


public class Activity {
	private int activityId;
	private List<Long> statusList;
	private Map<Integer, Integer> statusMap;
	private int beginTime;
	private int endTime;
	private int open;

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public List<Long> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Long> statusList) {
		this.statusList = statusList;
	}

	public Map<Integer, Integer> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<Integer, Integer> statusMap) {
		this.statusMap = statusMap;
	}

	public int getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getOpen() {
		return open;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	public Activity() {
	}

	public Activity(ActivityBase activityBase, int begin) {
		this.activityId = activityBase.getStaticActivity().getActivityId();
		this.beginTime = begin;
		this.statusMap = new HashMap<Integer, Integer>();
	}

	/**
	 * 开启时间不同,则为两次开启活动,重置活动数据
	 * 
	 * @param begin
	 */
	public boolean isReset(int begin) {
		if (this.beginTime == begin) {
			return false;
		}
		this.beginTime = begin;
		this.endTime = begin;
		this.cleanActivity();
		return true;
	}

	/**
	 * 自动清理活动数据
	 * 
	 * @param staticActivity
	 * @param theDay
	 */
	public void autoDayClean(ActivityBase activityBase) {
		ConfActivity sa = activityBase.getStaticActivity();
		if (sa.getClean() == ActivityConst.CLEAN_DAY) {
			int nowDay = DateUtil.getToday();
			if (this.endTime != nowDay) {
				cleanActivity();
				this.endTime = nowDay;
			}
		}
	}

	/**
	 * 清理活动中记录的数据
	 */
	private void cleanActivity() {
		this.statusMap.clear();
		if (this.statusList != null && this.statusList.size() > 0) {
			for (int i = 0; i < this.statusList.size(); i++) {
				this.statusList.set(i, 0L);
			}
		}
	}
}
