package data.bean;


public class ActPlayerRank {
	private int rank;
	private long lordId;// 玩家ID
	private int rankType;// 组别
	private long rankValue;// 值
	private String param;// 参数

	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	public int getRankType() {
		return rankType;
	}

	public void setRankType(int rankType) {
		this.rankType = rankType;
	}

	public long getRankValue() {
		return rankValue;
	}

	public void setRankValue(long rankValue) {
		this.rankValue = rankValue;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public ActPlayerRank() {
	}

	public ActPlayerRank(long lordId, int type, long value) {
		this.lordId = lordId;
		this.rankType = type;
		this.rankValue = value;
	}

}
