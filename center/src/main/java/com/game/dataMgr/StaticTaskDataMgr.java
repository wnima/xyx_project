package com.game.dataMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.util.RandomHelper;

import config.bean.ConfTask;
import config.bean.ConfTaskLive;
import define.TaskType;

public class StaticTaskDataMgr extends BaseDataMgr {

	private Map<Integer, ConfTask> majorMap = new HashMap<>();
	private Map<Integer, ConfTask> dayiyMap = new HashMap<>();
	private Map<Integer, ConfTask> liveMap = new HashMap<>();
	private Map<Integer, List<ConfTask>> triggerMap = new HashMap<>();
	private List<ConfTask> dayiyList = new ArrayList<ConfTask>();
	private List<ConfTask> liveList = new ArrayList<ConfTask>();
	private List<ConfTaskLive> taskLiveList = new ArrayList<ConfTaskLive>();

	@Override
	public void init() {
//		List<StaticTask> list = staticDataDao.selectTask();
//		for (StaticTask e : list) {
//			int triggerId = e.getTriggerId();
//			if (e.getType() == TaskType.TYPE_MAIN) {
//				majorMap.put(e.getTaskId(), e);
//				List<StaticTask> ttlist = triggerMap.get(triggerId);
//				if (ttlist == null) {
//					ttlist = new ArrayList<StaticTask>();
//					triggerMap.put(triggerId, ttlist);
//				}
//				ttlist.add(e);
//			} else if (e.getType() == TaskType.TYPE_DAYIY) {
//				dayiyMap.put(e.getTaskId(), e);
//				dayiyList.add(e);
//			} else if (e.getType() == TaskType.TYPE_LIVE) {
//				liveMap.put(e.getTaskId(), e);
//				liveList.add(e);
//			}
//		}
//
//		taskLiveList = staticDataDao.selectLiveTask();
	}

	public Map<Integer, ConfTask> getMajorMap() {
		return majorMap;
	}

	public Map<Integer, ConfTask> getDayiyMap() {
		return dayiyMap;
	}

	public Map<Integer, ConfTask> getLiveMap() {
		return liveMap;
	}

	public ConfTask getTaskById(int taskId) {
		if (majorMap.containsKey(taskId)) {
			return majorMap.get(taskId);
		} else if (dayiyMap.containsKey(taskId)) {
			return dayiyMap.get(taskId);
		} else if (liveMap.containsKey(taskId)) {
			return liveMap.get(taskId);
		}
		return null;
	}

	public List<ConfTask> getInitMajorTask() {
		return triggerMap.get(0);
	}

	public List<ConfTask> getLiveList() {
		return liveList;
	}

//	public List<Integer> getRadomDayiyTask() {// 随机五个任务
//		List<Integer> rs = new ArrayList<Integer>();
//		List<Integer> tempList = new ArrayList<Integer>();
//		List<Integer> probabilityList = new ArrayList<Integer>();
//		int seed = 0;
//		for (ConfTask ee : dayiyList) {
//			tempList.add(ee.getTaskId());
//			probabilityList.add(ee.getProbability());
//			seed += ee.getProbability();
//		}
//		for (int i = 0; i < 5; i++) {
//			int total = 0;
//			int goal = RandomHelper.randomInSize(seed);
//			for (int j = 0; j < probabilityList.size(); j++) {
//				int probability = probabilityList.get(j);
//				total += probability;
//				if (goal <= total) {
//					seed -= probability;
//					rs.add(tempList.remove(j));
//					probabilityList.remove(j);
//					break;
//				}
//			}
//		}
//		return rs;
//	}
//
//	public int getOneDayiyTask() {
//		int seeds[] = { 0, 0 };
//		for (ConfTask ee : dayiyList) {
//			seeds[0] += ee.getProbability();
//		}
//		seeds[0] = RandomHelper.randomInSize(seeds[0]);
//		for (ConfTask ee : dayiyList) {
//			seeds[1] += ee.getProbability();
//			if (seeds[0] <= seeds[1]) {
//				return ee.getTaskId();
//			}
//		}
//		return 0;
//	}
//
//	public List<ConfTask> getTriggerTask(int taskId) {
//		return triggerMap.get(taskId);
//	}
//
//	public ConfTaskLive getTaskLive(int liveAd, int totalLive) {
//		for (ConfTaskLive e : taskLiveList) {
//			if (e.getLive() > liveAd && e.getLive() <= totalLive) {
//				return e;
//			}
//		}
//		return null;
//	}

	public static void main(String[] args) {

	}
}
