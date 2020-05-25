package define;

public enum EHost {

	OSS(1), // oss热更地址
	POPULARIZE(2), // popularize推广地址
	DOWNLOAD(3),// 下载地址
	PAY(4),// 支付地址
	;

	private int type;

	private EHost(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static EHost get(int type) {
		for (EHost e : EHost.values()) {
			if (e.getType() == type) {
				return e;
			}
		}
		return null;
	}
}
