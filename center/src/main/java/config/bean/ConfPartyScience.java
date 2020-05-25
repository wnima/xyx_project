package config.bean;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2015-9-10 下午2:01:27
 * @declare
 */

public class ConfPartyScience implements IConfigBean{

	private int keyId;
	private int scienceId;
	private int scienceLv;
	private int schedule;
	private int lockLv;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getScienceId() {
		return scienceId;
	}

	public void setScienceId(int scienceId) {
		this.scienceId = scienceId;
	}

	public int getScienceLv() {
		return scienceLv;
	}

	public void setScienceLv(int scienceLv) {
		this.scienceLv = scienceLv;
	}

	public int getSchedule() {
		return schedule;
	}

	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}

	public int getLockLv() {
		return lockLv;
	}

	public void setLockLv(int lockLv) {
		this.lockLv = lockLv;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
