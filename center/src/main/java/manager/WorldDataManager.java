package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.game.domain.Player;
import com.game.util.RandomHelper;
import com.game.util.Turple;
import com.google.inject.Singleton;

import config.bean.ConfMine;
import config.bean.ConfMineForm;
import config.provider.ConfMineFormProvider;
import config.provider.ConfMineProvider;
import config.provider.ConfSlotProvider;
import data.bean.Army;
import data.bean.Guard;
import data.bean.March;
import define.ArmyState;
import inject.BeanManager;

@Singleton
public class WorldDataManager {

	// 世界地图上的玩家
	private Map<Integer, Player> posMap = new HashMap<Integer, Player>();

	// 世界地图分块上的玩家
	private Map<Integer, Map<Integer, Player>> areaMap = new HashMap<>();

	// 矿的防守阵型
	private Map<Integer, ConfMineForm> mineFormMap = new HashMap<>();

	// 全地图驻军数据
	// private Map<Integer, Guard> guardMap = new HashMap<>();
	private Map<Integer, List<Guard>> guardMap = new HashMap<>();

	// 全地图行军数据 MAP<POS>
	private Map<Integer, List<March>> marchMap = new HashMap<>();

	public static WorldDataManager getInst() {
		return BeanManager.getBean(WorldDataManager.class);
	}

	@PostConstruct
	private void init() {
		for (int i = 0; i < 1600; i++) {
			areaMap.put(i, new HashMap<Integer, Player>());
		}
	}

	static final int INVALID_POS_1 = 298 + 298 * 600;
	static final int INVALID_POS_2 = 299 + 298 * 600;
	static final int INVALID_POS_3 = 300 + 298 * 600;

	static final int INVALID_POS_4 = 298 + 299 * 600;
	static final int INVALID_POS_5 = 299 + 299 * 600;
	static final int INVALID_POS_6 = 300 + 299 * 600;

	static final int INVALID_POS_7 = 298 + 300 * 600;
	static final int INVALID_POS_8 = 299 + 300 * 600;
	static final int INVALID_POS_9 = 300 + 300 * 600;

	public boolean isValidPos(int pos) {
		if (pos == INVALID_POS_1 || pos == INVALID_POS_2 || pos == INVALID_POS_3 || pos == INVALID_POS_4 || pos == INVALID_POS_5 || pos == INVALID_POS_6 || pos == INVALID_POS_7 || pos == INVALID_POS_8 || pos == INVALID_POS_9) {
			return false;
		}

		return true;
	}

	public void addNewPlayer(Player player) {
		int pos;
		int slot;
		int xBegin;
		int yBegin;
		while (true) {
			slot = ConfSlotProvider.getInst().getSlot(posMap.size());
			xBegin = slot % 20 * 30;
			yBegin = slot / 20 * 30;
			pos = (RandomHelper.randomInSize(30) + xBegin) + (RandomHelper.randomInSize(30) + yBegin) * 600;
			if (posMap.containsKey(pos) || evaluatePos(pos) != null || !isValidPos(pos)) {
				continue;
			}
			break;
		}

		player.lord.setPos(pos);
		putPlayer(player);
	}

	/**
	 * 
	 * Method: slot
	 * 
	 * @Description: 15 x 15 的客户端拉区区域 @param pos @return @return int @throws
	 */
	private int area(int pos) {
		Turple<Integer, Integer> xy = reducePos(pos);
		return xy.getA() / 15 + xy.getB() / 15 * 40;
	}

	public List<Guard> getAreaMineGuard(int area) {
		int xBegin = area % 40 * 15;
		int xEnd = xBegin + 15;
		int yBegin = area / 40 * 15;
		int yEnd = yBegin + 15;
		int pos = 0;
		Guard guard;
		ArrayList<Guard> guards = new ArrayList<>();
		for (int i = xBegin; i < xEnd; i++) {
			for (int j = yBegin; j < yEnd; j++) {
				pos = i + j * 600;
				guard = getMineGuard(pos);
				if (guard != null && evaluatePos(pos) != null) {
					guards.add(guard);
				}
			}
		}

		return guards;
	}

