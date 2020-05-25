package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.PartyScience;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class PartyScienceProvider extends DataProvider<PartyScience> {

	private static final Logger logger = LoggerFactory.getLogger(PartyScienceProvider.class);

	public static PartyScienceProvider getInst() {
		return BeanManager.getBean(PartyScienceProvider.class);
	}

	public PartyScienceProvider() {
		super("");
	}

	@Override
	protected Class<PartyScience[]> getClassType() {
		return PartyScience[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected PartyScience convertDBResult2Bean(ASObject o) {
		PartyScience bean = new PartyScience();
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(PartyScience bean) {
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
