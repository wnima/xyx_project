package config.bean;

public class PairInt{
	private int k; 

	private int v; 

	public int getK(){
		return k;
	}

	public void setK(int k){
		this.k = k;
	}

	public int getV(){
		return v;
	}

	public void setV(int v){
		this.v = v;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("k");
		builder.append(":");
		builder.append(k);
		builder.append(",");
		builder.append("v");
		builder.append(":");
		builder.append(v);
		return builder.toString();
	}
}