package data.bean;

public class RptTank {
	private int tankId;
	private int count;

	public int getTankId() {
		return tankId;
	}

	public void setTankId(int tankId) {
		this.tankId = tankId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @param tankId
	 * @param count
	 */
	public RptTank(int tankId, int count) {
		super();
		this.tankId = tankId;
		this.count = count;
	}

}
