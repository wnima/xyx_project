package data.bean;

public class Mill {
	private int pos;
	private int id;
	private int lv;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	/**      
	* @param pos
	* @param id
	* @param lv    
	*/
	public Mill(int pos, int id, int lv) {
		super();
		this.pos = pos;
		this.id = id;
		this.lv = lv;
	}

	
}
