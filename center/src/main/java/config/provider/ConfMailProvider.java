package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfMail;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfMailProvider extends BaseProvider<ConfMail> {

	public static ConfMailProvider getInst() {
		return BeanManager.getBean(ConfMailProvider.class);
	}

	public ConfMailProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfMail[]> getClassType() {
		return ConfMail[].class;
	}

	@Override
	protected ConfMail convertDBResult2Bean(ASObject o) {
		ConfMail conf = new ConfMail();
		conf.setMoldId(o.getInt("moldId"));
		conf.setType(o.getInt("type"));
		conf.setSname(o.getString("sname"));
		conf.setMtitle(o.getString("mtitle"));
		conf.setMcontent(o.getString("mcontent"));
		conf.setParam(o.getString("param"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfMail conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
