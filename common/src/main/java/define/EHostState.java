package define;

public enum EHostState {

	FINE(0), // 正常地址
	IN_USE(1), // 使用中
	APP_STORE(4), // 上架
	BAD(999),// 已损坏
	;

	private int value;

	private EHostState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static EHostState get(int state) {
		for (EHostState e : EHostState.values()) {
			if (e.getValue() == state) {
				return e;
			}
		}
		return null;
	}
}
