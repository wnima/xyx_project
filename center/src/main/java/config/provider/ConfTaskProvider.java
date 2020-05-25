package config.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.game.util.RandomHelper;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfTask;
import config.bean.ConfTaskLive;
import define.TaskType;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfTaskProvider extends BaseProvider<ConfTask> {

	private Map<Integer, ConfTask> majorMap = new HashMap<>();
	private Map<Integer, ConfTask> dayiyMap = new HashMap<>();
	private Map<Integer, ConfTask> liveMap = new HashMap<>();
	Map<Integer, List<ConfTask>> triggerMap = new HashMap<>();
	List<ConfTask> dayiyList = new ArrayList<ConfTask>();
	List<ConfTask> liveList = new ArrayList<ConfTask>();

	public static ConfTaskProvider getInst() {
		return BeanManager.getBean(ConfTaskProvider.class);
	}

	public ConfTaskProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfTask[]> getClassType() {
		return ConfTask[].class;
	}

	@Override
	protected ConfTask convertDBResult2Bean(ASObject o) {
		ConfTask conf = new ConfTask();
		conf.setTaskId(o.getInt("taskId"));
		conf.setType(o.getInt("type"));
		conf.setTypeChild(o.getInt("typeChild"));
		conf.setTriggerId(o.getInt("triggerId"));
		conf.setTaskStar(o.getInt("taskStar"));
		conf.setProbability(o.getInt("probability"));
		conf.setCond(o.getInt("cond"));
		conf.setSchedule(o.getInt("schedule"));
		conf.setFinishCount(o.getInt("finishCount"));
		conf.setExp(o.getInt("exp"));
		conf.setLive(o.getInt("live"));
		conf.setAwardList(JSON.parseObject(o.getString("awardList"), List.class));
		conf.setParam(JSON.parseObject(o.getString("param"), List.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfTask conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			int triggerId = e.getTriggerId();
			if (e.getType() == TaskType.TYPE_MAIN) {
				majorMap.put(e.getTaskId(), e);
				List<ConfTask> ttlist = triggerMap.get(triggerId);
				if (ttlist == null) {
					ttlist = new ArrayList<ConfTask>();
					triggerMap.put(triggerId, ttlist);
				}
				ttlist.add(e);
			} else if (e.getType() == TaskType.TYPE_DAYIY) {
				dayiyMap.put(e.getTaskId(), e);
				dayiyList.add(e);
			} else if (e.getType() == TaskType.TYPE_LIVE) {
				liveMap.put(e.getTaskId(), e);
				liveList.add(e);
			}
		});
	}

	public List<Integer> getRadomDayiyTask() {// 随机五个任务
		List<Integer> rs = new ArrayList<Integer>();
		List<Integer> tempList = new ArrayList<Integer>();
		List<Integer> probabilityList = new ArrayList<Integer>();
		int seed = 0;
		for (ConfTask ee : dayiyList) {
			tempList.add(ee.getTaskId());
			probabilityList.add(ee.getProbability());
			seed += ee.getProbability();
		}
		for (int i = 0; i < 5; i++) {
			int total = 0;
			int goal = RandomHelper.randomInSize(seed);
			for (int j = 0; j < probabilityList.size(); j++) {
				int probability = probabilityList.get(j);
				total += probability;
				if (goal <= total) {
					seed -= probability;
					rs.add(tempList.remove(j));
					probabilityList.remove(j);
					break;
				}
			}
		}
		return rs;
	}

	public int getOneDayiyTask() {
		int seeds[] = { 0, 0 };
		for (ConfTask ee : dayiyList) {
			seeds[0] += ee.getProbability();
		}
		seeds[0] = RandomHelper.randomInSize(seeds[0]);
		for (ConfTask ee : dayiyList) {
			seeds[1] += ee.getProbability();
			if (seeds[0] <= seeds[1]) {
				return ee.getTaskId();
			}
		}
		return 0;
	}

	public List<ConfTask> getTriggerTask(int taskId) {
		return triggerMap.get(taskId);
	}

	public List<ConfTask> getInitMajorTask() {
		return triggerMap.get(0);
	}

	public List<ConfTask> getLiveList() {
		return liveList;
	}
}
