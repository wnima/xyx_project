package data.bean;

import com.game.util.PbHelper;

import pb.CommonPb;

public class Army {
	private int keyId;
	private int target;
	private int state;
	private Form form;
	private int period;
	private int endTime;
	private Grab grab;
	private Collect collect;
	private int staffingTime;
	private boolean senior;
	private boolean occupy;

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	/**
	 * @param keyId
	 * @param type
	 * @param target
	 * @param state
	 * @param form
	 * @param period
	 * @param endTime
	 */
	public Army(CommonPb.Army armyPb) {
		super();
		this.keyId = armyPb.getKeyId();
		this.target = armyPb.getTarget();
		this.state = armyPb.getState();
		this.period = armyPb.getPeriod();
		this.endTime = armyPb.getEndTime();
		this.form = PbHelper.createForm(armyPb.getForm());
		if (armyPb.hasField(CommonPb.Army.getDescriptor().findFieldByName("grab"))) {
			this.grab = PbHelper.createGrab(armyPb.getGrab());
		}

		if (armyPb.hasField(CommonPb.Army.getDescriptor().findFieldByName("collect"))) {
			this.collect = PbHelper.createCollect(armyPb.getCollect());
		}

		if (armyPb.hasField(CommonPb.Army.getDescriptor().findFieldByName("staffingTime"))) {
			this.staffingTime = armyPb.getStaffingTime();
		}

		if (armyPb.hasField(CommonPb.Army.getDescriptor().findFieldByName("senior"))) {
			this.senior = armyPb.getSenior();
		}

		if (armyPb.hasField(CommonPb.Army.getDescriptor().findFieldByName("occupy"))) {
			this.occupy = armyPb.getOccupy();
		}

	}

	/**
	 * @param keyId
	 * @param target
	 * @param state
	 * @param form
	 * @param period
	 * @param endTime
	 */
	public Army(int keyId, int target, int state, Form form, int period, int endTime) {
		super();
		this.keyId = keyId;
		this.target = target;
		this.state = state;
		this.form = form;
		this.period = period;
		this.endTime = endTime;
	}

	public Grab getGrab() {
		return grab;
	}

	public void setGrab(Grab grab) {
		this.grab = grab;
	}

	public Collect getCollect() {
		return collect;
	}

	public void setCollect(Collect collect) {
		this.collect = collect;
	}

	public int getStaffingTime() {
		return staffingTime;
	}

	public void setStaffingTime(int staffingTime) {
		this.staffingTime = staffingTime;
	}

	public boolean getSenior() {
		return senior;
	}

	public void setSenior(boolean senior) {
		this.senior = senior;
	}

	public boolean getOccupy() {
		return occupy;
	}

	public void setOccupy(boolean occupy) {
		this.occupy = occupy;
	}

}
