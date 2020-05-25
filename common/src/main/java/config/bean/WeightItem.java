package config.bean;

public class WeightItem{
	private int id; 

	private int weight; 

	private int count; 

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getWeight(){
		return weight;
	}

	public void setWeight(int weight){
		this.weight = weight;
	}

	public int getCount(){
		return count;
	}

	public void setCount(int count){
		this.count = count;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("id");
		builder.append(":");
		builder.append(id);
		builder.append(",");
		builder.append("weight");
		builder.append(":");
		builder.append(weight);
		builder.append(",");
		builder.append("count");
		builder.append(":");
		builder.append(count);
		return builder.toString();
	}
}