package define;

public enum PayEnum {

	WEIX(1,"weix"), // 微信
	ALIPAY(2,"alipay"), // 支付宝
	THREE(3,"three"), //第三方
	;

	private int type;
	private String key;

	private PayEnum(int type, String key) {
		this.type = type;
		this.key = key;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
}
