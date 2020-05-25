package data.bean;

public class Combat {
	private int combatId;
	private int star;

	public int getCombatId() {
		return combatId;
	}

	public void setCombatId(int combatId) {
		this.combatId = combatId;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	/**      
	* @param combatId
	* @param star    
	*/
	public Combat(int combatId, int star) {
		super();
		this.combatId = combatId;
		this.star = star;
	}

	
	
}
