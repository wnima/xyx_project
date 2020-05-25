package data.bean;

public class MilitaryMaterial {
	private int id;
	private long count;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "MilitaryMaterial [id=" + id + ", count=" + count + "]";
	}

	/**      
	* @param id
	* @param count    
	*/
	public MilitaryMaterial(int id, long count) {
		super();
		this.id = id;
		this.count = count;
	}

}
