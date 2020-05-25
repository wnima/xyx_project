package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.Party;
import data.bean.PartyMember;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class PartyMemberProvider extends DataProvider<PartyMember> {

	private static final Logger logger = LoggerFactory.getLogger(PartyMemberProvider.class);

	public static PartyMemberProvider getInst() {
		return BeanManager.getBean(PartyMemberProvider.class);
	}

	public PartyMemberProvider() {
		super("");
	}

	@Override
	protected Class<PartyMember[]> getClassType() {
		return PartyMember[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected PartyMember convertDBResult2Bean(ASObject o) {
		PartyMember bean = new PartyMember();
		bean.setLordId(o.getLong("lordId"));
		bean.setPartyId(o.getInt("partyId"));
		bean.setJob(o.getInt("job"));
		bean.setPrestige(o.getInt("prestige"));
		bean.setDonate(o.getInt("donate"));
		bean.setWeekDonate(o.getInt("weekDonate"));
		bean.setWeekAllDonate(o.getInt("weekAllDonate"));
		bean.setDonateTime(o.getInt("donateTime"));
		bean.setDayWeal(o.getInt("dayWeal"));
		bean.setHallMine(o.getBlob("hallMine"));
		bean.setScienceMine(o.getBlob("scienceMine"));
		bean.setWealMine(o.getBlob("wealMine"));
		bean.setPartyProp(o.getBlob("partyProp"));
		bean.setCombatId(o.getBlob("combatId"));
		bean.setRefreshTime(o.getInt("refreshTime"));
		bean.setCombatCount(o.getInt("combatCount"));
		bean.setApplyList(o.getString("applyList"));
		bean.setEnterTime(o.getInt("enterTime"));
		bean.setActivity(o.getInt("activity"));
		bean.setRegLv(o.getInt("regLv"));
		bean.setRegFight(o.getLong("regFight"));
		bean.setWarRecord(o.getBlob("warRecord"));
		bean.setRegTime(o.getInt("regTime"));
		bean.setWinCount(o.getInt("winCount"));
		bean.setRegParty(o.getInt("regParty"));
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(PartyMember bean) {
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
