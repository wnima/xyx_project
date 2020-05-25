package data.bean;

public class Grab {
	public long[] rs = new long[5];

	public long payload() {
		return rs[0] + rs[1] + rs[2] + rs[3] + rs[4];
	}
}
