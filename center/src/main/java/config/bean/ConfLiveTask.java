package config.bean;

import config.IConfigBean;

public class ConfLiveTask implements IConfigBean{

	private int taskId;
	private String taskName;
	private int count;
	private int live;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getLive() {
		return live;
	}

	public void setLive(int live) {
		this.live = live;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
