package data.bean;

public class Prop {
	private int propId;
	private int count;

	public int getPropId() {
		return propId;
	}

	public void setPropId(int propId) {
		this.propId = propId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @param propId
	 * @param count
	 */
	public Prop(int propId, int count) {
		super();
		this.propId = propId;
		this.count = count;
	}

}
