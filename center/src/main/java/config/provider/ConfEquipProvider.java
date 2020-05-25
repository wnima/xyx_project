package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfEquip;
import config.bean.ConfEquipLv;
import data.bean.Equip;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfEquipProvider extends BaseProvider<ConfEquip> {

	public static ConfEquipProvider getInst() {
		return BeanManager.getBean(ConfEquipProvider.class);
	}

	public ConfEquipProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfEquip[]> getClassType() {
		return ConfEquip[].class;
	}

	@Override
	protected ConfEquip convertDBResult2Bean(ASObject o) {
		ConfEquip conf = new ConfEquip();
		conf.setEquipId(o.getInt("equipId"));
		conf.setQuality(o.getInt("quality"));
		conf.setAttributeId(o.getInt("attributeId"));
		conf.setA(o.getInt("a"));
		conf.setB(o.getInt("b"));
		conf.setPrice(o.getInt("price"));
		conf.setEquipName(o.getString("equipName"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfEquip conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {

	}

	public boolean addEquipExp(Equip equip, int add) {
		ConfEquip staticEquip = getConfigById(equip.getEquipId());
		int quality = staticEquip.getQuality();
		int oriLv = equip.getLv();
		int lv = oriLv;
		int curExp = equip.getExp() + add;
		while (true) {
			ConfEquipLv staticEquipLv = ConfEquipLvProvider.getInst().getConfEquipLv(quality, lv + 1);
			if (staticEquipLv == null) {
				break;
			}

			if (curExp >= staticEquipLv.getNeedExp()) {
				lv++;
				curExp -= staticEquipLv.getNeedExp();
			} else {
				break;
			}
		}

		equip.setLv(lv);
		equip.setExp(curExp);
		return oriLv != lv;
	}

}