	static public Turple<Integer, Integer> reducePos(int pos) {
		Turple<Integer, Integer> turple = new Turple<Integer, Integer>(pos % 600, pos / 600);
		return turple;
	}

	public static void main(String[] args) {
		Turple<Integer, Integer> xy = reducePos(128920);
		int x = xy.getA();
		int y = xy.getB();
		int index = x / 40 + y / 40 * 15;
		int reflection = (x % 40 + y % 40 * 40 + 13 * index) % 1600;
		System.out.println(reflection);
	}

	public ConfMine evaluatePos(int pos) {
		Turple<Integer, Integer> xy = reducePos(pos);
		int x = xy.getA();
		int y = xy.getB();
		int index = x / 40 + y / 40 * 15;
		int reflection = (x % 40 + y % 40 * 40 + 13 * index) % 1600;
		ConfMine staticMine = ConfMineProvider.getInst().getConfigById(reflection);
		return staticMine;
	}

	public void putPlayer(Player player) {
		int pos = player.lord.getPos();
		if (pos != -1) {
			posMap.put(pos, player);
			areaMap.get(area(pos)).put(pos, player);
		}
	}

	public void removePos(int pos) {
		posMap.remove(pos);
		areaMap.get(area(pos)).remove(pos);
	}

	public List<Player> getMap(int area) {
		List<Player> list = new ArrayList<>();
		Map<Integer, Player> slot = areaMap.get(area);
		Iterator<Player> it = slot.values().iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}

	public Player getPosData(int pos) {
		return posMap.get(pos);
	}

	public ConfMineForm getMineForm(int pos, int lv) {
		ConfMineForm form = mineFormMap.get(pos);
		if (form == null) {
			form = ConfMineFormProvider.getInst().randomForm(lv);
			mineFormMap.put(pos, form);
		}
		return form;
	}

	public void resetMineForm(int pos, int lv) {
		mineFormMap.put(pos, ConfMineFormProvider.getInst().randomForm(lv));
	}

	/**
	 * 
	 * Method: getGuard
	 * 
	 * @Description: 获取驻军 @param pos @return void @throws
	 */
	public List<Guard> getGuard(int pos) {
		return guardMap.get(pos);
	}

	public Guard getMineGuard(int pos) {
		List<Guard> list = guardMap.get(pos);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public Guard getHomeGuard(int pos) {
		List<Guard> list = guardMap.get(pos);
		if (list != null && !list.isEmpty()) {
			Guard guard = list.get(0);
			if (guard.getArmy().getState() == ArmyState.GUARD) {
				return guard;
			}
		}
		return null;
	}

	public void setGuard(Guard guard) {
		int pos = guard.getArmy().getTarget();
		List<Guard> list = guardMap.get(pos);
		if (list == null) {
			list = new ArrayList<>();
			guardMap.put(pos, list);
		}
		list.add(guard);
	}

	public void removeGuard(Guard guard) {
		int pos = guard.getArmy().getTarget();
		guardMap.get(pos).remove(guard);
	}

	public void removeGuard(int pos) {
		guardMap.remove(pos);
	}

	public void removeGuard(Player player, Army army) {
		int pos = army.getTarget();
		List<Guard> list = guardMap.get(pos);
		Guard e;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				e = list.get(i);
				if (e.getPlayer() == player && e.getArmy().getKeyId() == army.getKeyId()) {
					list.remove(i);
					break;
				}
			}
		}
	}

	public List<March> getMarch(int pos) {
		return marchMap.get(pos);
	}

	public void addMarch(March march) {
		int pos = march.getArmy().getTarget();
		List<March> list = marchMap.get(pos);
		if (list == null) {
			list = new ArrayList<>();
			marchMap.put(pos, list);
		}
		list.add(march);
	}

	public void removeMarch(Player player, Army army) {
		int pos = army.getTarget();
		List<March> list = marchMap.get(pos);
		March e;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				e = list.get(i);
				if (e.getPlayer() == player && e.getArmy().getKeyId() == army.getKeyId()) {
					list.remove(i);
					break;
				}
			}
		}
	}

	public Map<Integer, List<Guard>> getGuardMap() {
		return guardMap;
	}
}
