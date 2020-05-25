package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfEndConditionItem;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfEndConditionItemProvider extends BaseProvider<ConfEndConditionItem> {

	public static ConfEndConditionItemProvider getInst() {
		return BeanManager.getBean(ConfEndConditionItemProvider.class);
	}

	public ConfEndConditionItemProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfEndConditionItem[]> getClassType() {
		return ConfEndConditionItem[].class;
	}

	@Override
	protected ConfEndConditionItem convertDBResult2Bean(ASObject o) {
		ConfEndConditionItem conf = new ConfEndConditionItem();
		conf.setItemType(o.getInt("itemType"));
		conf.setItemId(o.getInt("itemId"));
		conf.setQuality(o.getInt("quality"));
		conf.setStar(o.getInt("star"));
		conf.setChatId(o.getInt("chatId"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfEndConditionItem conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
