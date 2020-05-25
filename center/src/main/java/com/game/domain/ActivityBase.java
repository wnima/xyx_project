package com.game.domain;

import java.util.Calendar;
import java.util.Date;

import config.bean.ConfActivity;
import config.bean.ConfActivityPlan;
import util.DateUtil;

/**
 * @author ChenKui
 * @version 创建时间：2015-12-18 下午2:52:10
 * @declare
 */

public class ActivityBase {
	private Date openTime;// 开服时间

	private ConfActivityPlan plan;// 活动计划

	private ConfActivity staticActivity;// 活动模板

	private Date beginTime;
	private Date endTime;
	private Date displayTime;

	public ConfActivity getStaticActivity() {
		return staticActivity;
	}

	public void setStaticActivity(ConfActivity staticActivity) {
		this.staticActivity = staticActivity;
	}

	public ConfActivityPlan getPlan() {
		return plan;
	}

	public void setPlan(ConfActivityPlan plan) {
		this.plan = plan;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getDisplayTime() {
		return displayTime;
	}

	public void setDisplayTime(Date displayTime) {
		this.displayTime = displayTime;
	}

	public int getActivityId() {
		return this.plan.getActivityId();
	}

	public int getKeyId() {
		return this.plan.getAwardId();
	}

	public int getDayiy() {
		return DateUtil.dayiy(openTime, new Date());
	}

	public boolean initData() {
		int beginDate = plan.getOpenBegin();
		if (beginDate != 0) {
			int openDuration = plan.getOpenDuration();
			if (openDuration == 0) {
				return false;
			}
			int displayDuration = plan.getDisplayDuration();
			Calendar open = Calendar.getInstance();
			open.setTime(openTime);
			open.set(Calendar.HOUR_OF_DAY, 0);
			open.set(Calendar.MINUTE, 0);
			open.set(Calendar.SECOND, 0);
			open.set(Calendar.MILLISECOND, 0);
			long openMillis = open.getTimeInMillis();

			long beginMillis = (beginDate - 1) * 24 * 3600 * 1000L + openMillis;
			this.beginTime = new Date(beginMillis);

			// 等级活动和投资计划时间必须按之前设定的做处理
			int activityId = this.staticActivity.getActivityId();
			if (activityId == 1 || activityId == 15 || activityId == 18) {
				this.beginTime = DateUtil.parseDay("2015-11-17 00:00:00");
			}

			long endMillis = beginMillis + openDuration * 24 * 3600 * 1000L - 1;
			this.endTime = new Date(endMillis);

			if (displayDuration != 0) {
				long displayMillis = endMillis + displayDuration * 24 * 3600 * 1000L;
				this.displayTime = new Date(displayMillis);
			}
			return true;

		} else {
			if (plan.getBeginTime() == null || plan.getEndTime() == null) {
				return false;
			}

			this.beginTime = plan.getBeginTime();
			this.endTime = plan.getEndTime();
			this.displayTime = plan.getDisplayTime();
			return true;
		}
	}

	/**
	 * 活动
	 * 
	 * @param openTime
	 * @return
	 */
	public int getStep() {
		int whole = staticActivity.getWhole();// 0开服,常规,促销。1全服

		int openBegin = plan.getOpenBegin();
		int openDuration = plan.getOpenDuration();
		int displayDuration = plan.getDisplayDuration();

		Date now = new Date();// 当前时间
		int wholeDayiy = DateUtil.dayiy(openTime, now);// 开服多少天

		if (whole == 2 && wholeDayiy < 30) {// 新服小于30天的都不可开启
			return -1;
		}

		if (openBegin > 0) {// 开服计划类活动跟着顺序走
			int dayiy = DateUtil.dayiy(openTime, now);// 开服距当前天数

			int endDate = openBegin + openDuration;
			int displayDate = endDate + displayDuration;

			if (dayiy >= openBegin && dayiy < endDate) {
				return 0;
			}

			if (dayiy >= endDate && dayiy < displayDate) {
				return 1;
			}

		} else {// 常规,促销,全服性质活动
			Date beginTime = plan.getBeginTime();
			Date endTime = plan.getEndTime();
			Date displayTime = plan.getDisplayTime();

			if (beginTime == null || endTime == null) {
				return -1;
			}

			int beginLimit = plan.getServerBegin();// 活动开启限制
			int endLimit = plan.getServerEnd();// 活动结束限制

			if (beginLimit != 0) {// 活动开启限制
				int dayiy = DateUtil.dayiy(openTime, beginTime);// 开服时间距活动开启天数
				if (dayiy < beginLimit) {
					return -1;
				}
			}

			if (endLimit != 0) {// 活动结束限制
				if (displayTime != null) {
					int dayiy = DateUtil.dayiy(openTime, displayTime);// 开服时间距活动显示结束天数
					if (dayiy > endLimit) {
						return -1;
					}
				} else {
					int dayiy = DateUtil.dayiy(openTime, endTime);// 开服时间距活动结束天数
					if (dayiy > endLimit) {
						return -1;
					}
				}
			}

			if (now.after(beginTime) && now.before(endTime)) {
				return 0;
			}

			if (displayTime != null && now.after(endTime) && now.before(displayTime)) {
				return 1;
			}
		}
		return -1;
	}

	/**
	 * 获取活动{1可领奖,0不可领奖,-1活动未开启}
	 * 
	 * @return
	 */
	public int getBaseOpen() {
		int step = getStep();
		if (step == -1) {// 活动未开启
			return step;
		}
		int podium = staticActivity.getPodium();
		if (podium == 0) {// 活动过程中可领奖
			if (step == 0) {
				return 1;
			}
			return step;
		} else if (podium == 1) {// 活动结束后才可领奖
			return step;
		} else if (podium == 2) {// 活动过程和结束后都可领奖
			return 1;
		}
		return 0;
	}
}
