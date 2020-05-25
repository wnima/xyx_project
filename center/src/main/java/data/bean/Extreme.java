package data.bean;

import java.util.LinkedList;

import pb.CommonPb.AtkExtreme;

public class Extreme {
	private int extremeId;
	private AtkExtreme first1;
	private LinkedList<AtkExtreme> last3 = new LinkedList<>();

	public int getExtremeId() {
		return extremeId;
	}

	public void setExtremeId(int extremeId) {
		this.extremeId = extremeId;
	}

	public AtkExtreme getFirst1() {
		return first1;
	}

	public void setFirst1(AtkExtreme first) {
		this.first1 = first;
	}

	public LinkedList<AtkExtreme> getLast3() {
		return last3;
	}

	public void setLast3(LinkedList<AtkExtreme> last3) {
		this.last3 = last3;
	}

}
