package manager;

import com.game.domain.Player;
import com.google.inject.Singleton;

import data.bean.Hero;
import inject.BeanManager;

@Singleton
public class HeroManager {

	public static HeroManager getInst() {
		return BeanManager.getBean(HeroManager.class);
	}
	
	/**
	 * 
	 * Method: addHero
	 * 
	 * @Description: 增加武将 @param player @param heroId @param
	 *               count @return @return Hero @throws
	 */
	public Hero addHero(Player player, int heroId, int count) {
		Hero hero = player.heros.get(heroId);
		if (hero != null) {
			hero.setCount(hero.getCount() + count);
			if (count < 0 && hero.getCount() <= 0) {
				player.heros.remove(heroId);
			}
		} else {
			hero = new Hero(heroId, heroId, count);
			player.heros.put(hero.getHeroId(), hero);
		}
		return hero;
	}
}
