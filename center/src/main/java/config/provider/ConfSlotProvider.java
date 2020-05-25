package config.provider;

import com.game.util.RandomHelper;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfSlot;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfSlotProvider extends BaseProvider<ConfSlot> {

	public static ConfSlotProvider getInst() {
		return BeanManager.getBean(ConfSlotProvider.class);
	}

	public ConfSlotProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfSlot[]> getClassType() {
		return ConfSlot[].class;
	}

	@Override
	protected ConfSlot convertDBResult2Bean(ASObject o) {
		ConfSlot conf = new ConfSlot();
		conf.setKeyId(o.getInt("keyId"));
		conf.setSlotA(o.getInt("slotA"));
		conf.setSlotB(o.getInt("slotB"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfSlot conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	/**
	 * 
	 * Method: getSlot
	 * 
	 * @Description: 根据地图上的玩家数量，分配新进入地图玩家的slot(0 ~ 399) @param
	 *               playerNumber @return @return int @throws
	 */
	public int getSlot(int playerNumber) {
		int index = playerNumber / 400;
		if (index > 199) {
			return RandomHelper.randomInSize(400);
		} else {
			// return 125;
			ConfSlot staticSlot = getAllConfig().get(index);
			if (playerNumber % 2 == 0) {
				return staticSlot.getSlotA();
			} else {
				return staticSlot.getSlotB();
			}
		}
	}

}
