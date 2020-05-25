package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.Party;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class PartyProvider extends DataProvider<Party> {

	private static final Logger logger = LoggerFactory.getLogger(PartyProvider.class);

	public static PartyProvider getInst() {
		return BeanManager.getBean(PartyProvider.class);
	}

	public PartyProvider() {
		super("");
	}

	@Override
	protected Class<Party[]> getClassType() {
		return Party[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected Party convertDBResult2Bean(ASObject o) {
		Party bean = new Party();
		bean.setPartyId(o.getInt("partyId"));
		bean.setPartyName(o.getString("partyName"));
		bean.setLegatusName(o.getString("legatusName"));
		bean.setPartyLv(o.getInt("partyLv"));
		bean.setScienceLv(o.getInt("scienceLv"));
		bean.setWealLv(o.getInt("wealLv"));
		bean.setLively(o.getInt("lively"));
		bean.setBuild(o.getInt("build"));
		bean.setFight(o.getLong("fight"));
		bean.setApply(o.getInt("apply"));
		bean.setApplyLv(o.getInt("applyLv"));
		bean.setApplyFight(o.getInt("applyFight"));
		bean.setSlogan(o.getString("slogan"));
		bean.setInnerSlogan(o.getString("innerSlogan"));
		bean.setJobName1(o.getString("jobName1"));
		bean.setJobName2(o.getString("jobName2"));
		bean.setJobName3(o.getString("jobName3"));
		bean.setJobName4(o.getString("jobName4"));
		bean.setMine(o.getBlob("mine"));
		bean.setScience(o.getBlob("science"));
		bean.setApplyList(o.getBlob("applyList"));
		bean.setTrend(o.getBlob("trend"));
		bean.setPartyCombat(o.getBlob("partyCombat"));
		bean.setRefreshTime(o.getInt("refreshTime"));
		bean.setLiveTask(o.getBlob("liveTask"));
		bean.setActivity(o.getBlob("activity"));
		bean.setAmyProps(o.getBlob("amyProps"));
		bean.setWarRecord(o.getBlob("warRecord"));
		bean.setRegLv(o.getInt("regLv"));
		bean.setWarRank(o.getInt("warRank"));
		bean.setRegFight(o.getLong("regFight"));
		bean.setShopProps(o.getString("shopProps"));
		bean.setScore(o.getInt("score"));
		bean.setDonates(o.getBlob("donates"));
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(Party bean) {
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
