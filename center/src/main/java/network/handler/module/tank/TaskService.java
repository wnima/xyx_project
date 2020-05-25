package network.handler.module.tank;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.domain.Player;
import com.game.util.PbHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import config.bean.ConfTask;
import config.bean.ConfTaskLive;
import config.bean.ConfVip;
import config.provider.ConfTaskLiveProvider;
import config.provider.ConfTaskProvider;
import config.provider.ConfVipProvider;
import data.bean.Lord;
import data.bean.Task;
import define.AwardFrom;
import define.AwardType;
import define.BuildingId;
import define.GoldCost;
import define.PartyType;
import define.TaskType;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.PlayerDataManager;
import manager.TaskManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.GamePb.AcceptNoTaskRs;
import pb.GamePb.AcceptTaskRq;
import pb.GamePb.AcceptTaskRs;
import pb.GamePb.GetDayiyTaskRs;
import pb.GamePb.GetLiveTaskRs;
import pb.GamePb.GetMajorTaskRs;
import pb.GamePb.RefreshDayiyTaskRs;
import pb.GamePb.TaskAwardRq;
import pb.GamePb.TaskAwardRs;
import pb.GamePb.TaskDaylyResetRs;
import pb.GamePb.TaskLiveAwardRq;
import pb.GamePb.TaskLiveAwardRs;
import protocol.s2c.ResponseCode;
import util.MsgHelper;

@Singleton
public class TaskService implements IModuleMessageHandler{

