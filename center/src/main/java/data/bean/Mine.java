package data.bean;

public class Mine {
	private int mineId;
	private int mineLv;
	private int pos;

	public int getMineId() {
		return mineId;
	}

	public void setMineId(int mineId) {
		this.mineId = mineId;
	}

	public int getMineLv() {
		return mineLv;
	}

	public void setMineLv(int mineLv) {
		this.mineLv = mineLv;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
	public Mine(){}
	public Mine(int mineId, int mineLv, int pos) {
		this.mineId = mineId;
		this.mineLv = mineLv;
		this.pos = pos;
	}

}
