package data.bean;

public class Chip {
	private int chipId;
	private int count;

	public int getChipId() {
		return chipId;
	}

	public void setChipId(int chipId) {
		this.chipId = chipId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @param chipId
	 * @param count
	 */
	public Chip(int chipId, int count) {
		super();
		this.chipId = chipId;
		this.count = count;
	}

}
