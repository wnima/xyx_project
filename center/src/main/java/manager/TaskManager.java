package manager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.domain.Player;
import com.google.inject.Singleton;

import config.bean.ConfTask;
import config.provider.ConfTaskProvider;
import data.bean.Lord;
import data.bean.Task;
import define.TaskType;
import inject.BeanManager;
import util.DateUtil;

@Singleton
public class TaskManager {

	public static TaskManager getInst() {
		return BeanManager.getBean(TaskManager.class);
	}

	/**
	 * 更新玩家任务
	 * 
	 * @param player
	 * @param cond参见TaskType类
	 * @param schedule增加进度值
	 * @param param
	 */
	public void updTask(Player player, int cond, int schedule, int... param) {
		refreshTask(player);

		// 主线任务
		Iterator<Task> it1 = player.majorTasks.values().iterator();
		while (it1.hasNext()) {
			Task next = it1.next();
			ConfTask stask = ConfTaskProvider.getInst().getConfigById(next.getTaskId());
			if (stask == null || stask.getCond() != cond) {
				continue;
			}
			modifyTaskSchedule(next, stask, schedule, param);
		}

		// 日常任务
		Iterator<Task> it2 = player.dayiyTask.iterator();
		while (it2.hasNext()) {
			Task next = it2.next();
			if (next.getAccept() != 1) {
				continue;
			}
			ConfTask stask = ConfTaskProvider.getInst().getConfigById(next.getTaskId());
			if (stask == null || stask.getCond() != cond) {
				continue;
			}
			modifyTaskSchedule(next, stask, schedule, param);
		}

		// 活跃任务
		Iterator<Task> it3 = player.liveTask.values().iterator();
		while (it3.hasNext()) {
			Task next = it3.next();
			ConfTask stask = ConfTaskProvider.getInst().getConfigById(next.getTaskId());
			if (stask == null || stask.getCond() != cond) {
				continue;
			}

			if (next.getSchedule() >= stask.getSchedule()) {
				continue;
			}

			boolean flag = modifyTaskSchedule(next, stask, schedule, param);
			if (flag && next.getSchedule() >= stask.getSchedule()) {// 整个任务完成之后再加活跃值
				player.lord.setTaskLive(player.lord.getTaskLive() + stask.getLive());
			}
		}
	}

	public void updTask(long lordId, int cond, int schedule, int... param) {
		Player player = PlayerDataManager.getInst().getPlayer(lordId);
		if (player != null) {
			updTask(player, cond, schedule, param);
		}
	}

