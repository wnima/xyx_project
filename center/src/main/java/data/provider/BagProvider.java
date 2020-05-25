package data.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.inject.Singleton;

import config.bean.ConfShop;
import config.provider.ConfShopProvider;
import data.DataProvider;
import data.bean.Bag;
import define.MxwPropId;
import inject.BeanManager;
import mxw.PlayerManager;
import mxw.UserData;
import util.ASObject;

@Singleton
public class BagProvider extends DataProvider<Bag> {

	private static final Logger logger = LoggerFactory.getLogger(BagProvider.class);

	Object lock = new Object();

	HashBasedTable<Long, Integer, Bag> bags = HashBasedTable.create();

	public static BagProvider getInst() {
		return BeanManager.getBean(BagProvider.class);
	}

	public BagProvider() {
		super("p_bag");
	}

	@Override
	protected Class<Bag[]> getClassType() {
		return Bag[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected Bag convertDBResult2Bean(ASObject o) {
		Bag bean = new Bag();
		bean.setId(o.getLong("id"));
		bean.setUserId(o.getInt("userId"));
		bean.setPropId(o.getInt("propId"));
		bean.setPropNum(o.getInt("propNum"));
		bean.setState(o.getInt("state"));
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(Bag bean) {
		ASObject o = new ASObject();
		o.put("id", bean.getId());
		o.put("userId", bean.getUserId());
		o.put("propId", bean.getPropId());
		o.put("propNum", bean.getPropNum());
		o.put("state", bean.getState());
		return o;
	}

	@Override
	public void init() {
		getAllBean().forEach(e -> {
			bags.put(e.getUserId(), e.getPropId(), e);
		});
	}

	public Bag getById(long userId, int propId) {
		UserData user = PlayerManager.getInst().getPlayerById(userId);
		if (propId == MxwPropId.GLOD) {
			return new Bag(userId, propId, (int) user.getUser().getGold(), 1);
		} else if (propId == MxwPropId.COIN) {
			return new Bag(userId, propId, (int) user.getUser().getCoin(), 1);
		} else if (propId == MxwPropId.HP) {
			return new Bag(userId, propId, user.getUser().getPower(), 1);
		}
		return bags.get(userId, propId);
	}

	public Bag addProp(UserData userData, int propId, int propNum, int state) {
		long userId = userData.getPlayerId();
		if (propId == MxwPropId.GLOD) {
			userData.getUser().setGold(userData.getUser().getGold() + propNum);
			return new Bag(userId, propId, (int) userData.getUser().getGold(), 1);
		} else if (propId == MxwPropId.COIN) {
			userData.getUser().setCoin(userData.getUser().getCoin() + propNum);
			return new Bag(userId, propId, (int) userData.getUser().getCoin(), 1);
		} else if (propId == MxwPropId.HP) {
			userData.addPower(propNum);
			return new Bag(userId, propId, userData.getUser().getPower(), 1);
		}
		Bag bag = bags.get(userId, propId);
		if (bag == null) {
			bag = new Bag(userId, propId, propNum, state);
			insert(bag);
			putBean(bag);
			bags.put(userId, propId, bag);
			logger.info("添加道具 id:{} propId:{} propNum:{}", bag.getId(), bag.getPropId(), bag.getPropNum());
		} else {
			bag.setPropNum(bag.getPropNum() + propNum);
			udp(bag);
		}
		return bag;
	}

	public boolean subProp(UserData userData, int propId, int propNum) {
		long userId = userData.getPlayerId();
		if (propId == MxwPropId.GLOD) {
			if (userData.getUser().getGold() < propNum) {
				return false;
			}
			userData.getUser().setGold(userData.getUser().getGold() - propNum);
			return true;
		} else if (propId == MxwPropId.COIN) {
			if (userData.getUser().getCoin() < propNum) {
				return false;
			}
			userData.getUser().setCoin(userData.getUser().getCoin() - propNum);
			return true;
		} else if (propId == MxwPropId.HP) {
			if (userData.getUser().getPower() < propNum) {
				return false;
			}
			userData.reducePower(propNum);
			return true;
		}
		Bag bag = bags.get(userId, propId);
		if (bag == null || bag.getPropNum() < propNum) {
			return false;
		}

		bag.setPropNum(bag.getPropNum() - propNum);
		udp(bag);
		return true;
	}

	public void updState(UserData userData, int propId, int propNum, int state) {
		long userId = userData.getPlayerId();
		if (propId == MxwPropId.GLOD) {
			userData.getUser().setGold(propNum);
		} else if (propId == MxwPropId.COIN) {
			userData.getUser().setCoin(propNum);
		} else if (propId == MxwPropId.HP) {
			userData.getUser().setPower(propNum);
		} else {
			Bag bag = bags.get(userId, propId);
			if (bag != null) {// 更新背包
				bag.setPropNum(propNum);
				bag.setState(state);
				udp(bag);
			}
		}
	}

	public void setPropNum(UserData userData, int propId, int propNum) {
		long userId = userData.getPlayerId();
		if (propId == MxwPropId.GLOD) {
			userData.getUser().setGold(propNum);
		} else if (propId == MxwPropId.COIN) {
			userData.getUser().setCoin(propNum);
		} else if (propId == MxwPropId.HP) {
			userData.getUser().setPower(propNum);
		} else {
			Bag bag = bags.get(userId, propId);
			if (bag != null) {// 更新背包
				bag.setPropNum(propNum);
				udp(bag);
			} else {
				bag = new Bag(userId, propId, propNum, 1);
				insert(bag);
				putBean(bag);
				bags.put(userId, propId, bag);
				logger.info("添加道具 id:{} propId:{} propNum:{}", bag.getId(), bag.getPropId(), bag.getPropNum());
			}
		}

	}

	public int getPropNum(long userId, int propId) {
		Bag bag = getById(userId, propId);
		return bag == null ? 0 : bag.getPropNum();
	}
	
	public Map<Integer,Bag> getPropList(long userId) {
		Map<Integer, Bag> val = bags.row(userId);
		val = val != null ? new HashMap<>(val) : new HashMap<>();
		return val;
	}

	public int getPortrait(long userId) {
		List<ConfShop> shopList = ConfShopProvider.getInst().getConfigList(e -> e.getType() == 1);
		for (ConfShop e : shopList) {
			Bag bag = bags.get(userId, e.getId());
			if (bag != null && bag.getState() == 3) {
				return e.getId();
			}
		}
		return 1;
	}

	public Map<Integer, Bag> getByType(long userId, int type) {
		Map<Integer, Bag> r = new HashMap<>();
		List<ConfShop> rlist = ConfShopProvider.getInst().getConfigList(e -> e.getType() == type);
		for (ConfShop e : rlist) {
			Bag bag = bags.get(userId, e.getId());
			if (bag != null) {
				r.put(e.getId(), bag);
			}
		}
		return r;
	}

}
