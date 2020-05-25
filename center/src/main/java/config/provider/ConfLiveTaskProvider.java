package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfLiveTask;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfLiveTaskProvider extends BaseProvider<ConfLiveTask> {

	public static ConfLiveTaskProvider getInst() {
		return BeanManager.getBean(ConfLiveTaskProvider.class);
	}

	public ConfLiveTaskProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfLiveTask[]> getClassType() {
		return ConfLiveTask[].class;
	}

	@Override
	protected ConfLiveTask convertDBResult2Bean(ASObject o) {
		ConfLiveTask conf = new ConfLiveTask();
		conf.setTaskId(o.getInt("taskId"));
		conf.setTaskName(o.getString("taskName"));
		conf.setCount(o.getInt("count"));
		conf.setLive(o.getInt("live"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfLiveTask conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
