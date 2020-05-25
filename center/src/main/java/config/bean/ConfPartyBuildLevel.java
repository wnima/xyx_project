package config.bean;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2015-9-10 下午1:54:38
 * @declare
 */

public class ConfPartyBuildLevel implements IConfigBean{

	private int type;
	private int buildLv;
	private int needExp;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBuildLv() {
		return buildLv;
	}

	public void setBuildLv(int buildLv) {
		this.buildLv = buildLv;
	}

	public int getNeedExp() {
		return needExp;
	}

	public void setNeedExp(int needExp) {
		this.needExp = needExp;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
