package util;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class TimerEvent implements Runnable, ICommand {

	protected long id;

	protected long delay;

	protected long period;

	protected TimeUnit timeUnit;

	protected int loop;

	protected int works = 0;

	private long createTime;

	private long endTime;

	private ScheduledFuture<?> future;

	public TimerEvent(long id, int loop, long delay, long period) {
		this.id = id;
		this.loop = loop;
		this.delay = delay;
		this.period = period;
		this.createTime = System.currentTimeMillis();
		this.timeUnit = TimeUnit.MILLISECONDS;
		if (loop != -1) {
			this.endTime = loop == 1 ? delay == 0 ? period + createTime : delay + createTime : delay + period * loop + createTime;
		}
	}

	public long getId() {
		return id;
	}

	public long getDelay() {
		return delay;
	}

	public long getPeriod() {
		return period;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public int getLoop() {
		return loop;
	}

	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}

	public long getRemain() {
		if (this.loop == -1) {
			return Long.MAX_VALUE;
		}
		if (isComplete()) {
			return 0;
		}
		return this.endTime - createTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isComplete() {
		if (this.loop != -1 && works >= loop) {
			return true;
		}
		return false;
	}

	public boolean cancel(boolean flag) {
		return this.future.cancel(flag);
	}

	@Override
	public void run() {
		try {
			if (!isComplete()) {
				action();
				works++;
			} else {
				if (future != null) {
					future.cancel(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
