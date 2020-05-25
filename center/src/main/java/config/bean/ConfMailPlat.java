package config.bean;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2016-4-8 下午3:26:43
 * @declare
 */

public class ConfMailPlat implements IConfigBean{

	private int platNo;
	private int mailId;

	public int getPlatNo() {
		return platNo;
	}

	public void setPlatNo(int platNo) {
		this.platNo = platNo;
	}

	public int getMailId() {
		return mailId;
	}

	public void setMailId(int mailId) {
		this.mailId = mailId;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
