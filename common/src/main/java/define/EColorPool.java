package define;

public enum EColorPool{
	c_no(0),
	c_niuniu(1),
//	c_double_two(2),
	c_three(3),
	c_hulu(4),
	c_bomb(5),
	;

	EColorPool(int value){
		this.value = value;
	}

	private int value;

	public int getValue(){
		return this.value;
	}

	public static EColorPool getByValue(int value){
		for(EColorPool type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		return null;
	}

}