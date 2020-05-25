package define;

public enum ELoginType {

	QQ(1, "微信"), WECHAT(2, "微信");

	private int type;
	private String name;

	private ELoginType(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
