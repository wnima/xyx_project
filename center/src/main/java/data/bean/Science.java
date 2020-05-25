package data.bean;

public class Science {
	private int scienceId;
	private int scienceLv;


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

	public Science() {
	}

	public Science(int scienceId, int scienceLv) {
		this.scienceId = scienceId;
		this.scienceLv = scienceLv;
	}

}
