package data;

public class Signet {

	long id;
	int time;

	public Signet() {
	}

	public Signet(long id, int time) {
		this.id = id;
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
