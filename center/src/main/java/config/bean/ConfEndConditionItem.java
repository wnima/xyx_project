package config.bean;

import config.IConfigBean;

public class ConfEndConditionItem implements IConfigBean{
	private int itemType;
	private int itemId;
	private int quality;
	private int star;
	private int chatId;

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}
	
	public int getChatId() {
		return chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	@Override
	public String toString() {
		return "EndConditionItem [itemType=" + itemType + ", itemId=" + itemId + ", quality=" + quality + ", star=" + star + ", chatId=" + chatId + "]";
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
