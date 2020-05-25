package data.bean;

public class Trend {

	private int trendId;
	private String[] param;
	private int trendTime;

	public int getTrendId() {
		return trendId;
	}

	public void setTrendId(int trendId) {
		this.trendId = trendId;
	}

	public String[] getParam() {
		return param;
	}

	public void setParam(String[] param) {
		this.param = param;
	}

	public int getTrendTime() {
		return trendTime;
	}

	public void setTrendTime(int trendTime) {
		this.trendTime = trendTime;
	}

	public Trend(int trendId, int day) {
		this.trendId = trendId;
		this.trendTime = day;
	}

}
