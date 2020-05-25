package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.Resource;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ResourceProvider extends DataProvider<Resource> {

	private static final Logger logger = LoggerFactory.getLogger(ResourceProvider.class);

	public static ResourceProvider getInst() {
		return BeanManager.getBean(ResourceProvider.class);
	}

	public ResourceProvider() {
		super("");
	}

	@Override
	protected Class<Resource[]> getClassType() {
		return Resource[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected Resource convertDBResult2Bean(ASObject o) {
		Resource bean = new Resource();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(Resource bean) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

	@Override
	protected void cronTask() {
	}

}
