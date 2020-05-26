package config.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfGameType;
import inject.BeanManager;
import util.ASObject;
@Singleton
public class ConfGameTypeProvider extends BaseProvider<ConfGameType> {

	private Map<Integer,String> types = new HashMap<Integer, String>();
	
	protected ConfGameTypeProvider() {
		super("","conf_game_type");
	}
	
	public static ConfGameTypeProvider getInst() {
		return BeanManager.getBean(ConfGameTypeProvider.class);
	}

	@Override
	protected Class<ConfGameType[]> getClassType() {
		return ConfGameType[].class;
	}

	@Override
	protected ConfGameType convertDBResult2Bean(ASObject o) {
		ConfGameType bean = new ConfGameType();
		bean.setId(o.getInt("id"));
		bean.setName(o.getString("name"));
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(ConfGameType bean) {
		ASObject o = new ASObject();
		o.put("id", bean.getId());
		o.put("name", bean.getName());
		return o;
	}
	
	@Override
	protected void init() {
		getAllConfig().forEach(e -> {
			types.put((int)e.getId(),e.getName());
		});
	}

	public Map<Integer, String> getTypes() {
		return types;
	}

}
