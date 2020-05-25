package data.bean;

public class Award {

	private int type;
	private int id;
	private int count;
	private int keyId;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public Award() {
	}

	public Award(int type, int id, int count, int keyId) {
		this.type = type;
		this.id = id;
		this.count = count;
		this.keyId = keyId;
	}
}