	/**
	 * 任务按类型单独处理,便于后期修改维护
	 * 
	 * @param task
	 * @param stask
	 * @param schedule
	 * @param param
	 * @return
	 */
	public boolean modifyTaskSchedule(Task task, ConfTask stask, int schedule, int... param) {
		if (task == null) {
			return false;
		}
		if (task.getSchedule() >= stask.getSchedule()) {
			return false;
		}
		int cond = stask.getCond();
		switch (cond) {
		case TaskType.COND_TANK_PRODUCT: {// 生产坦克,炮车,火炮,火箭
			List<Integer> sparam = stask.getParam();
			if (sparam.size() != 1 || param.length != 1) {
				return false;
			}
			int stankId = sparam.get(0);
			int tankId = param[0];
			if (stankId == 0 || tankId == stankId) {
				task.setSchedule(task.getSchedule() + schedule);
				return true;
			}
			break;
		}
		case TaskType.COND_COMBAT: {// 攻打关卡
			List<Integer> sparam = stask.getParam();
			if (sparam.size() != 1 || param.length != 1) {
				return false;
			}
			int scombatId = sparam.get(0);
			int combatId = param[0];
			if (scombatId == 0 || scombatId == combatId) {
				task.setSchedule(task.getSchedule() + schedule);
				return true;
			}
			break;
		}
		case TaskType.COND_ATTACK_PLAYER: {// 攻击玩家
			task.setSchedule(task.getSchedule() + schedule);
			return true;
		}
		case TaskType.COND_ATTACK_COMBAT: {// 攻击关卡
			task.setSchedule(task.getSchedule() + schedule);
			return true;
		}
		case TaskType.COND_WIN_RESOURCE: { // 攻击世界资源胜利
			if (param.length != 1) {
				return false;
			}
			int finishCount = stask.getParam().get(0);
			if (param[0] >= finishCount) {
				task.setSchedule(task.getSchedule() + schedule);
			}
			return true;
		}
		case TaskType.COND_ATTACK_RESOURCE: {// 攻击世界资源
			if (param.length != 1) {
				return false;
			}
			int finishCount = stask.getParam().get(0);
			if (finishCount == 0 || param[0] >= finishCount) {
				task.setSchedule(task.getSchedule() + schedule);
			}
			return true;
		}
		case TaskType.COND_BUILDING_LV_UP:// 任意建筑升级
		case TaskType.COND_EQUIP_LV_UP: // 装备升级
		case TaskType.COND_COST_GOLD: // 消费任意笔金币次数
		case TaskType.COND_SCIENCE_LV_UP: // 科技升级
		case TaskType.COND_ARENA: // 竞技场决斗
		case TaskType.COND_PART_EPR: // 碎片探险
		case TaskType.COND_EXTR_EPR: // 极限探险
		case TaskType.COND_EQUIP_EPR: // 极限探险
		case TaskType.COND_PARTY_COMBAT: // 军团试炼
		case TaskType.COND_LIMIT_COMBAT: // 废墟寻宝(限时副本)
		case TaskType.COND_PARTY_PROP: // 兑换军团道具
		case TaskType.COND_PARTY_DONATE: // 军团捐献
		case TaskType.COND_HERO_UP: // 武将进阶
		case TaskType.COND_HERO_LOTTERY: // 招募武将
		case TaskType.COND_PARTY_BOX: // 军团试炼宝箱
		case TaskType.COND_PARTY_GUARD: // 军团驻军
		case TaskType.COND_UP_EQUIP: // 升级装备
		case TaskType.COND_UP_PART: // 强化配件
			task.setSchedule(task.getSchedule() + schedule);
			return true;
		default:
			break;
		}
		return false;
	}

	public boolean refreshTask(Player player) {
		int today = DateUtil.getToday();
		Lord lord = player.lord;
		if (today != lord.getTaskTime()) {
			lord.setDayiyCount(0);
			lord.setTaskDayiy(0);
			lord.setTaskLive(0);
			lord.setTaskLiveAd(0);
			lord.setTaskTime(today);

			// 重置日常及活跃任务和活跃任务
			refreshTask(player, 1);
			refreshTask(player, 2);
			return true;
		}
		return false;
	}

	public void refreshTask(Player player, int type) {
		if (type == 1) {// 重置日常任务和活跃任务
			List<Integer> staskList = ConfTaskProvider.getInst().getRadomDayiyTask();
			List<Task> dayiyList = player.dayiyTask;
			if (dayiyList.size() == 0) {
				for (Integer taskId : staskList) {
					Task task = new Task(taskId);
					dayiyList.add(task);
				}
			} else {
				int i = 0;
				Iterator<Task> it = dayiyList.iterator();
				while (it.hasNext()) {
					Task next = it.next();
					int taskId = staskList.get(i++);
					next.setTaskId(taskId);
					next.setSchedule(0);
					next.setAccept(0);
					next.setStatus(0);
				}
			}
		} else if (type == 2) {// 日常活跃任务
			Map<Integer, Task> liveTaskMap = player.liveTask;
			if (liveTaskMap.size() == 0) {
				List<ConfTask> liveList = ConfTaskProvider.getInst().getLiveList();
				for (ConfTask ee : liveList) {
					Task task = new Task(ee.getTaskId());
					liveTaskMap.put(ee.getTaskId(), task);
				}
			} else {
				Iterator<Task> it = liveTaskMap.values().iterator();
				while (it.hasNext()) {
					Task task = it.next();
					task.setSchedule(0);
				}
			}
		}
	}

	// public void subKilledTank(Player player, Map<Integer, RptTank> tanks){
	// if (tanks == null || tanks.isEmpty()) {
	// return;
	// }
	//
	// Iterator<RptTank> it = tanks.values().iterator();
	// while (it.hasNext()) {
	// RptTank rptTank = (RptTank) it.next();
	// Tank tank = player.tanks.get(rptTank.getTankId());
	// subTank(tank, rptTank.getCount());
	// }
	// }


}
