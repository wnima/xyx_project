package data.bean;


public class PartyCombat {
	private int combatId;
	private int schedule;
	private Form form;

	public int getCombatId() {
		return combatId;
	}

	public void setCombatId(int combatId) {
		this.combatId = combatId;
	}

	public int getSchedule() {
		return schedule;
	}

	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public PartyCombat() {
	}

	public PartyCombat(int combatId, int schedule) {
		this.combatId = combatId;
		this.schedule = schedule;
	}
}
