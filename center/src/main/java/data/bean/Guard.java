package data.bean;

import com.game.domain.Player;

public class Guard {
	private Player player;
	private Army army;

	public Army getArmy() {
		return army;
	}

	public void setArmy(Army army) {
		this.army = army;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @param army
	 * @param player
	 */
	public Guard(Player player, Army army) {
		super();
		this.army = army;
		this.player = player;
	}

}