	public static TaskService getInst() {
		return BeanManager.getBean(TaskService.class);
	}
	
	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	public void getMajorTaskRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}

		// 将主线任务添加到玩家身上
		Map<Integer, Task> taskMap = player.majorTasks;
		if (taskMap.size() == 0) {
			List<ConfTask> list = ConfTaskProvider.getInst().getInitMajorTask();
			for (ConfTask e : list) {
				Task task = new Task(e.getTaskId());
				taskMap.put(e.getTaskId(), task);
			}
		}

		GetMajorTaskRs.Builder builder = GetMajorTaskRs.newBuilder();
		Iterator<Task> it = taskMap.values().iterator();
		while (it.hasNext()) {
			Task task = it.next();
			int taskId = task.getTaskId();
			ConfTask stask = ConfTaskProvider.getInst().getConfigById(taskId);
			if (stask == null) {
				continue;
			}
			currentMajorTask(player, task, stask);
			if (task.getSchedule() >= stask.getSchedule() && task.getStatus() == 0) {
				task.setStatus(1);
			}
			if (task.getSchedule() < stask.getSchedule()) {
				task.setStatus(0);
			}
			builder.addTask(PbHelper.createTaskPb(task));
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetMajorTaskRs, builder.build());
	}

	public void getDayiyTaskRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		TaskManager.getInst().refreshTask(player);
		List<Task> dayiyTaskList = player.dayiyTask;
		GetDayiyTaskRs.Builder builder = GetDayiyTaskRs.newBuilder();
		Iterator<Task> it = dayiyTaskList.iterator();

		while (it.hasNext()) {
			Task task = it.next();
			int taskId = task.getTaskId();
			ConfTask stask = ConfTaskProvider.getInst().getConfigById(taskId);
			if (stask == null) {
				continue;
			}

			if (stask.getType() != TaskType.TYPE_DAYIY) {// 日常
				continue;
			}

			if (task.getAccept() == 1 && task.getSchedule() >= stask.getSchedule() && task.getStatus() == 0) {
				task.setStatus(1);
			}

			if (task.getSchedule() < stask.getSchedule()) {
				task.setStatus(0);
			}
			builder.addTask(PbHelper.createTaskPb(task));
		}
		builder.setTaskDayiy(PbHelper.createTaskDayiyPb(player.lord));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetDayiyTaskRs, builder.build());
	}

	public void taskDaylyReset(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		TaskManager.getInst().refreshTask(player);
		Lord lord = player.lord;
		if (lord.getGold() < 25) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
			return;
		}
		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(lord.getVip());
		if (staticVip == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}
		if (staticVip.getResetDaily() <= lord.getDayiyCount()) {
//			handler.sendErrorMsgToPlayer(GameError.VIP_NOT_ENOUGH);
			return;
		}
		lord.setTaskDayiy(0);
		lord.setDayiyCount(lord.getDayiyCount() + 1);
		PlayerDataManager.getInst().subGold(lord, 25, GoldCost.TASK_RESET_DAYIY);
		TaskDaylyResetRs.Builder builder = TaskDaylyResetRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.TaskDaylyResetRs, builder.build());
	}

	public void refreshDayiyTask(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		TaskManager.getInst().refreshTask(player);
		Lord lord = player.lord;
		if (lord.getGold() < 5) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
			return;
		}
		List<Integer> refreshTaskList = ConfTaskProvider.getInst().getRadomDayiyTask();
		List<Task> dayiyTaskList = player.dayiyTask;
		if (refreshTaskList.size() != 5 || dayiyTaskList.size() != 5) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}
		RefreshDayiyTaskRs.Builder builder = RefreshDayiyTaskRs.newBuilder();
		Iterator<Task> it = dayiyTaskList.iterator();
		int i = 0;
		while (it.hasNext()) {
			Task task = it.next();
			// if (task.getAccept() == 1) {
			// int dayiy = lord.getTaskDayiy();
			// dayiy = dayiy - 1 < 0 ? 0 : dayiy - 1;
			// lord.setTaskDayiy(dayiy);
			// }
			int ntaskId = refreshTaskList.get(i++);
			task.setAccept(0);
			task.setSchedule(0);
			task.setTaskId(ntaskId);
			task.setStatus(0);
			builder.addTask(PbHelper.createTaskPb(task));
		}
		builder.setTaskDayiy(PbHelper.createTaskDayiyPb(lord));
		PlayerDataManager.getInst().subGold(lord, 5, GoldCost.TASK_REFRESH_DAYIY);
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.RefreshDayiyTaskRs, builder.build());
	}

	public void getLiveTask(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		Map<Integer, Task> taskMap = player.liveTask;
		TaskManager.getInst().refreshTask(player);
		GetLiveTaskRs.Builder builder = GetLiveTaskRs.newBuilder();
		Iterator<Task> it = taskMap.values().iterator();
		while (it.hasNext()) {
			Task task = it.next();
			int taskId = task.getTaskId();
			ConfTask stask = ConfTaskProvider.getInst().getConfigById(taskId);
			if (stask == null) {
				continue;
			}
			if (stask.getType() != TaskType.TYPE_LIVE) {// 主线任务
				continue;
			}
			if (task.getSchedule() >= stask.getSchedule()) {
				continue;
			}
			builder.addTask(PbHelper.createTaskPb(task));
		}
		builder.setTaskLive(PbHelper.createTaskLivePb(player.lord));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetLiveTaskRs, builder.build());
	}

	public void taskAwardRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		TaskAwardRq req = msg.get();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		Lord lord = player.lord;
		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		int taskId = req.getTaskId();
		int awardType = req.getAwardType();
		if (awardType != 1 && awardType != 2) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
			return;
		}
		ConfTask staticTask = ConfTaskProvider.getInst().getConfigById(taskId);
		if (staticTask == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}
		int finishValue = staticTask.getSchedule();
		if (awardType == 2) {
			if (player.lord.getGold() < 5) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}
			PlayerDataManager.getInst().subGold(lord, 5, GoldCost.TASK_QUICK_FINISH);
			finishValue = 0;
		}
		long scheduleTank = 0;
		Task task = null;
		if (staticTask.getType() == TaskType.TYPE_MAIN) {
			task = player.majorTasks.get(taskId);
			if (task == null) {
//				handler.sendErrorMsgToPlayer(GameError.TASK_NO_FINISH);
				return;
			}
			currentMajorTask(player, task, staticTask);
			if (task.getSchedule() < finishValue) {
//				handler.sendErrorMsgToPlayer(GameError.TASK_NO_FINISH);
				return;
			}
			scheduleTank = task.getSchedule();
		} else if (staticTask.getType() == TaskType.TYPE_DAYIY) {
			Iterator<Task> it = player.dayiyTask.iterator();
			boolean flag = false;
			while (it.hasNext()) {
				Task next = it.next();
				if (next.getTaskId() == taskId && next.getSchedule() >= finishValue) {
					task = next;
					it.remove();
					flag = true;
					break;
				}
			}
			if (flag) {
				lord.setTaskDayiy(lord.getTaskDayiy() + 1);
			} else {
//				handler.sendErrorMsgToPlayer(GameError.TASK_NO_FINISH);
				return;
			}
		}
		if (task == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_TASK);
			return;
		}

		TaskAwardRs.Builder builder = TaskAwardRs.newBuilder();
		List<List<Integer>> awardList = staticTask.getAwardList();
		for (List<Integer> ee : awardList) {
			int type = ee.get(0);
			int id = ee.get(1);
			int count = ee.get(2);
			int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.TASK_DAYIY_AWARD);
			builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
		}
		int exp = staticTask.getExp();
		PlayerDataManager.getInst().addAward(player, AwardType.EXP, 0, exp, AwardFrom.TASK_DAYIY_AWARD);
		builder.addAward(PbHelper.createAwardPb(AwardType.EXP, 0, exp, 0));
		// 触发下一个任务
		if (staticTask.getType() == TaskType.TYPE_MAIN) {
			player.majorTasks.remove(taskId);
			List<ConfTask> triggerList = ConfTaskProvider.getInst().getTriggerTask(taskId);
			if (triggerList != null) {
				for (ConfTask ee : triggerList) {
					Task etask = new Task(ee.getTaskId());
					currentMajorTask(player, etask, ee);
					if (staticTask.getCond() == TaskType.COND_TANK_PRODUCT && ee.getCond() == TaskType.COND_TANK_PRODUCT && staticTask.getParam().size() > 0 && ee.getParam().size() > 0) {
						int paramId = staticTask.getParam().get(0);
						int eparamId = ee.getParam().get(0);
						if (paramId == eparamId) {
							etask.setSchedule(scheduleTank);
							if (etask.getSchedule() >= ee.getSchedule()) {
								etask.setStatus(1);
							}
						}
					}
					player.majorTasks.put(ee.getTaskId(), etask);
					builder.addTask(PbHelper.createTaskPb(etask));
				}
			}
		} else if (staticTask.getType() == TaskType.TYPE_DAYIY) {
			int ntaskId = ConfTaskProvider.getInst().getOneDayiyTask();
			Task ntask = new Task(ntaskId);
			player.dayiyTask.add(ntask);
			builder.addTask(PbHelper.createTaskPb(ntask));
		}
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.TaskAwardRs, builder.build());
	}

	public void taskLiveAward(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		TaskLiveAwardRq req = msg.get();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		Lord lord = player.lord;
		TaskManager.getInst().refreshTask(player);
		int taskLive = lord.getTaskLive();// 当前总活跃
		int taskAd = lord.getTaskLiveAd();// 已领取活跃奖励的活跃值
		ConfTaskLive staticTaskLive = ConfTaskLiveProvider.getInst().getTaskLive(taskAd, taskLive);
		if (staticTaskLive == null) {
//			handler.sendErrorMsgToPlayer(GameError.LIVE_NO_ENOUGH);
			return;
		}
		lord.setTaskLiveAd(staticTaskLive.getLive());
		TaskLiveAwardRs.Builder builder = TaskLiveAwardRs.newBuilder();
		for (List<Integer> e : staticTaskLive.getAwardList()) {
			if (e.size() != 3) {
				continue;
			}
			int type = e.get(0);
			int id = e.get(1);
			int count = e.get(2);
			int keyId = PlayerDataManager.getInst().addAward(player, type, id, count, AwardFrom.TASK_LIVILY_AWARD);
			builder.addAward(PbHelper.createAwardPb(type, id, count, keyId));
		}

		builder.setTaskLive(PbHelper.createTaskLivePb(lord));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.TaskLiveAwardRs, builder.build());
	}

	public void acceptTaskRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		AcceptTaskRq req = msg.get();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		int taskId = req.getTaskId();
		ConfTask staticTask = ConfTaskProvider.getInst().getConfigById(taskId);
		if (staticTask == null || staticTask.getType() != TaskType.TYPE_DAYIY) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}
		Lord lord = player.lord;
		TaskManager.getInst().refreshTask(player);
		if (lord.getTaskDayiy() >= 5) {
//			handler.sendErrorMsgToPlayer(GameError.TASK_DAYIY_FULL);
			return;
		}
		Task task = null;
		boolean flag = false;
		Iterator<Task> it = player.dayiyTask.iterator();
		while (it.hasNext()) {
			Task next = it.next();
			if (next.getAccept() == 1) {
				flag = true;
				break;
			}
			if (next.getTaskId() == taskId) {
				task = next;
			}
		}
		if (flag) {
//			handler.sendErrorMsgToPlayer(GameError.HAD_ACCEPT);
			return;
		}
		if (task == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_TASK);
			return;
		}
		task.setAccept(1);
		// lord.setTaskDayiy(lord.getTaskDayiy() + 1);//在完成之后增加
		AcceptTaskRs.Builder builder = AcceptTaskRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.AcceptTaskRs, builder.build());
	}

	public void acceptNoTaskRq(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
			return;
		}
		TaskManager.getInst().refreshTask(player);
		Iterator<Task> it = player.dayiyTask.iterator();
		while (it.hasNext()) {
			Task next = it.next();
			if (next.getAccept() == 1) {
				next.setAccept(0);
				next.setSchedule(0);
				next.setStatus(0);
				break;
			}
		}
		AcceptNoTaskRs.Builder builder = AcceptNoTaskRs.newBuilder();
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.AcceptNoTaskRs, builder.build());
	}

	/**
	 * 主线任务进度检测并且更新
	 * 
	 * @param player
	 * @param task
	 * @param stask
	 * @return
	 */
	public Task currentMajorTask(Player player, Task task, ConfTask stask) {
		if (task == null) {
			return task;
		}

		int cond = stask.getCond();
		long schedule = 0;
		switch (cond) {
		case TaskType.COND_IRON: {// 铁矿升级
			schedule = PlayerDataManager.getInst().getMillTopLv(player, BuildingId.IRON);
			break;
		}
		case TaskType.COND_OIL: {// 石油升级
			schedule = PlayerDataManager.getInst().getMillTopLv(player, BuildingId.OIL);
			break;
		}
		case TaskType.COND_COPPER: {
			schedule = PlayerDataManager.getInst().getMillTopLv(player, BuildingId.COPPER);
			break;
		}
		case TaskType.COND_SILICON: {
			schedule = PlayerDataManager.getInst().getMillTopLv(player, BuildingId.SILICON);
			break;
		}
		case TaskType.COND_STONE: {
			schedule = PlayerDataManager.getInst().getMillTopLv(player, BuildingId.STONE);
			break;
		}
		case TaskType.COND_COMMANDER: {
			schedule = PlayerDataManager.getBuildingLv(BuildingId.COMMAND, player.building);
			break;
		}
		case TaskType.COND_FACTORY: {// 战车工厂升级任务
			schedule = PlayerDataManager.getBuildingLv(BuildingId.FACTORY_1, player.building);
			int tempLv = PlayerDataManager.getBuildingLv(BuildingId.FACTORY_2, player.building);
			if (tempLv > schedule) {
				schedule = tempLv;
			}
			break;
		}
		case TaskType.COND_REFIT: {
			schedule = PlayerDataManager.getBuildingLv(BuildingId.REFIT, player.building);
			break;
		}
		case TaskType.COND_SCIENCE: {
			schedule = PlayerDataManager.getBuildingLv(BuildingId.TECH, player.building);
			break;
		}
		case TaskType.COND_STORE: {
			schedule = PlayerDataManager.getBuildingLv(BuildingId.WARE_1, player.building);
			int tempLv = PlayerDataManager.getBuildingLv(BuildingId.WARE_2, player.building);
			if (tempLv > schedule) {
				schedule = tempLv;
			}
			break;
		}
		case TaskType.COND_WORKSHOP: {
			schedule = PlayerDataManager.getBuildingLv(BuildingId.WORKSHOP, player.building);
			break;
		}
		case TaskType.COND_COMBAT: {
			if (stask.getParam().size() > 0) {
				int combatId = stask.getParam().get(0);
				if (player.combatId > combatId) {
					schedule = 1;
				}
			}
			break;
		}
		case TaskType.COND_FAME: {// 声望等级任务
			schedule = player.lord.getFameLv();
			break;
		}
		case TaskType.COND_MILITARY: {
			schedule = player.lord.getRanks();
			break;
		}
		case TaskType.COND_IRON_PDCT: {// 铁产量
			schedule = PlayerDataManager.getInst().getResourceOut(player, PartyType.RESOURCE_IRON);
			break;
		}
		case TaskType.COND_OIL_PDCT: {// 石油产量
			schedule = PlayerDataManager.getInst().getResourceOut(player, PartyType.RESOURCE_OIL);
			break;
		}
		case TaskType.COND_COPPER_PDCT: {// 铜产量
			schedule = PlayerDataManager.getInst().getResourceOut(player, PartyType.RESOURCE_COPPER);
			break;
		}
		case TaskType.COND_IRON_MADE: {
			schedule = PlayerDataManager.getInst().getMillCount(player, BuildingId.IRON, 1);
			break;
		}
		case TaskType.COND_OIL_MADE: {
			schedule = PlayerDataManager.getInst().getMillCount(player, BuildingId.OIL, 1);
			break;
		}
		case TaskType.COND_COPPER_MADE: {
			schedule = PlayerDataManager.getInst().getMillCount(player, BuildingId.COPPER, 1);
			break;
		}
		case TaskType.COND_SILICON_MADE: {
			schedule = PlayerDataManager.getInst().getMillCount(player, BuildingId.SILICON, 1);
			break;
		}
		case TaskType.COND_STONE_MADE: {
			schedule = PlayerDataManager.getInst().getMillCount(player, BuildingId.STONE, 1);
			break;
		}
		case TaskType.COND_FACTORY_2: {
			schedule = PlayerDataManager.getBuildingLv(BuildingId.FACTORY_2, player.building);
			if (schedule > 1) {
				schedule = 1;
			}
			break;
		}
		case TaskType.COND_WARE_2: {
			int temp1 = PlayerDataManager.getBuildingLv(BuildingId.WARE_1, player.building);
			int temp2 = PlayerDataManager.getBuildingLv(BuildingId.WARE_2, player.building);
			if (temp1 > 0) {
				schedule++;
			}
			if (temp2 > 0) {
				schedule++;
			}
			break;
		}
		case TaskType.COND_BUILD_REFIT: {
			schedule = PlayerDataManager.getBuildingLv(BuildingId.REFIT, player.building);
			if (schedule > 1) {
				schedule = 1;
			}
			break;
		}
		case TaskType.COND_BUILD_SCIENCE: {
			schedule = PlayerDataManager.getBuildingLv(BuildingId.TECH, player.building);
			if (schedule > 1) {
				schedule = 1;
			}
			break;
		}
		case TaskType.COND_BUILD_WORKSHOP: {
			schedule = PlayerDataManager.getBuildingLv(BuildingId.WORKSHOP, player.building);
			if (schedule > 1) {
				schedule = 1;
			}
			break;
		}
		default:
			break;
		}
		if (schedule > task.getSchedule()) {
			task.setSchedule(schedule);
		}
		if (schedule >= stask.getSchedule()) {
			task.setStatus(1);
		}
		return task;
	}

}
