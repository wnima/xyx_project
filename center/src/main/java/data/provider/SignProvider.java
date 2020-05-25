package data.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.Sign;
import inject.BeanManager;
import util.ASObject;
import util.DateUtil;

@Singleton
public class SignProvider extends DataProvider<Sign> {

	private static final Logger logger = LoggerFactory.getLogger(SignProvider.class);

	private Map<Long, Sign> signs = new ConcurrentHashMap<Long, Sign>();

	public static SignProvider getInst() {
		return BeanManager.getBean(SignProvider.class);
	}

	public SignProvider() {
		super("p_sign");
	}

	@Override
	protected String getIdKey() {
		return "userId";
	}

	@Override
	protected Class<Sign[]> getClassType() {
		return Sign[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected Sign convertDBResult2Bean(ASObject o) {
		Sign bean = new Sign();
		bean.setUserId(o.getInt("userId"));
		bean.setSign1(o.getInt("sign1"));
		bean.setSign2(o.getInt("sign2"));
		bean.setSign3(o.getInt("sign3"));
		bean.setSign4(o.getInt("sign4"));
		bean.setSign5(o.getInt("sign5"));
		bean.setSign6(o.getInt("sign6"));
		bean.setSign7(o.getInt("sign7"));
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(Sign bean) {
		ASObject o = new ASObject();
		o.put("userId", bean.getUserId());
		o.put("sign1", bean.getSign1());
		o.put("sign2", bean.getSign2());
		o.put("sign3", bean.getSign3());
		o.put("sign4", bean.getSign4());
		o.put("sign5", bean.getSign5());
		o.put("sign6", bean.getSign6());
		o.put("sign7", bean.getSign7());
		return o;
	}

	@Override
	public void init() {
		getAllBean().forEach(e -> {
			signs.put(e.getUserId(), e);
		});
	}

	public Sign getSign(long userId) {
		Sign sign = signs.get(userId);
		if (sign == null) {
			sign = new Sign(userId);
			insert(sign);
			putBean(sign);
			signs.put(userId, sign);
		}
		return sign;
	}

	public boolean sign(long userId, int signId, int today) {
		Sign sign = signs.get(userId);
		if (sign == null) {
			return false;
		}
		if (signId == 1 && sign.getSign1() == 0) {
			sign.setSign1(today);
			putCache(sign);
			return true;
		} else if (signId == 2 && sign.getSign2() == 0) {
			sign.setSign2(today);
			udp(sign);
			return true;
		} else if (signId == 3 && sign.getSign3() == 0) {
			sign.setSign3(today);
			udp(sign);
			return true;
		} else if (signId == 4 && sign.getSign4() == 0) {
			sign.setSign4(today);
			udp(sign);
			return true;
		} else if (signId == 5 && sign.getSign5() == 0) {
			sign.setSign5(today);
			udp(sign);
			return true;
		} else if (signId == 6 && sign.getSign6() == 0) {
			sign.setSign6(today);
			udp(sign);
			return true;
		} else if (signId == 7 && sign.getSign7() == 0) {
			sign.setSign7(today);
			udp(sign);
			return true;
		}
		return false;
	}

	public int getSignId(long userId) {
		Sign sign = getSign(userId);
		int today = DateUtil.getToday();
		if (sign.getSign1() == 0) {
			return 1;
		} else if (sign.getSign2() == 0) {
			return sign.getSign1() == today ? 1 : 2;
		} else if (sign.getSign3() == 0) {
			return sign.getSign2() == today ? 2 : 3;
		} else if (sign.getSign4() == 0) {
			return sign.getSign3() == today ? 3 : 4;
		} else if (sign.getSign5() == 0) {
			return sign.getSign4() == today ? 4 : 5;
		} else if (sign.getSign6() == 0) {
			return sign.getSign5() == today ? 5 : 6;
		} else if (sign.getSign7() == 0) {
			return sign.getSign6() == today ? 6 : 7;
		}
		return -1;
	}

	public int getState(Sign sign, int signId) {
		if (signId == 1) {
			return sign.getSign1() == 0 ? 2 : 3;
		} else if (signId == 2) {
			return sign.getSign2() == 0 ? 2 : 3;
		} else if (signId == 3) {
			return sign.getSign3() == 0 ? 2 : 3;
		} else if (signId == 4) {
			return sign.getSign4() == 0 ? 2 : 3;
		} else if (signId == 5) {
			return sign.getSign5() == 0 ? 2 : 3;
		} else if (signId == 6) {
			return sign.getSign6() == 0 ? 2 : 3;
		} else if (signId == 7) {
			return sign.getSign7() == 0 ? 2 : 3;
		}
		return 3;
	}

	public int getState(long userId) {
		Sign sign = getSign(userId);
		int signId = getSignId(userId);
		return getState(sign, signId);
	}
}
