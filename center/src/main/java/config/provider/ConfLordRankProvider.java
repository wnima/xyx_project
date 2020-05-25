package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfLordRank;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfLordRankProvider extends BaseProvider<ConfLordRank> {

	public static ConfLordRankProvider getInst() {
		return BeanManager.getBean(ConfLordRankProvider.class);
	}

	public ConfLordRankProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfLordRank[]> getClassType() {
		return ConfLordRank[].class;
	}

	@Override
	protected ConfLordRank convertDBResult2Bean(ASObject o) {
		ConfLordRank conf = new ConfLordRank();
		conf.setRankId(o.getInt("rankId"));
		conf.setLordLv(o.getInt("lordLv"));
		conf.setStoneCost(o.getInt("stoneCost"));
		conf.setFame(o.getInt("fame"));
		conf.setName(o.getString("name"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfLordRank conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
