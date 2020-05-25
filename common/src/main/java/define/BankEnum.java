package define;

public enum BankEnum {

	COIN_IN(1, "存钱"), // 存钱
	COIN_OUT(2, "取钱"), // 取钱
	PRESENT(3, "赠送"), // 赠送
	MONEY_IPAY(4, "提现到支付宝"), // 提现到支付宝
	MONEY_CARD(5, "提现到银行卡"),// 提现到银行卡
	;

	private int type;
	private String value;

	private BankEnum(int type, String value) {
		this.type = type;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
