package config.bean;

import config.IConfigBean;

public class ConfShop implements IConfigBean {

	private int id;
	private int type;
	private int itemId;
	private String itemName;
	private int itemNum;
	private int gold;
	private int coin;
	private String desc;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id");
		sb.append(":");
		sb.append(id);
		sb.append(",");
		sb.append("type");
		sb.append(":");
		sb.append(type);
		sb.append(",");
		sb.append("itemId");
		sb.append(":");
		sb.append(itemId);
		sb.append(",");
		sb.append("itemName");
		sb.append(":");
		sb.append(itemName);
		sb.append(",");
		sb.append("desc");
		sb.append(":");
		sb.append(desc);
		return sb.toString();
	}
}