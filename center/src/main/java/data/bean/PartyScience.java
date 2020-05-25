package data.bean;

import data.DataBean;

public class PartyScience implements DataBean {

	private int scienceId;
	private int scienceLv;
	private int schedule;

	public int getScienceId() {
		return scienceId;
	}

	public void setScienceId(int scienceId) {
		this.scienceId = scienceId;
	}

	public int getScienceLv() {
		return scienceLv;
	}

	public void setScienceLv(int scienceLv) {
		this.scienceLv = scienceLv;
	}

	public int getSchedule() {
		return schedule;
	}

	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}

	public PartyScience() {
	}

	public PartyScience(int scienceId, int scienceLv) {
		this.scienceId = scienceId;
		this.scienceLv = scienceLv;

	}

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub

	}
}
