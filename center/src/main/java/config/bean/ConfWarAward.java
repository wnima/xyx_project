package config.bean;

import java.util.List;

import config.IConfigBean;

public class ConfWarAward implements IConfigBean {
	private int rank;
	private List<List<Integer>> rankAwards;
	private List<List<Integer>> winAwards;
	private List<List<Integer>> hurtAwards;
	private List<List<Integer>> scoreAwards;
	private List<List<Integer>> scorePartyAwards;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public List<List<Integer>> getRankAwards() {
		return rankAwards;
	}

	public void setRankAwards(List<List<Integer>> rankAwards) {
		this.rankAwards = rankAwards;
	}

	public List<List<Integer>> getWinAwards() {
		return winAwards;
	}

	public void setWinAwards(List<List<Integer>> winAwards) {
		this.winAwards = winAwards;
	}

	public List<List<Integer>> getHurtAwards() {
		return hurtAwards;
	}

	public void setHurtAwards(List<List<Integer>> hurtAwards) {
		this.hurtAwards = hurtAwards;
	}

	public List<List<Integer>> getScoreAwards() {
		return scoreAwards;
	}

	public void setScoreAwards(List<List<Integer>> scoreAwards) {
		this.scoreAwards = scoreAwards;
	}

	public List<List<Integer>> getScorePartyAwards() {
		return scorePartyAwards;
	}

	public void setScorePartyAwards(List<List<Integer>> scorePartyAwards) {
		this.scorePartyAwards = scorePartyAwards;
	}

	@Override
	public int getId() {
		return rank;
	}

}
