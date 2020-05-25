package define;

public enum EGameCategories{
	MULTI(1),
	FIGHT(2),
	VIDEO(3),
	CAPTURE_FISH(4),
	LEISURE(5);

	EGameCategories(int value){
		this.value = value;
	}

	private int value;

	public int getValue(){
		return this.value;
	}

	public static EGameCategories getByValue(int value){
		for(EGameCategories type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		return null;
	}

}