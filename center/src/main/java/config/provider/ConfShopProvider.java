package config.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfShop;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfShopProvider extends BaseProvider<ConfShop> {

	private Map<Integer, ConfShop> shops = new ConcurrentHashMap<Integer, ConfShop>();

	public static ConfShopProvider getInst() {
		return BeanManager.getBean(ConfShopProvider.class);
	}

	public ConfShopProvider() {
		super("", "conf_shop");
	}

	@Override
	protected Class<ConfShop[]> getClassType() {
		return ConfShop[].class;
	}

	@Override
	protected ConfShop convertDBResult2Bean(ASObject o) {
		ConfShop conf = new ConfShop();
		conf.setId(o.getInt("id"));
		conf.setType(o.getInt("type"));
		conf.setItemId(o.getInt("itemId"));
		conf.setItemName(o.getString("itemName"));
		conf.setItemNum(o.getInt("itemNum"));
		conf.setGold(o.getInt("gold"));
		conf.setCoin(o.getInt("coin"));
		conf.setDesc(o.getString("desc"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfShop conf) {
		ASObject o = new ASObject();
		o.put("id", conf.getId());
		o.put("type", conf.getType());
		o.put("itemId", conf.getItemId());
		o.put("itemName", conf.getItemName());
		o.put("itemNum", conf.getItemNum());
		o.put("gold", conf.getGold());
		o.put("coin", conf.getCoin());
		o.put("desc", conf.getDesc());
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			shops.put(e.getId(), e);
		});
	}

}
