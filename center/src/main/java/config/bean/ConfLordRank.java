package config.bean;

import config.IConfigBean;

/**
 * 军衔
 * 
 * @author Administrator
 *
 */
public class ConfLordRank implements IConfigBean {
	private int rankId;
	private String name;
	private int lordLv;
	private int stoneCost;
	private int fame;

	public int getRankId() {
		return rankId;
	}

	public void setRankId(int rankId) {
		this.rankId = rankId;
	}

	public int getLordLv() {
		return lordLv;
	}

	public void setLordLv(int lordLv) {
		this.lordLv = lordLv;
	}

	public int getStoneCost() {
		return stoneCost;
	}

	public void setStoneCost(int stoneCost) {
		this.stoneCost = stoneCost;
	}

	public int getFame() {
		return fame;
	}

	public void setFame(int fame) {
		this.fame = fame;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
