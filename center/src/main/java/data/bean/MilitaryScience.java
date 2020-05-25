package data.bean;
public class MilitaryScience {
	private int militaryScienceId;
	private int level;
	private int fitTankId;
	private int fitPos;

	/**      
	*     
	*/
	public MilitaryScience() {
		super();
	}

	/**      
	* @param militaryScienceId
	* @param level
	* @param fitTankId
	* @param fitPos    
	*/
	public MilitaryScience(int militaryScienceId, int level, int fitTankId, int fitPos) {
		super();
		this.militaryScienceId = militaryScienceId;
		this.level = level;
		this.fitTankId = fitTankId;
		this.fitPos = fitPos;
	}

	public int getMilitaryScienceId() {
		return militaryScienceId;
	}

	public void setMilitaryScienceId(int militaryScienceId) {
		this.militaryScienceId = militaryScienceId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getFitTankId() {
		return fitTankId;
	}

	public void setFitTankId(int fitTankId) {
		this.fitTankId = fitTankId;
	}

	public int getFitPos() {
		return fitPos;
	}

	public void setFitPos(int fitPos) {
		this.fitPos = fitPos;
	}

}
