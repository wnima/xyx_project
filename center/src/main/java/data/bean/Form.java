package data.bean;

public class Form {
	private int type;
	private int commander;
	public int[] p = new int[6];
	public int[] c = new int[6];

	public int getCommander() {
		return commander;
	}

	public void setCommander(int commander) {
		this.commander = commander;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Form(Form form) {
		type = form.type;
		commander = form.commander;
		System.arraycopy(form.p, 0, p, 0, 6);
		System.arraycopy(form.c, 0, c, 0, 6);
	}
	
	public Form() {
		
	}
}
