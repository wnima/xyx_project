package config.bean;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2015-9-10 下午1:57:46
 * @declare
 */

public class ConfPartyLively implements IConfigBean{

	private int livelyLv;
	private int livelyExp;
	private int costLively;
	private int resource;
	private int science;

	public int getLivelyLv() {
		return livelyLv;
	}

	public void setLivelyLv(int livelyLv) {
		this.livelyLv = livelyLv;
	}

	public int getLivelyExp() {
		return livelyExp;
	}

	public void setLivelyExp(int livelyExp) {
		this.livelyExp = livelyExp;
	}

	public int getCostLively() {
		return costLively;
	}

	public void setCostLively(int costLively) {
		this.costLively = costLively;
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	public int getScience() {
		return science;
	}

	public void setScience(int science) {
		this.science = science;
	}

	@Override
	public int getId() {
		return livelyLv;
	}

}
