package define;

public enum ItemID {

	PHONE_BILL(2003), // 话费卷
	FROZEN_CARD(2004), // 冰冻卡
	LOCK_CARD(2005), // 锁定卡
	VIOLENT_CARD(2006),// 狂暴卡
	;

	private int itemId;

	private ItemID(int propId) {
		this.itemId = propId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

}
