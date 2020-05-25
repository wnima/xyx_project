package data.bean;

public class TrendParam {

	private String content;
	private Man man;

	public TrendParam() {
	}

	public TrendParam(String content) {
		this.content = content;
	}

	public TrendParam(String content, long lordId) {
		this.content = content;
		this.man = new Man();
		man.setLordId(lordId);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Man getMan() {
		return man;
	}

	public void setMan(Man man) {
		this.man = man;
	}

}
