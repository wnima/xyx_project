package define;

public enum GoldGive {
	AWARD(1, "AWARD") {

	},
	CAIZHENG(2, "CAI ZHENG GUAN") {

	},
	RED_PACKET(3, "RED PACKET") {

	},
	DO_SOME(99, "DO SOME") {

	},
	PAY(8, "PAY IN GAME") {

	};
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private GoldGive(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private int code;
	private String msg;
}
