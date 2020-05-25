package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.util.RandomHelper;
import com.google.inject.Singleton;

import config.bean.ConfArenaAward;
import config.provider.ConfArenaAwardProvider;
import data.bean.Arena;
import data.bean.ArenaLog;
import data.provider.ArenaLogProvider;
import data.provider.ArenaProvider;
import inject.BeanManager;
import util.DateUtil;

@Singleton
public class ArenaDataManager {

	private Map<Integer, Arena> rankMap = new HashMap<>();

	private Map<Long, Arena> playerMap;

	private ArenaLog arenaLog;

	public static ArenaDataManager getInst() {
		return BeanManager.getBean(ArenaDataManager.class);
	}

	public void load() {
		initArenaData();
		initArenaLog();
	}

	private void initArenaData() {
		fillData();
		playerMap = new HashMap<Long, Arena>();
		Iterator<Arena> it = rankMap.values().iterator();
		while (it.hasNext()) {
			Arena arena = (Arena) it.next();
			if (!SmallIdManager.getInst().isSmallId(arena.getLordId())) {
				playerMap.put(arena.getLordId(), arena);
			}
		}
	}

	private void fillData() {
		List<Arena> list = ArenaProvider.getInst().getAllBean();
		Arena arena;
		int rank = 1;
		for (int i = 0; i < list.size(); i++) {
			arena = list.get(i);
			if (!SmallIdManager.getInst().isSmallId(arena.getLordId())) {
				arena.setRank(rank);
				rankMap.put(arena.getRank(), arena);
				rank++;
			}
		}
	}

	private void initArenaLog() {
		arenaLog = ArenaLogProvider.getInst().getBeanById(0);
		if (arenaLog == null) {
			arenaLog = new ArenaLog(DateUtil.getToday(), 0);
		}
	}

	public ArenaLog getArenaLog() {
		return arenaLog;
	}

	public void setArenaLog(ArenaLog arenaLog) {
		this.arenaLog = arenaLog;
	}

	public void flushArenaLog() {
		ArenaLogProvider.getInst().insert(arenaLog);
//		serverLogDao.insertArenaLog(arenaLog);
	}

	public Map<Integer, Arena> getRankMap() {
		return rankMap;
	}

	public void setRankMap(Map<Integer, Arena> rankMap) {
		this.rankMap = rankMap;
	}

	public Arena getArena(long lordId) {
		return playerMap.get(lordId);
	}

	public Arena getArenaByRank(int rank) {
		return rankMap.get(rank);
	}

