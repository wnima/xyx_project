//package manager;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.DefaultTransactionDefinition;
//
//import com.game.dao.impl.p.AccountDao;
//import com.game.dao.impl.p.ArenaDao;
//import com.game.dao.impl.p.BossDao;
//import com.game.dao.impl.p.BuildingDao;
//import com.game.dao.impl.p.DataNewDao;
//import com.game.dao.impl.p.LordDao;
//import com.game.dao.impl.p.PartyDao;
//import com.game.dao.impl.p.ResourceDao;
//import com.game.dao.impl.p.TipGuyDao;
//import com.game.dataMgr.StaticBuildingDataMgr;
//import com.game.dataMgr.StaticHeroDataMgr;
//import com.game.dataMgr.StaticIniDataMgr;
//import com.game.dataMgr.StaticLordDataMgr;
//import com.game.dataMgr.StaticMailDataMgr;
//import com.game.dataMgr.StaticPropDataMgr;
//import com.game.dataMgr.StaticRefineDataMgr;
//import com.game.dataMgr.StaticStaffingDataMgr;
//import com.game.dataMgr.StaticTankDataMgr;
//import com.game.dataMgr.StaticTaskDataMgr;
//import com.game.dataMgr.StaticVipDataMgr;
//import com.game.domain.Guy;
//import com.game.domain.Member;
//import com.game.domain.PartyData;
//import com.game.domain.Player;
//import com.game.domain.Role;
//import config.bean.ConfHero;
//import config.bean.ConfIniLord;
//import config.bean.ConfLordCommand;
//import config.bean.ConfLordLv;
//import config.bean.ConfLordPros;
//import config.bean.ConfMail;
//import config.bean.ConfMailPlat;
//import config.bean.ConfMine;
//import config.bean.ConfProp;
//import config.bean.ConfRefine;
//import config.bean.ConfTank;
//import config.bean.ConfTask;
//import config.bean.ConfVip;
////import com.game.server.GameServer;
////import com.game.service.ChatService;
////import com.game.service.WorldService;
//import com.game.util.LogHelper;
//import com.game.util.PbHelper;
//import com.google.inject.Singleton;
//import com.google.protobuf.InvalidProtocolBufferException;
//
//import config.bean.ConfBuildingLv;
//import data.bean.Account;
//import data.bean.Arena;
//import data.bean.Army;
//import data.bean.ArmyStatu;
//import data.bean.BuildQue;
//import data.bean.Building;
//import data.bean.Chip;
//import data.bean.Collect;
//import data.bean.DataNew;
//import data.bean.Effect;
//import data.bean.Equip;
//import data.bean.FailNum;
//import data.bean.Form;
//import data.bean.Grab;
//import data.bean.Guard;
//import data.bean.Hero;
//import data.bean.Lord;
//import data.bean.Mail;
//import data.bean.Man;
//import data.bean.March;
//import data.bean.MilitaryMaterial;
//import data.bean.Mill;
//import data.bean.Part;
//import data.bean.PartyScience;
//import data.bean.Prop;
//import data.bean.Resource;
//import data.bean.RptTank;
//import data.bean.Ruins;
//import data.bean.Science;
//import data.bean.Tank;
//import data.bean.Task;
//import data.bean.TipGuy;
//import data.provider.AccountProvider;
//import define.ActivityConst;
//import define.ArmyState;
//import define.AwardFrom;
//import define.AwardType;
//import define.BuildingId;
//import define.EffectType;
//import define.FormType;
//import define.GoldCost;
//import define.MailType;
//import define.PartyType;
//import define.ScienceId;
//import define.SysChatId;
//import define.TaskType;
//import pb.ActivityPb.SynApplyRq;
//import pb.ActivityPb.SynGoldRq;
//import pb.CommonPb;
//import pb.CommonPb.Report;
//import pb.GamePb.SynArmyRq;
//import pb.GamePb.SynBlessRq;
//import pb.GamePb.SynBuildRq;
//import pb.GamePb.SynInvasionRq;
//import pb.GamePb.SynMailRq;
//import pb.GamePb.SynPartyAcceptRq;
//import pb.GamePb.SynPartyOutRq;
//import pb.GamePb.SynResourceRq;
//import pb.GamePb.SynStaffingRq;
//import pb.GamePb.SynWarRecordRq;
//import pb.GamePb.SynWarStateRq;
//
//@Singleton
//public class AccountDataManager implements PlayerDM {
////	@Autowired
////	private AccountDao accountDao;
////
////	@Autowired
////	private LordDao lordDao;
////
////	@Autowired
////	private TipGuyDao tipGuyDao;
////
////	@Autowired
////	private ResourceDao resourceDao;
////
////	@Autowired
////	private BuildingDao buildingDao;
////
////	@Autowired
////	private DataNewDao dataDao;
////
////	@Autowired
////	private ArenaDao arenaDao;
////
////	@Autowired
////	private PartyDao partyDao;
////
////	@Autowired
////	private BossDao bossDao;
////
////	@Autowired
////	private StaticIniDataMgr staticIniDataMgr;
////
////	@Autowired
////	private StaticLordDataMgr staticLordDataMgr;
////
////	@Autowired
////	private StaticTankDataMgr staticTankDataMgr;
////
////	@Autowired
////	private StaticBuildingDataMgr staticBuildingDataMgr;
////
////	@Autowired
////	private StaticRefineDataMgr staticRefineDataMgr;
////
////	@Autowired
////	private StaticTaskDataMgr staticTaskDataMgr;
////
////	@Autowired
////	private StaticMailDataMgr staticMailDataMgr;
////
////	@Autowired
////	private StaticHeroDataMgr staticHeroDataMgr;
////
////	@Autowired
////	private StaticPropDataMgr staticPropDataMgr;
////
////	@Autowired
////	private StaticVipDataMgr staticVipDataMgr;
////
////	@Autowired
////	private StaticStaffingDataMgr staticStaffingDataMgr;
////
////	@Autowired
////	private ArenaDataManager arenaDataManager;
////
////	@Autowired
////	private PartyDataManager partyDataManager;
////
////	@Autowired
////	private ActivityDataManager activityDataManager;
////
////	@Autowired
////	private WorldDataManager worldDataManager;
////
////	@Autowired
////	private RankDataManager rankDataManager;
////
////	@Autowired
////	private SeniorMineDataManager seniorMineDataManager;
////
////	@Autowired
////	private StaffingDataManager staffingDataManager;
////
////	@Autowired
////	private SmallIdManager smallIdManager;
////
////	@Autowired
////	private ChatService chatService;
////
////	@Autowired
////	private WorldService worldService;
//
//	public boolean inited = false;
//
//	// 能量上限
//	public static final int POWER_MAX = 20;
//
//	// 恢复1点能量秒数
//	public static final int POWER_BACK_SECOND = 30 * 60;
//
//	// 恢复1点繁荣度秒数
//	public static final int PROS_BACK_SECOND = 60;
//
//	// 装备最高等级
//	public static final int MAX_EQUIP_LV = 80;
//
//	// 装备仓库最大上限容量
//	public static final int EQUIP_STORE_LIMIT = 300;
//
//	// 配件最高强化等级
//	public static final int MAX_PART_UP_LV = 80;
//
//	// 配件最高改造等级
//	public static final int MAX_PART_REFIT_LV = 4;
//
//	// 配件仓库最大上限容量
//	public static final int PART_STORE_LIMIT = 300;
//
//	@PostConstruct
//	public void init() {
//		for (int i = 0; i < 5000; i++) {
//			accountCache.put(i + 1, new ConcurrentHashMap<Integer, Account>());
//		}
//		loadAllPlayer();
//		inited = true;
//	}
//
//	// MAP<serverid, MAP<accountKey, Player>>
//	private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Account>> accountCache = new ConcurrentHashMap<>();
//
//	public Account getAccount(int serverId, int accountKey) {
//		Account account = accountCache.get(serverId).get(accountKey);
//		return account;
//	}
//
//	private Account createAccount(Account account, Player player) {
//		account.setLordId(player.roleId);
//		AccountProvider.getInst().insert(account);
////		accountDao.insertAccount(account);
//		player.account = account;
//
//		accountCache.get(account.getServerId()).put(account.getAccountKey(), account);
//		return account;
//	}
//
//	private Account createAccountAfterCutSmallId(Account account, Player player) {
//		account.setLordId(player.roleId);
//		player.account = account;
//		account.setCreated(0);
//		accountDao.updateIordId(account);
//		accountCache.get(account.getServerId()).put(account.getAccountKey(), account);
//		return account;
//	}
//
//	public void recordLogin(Account account) {
////		accountDao.recordLoginTime(account);
//		account.setLoginDate(new Date());
//	}
//
//	// public boolean loadPlayerFromDb(Long roleId) {
//	// return new Load().loadPlayer(roleId);
//	// }
//
//}
