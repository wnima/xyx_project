package define;

public enum GameType {
	MXW(1),RUN(2);
	private Integer val;
	private GameType(int val){
		this.val = val;
	}
	public Integer getVal() {
		return val;
	}
	
	public static GameType getByVal(int val) {
		for(GameType value:values()) {
			if(value.val == val) {
				return value;
			}
		}
		return null;
	}
}
