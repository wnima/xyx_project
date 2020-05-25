package data.bean;

public class MilitaryScienceGrid {
	private int tankId;
	private int pos;
	private int status;
	private int militaryScienceId;

	public int getTankId() {
		return tankId;
	}

	public void setTankId(int tankId) {
		this.tankId = tankId;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMilitaryScienceId() {
		return militaryScienceId;
	}

	public void setMilitaryScienceId(int militaryScienceId) {
		this.militaryScienceId = militaryScienceId;
	}

	/**      
	* @param tankId
	* @param pos
	* @param status
	* @param militaryScienceId    
	*/
	public MilitaryScienceGrid(int tankId, int pos, int status, int militaryScienceId) {
		super();
		this.tankId = tankId;
		this.pos = pos;
		this.status = status;
		this.militaryScienceId = militaryScienceId;
	}


}
