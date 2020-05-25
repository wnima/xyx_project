package config.bean;

import java.util.List;

import config.IConfigBean;

public class ConfArenaAward implements IConfigBean {
	private int keyId;
	private int beginRank;
	private int endRank;
	private List<List<Integer>> award;
	private int score;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getBeginRank() {
		return beginRank;
	}

	public void setBeginRank(int beginRank) {
		this.beginRank = beginRank;
	}

	public int getEndRank() {
		return endRank;
	}

	public void setEndRank(int endRank) {
		this.endRank = endRank;
	}

	public List<List<Integer>> getAward() {
		return award;
	}

	public void setAward(List<List<Integer>> award) {
		this.award = award;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
