package data.bean;

import data.DataBean;

public class DbExtreme implements DataBean {
	private int extremeId;
	private byte[] first1;
	private byte[] last3;

	public int getExtremeId() {
		return extremeId;
	}

	public void setExtremeId(int extremeId) {
		this.extremeId = extremeId;
	}

	public byte[] getFirst1() {
		return first1;
	}

	public void setFirst1(byte[] first) {
		this.first1 = first;
	}

	public byte[] getLast3() {
		return last3;
	}

	public void setLast3(byte[] last3) {
		this.last3 = last3;
	}

	@Override
	public long getId() {
		return extremeId;
	}

	@Override
	public void setId(long id) {
		this.extremeId = (int) id;
	}
}
