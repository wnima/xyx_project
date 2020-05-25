package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfSkill;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfSkillProvider extends BaseProvider<ConfSkill> {

	public static ConfSkillProvider getInst() {
		return BeanManager.getBean(ConfSkillProvider.class);
	}

	public ConfSkillProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfSkill[]> getClassType() {
		return ConfSkill[].class;
	}

	@Override
	protected ConfSkill convertDBResult2Bean(ASObject o) {
		ConfSkill conf = new ConfSkill();
		conf.setSkillId(o.getInt("skillId"));
		conf.setTarget(o.getInt("target"));
		conf.setAttr(o.getInt("attr"));
		conf.setAttrValue(o.getInt("attrValue"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfSkill conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
