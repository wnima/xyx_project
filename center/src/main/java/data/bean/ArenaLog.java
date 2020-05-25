package data.bean;

import data.DataBean;

public class ArenaLog implements DataBean {
	private int keyId;
	private int arenaTime;
	private int count;

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getArenaTime() {
		return arenaTime;
	}

	public void setArenaTime(int arenaTime) {
		this.arenaTime = arenaTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ArenaLog(int arenaTime, int count) {
		super();
		this.arenaTime = arenaTime;
		this.count = count;
	}

	/**      
	*     
	*/
	public ArenaLog() {
		super();
	}

	@Override
	public long getId() {
		return this.keyId;
	}

	@Override
	public void setId(long id) {
		this.keyId = (int) id;
	}

}
