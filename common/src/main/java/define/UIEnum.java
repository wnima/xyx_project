package define;

public enum UIEnum {

	UI_HALL(1), // 大厅
	UI_BANK(2),// 银行
	;

	private int modelu;

	private UIEnum(int modelu) {
		this.modelu = modelu;
	}

	public int getModelu() {
		return modelu;
	}

	public void setModelu(int modelu) {
		this.modelu = modelu;
	}

	public static UIEnum getByValue(int modelu) {
		for (UIEnum ui : values()) {
			if (ui.getModelu() == modelu) {
				return ui;
			}
		}
		return null;
	}

}