	public List<Arena> randomEnemy(int rank) {
		List<Arena> list = new ArrayList<>();
		int arenaSize = rankMap.size();
		if (rank > 15000) {
			list.add(rankMap.get(rank - 200));
			list.add(rankMap.get(rank - 400));
			list.add(rankMap.get(rank - 600));
			list.add(rankMap.get(rank - 800));
		} else if (rank > 5000) {
			list.add(rankMap.get(rank - 100));
			list.add(rankMap.get(rank - 200));
			list.add(rankMap.get(rank - 300));
			list.add(rankMap.get(rank - 400));
		} else if (rank > 1000) {
			list.add(rankMap.get(rank - 50));
			list.add(rankMap.get(rank - 100));
			list.add(rankMap.get(rank - 150));
			list.add(rankMap.get(rank - 200));
		} else if (rank > 500) {
			list.add(rankMap.get(rank - 20));
			list.add(rankMap.get(rank - 40));
			list.add(rankMap.get(rank - 60));
			list.add(rankMap.get(rank - 80));
		} else if (rank > 200) {
			list.add(rankMap.get(rank - 15));
			list.add(rankMap.get(rank - 30));
			list.add(rankMap.get(rank - 45));
			list.add(rankMap.get(rank - 60));
		} else if (rank > 100) {
			list.add(rankMap.get(rank - 10));
			list.add(rankMap.get(rank - 20));
			list.add(rankMap.get(rank - 30));
			list.add(rankMap.get(rank - 40));
		} else if (rank > 50) {
			list.add(rankMap.get(rank - 40 + RandomHelper.randomInSize(10)));
			list.add(rankMap.get(rank - 30 + RandomHelper.randomInSize(10)));
			list.add(rankMap.get(rank - 20 + RandomHelper.randomInSize(10)));
			list.add(rankMap.get(rank - 10 + RandomHelper.randomInSize(10)));
		} else if (rank > 20) {
			list.add(rankMap.get(rank - 16 + RandomHelper.randomInSize(4)));
			list.add(rankMap.get(rank - 12 + RandomHelper.randomInSize(4)));
			list.add(rankMap.get(rank - 8 + RandomHelper.randomInSize(4)));
			list.add(rankMap.get(rank - 4 + RandomHelper.randomInSize(4)));
		} else if (rank > 10) {
			list.add(rankMap.get(rank - 8 + RandomHelper.randomInSize(2)));
			list.add(rankMap.get(rank - 6 + RandomHelper.randomInSize(2)));
			list.add(rankMap.get(rank - 4 + RandomHelper.randomInSize(2)));
			list.add(rankMap.get(rank - 2 + RandomHelper.randomInSize(2)));
		} else if (rank > 8) {
			list.add(rankMap.get(rank - 7));
			list.add(rankMap.get(rank - 4));
			list.add(rankMap.get(rank - 2));
			list.add(rankMap.get(rank - 1));
		} else if (rank > 6) {
			list.add(rankMap.get(rank - 6));
			list.add(rankMap.get(rank - 3));
			list.add(rankMap.get(rank - 2));
			list.add(rankMap.get(rank - 1));
		} else if (rank == 6) {
			list.add(rankMap.get(1));
			list.add(rankMap.get(3));
			list.add(rankMap.get(4));
			list.add(rankMap.get(5));
		} else if (rank == 5) {
			list.add(rankMap.get(1));
			list.add(rankMap.get(2));
			list.add(rankMap.get(3));
			list.add(rankMap.get(4));
		} else if (rank == 4) {
			list.add(rankMap.get(1));
			list.add(rankMap.get(2));
			list.add(rankMap.get(3));
			if (arenaSize > 5) {
				list.add(rankMap.get(6));
			}
		} else if (rank == 3) {
			list.add(rankMap.get(1));
			list.add(rankMap.get(2));
			if (arenaSize > 4) {
				list.add(rankMap.get(5));
			}

			if (arenaSize > 5) {
				list.add(rankMap.get(6));
			}
		} else if (rank == 2) {
			list.add(rankMap.get(1));
			if (arenaSize > 3) {
				list.add(rankMap.get(4));
			}

			if (arenaSize > 4) {
				list.add(rankMap.get(5));
			}

			if (arenaSize > 5) {
				list.add(rankMap.get(6));
			}
		} else if (rank == 1) {
			if (arenaSize > 2) {
				list.add(rankMap.get(3));
			}

			if (arenaSize > 3) {
				list.add(rankMap.get(4));
			}

			if (arenaSize > 4) {
				list.add(rankMap.get(5));
			}

			if (arenaSize > 5) {
				list.add(rankMap.get(6));
			}
		}

		if (rank != rankMap.size()) {
			list.add(rankMap.get(rank + 1));
		}
		return list;
	}

	public Arena addNew(long lordId) {
		Arena arena = new Arena();
		arena.setLordId(lordId);
		arena.setCount(5);
		arena.setRank(playerMap.size() + 1);
		arena.setArenaTime(DateUtil.getToday());
		playerMap.put(lordId, arena);
		rankMap.put(arena.getRank(), arena);
		return arena;
	}

	private void refreshArena(Arena arena) {
		int nowDay = DateUtil.getToday();
		if (arena.getArenaTime() != nowDay) {
			arena.setCount(5);
			arena.setBuyCount(0);
			arena.setArenaTime(nowDay);
		}
	}

	public Arena enterArena(long lordId) {
		Arena arena = playerMap.get(lordId);
		if (arena != null) {
			refreshArena(arena);
		}
		return arena;
	}

	/**
	 * 
	 * Method: exchangeArena
	 * 
	 * @Description: 交换排名 @param arena1 @param arena2 @return void @throws
	 */
	public void exchangeArena(Arena arena1, Arena arena2) {
		int rank = arena1.getRank();
		arena1.setRank(arena2.getRank());
		arena2.setRank(rank);
		rankMap.put(arena1.getRank(), arena1);
		rankMap.put(arena2.getRank(), arena2);
	}

	public ConfArenaAward getRankAward(int rank) {
		List<ConfArenaAward> rankAward = ConfArenaAwardProvider.getInst().getAllConfig();
		for (int i = 0; i < rankAward.size(); i++) {
			ConfArenaAward staticArenaAward = rankAward.get(i);
			if (rank >= staticArenaAward.getBeginRank() && rank <= staticArenaAward.getEndRank()) {
				return staticArenaAward;
			}
		}
		return null;
	}

}
