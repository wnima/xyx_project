package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfLordCommand;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfLordCommandProvider extends BaseProvider<ConfLordCommand> {

	public static ConfLordCommandProvider getInst() {
		return BeanManager.getBean(ConfLordCommandProvider.class);
	}

	public ConfLordCommandProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfLordCommand[]> getClassType() {
		return ConfLordCommand[].class;
	}

	@Override
	protected ConfLordCommand convertDBResult2Bean(ASObject o) {
		ConfLordCommand conf = new ConfLordCommand();
		conf.setCommandLv(o.getInt("commandLv"));
		conf.setBook(o.getInt("book"));
		conf.setProb(o.getInt("prob"));
		conf.setTankCount(o.getInt("tankCount"));
		conf.setA(o.getInt("a"));
		conf.setB(o.getInt("b"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfLordCommand conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
