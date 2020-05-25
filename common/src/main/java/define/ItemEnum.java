package define;

public enum ItemEnum {

	GOLD(1), // 金币
	BAG(2), // 背包道具
	SCORE_ACT(3),// 活动积分
	;

	private int type;

	private ItemEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
