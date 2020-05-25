package define;

public enum ActEventEnum {

	ONLINE(2001), // 在线时长
	RANK(2004), // 查看排行榜
	ENJOY(2005), // 分享
	BUYU_COIN(2006), // 捕鱼获取金币
	;

	private int eventId;
	private String key;

	private ActEventEnum(int eventId) {
		this.eventId = eventId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
