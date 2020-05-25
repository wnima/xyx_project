package define;

public enum ShopEnum {
	HAD(1), // 已拥有
	UN_HAD(2), // 未有，可购买
	USEING(3),// 使用中
	;

	int state;

	private ShopEnum(int state) {
		this.state = state;
	}

	public int getValue() {
		return this.state;
	}

}
