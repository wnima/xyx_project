package com.game.domain;

import pb.CommonPb;

public class SeniorScoreRank {
	private long lordId;
	private long fight;
	private int score;
	private boolean get;

	public boolean getGet() {
		return get;
	}

	public void setGet(boolean get) {
		this.get = get;
	}

	public long getLordId() {
		return lordId;
	}

	public void setLordId(long lordId) {
		this.lordId = lordId;
	}

	public long getFight() {
		return fight;
	}

	public void setFight(long fight) {
		this.fight = fight;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public CommonPb.SeniorScore ser(){
		CommonPb.SeniorScore.Builder builder = CommonPb.SeniorScore.newBuilder();
		builder.setLordId(lordId);
		builder.setFight(fight);
		builder.setScore(score);
		builder.setGet(get);
		return builder.build();
	}
	
	public SeniorScoreRank(CommonPb.SeniorScore seniorScore) {
		lordId = seniorScore.getLordId();
		fight = seniorScore.getFight();
		score = seniorScore.getScore();
		get = seniorScore.getGet();
	}

	public SeniorScoreRank(Player player) {
		lordId = player.roleId;
		fight = player.lord.getFight();
		score = player.seniorScore;
	}
}
