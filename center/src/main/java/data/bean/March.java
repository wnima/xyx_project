package data.bean;

import com.game.domain.Player;

public class March {
	private Player player;
	private Army army;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Army getArmy() {
		return army;
	}

	public void setArmy(Army army) {
		this.army = army;
	}

	public March(Player player, Army army) {
		super();
		this.player = player;
		this.army = army;
	}

}
