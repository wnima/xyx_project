package data.bean;

public class Tank implements Cloneable {
	private int tankId;
	private int count;
	private int rest;

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

	public int getRest() {
		return rest;
	}

	public void setRest(int rest) {
		this.rest = rest;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param tankId
	 * @param count
	 * @param rest
	 */
	public Tank(int tankId, int count, int rest) {
		super();
		this.tankId = tankId;
		this.count = count;
		this.rest = rest;
	}

}
