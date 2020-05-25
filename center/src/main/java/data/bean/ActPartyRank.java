package data.bean;

import java.util.ArrayList;
import java.util.List;


public class ActPartyRank {
	private int Rank;
	private int partyId;
	private int rankType;
	private long rankValue;
	private String param;
	private List<Long> lordIds = new ArrayList<Long>();

	public int getRank() {
		return Rank;
	}

	public void setRank(int rank) {
		Rank = rank;
	}

	public int getPartyId() {
		return partyId;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public int getRankType() {
		return rankType;
	}

	public void setRankType(int rankType) {
		this.rankType = rankType;
	}

	public long getRankValue() {
		return rankValue;
	}

	public void setRankValue(long rankValue) {
		this.rankValue = rankValue;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public List<Long> getLordIds() {
		return lordIds;
	}

	public void setLordIds(List<Long> lordIds) {
		this.lordIds = lordIds;
	}

	public ActPartyRank() {
	}

	public ActPartyRank(int partyId, int type, long value) {
		this.partyId = partyId;
		this.rankType = type;
		this.rankValue = value;
	}
}
