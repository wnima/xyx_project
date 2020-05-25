package data.bean;

import data.DataBean;

public class SmallId implements DataBean {
	private long lordId;

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	@Override
	public long getId() {
		return this.lordId;
	}

	@Override
	public void setId(long id) {
		this.lordId = id;
	}

}
