package config.bean;

import config.IConfigBean;

public class ConfPartUp implements IConfigBean {
	private int partId;
	private int lv;
	private int prob;
	private int stone;
	private int stoneExplode;

	public int getPartId() {
		return partId;
	}

	public void setPartId(int partId) {
		this.partId = partId;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public int getProb() {
		return prob;
	}

	public void setProb(int prob) {
		this.prob = prob;
	}

	public int getStone() {
		return stone;
	}

	public void setStone(int stone) {
		this.stone = stone;
	}

	public int getStoneExplode() {
		return stoneExplode;
	}

	public void setStoneExplode(int stoneExplode) {
		this.stoneExplode = stoneExplode;
	}

	@Override
	public int getId() {
		return 0;
	}

}
