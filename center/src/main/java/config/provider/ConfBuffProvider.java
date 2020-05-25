package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfBuff;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfBuffProvider extends BaseProvider<ConfBuff> {

	public static ConfBuffProvider getInst() {
		return BeanManager.getBean(ConfBuffProvider.class);
	}

	public ConfBuffProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfBuff[]> getClassType() {
		return ConfBuff[].class;
	}

	@Override
	protected ConfBuff convertDBResult2Bean(ASObject o) {
		ConfBuff conf = new ConfBuff();
		conf.setBuffId(o.getInt("buffId"));
		conf.setGroupId(o.getInt("groupId"));
		conf.setType(o.getInt("type"));
		conf.setTarget(o.getInt("target"));
		conf.setEffectType(o.getInt("effectType"));
		conf.setEffectValue(o.getInt("effectValue"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfBuff conf) {
		ASObject o = new ASObject();
		o.put("buffId", conf.getBuffId());
		o.put("groupId", conf.getGroupId());
		o.put("type", conf.getType());
		o.put("target", conf.getTarget());
		o.put("effectType", conf.getEffectType());
		o.put("effectValue", conf.getEffectValue());
		return o;
	}

	@Override
	public void init() {
	}

}
