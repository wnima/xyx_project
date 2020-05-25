package define;

public enum ButtonEnum {

	NOTICE(1001), // 公共
	CUSTOMER(1002), // 客服
	FREE(1014),// 免费
	;

	private int id;

	private ButtonEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static ButtonEnum getByValue(int id) {
		for (ButtonEnum e : values()) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}

}
