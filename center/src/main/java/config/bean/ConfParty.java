package config.bean;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2015-10-16 上午11:09:20
 * @declare
 */

public class ConfParty implements IConfigBean {

	private int partyLv;
	private int partyNum;

	public int getPartyLv() {
		return partyLv;
	}

	public void setPartyLv(int partyLv) {
		this.partyLv = partyLv;
	}

	public int getPartyNum() {
		return partyNum;
	}

	public void setPartyNum(int partyNum) {
		this.partyNum = partyNum;
	}

	@Override
	public int getId() {
		return partyLv;
	}

}
