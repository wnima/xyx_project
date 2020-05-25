package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfWarAward;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfWarAwardProvider extends BaseProvider<ConfWarAward> {

	public static ConfWarAwardProvider getInst() {
		return BeanManager.getBean(ConfWarAwardProvider.class);
	}

	public ConfWarAwardProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfWarAward[]> getClassType() {
		return ConfWarAward[].class;
	}

	@Override
	protected ConfWarAward convertDBResult2Bean(ASObject o) {
		ConfWarAward conf = new ConfWarAward();
		conf.setRank(o.getInt("vip"));
		conf.setRankAwards(JSON.parseObject(o.getString("rankAwards"), List.class));
		conf.setWinAwards(JSON.parseObject(o.getString("winAwards"), List.class));
		conf.setHurtAwards(JSON.parseObject(o.getString("hurtAwards"), List.class));
		conf.setScoreAwards(JSON.parseObject(o.getString("scoreAwards"), List.class));
		conf.setScorePartyAwards(JSON.parseObject(o.getString("scorePartyAwards"), List.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfWarAward conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}
	
	public List<List<Integer>> getRankAward(int rank) {
		return getConfigById(rank).getRankAwards();
	}

	public List<List<Integer>> getWinAward(int rank) {
		return getConfigById(rank).getWinAwards();
	}

	public List<List<Integer>> getHurtAward(int rank) {
		return getConfigById(rank).getHurtAwards();
	}

	public List<List<Integer>> getScoreAward(int rank) {
		return getConfigById(rank).getScoreAwards();
	}

	public List<List<Integer>> getScorePartyAward(int rank) {
		return getConfigById(rank).getScorePartyAwards();
	}

}
