package config.bean;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2015-9-10 下午1:55:45
 * @declare
 */

public class ConfPartyContribute implements IConfigBean{

	private int keyId;
	private int type;
	private int count;
	private int build;
	private int price;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getBuild() {
		return build;
	}

	public void setBuild(int build) {
		this.build = build;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
