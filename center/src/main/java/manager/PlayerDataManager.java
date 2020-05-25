package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import com.game.domain.Guy;
import com.game.domain.PartyData;
import com.game.domain.Player;
//import com.game.server.GameServer;
//import com.game.service.ChatService;
//import com.game.service.WorldService;
import com.game.util.LogHelper;
import com.game.util.PbHelper;
import com.google.inject.Singleton;

import config.bean.ConfBuildingLv;
import config.bean.ConfHero;
import config.bean.ConfIniLord;
import config.bean.ConfLordCommand;
import config.bean.ConfLordLv;
import config.bean.ConfLordPros;
import config.bean.ConfMail;
import config.bean.ConfMailPlat;
import config.bean.ConfMine;
import config.bean.ConfProp;
import config.bean.ConfRefine;
import config.bean.ConfTank;
import config.bean.ConfVip;
import config.provider.ConfBuildingLvProvider;
import config.provider.ConfHeroProvider;
import config.provider.ConfIniLordProvider;
import config.provider.ConfLordCommandProvider;
import config.provider.ConfLordLvProvider;
import config.provider.ConfLordProsProvider;
import config.provider.ConfMailPlatProvider;
import config.provider.ConfMailProvider;
import config.provider.ConfPropProvider;
import config.provider.ConfRefineProvider;
import config.provider.ConfTankProvider;
import config.provider.ConfVipProvider;
import data.bean.Account;
import data.bean.Arena;
import data.bean.Army;
import data.bean.ArmyStatu;
import data.bean.BuildQue;
import data.bean.Building;
import data.bean.Chip;
import data.bean.Collect;
import data.bean.Effect;
import data.bean.Equip;
import data.bean.FailNum;
import data.bean.Form;
import data.bean.Grab;
import data.bean.Hero;
import data.bean.Lord;
import data.bean.Mail;
import data.bean.Man;
import data.bean.March;
import data.bean.MilitaryMaterial;
import data.bean.Mill;
import data.bean.Part;
import data.bean.PartyScience;
import data.bean.Prop;
import data.bean.Resource;
import data.bean.RptTank;
import data.bean.Ruins;
import data.bean.Science;
import data.bean.Tank;
import data.provider.AccountProvider;
import data.provider.ArenaProvider;
import data.provider.BuildingProvider;
import data.provider.LordDataProvider;
import data.provider.LordProvider;
import data.provider.ResourceProvider;
import define.AwardFrom;
import define.AwardType;
import define.BuildingId;
import define.EffectType;
import define.FormType;
import define.GoldCost;
import define.MailType;
import define.PartyType;
import define.ScienceId;
import define.SysChatId;
import define.TaskType;
import domain.Member;
import inject.BeanManager;
import network.handler.module.tank.ChatService;
import pb.ActivityPb.SynApplyRq;
import pb.ActivityPb.SynGoldRq;
import pb.CommonPb;
import pb.CommonPb.AwardPB;
import pb.GamePb.SynArmyRq;
import pb.GamePb.SynBlessRq;
import pb.GamePb.SynBuildRq;
import pb.GamePb.SynInvasionRq;
import pb.GamePb.SynPartyAcceptRq;
import pb.GamePb.SynPartyOutRq;
import pb.GamePb.SynResourceRq;
import pb.GamePb.SynStaffingRq;
import pb.GamePb.SynWarRecordRq;
import pb.GamePb.SynWarStateRq;
import util.DateUtil;

@Singleton
public class PlayerDataManager  {

	public boolean inited = false;

	// 能量上限
	public static final int POWER_MAX = 20;

	// 恢复1点能量秒数
	public static final int POWER_BACK_SECOND = 30 * 60;

	// 恢复1点繁荣度秒数
	public static final int PROS_BACK_SECOND = 60;

	// 装备最高等级
	public static final int MAX_EQUIP_LV = 80;

	// 装备仓库最大上限容量
	public static final int EQUIP_STORE_LIMIT = 300;

	// 配件最高强化等级
	public static final int MAX_PART_UP_LV = 80;

	// 配件最高改造等级
	public static final int MAX_PART_REFIT_LV = 4;

	// 配件仓库最大上限容量
	public static final int PART_STORE_LIMIT = 300;

	public static PlayerDataManager getInst() {
		return BeanManager.getBean(PlayerDataManager.class);
	}

	@PostConstruct
	public void init() {
		loadAllPlayer();
		inited = true;
	}

	// MAP<serverid, MAP<accountKey, Player>>
	private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Account>> accountCache = new ConcurrentHashMap<>();

	// MAP<roleId, Player>
	private ConcurrentHashMap<Long, Player> playerCache = new ConcurrentHashMap<>();

	private ConcurrentHashMap<String, Player> onlinePlayer = new ConcurrentHashMap<>();

	private ConcurrentHashMap<String, Player> allPlayer = new ConcurrentHashMap<>();

	// 新建玩家
	private ConcurrentHashMap<Long, Player> newPlayerCache = new ConcurrentHashMap<>();

	// 新建玩家
	private Map<Long, Guy> guyMap = new HashMap<Long, Guy>();

	private Set<String> usedNames = Collections.synchronizedSet(new HashSet<String>());

	public void loadAllPlayer() {
		LogHelper.ERROR_LOGGER.error("load all players data!!");
	}

	public Map<Long, Player> getPlayers() {
		return playerCache;
	}

	public Player createPlayer(Account account) {
		Player player = initPlayerData(account);
		return player;
	}

	public Account getAccount(int serverId, int accountKey) {
		Account account = accountCache.get(serverId).get(accountKey);
		return account;
	}

	public Player getPlayer(Long roleId) {
		return playerCache.get(roleId);
	}

	public Player getPlayer(String nick) {
		return allPlayer.get(nick);
	}

	public Player getNewPlayer(Long roleId) {
		return newPlayerCache.get(roleId);
	}

	public Player removeNewPlayer(Long roleId) {
		return newPlayerCache.remove(roleId);
	}

	public void addPlayer(Player player) {
		playerCache.put(player.roleId, player);
		allPlayer.put(player.lord.getNick(), player);
	}

	public void addOnline(Player player) {
		onlinePlayer.put(player.lord.getNick(), player);
	}

	public void removeOnline(Player player) {
		onlinePlayer.remove(player.lord.getNick());
	}

	public Player getOnlinePlayer(String nick) {
		return onlinePlayer.get(nick);
	}

	public Map<String, Player> getAllOnlinePlayer() {
		return onlinePlayer;
	}

	public Map<Long, Guy> getGuyMap() {
		return guyMap;
	}

	/**
	 * 
	 * Method: initPlayerData
	 * 
	 * @Description: 初始化玩家数据 @return @return Player @throws
	 */
	private Player initPlayerData(Account account) {
		ConfIniLord confIniLord = ConfIniLordProvider.getInst().getConfigById(0);

		Lord lord = createLord(confIniLord);

		Player player = new Player(lord, DateUtil.getSecondTime());
		// createAccount(account, player);
		newPlayerCache.put(player.roleId, player);
		return player;
	}

	public boolean createFullPlayer(Player player) {
		ConfIniLord confIniLord = ConfIniLordProvider.getInst().getConfigById(0);
		// DefaultTransactionDefinition def = new
		// DefaultTransactionDefinition();
		// def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		// DataSourceTransactionManager txManager =
		// (DataSourceTransactionManager)
		// GameServer.ac.getBean("transactionManager");
		// TransactionStatus status = txManager.getTransaction(def);
		// try {
		createTanks(player, confIniLord);
		createBuilding(player);
		createResource(player, confIniLord);
		createForms(player);
		createBuildQue(player);
		createArmys(player);
		createTankQue(player);
		createRefitQue(player);
		createProps(player, confIniLord);
		createPropQue(player);
		createEquip(player, confIniLord);
		createPart(player);
		createChip(player);
		createCombat(player);
		createExplore(player);
		// createPartyMember(player);
		createData(player);

		AccountProvider.getInst().insert(player.account);
		// } catch (Exception ex) {
		// txManager.rollback(status);
		// return false;
		// }
		//
		// txManager.commit(status);

		return true;
	}

	private Lord createLord(ConfIniLord staticIniLord) {
		Lord lord = new Lord();
		int now = DateUtil.getSecondTime();
		if (staticIniLord != null) {
			lord.setLevel(staticIniLord.getLevel());
			lord.setVip(staticIniLord.getVip());
			lord.setGold(staticIniLord.getGoldGive());
			lord.setGoldGive(staticIniLord.getGoldGive());
			lord.setHuangbao(0);
			lord.setRanks(staticIniLord.getRanks());
			lord.setCommand(staticIniLord.getCommand());
			lord.setFameLv(staticIniLord.getFameLv());
			lord.setHonour(staticIniLord.getHonour());
			lord.setPros(staticIniLord.getProsMax());
			lord.setProsMax(staticIniLord.getProsMax());
			lord.setProsTime(now);
			lord.setPower(staticIniLord.getPower());
			lord.setPowerTime(now);
			lord.setNewState(staticIniLord.getNewState());
		} else {
			lord.setLevel(1);
			lord.setVip(0);
			lord.setGold(1000);
			lord.setGoldGive(1000);
			lord.setHuangbao(0);
			lord.setRanks(1);
			lord.setCommand(0);
			lord.setFameLv(1);
			lord.setHonour(0);
			lord.setPros(0);
			lord.setProsMax(0);
			lord.setProsTime(now);
			lord.setPower(20);
			lord.setPowerTime(now);
			lord.setNewState(0);
		}

		lord.setPos(-1);
		lord.setEquip(100);
		lord.setEplrTime(DateUtil.getToday());

		LordProvider.getInst().insert(lord);
		return lord;
	}

	private void createResource(Player player, ConfIniLord confIniLord) {
		Resource resource = new Resource();
		resource.setLordId(player.roleId);
		resource.setStone(confIniLord.getStone());
		resource.setIron(confIniLord.getIron());
		resource.setOil(confIniLord.getOil());
		resource.setCopper(confIniLord.getCopper());
		resource.setSilicon(confIniLord.getSilicon());

		resource.settStone(confIniLord.getStone());
		resource.settIron(confIniLord.getIron());
		resource.settOil(confIniLord.getOil());
		resource.settCopper(confIniLord.getCopper());
		resource.settSilicon(confIniLord.getSilicon());
		resource.setStoreTime(DateUtil.getMinuteTime());

		ConfBuildingLv staticCommand = ConfBuildingLvProvider.getInst().getConfBuildingLevel(BuildingId.COMMAND, 1);
		resource.setStoneOut(staticCommand.getStoneOut());
		resource.setIronOut(staticCommand.getIronOut());
		resource.setOilOut(staticCommand.getOilOut());
		resource.setCopperOut(staticCommand.getCopperOut());
		resource.setSiliconOut(staticCommand.getSiliconOut());

		resource.setStoneMax(staticCommand.getStoneMax());
		resource.setIronMax(staticCommand.getIronMax());
		resource.setOilMax(staticCommand.getOilMax());
		resource.setCopperMax(staticCommand.getCopperMax());
		resource.setSiliconMax(staticCommand.getSiliconMax());
		ResourceProvider.getInst().insert(resource);
		player.resource = resource;
	}

	private void createBuilding(Player player) {
		Building building = new Building();
		building.setLordId(player.roleId);
		building.setCommand(1);
		building.setFactory1(1);
		BuildingProvider.getInst().insert(building);
		player.building = building;
	}

	private void createData(Player player) {
		player.setMaxKey(0);

		ConfMail staticMail = ConfMailProvider.getInst().getConfigById(MailType.MOLD_WELCOME_MUZHI);
		if (staticMail == null) {
			return;
		}

		int type = staticMail.getType();
		Account account = player.account;

		if (account != null) {
			List<ConfMailPlat> platMailList = ConfMailPlatProvider.getInst().getConfigList(e -> e.getPlatNo() == account.getPlatNo());
			if (platMailList != null) {
				for (ConfMailPlat e : platMailList) {
					Mail mail = new Mail(player.maxKey(), type, e.getMailId(), MailType.STATE_UNREAD, DateUtil.getSecondTime());
					player.mails.put(mail.getKeyId(), mail);
				}
			} else {
				Mail mail = new Mail(player.maxKey(), type, MailType.MOLD_WELCOME_MUZHI, MailType.STATE_UNREAD, DateUtil.getSecondTime());
				player.mails.put(mail.getKeyId(), mail);
			}
		} else {
			Mail mail = new Mail(player.maxKey(), type, MailType.MOLD_WELCOME_MUZHI, MailType.STATE_UNREAD, DateUtil.getSecondTime());
			player.mails.put(mail.getKeyId(), mail);
		}

		Mail mail = new Mail(player.maxKey(), type, MailType.MOLD_WELCOME_FANGPIAN, MailType.STATE_UNREAD, DateUtil.getSecondTime());
		player.mails.put(mail.getKeyId(), mail);

		LordDataProvider.getInst().insert(player.serNewData());
	}

	// private void createPartyMember(Player player) {
	// int day = TimeHelper.getCurrentDay();
	// partyDataManager.createMember(player.lord, PartyType.COMMON, day);
	// }

	private void createForms(Player player) {

	}

	private void createTanks(Player player, ConfIniLord confIniLord) {
		Map<Integer, Integer> staticTanks = confIniLord.getTanks();
		if (staticTanks == null) {
			return;
		}

		Map<Integer, Tank> map = player.tanks;
		for (Map.Entry<Integer, Integer> entry : staticTanks.entrySet()) {
			Tank tank = new Tank(entry.getKey(), entry.getValue(), 0);
			map.put(tank.getTankId(), tank);
		}
	}

	private void createBuildQue(Player player) {

	}

	private void createArmys(Player player) {

	}

	private void createTankQue(Player player) {

	}

	private void createRefitQue(Player player) {

	}

	private void createProps(Player player, ConfIniLord confIniLord) {
		Map<Integer, Integer> staticProps = confIniLord.getProps();
		if (staticProps == null) {
			return;
		}

		Map<Integer, Prop> map = player.props;
		for (Map.Entry<Integer, Integer> entry : staticProps.entrySet()) {
			Prop prop = new Prop(entry.getKey(), entry.getValue());
			map.put(prop.getPropId(), prop);
		}
	}

	private void createEquip(Player player, ConfIniLord confIniLord) {
		Map<Integer, Integer> staticEquips = confIniLord.getEquips();
		if (staticEquips == null) {
			return;
		}

		Map<Integer, Equip> map = player.equips.get(0);
		for (Map.Entry<Integer, Integer> entry : staticEquips.entrySet()) {
			Equip equip = new Equip(player.maxKey(), entry.getKey(), entry.getValue(), 0, 0);
			map.put(equip.getKeyId(), equip);
		}
	}

	private void createPropQue(Player player) {

	}

	private void createPart(Player player) {

	}

	private void createChip(Player player) {

	}

	private void createCombat(Player player) {

	}

	private void createExplore(Player player) {

	}

	/**
	 * 
	 * Method: addEffect
	 * 
	 * @Description: 添加加成效果 @param player @param id @param time @return
	 *               void @throws
	 */
	public Effect addEffect(Player player, int id, int time) {
		if (id >= EffectType.CHANGE_SURFACE_1 && id <= EffectType.CHANGE_SURFACE_7) {
			if (player.surface != 0) {
				player.effects.remove(player.surface + 10);
				vaildEffect(player, player.surface + 10, -1);
			}
		} else if (id >= EffectType.ADD_HURT_SUPUR && id <= EffectType.MARCH_SPEED_SUPER) {
			if (player.effects.containsKey(id - 12)) {
				player.effects.remove(id - 12);
			}
		} else if (id >= EffectType.ADD_HURT && id <= EffectType.MARCH_SPEED) {
			if (player.effects.containsKey(id + 12)) {
				return null;
			}
		}

		Effect effect = player.effects.get(id);
		if (effect != null) {
			effect.setEndTime(effect.getEndTime() + time);
		} else {
			int now = DateUtil.getSecondTime();
			effect = new Effect(id, now + time);
			player.effects.put(id, effect);
			vaildEffect(player, id, 1);

//			if (id == EffectType.MARCH_SPEED || id == EffectType.MARCH_SPEED_SUPER) {
//				worldService.recalcArmyMarch(player);
//			}
		}

		return effect;
	}

	public void vaildEffect(Player player, int id, int factor) {
		Resource resource = player.resource;
		if (id == EffectType.ALL_PRODUCT) {
			int v = factor * 50;
			resource.setStoneOutF(resource.getStoneOutF() + v);
			resource.setSiliconOutF(resource.getSiliconOutF() + v);
			resource.setIronOutF(resource.getIronOutF() + v);
			resource.setCopperOutF(resource.getCopperOutF() + v);
			resource.setOilOutF(resource.getOilOutF() + v);
		} else if (id == EffectType.STONE_PRODUCT) {
			int v = factor * 50;
			resource.setStoneOutF(resource.getStoneOutF() + v);
		} else if (id == EffectType.IRON_PRODUCT) {
			int v = factor * 50;
			resource.setIronOutF(resource.getIronOutF() + v);
		} else if (id == EffectType.OIL_PRODUCT) {
			int v = factor * 50;
			resource.setOilOutF(resource.getOilOutF() + v);
		} else if (id == EffectType.COPPER_PRODUCT) {
			int v = factor * 50;
			resource.setCopperOutF(resource.getCopperOutF() + v);
		} else if (id == EffectType.SILICON_PRODUCT) {
			int v = factor * 50;
			resource.setSiliconOutF(resource.getSiliconOutF() + v);
		} else if (id == EffectType.CHANGE_SURFACE_2) {
			int v = factor * 25;
			resource.setStoneOutF(resource.getStoneOutF() + v);
			resource.setSiliconOutF(resource.getSiliconOutF() + v);
			resource.setIronOutF(resource.getIronOutF() + v);
			resource.setCopperOutF(resource.getCopperOutF() + v);
			resource.setOilOutF(resource.getOilOutF() + v);
			if (factor > 0) {
				player.surface = 2;
			} else {
				player.surface = 0;
			}
		} else if (id >= EffectType.CHANGE_SURFACE_1 && id <= EffectType.CHANGE_SURFACE_7) {
			if (factor > 0) {
				player.surface = id - 10;
			} else {
				player.surface = 0;
			}
		} else if (id == EffectType.WAR_CHAMPION) {
			int v = factor * 20;
			resource.setStoneOutF(resource.getStoneOutF() + v);
			resource.setSiliconOutF(resource.getSiliconOutF() + v);
			resource.setIronOutF(resource.getIronOutF() + v);
			resource.setCopperOutF(resource.getCopperOutF() + v);
			resource.setOilOutF(resource.getOilOutF() + v);
		}
	}

	public void clearAttackFree(Player player) {
		player.effects.remove(EffectType.ATTACK_FREE);
	}

	// public boolean addResourceOutAndMax(int buildingId, int buildingLv,
	// Resource resource) {
	// StaticBuildingLv staticBuildingLevel =
	// staticBuildingDataMgr.getStaticBuildingLevel(buildingId, buildingLv);
	// resource.setStoneMax(staticBuildingLevel.getStoneMaxAdd() +
	// resource.getStoneMax());
	// resource.setIronMax(staticBuildingLevel.getIronMaxAdd() +
	// resource.getIronMax());
	// resource.setOilMax(staticBuildingLevel.getOilMaxAdd() +
	// resource.getOilMax());
	// resource.setCopperMax(staticBuildingLevel.getCopperMaxAdd() +
	// resource.getCopperMax());
	// resource.setSiliconMax(staticBuildingLevel.getSiliconMaxAdd() +
	// resource.getSiliconMax());
	//
	// resource.setStoneOut(staticBuildingLevel.getStoneOutAdd() +
	// resource.getStoneOut());
	// resource.setIronOut(staticBuildingLevel.getIronOutAdd() +
	// resource.getIronOut());
	// resource.setOilOut(staticBuildingLevel.getOilOutAdd() +
	// resource.getOilOut());
	// resource.setCopperOut(staticBuildingLevel.getCopperOutAdd() +
	// resource.getCopperOut());
	// resource.setSiliconOut(staticBuildingLevel.getSiliconOutAdd() +
	// resource.getSiliconOut());
	//
	// return true;
	// }
	//
	// public void subResourceOutAndMax(int buildingId, int buildingLv, Resource
	// resource) {
	// StaticBuildingLv staticBuildingLevel =
	// staticBuildingDataMgr.getStaticBuildingLevel(buildingId, buildingLv);
	// resource.setStoneMax(resource.getStoneMax() -
	// staticBuildingLevel.getStoneMaxAdd());
	// resource.setIronMax(resource.getIronMax() -
	// staticBuildingLevel.getIronMaxAdd());
	// resource.setOilMax(resource.getOilMax() -
	// staticBuildingLevel.getOilMaxAdd());
	// resource.setCopperMax(resource.getCopperMax() -
	// staticBuildingLevel.getCopperMaxAdd());
	// resource.setSiliconMax(resource.getSiliconMax() -
	// staticBuildingLevel.getSiliconMaxAdd());
	//
	// resource.setStoneOut(resource.getStoneOut() -
	// staticBuildingLevel.getStoneOutAdd());
	// resource.setIronOut(resource.getIronOut() -
	// staticBuildingLevel.getIronOutAdd());
	// resource.setOilOut(resource.getOilOut() -
	// staticBuildingLevel.getOilOutAdd());
	// resource.setCopperOut(resource.getCopperOut() -
	// staticBuildingLevel.getCopperOutAdd());
	// resource.setSiliconOut(resource.getSiliconOut() -
	// staticBuildingLevel.getSiliconOutAdd());
	// }

	public boolean addResourceOutAndMax(int buildingId, int buildingLv, Resource resource) {
		ConfBuildingLv staticBuildingLevel = ConfBuildingLvProvider.getInst().getConfBuildingLevel(buildingId, buildingLv);
		resource.setStoneMax(staticBuildingLevel.getStoneMaxAdd() + resource.getStoneMax());
		resource.setIronMax(staticBuildingLevel.getIronMaxAdd() + resource.getIronMax());
		resource.setOilMax(staticBuildingLevel.getOilMaxAdd() + resource.getOilMax());
		resource.setCopperMax(staticBuildingLevel.getCopperMaxAdd() + resource.getCopperMax());
		resource.setSiliconMax(staticBuildingLevel.getSiliconMaxAdd() + resource.getSiliconMax());

		resource.setStoneOut(staticBuildingLevel.getStoneOutAdd() + resource.getStoneOut());
		resource.setIronOut(staticBuildingLevel.getIronOutAdd() + resource.getIronOut());
		resource.setOilOut(staticBuildingLevel.getOilOutAdd() + resource.getOilOut());
		resource.setCopperOut(staticBuildingLevel.getCopperOutAdd() + resource.getCopperOut());
		resource.setSiliconOut(staticBuildingLevel.getSiliconOutAdd() + resource.getSiliconOut());

		return true;
	}

	public void subResourceOutAndMax(int buildingId, int buildingLv, Resource resource) {
		ConfBuildingLv staticBuildingLevel = ConfBuildingLvProvider.getInst().getConfBuildingLevel(buildingId, buildingLv);
		resource.setStoneMax(resource.getStoneMax() - staticBuildingLevel.getStoneMax());
		resource.setIronMax(resource.getIronMax() - staticBuildingLevel.getIronMax());
		resource.setOilMax(resource.getOilMax() - staticBuildingLevel.getOilMax());
		resource.setCopperMax(resource.getCopperMax() - staticBuildingLevel.getCopperMax());
		resource.setSiliconMax(resource.getSiliconMax() - staticBuildingLevel.getSiliconMax());

		resource.setStoneOut(resource.getStoneOut() - staticBuildingLevel.getStoneOut());
		resource.setIronOut(resource.getIronOut() - staticBuildingLevel.getIronOut());
		resource.setOilOut(resource.getOilOut() - staticBuildingLevel.getOilOut());
		resource.setCopperOut(resource.getCopperOut() - staticBuildingLevel.getCopperOut());
		resource.setSiliconOut(resource.getSiliconOut() - staticBuildingLevel.getSiliconOut());
	}

	public static int getBuildingLv(int buildingId, Building building) {
		switch (buildingId) {
		case BuildingId.COMMAND:
			return building.getCommand();
		case BuildingId.WARE_1:
			return building.getWare1();
		case BuildingId.REFIT:
			return building.getRefit();
		case BuildingId.WORKSHOP:
			return building.getWorkShop();
		case BuildingId.TECH:
			return building.getTech();
		case BuildingId.FACTORY_1:
			return building.getFactory1();
		case BuildingId.FACTORY_2:
			return building.getFactory2();
		case BuildingId.STONE:
			return 0;
		case BuildingId.SILICON:
			return 0;
		case BuildingId.IRON:
			return 0;
		case BuildingId.COPPER:
			return 0;
		case BuildingId.OIL:
			return 0;
		case BuildingId.WARE_2:
			return building.getWare2();
		default:
			return 0;
		}
	}

	public int getMillTopLv(Player player, int millId) {
		int lv = 0;
		Iterator<Mill> it = player.mills.values().iterator();
		while (it.hasNext()) {
			Mill mill = it.next();
			if (mill.getId() == millId && mill.getLv() > lv) {
				lv = mill.getLv();
			}
		}
		return lv;
	}

	public int getMillCount(Player player, int millId, int lv) {
		int count = 0;
		Iterator<Mill> it = player.mills.values().iterator();
		while (it.hasNext()) {
			Mill mill = it.next();
			if (mill.getId() == millId && mill.getLv() >= lv) {
				count++;
			}
		}
		return count;
	}

	public long calcProtect(Player player) {
		int lv1 = getBuildingLv(BuildingId.WARE_1, player.building);
		int lv2 = getBuildingLv(BuildingId.WARE_2, player.building);
		long protect = 0;
		if (lv1 != 0) {
			protect += ConfBuildingLvProvider.getInst().getConfBuildingLevel(BuildingId.WARE_1, lv1).getStoneMax();
		}

		if (lv2 != 0) {
			protect += ConfBuildingLvProvider.getInst().getConfBuildingLevel(BuildingId.WARE_2, lv2).getStoneMax();
		}

		int storeF = player.resource.getStoreF();
		Map<Integer, PartyScience> sciences = PartyDataManager.getInst().getScience(player);
		if (sciences != null) {
			PartyScience science = sciences.get(ScienceId.PARTY_STORAGE);
			if (science != null) {
				storeF += science.getScienceLv();
			}
		}

		protect = (long) (protect * (storeF + 100) / 100.0f);
		return protect;
	}

	public long calcLoad(Player player, Form form) {
		long load = 0L;
		int[] p = form.p;
		int[] c = form.c;
		for (int i = 0; i < p.length; i++) {
			if (p[i] != 0) {
				load += ConfTankProvider.getInst().getConfigById(p[i]).getPayload() * (long) c[i];
			}
		}

		// 载重技术
		Map<Integer, PartyScience> sciences = PartyDataManager.getInst().getScience(player);
		if (sciences != null) {
			PartyScience science = sciences.get(ScienceId.PAY_LOAD);
			if (science != null) {
				load = (long) (load * (science.getScienceLv() + 100.0f) / 100);
			}
		}

		return load;
	}

	/**
	 * 
	 * Method: takeNick
	 * 
	 * @Description: 占用一个名字 @param nick @return @return boolean @throws
	 */
	public boolean takeNick(String nick) {
		synchronized (usedNames) {
			if (usedNames.contains(nick)) {
				return false;
			}

			usedNames.add(nick);
			return true;
		}
	}

	public void rename(Player player, String newNick) {
		String nick = player.lord.getNick();
		onlinePlayer.remove(nick);
		onlinePlayer.put(newNick, player);
		allPlayer.remove(nick);
		allPlayer.put(newNick, player);
		player.lord.setNick(newNick);
	}

	public boolean canUseName(String nick) {
		return !usedNames.contains(nick);
	}

	// public boolean subGoldWithCommit(Lord lord, int cost, GoldCost type) {
	// if (subGold(lord, cost, type)) {
	// lordDao.updateGold(lord);
	// return true;
	// }
	//
	// return false;
	// }

	/**
	 * 
	 * Method: modifyMetal
	 * 
	 * @Description: 修改记忆金属 @param lord @param add @param commit @return
	 *               void @throws
	 */
	public void modifyMetal(Lord lord, int add) {
		lord.setMetal(lord.getMetal() + add);
	}

	/**
	 * 
	 * Method: modifyFitting
	 * 
	 * @Description: 修改零件数量 @param lord @param add @param commit @return
	 *               void @throws
	 */
	public void modifyFitting(Lord lord, int add) {
		lord.setFitting(lord.getFitting() + add);
	}

	/**
	 * 
	 * Method: modifyPlan
	 * 
	 * @Description: 修改设计蓝图数量 @param lord @param add @param commit @return
	 *               void @throws
	 */
	public void modifyPlan(Lord lord, int add) {
		lord.setPlan(lord.getPlan() + add);
	}

	/**
	 * 
	 * Method: modifyMineral
	 * 
	 * @Description: 修改金属矿物数量 @param lord @param add @param commit @return
	 *               void @throws
	 */
	public void modifyMineral(Lord lord, int add) {
		lord.setMineral(lord.getMineral() + add);
	}

	/**
	 * 
	 * Method: modifyTool
	 * 
	 * @Description: 修改改造工具数量 @param lord @param add @param commit @return
	 *               void @throws
	 */
	public void modifyTool(Lord lord, int add) {
		lord.setTool(lord.getTool() + add);
	}

	/**
	 * 
	 * Method: modifyDraw
	 * 
	 * @Description: 修改改造图纸数量 @param lord @param add @param commit @return
	 *               void @throws
	 */
	public void modifyDraw(Lord lord, int add) {
		lord.setDraw(lord.getDraw() + add);
	}

	/**
	 * 
	 * Method: addEquipCapacity
	 * 
	 * @Description: 增加装备仓库容量，扣除金币 @param lord @param cost @param
	 *               add @return @return boolean @throws
	 */
	public boolean addEquipCapacity(Lord lord, int cost, int add) {
		if (subGold(lord, cost, GoldCost.EQUIP_STORE)) {
			lord.setEquip(lord.getEquip() + add);
			return true;
		}

		return false;
	}

	/**
	 * 
	 * Method: subGold
	 * 
	 * @Description: 扣除玩家金币，不写数据库 @param lord @param sub @param
	 *               type @return @return boolean @throws
	 */
	public boolean subGold(Lord lord, int sub, GoldCost type) {
		LogHelper.logGoldCost(lord, sub, type);
		if (sub <= 0) {
			return false;
		}

		lord.setGold(lord.getGold() - sub);
		lord.setGoldCost(lord.getGoldCost() + sub);

		TaskManager.getInst().updTask(lord.getLordId(), TaskType.COND_COST_GOLD, 1, null);
		// activityDataManager.ActCostGold(lord, sub);
		// activityDataManager.actConsumeDail(lord, sub);
		// LogHelper.logGoldCost(lord, sub, type);
		return true;
	}

	private boolean checkGoldIsEnought(Lord lord, long sub) {
		if (sub < 0) {
			return false;
		}
		return lord.getGold() >= sub;
	}

	/**
	 * 
	 * Method: addGold
	 * 
	 * @Description: 非充值增加玩家金币 @param lord @param add @param
	 *               type @return @return boolean @throws
	 */
	public boolean addGold(Lord lord, int add, AwardFrom from) {
		if (add <= 0) {
			return false;
		}
		lord.setGold(lord.getGold() + add);
		lord.setGoldGive(lord.getGoldGive() + add);

		LogHelper.logGoldGive(lord, add, from);
		return true;
	}

	/**
	 * 
	 * Method: addHunagbao
	 * 
	 * @Description: 增加荒宝碎片 @param lord @param add @return void @throws
	 */
	public void addHunagbao(Lord lord, int add) {
		lord.setHuangbao(lord.getHuangbao() + add);
	}

	/**
	 * 
	 * Method: subHuangbao
	 * 
	 * @Description: 扣除荒宝碎片 @param lord @param sub @return void @throws
	 */
	public void subHuangbao(Lord lord, int sub) {
		lord.setHuangbao(lord.getHuangbao() - sub);
	}

	/**
	 * 
	 * Method: fullPower
	 * 
	 * @Description: 能量是否达到上限 @param lord @return @return boolean @throws
	 */
	public boolean fullPower(Lord lord) {
		if (lord.getPower() < POWER_MAX) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * Method: backPower
	 * 
	 * @Description: 恢复能量 @param lord @param now @return void @throws
	 */
	public void backPower(Lord lord, int now) {
		int period = now - lord.getPowerTime();
		int back = period / POWER_BACK_SECOND;
		if (back > 0) {
			int power = lord.getPower() + back;
			power = (power > POWER_MAX) ? POWER_MAX : power;
			lord.setPowerTime(lord.getPowerTime() + (power - lord.getPower()) * POWER_BACK_SECOND);
			lord.setPower(power);
		}
	}

	/**
	 * 
	 * Method: subPower
	 * 
	 * @Description: 扣除能量 @param lord @param sub @return @return boolean @throws
	 */
	public boolean subPower(Lord lord, int sub) {
		if (lord.getPower() < sub) {
			return false;
		}

		if (fullPower(lord)) {
			lord.setPowerTime(DateUtil.getSecondTime());
		}

		lord.setPower(lord.getPower() - sub);
		return true;
	}

	/**
	 * 
	 * Method: addPower
	 * 
	 * @Description: 增加能量 @param lord @param add @return void @throws
	 */
	public void addPower(Lord lord, int add) {
		lord.setPower(lord.getPower() + add);
	}

	/**
	 * 
	 * Method: leftBackPowerTime
	 * 
	 * @Description: 下次恢复能量的剩余时间秒数 @param lord @return @return int @throws
	 */
	public int leftBackPowerTime(Lord lord) {
		if (!fullPower(lord)) {
			return lord.getPowerTime() + POWER_BACK_SECOND - DateUtil.getSecondTime();
		}
		return 0;
	}

	/**
	 * 
	 * Method: fullPros
	 * 
	 * @Description: 繁荣度是否达到上限 @param lord @return @return boolean @throws
	 */
	public boolean fullPros(Lord lord) {
		if (lord.getPros() < lord.getProsMax()) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * Method: backProsWith
	 * 
	 * @Description: 1分钟恢复一点繁荣度,并写数据库 @param lord @param now @return
	 *               void @throws
	 */
	public void backPros(Player player, int now) {
		Lord lord = player.lord;
		int period = now - lord.getProsTime();
		int restoreUnit = PROS_BACK_SECOND;
		// 若是废墟,2分钟恢复一点
		if (isRuins(player)) {
			restoreUnit *= 2;
		}

		int back = period / restoreUnit;
		if (back > 0) {
			int max = lord.getProsMax();
			back = lord.getPros() + back;
			back = (back > max) ? max : back;
			lord.setProsTime(lord.getProsTime() + (back - lord.getPros()) * restoreUnit);
			lord.setPros(back);

			outOfRuins(player);
		}
	}

	// public boolean isRuins(Lord lord) {
	// if (lord.getProsMax() < 600) {
	// if (lord.getPros() == 0) {
	// return true;
	// }
	// } else {
	// if (lord.getPros() < 600) {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	public boolean isRuins(Player player) {
		return (player.ruins.isRuins());
	}

	/**
	 * 成为废墟 Method: becomeRuins
	 * 
	 * @Description: TODO @return void @throws
	 */
	public void becomeRuins(Player defencer, Player attacker) {
		// 不是废墟才需要判断会不会成为废墟
		if (!isRuins(defencer)) {
			// 1.玩家繁荣度最大值低于600时，遭到攻击后繁荣度被打为0才变为废墟，直至恢复满繁荣度脱离废墟
			// 2.玩家繁荣度最大值不低于600时，遭到攻击后繁荣度被打下600变为废墟，直至恢复到不低于600脱离废墟
			if ((defencer.lord.getProsMax() < 600 && defencer.lord.getPros() == 0) || (defencer.lord.getProsMax() >= 600 && defencer.lord.getPros() < 600)) {
				Ruins r = defencer.ruins;
				r.setRuins(true);
				r.setLordId(attacker.lord.getLordId());
				r.setAttackerName(attacker.lord.getNick());
			}
		}
	}

	/**
	 * 脱离废墟 Method: outOfRuins
	 * 
	 * @Description: TODO @param player @return void @throws
	 */
	public void outOfRuins(Player player) {
		// 判断是否是废墟,若是才需要脱离
		if (isRuins(player)) {
			if ((player.lord.getProsMax() < 600 && player.lord.getPros() >= player.lord.getProsMax()) || (player.lord.getProsMax() >= 600 && player.lord.getPros() >= 600)) {
				Ruins r = player.ruins;
				r.setRuins(false);
				r.setLordId(0);
				r.setAttackerName("");
			}
		}
	}

	// public boolean addProsMaxWithCommit(Lord lord, int add) {
	// if (addProsMax(lord, add)) {
	// lordDao.updatePros(lord);
	// return true;
	// }
	//
	// return false;
	// }

	/**
	 * 
	 * Method: addPros
	 * 
	 * @Description: 增加繁荣度上限,不写数据库 @param lord @param add @return @return
	 *               boolean @throws
	 */
	public boolean addProsMax(Player player, int add) {
		if (add <= 0) {
			return false;
		}
		player.lord.setProsMax(player.lord.getProsMax() + add);
		player.lord.setPros(player.lord.getPros() + add);

		outOfRuins(player);
		return true;
	}

	public void subProsMax(Lord lord, int sub) {
		lord.setProsMax(lord.getProsMax() - sub);
	}

	/**
	 * 
	 * Method: subPros
	 * 
	 * @Description: 扣除繁荣度 @param player @param sub @return @return
	 *               boolean @throws
	 */
	public void subPros(Lord lord, int sub) {
		int pros = lord.getPros() - sub;
		if (pros < 0) {
			pros = 0;
		}

		if (fullPros(lord)) {
			lord.setProsTime(DateUtil.getSecondTime());
		}

		lord.setPros(pros);
	}

	/**
	 * 被攻击扣除繁荣度
	 * 
	 * @param defense
	 * @param sub
	 * @param attacker
	 * @return void @throws
	 */
	public void subProsByAttack(Player defencer, int sub, Player attacker) {
		subPros(defencer.lord, sub);
		becomeRuins(defencer, attacker);
	}

	public void addPros(Player player, int add) {
		int pros = player.lord.getPros() + add;
		if (pros > player.lord.getProsMax()) {
			pros = player.lord.getProsMax();
		}

		player.lord.setPros(pros);

		outOfRuins(player);
	}

	// public boolean subProsWithCommit(Lord lord, int sub) {
	// if (!subPros(lord, sub)) {
	// return false;
	// }
	//
	// lordDao.updatePros(lord);
	// return true;
	// }

	/**
	 * 
	 * Method: leftBackProsTime
	 * 
	 * @Description: 下次恢复繁荣度的剩余时间秒数 @param lord @return @return int @throws
	 */
	public int leftBackProsTime(Lord lord) {
		if (!fullPros(lord)) {
			return lord.getProsTime() + PROS_BACK_SECOND - DateUtil.getSecondTime();
		}
		return 0;
	}

	/**
	 * 
	 * Method: modifyStone
	 * 
	 * @Description: 修改宝石数量 @param resource @param add @param commit @return
	 *               void @throws
	 */
	public void modifyStone(Resource resource, long add) {
		if (add > 0) {
			resource.settStone(resource.gettStone() + add);
		}
		resource.setStone(resource.getStone() + add);
	}

	/**
	 * 
	 * Method: modifyIron
	 * 
	 * @Description: 修改铁资源数量 @param resource @param add @param commit @return
	 *               void @throws
	 */
	public void modifyIron(Resource resource, long add) {
		if (add > 0) {
			resource.settIron(resource.gettIron() + add);
		}
		resource.setIron(resource.getIron() + add);
	}

	/**
	 * 
	 * Method: modifyOil
	 * 
	 * @Description: 修改石油资源数量 @param resource @param add @param commit @return
	 *               void @throws
	 */
	public void modifyOil(Resource resource, long add) {
		if (add > 0) {
			resource.settOil(resource.gettOil() + add);
		}
		resource.setOil(resource.getOil() + add);
	}

	/**
	 * 
	 * Method: modifyCopper
	 * 
	 * @Description: 修改铜资源数量 @param resource @param add @param commit @return
	 *               void @throws
	 */
	public void modifyCopper(Resource resource, long add) {
		if (add > 0) {
			resource.settCopper(resource.gettCopper() + add);
		}
		resource.setCopper(resource.getCopper() + add);
	}

	/**
	 * 
	 * Method: modifySilicon
	 * 
	 * @Description: 修改硅资源数量 @param resource @param add @param commit @return
	 *               void @throws
	 */
	public void modifySilicon(Resource resource, long add) {
		if (add > 0) {
			resource.settSilicon(resource.gettSilicon() + add);
		}
		resource.setSilicon(resource.getSilicon() + add);
	}

	private void addPartyBuild(PartyData partyData, int build) {
		if (partyData == null) {
			return;
		}
		partyData.setBuild(partyData.getBuild() + build);
	}

	/**
	 * 
	 * Method: undergoGrab
	 * 
	 * @Description: 被掠夺资源 @param target @param grab @return void @throws
	 */
	public void undergoGrab(Player target, Grab grab) {
		Resource resource = target.resource;
		modifyIron(resource, -grab.rs[0]);
		modifyOil(resource, -grab.rs[1]);
		modifyCopper(resource, -grab.rs[2]);
		modifySilicon(resource, -grab.rs[3]);
		modifyStone(resource, -grab.rs[4]);
	}

	public void gainGrab(Player target, Grab grab) {
		Resource resource = target.resource;
		modifyIron(resource, grab.rs[0]);
		modifyOil(resource, grab.rs[1]);
		modifyCopper(resource, grab.rs[2]);
		modifySilicon(resource, grab.rs[3]);
		modifyStone(resource, grab.rs[4]);
	}

	private void addResource(Resource resource, int type, long add) {
		switch (type) {
		case 1:
			modifyIron(resource, add);
			break;
		case 2:
			modifyOil(resource, add);
			break;
		case 3:
			modifyCopper(resource, add);
			break;
		case 4:
			modifySilicon(resource, add);
			break;
		case 5:
			modifyStone(resource, add);
			break;
		default:
			break;
		}
	}

	/**
	 * 判断资源够不够 Method: checkResourceIsEnought
	 * 
	 * @Description: TODO @return @return boolean @throws
	 */
	private boolean checkResourceIsEnougth(Resource resource, int type, long num) {
		boolean ret = false;
		switch (type) {
		case 1:
			ret = resource.getIron() >= num;
			break;
		case 2:
			ret = resource.getOil() >= num;
			break;
		case 3:
			ret = resource.getCopper() >= num;
			break;
		case 4:
			ret = resource.getSilicon() >= num;
			break;
		case 5:
			ret = resource.getStone() >= num;
			break;
		default:
			ret = false;
			break;
		}
		return ret;
	}

	/**
	 * 判断军工材料够不够 Method: checkMilitaryMaterialIsEnought
	 * 
	 * @Description: TODO @param player @param type @param count @return @return
	 *               boolean @throws
	 */
	private boolean checkMilitaryMaterialIsEnougth(Player player, int id, long count) {
		MilitaryMaterial m = player.militaryMaterials.get(id);
		return !(m == null || m.getCount() < count);
	}

	/**
	 * 判断物品是否够 Method: checkPropIsEnought
	 * 
	 * @Description: TODO @param player @param type @param id @param
	 *               count @return @return boolean @throws
	 */
	public boolean checkPropIsEnougth(Player player, int type, int id, long count) {
		boolean ret = false;
		switch (type) {
		case AwardType.RESOURCE:
			ret = checkResourceIsEnougth(player.resource, id, count);
			break;
		case AwardType.MILITARY_MATERIAL:
			ret = checkMilitaryMaterialIsEnougth(player, id, count);
			break;
		case AwardType.TANK:
			ret = checkTankIsEnougth(player, id, count);
			break;
		case AwardType.GOLD:
			ret = checkGoldIsEnought(player.lord, count);
			break;
		default:
			ret = false;
			break;
		}
		return ret;
	}

	/**
	 * Method: checkTankIsEnougth
	 * 
	 * @Description: TODO @param player @param id @param count @return @return
	 *               boolean @throws
	 */
	private boolean checkTankIsEnougth(Player player, int id, long count) {
		Tank tank = player.tanks.get(id);
		return !(tank == null || tank.getCount() < count);
	}

	/**
	 * 
	 * Method: addProp
	 * 
	 * @Description: 增加道具 @param player @param propId @param
	 *               count @return @return Prop @throws
	 */
	public Prop addProp(Player player, int propId, int count) {
		Prop prop = player.props.get(propId);
		if (prop != null) {
			prop.setCount(count + prop.getCount());
		} else {
			prop = new Prop(propId, count);
			player.props.put(propId, prop);
		}
		return prop;
	}

	/**
	 * 
	 * Method: subProp
	 * 
	 * @Description: 扣除道具 @param prop @param count @return void @throws
	 */
	public void subProp(Prop prop, int count) {
		prop.setCount(prop.getCount() - count);
	}

	/**
	 * 
	 * Method: addTank
	 * 
	 * @Description: 增加坦克 @param player @param tankId @param
	 *               count @return @return Tank @throws
	 */
	public Tank addTank(Player player, int tankId, int count) {
		Tank tank = player.tanks.get(tankId);
		if (tank != null) {
			tank.setCount(count + tank.getCount());
		} else {
			tank = new Tank(tankId, count, 0);
			player.tanks.put(tankId, tank);
		}
		return tank;
	}

	/**
	 * 
	 * Method: addScience
	 * 
	 * @Description: 增加科技 @param player @param scienceId @param
	 *               level @return @return Science @throws
	 */
	public Science addScience(Player player, int scienceId) {
		Science science = player.sciences.get(scienceId);
		if (science != null) {
			science.setScienceLv(science.getScienceLv() + 1);
		} else {
			science = new Science(scienceId, 1);
			player.sciences.put(scienceId, science);
		}

		ConfRefine staticRefine = ConfRefineProvider.getInst().getConfigById(scienceId);
		if (staticRefine != null) {
			Resource resource = player.resource;
			int percent = staticRefine.getAddtion();
			if (staticRefine.getBuildId() == 8) {// 宝石
				resource.setStoneOutF(resource.getStoneOutF() + percent);
			} else if (staticRefine.getBuildId() == 9) {// 硅石
				resource.setSiliconOutF(resource.getSiliconOutF() + percent);
			} else if (staticRefine.getBuildId() == 10) {// 铁矿
				resource.setIronOutF(resource.getIronOutF() + percent);
			} else if (staticRefine.getBuildId() == 11) {// 铜矿
				resource.setCopperOutF(resource.getCopperOutF() + percent);
			} else if (staticRefine.getBuildId() == 12) {// 石油
				resource.setOilOutF(resource.getOilOutF() + percent);
			}

			if (staticRefine.getCapacity() == 1) {
				resource.setStoreF(resource.getStoreF() + percent);
			}
		}

		// TODO 世界注释掉
		if (scienceId == ScienceId.ENGINE) {
//			worldService.recalcArmyMarch(player);
		}

		TaskManager.getInst().updTask(player, TaskType.COND_SCIENCE_LV_UP, 1, null);
		return science;
	}

	/**
	 * 获取某些操作失败的次数
	 * 
	 * @param operType
	 * @return
	 */
	public FailNum getFailNumByOperType(Player player, int operType) {
		FailNum f = player.failNums.get(operType);
		if (f == null) {
			f = new FailNum(operType, 0);
			player.failNums.put(operType, f);
		}
		return f;
	}

	/**
	 * 
	 * Method: addExp
	 * 
	 * @Description: 增加经验 @param player @param add @return void @throws
	 */
	public boolean addExp(Lord lord, int add) {
		return ConfLordLvProvider.getInst().addExp(lord, add);
	}

	/**
	 * 
	 * Method: addFame
	 * 
	 * @Description: 加声望 @param lord @param add @return @return boolean @throws
	 */
	public boolean addFame(Lord lord, int add) {
		int fame = lord.getFame() + add;
		int fameLv = lord.getFameLv();
		boolean up = false;
		while (true) {
			int upFame = (fameLv + 1) * 100;
			if (fame >= upFame) {
				fameLv++;
				fame -= upFame;
				up = true;
				continue;
			} else {
				break;
			}
		}
		lord.setFameLv(fameLv);
		lord.setFame(fame);
		if (up && fameLv >= 15) {
			ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(SysChatId.FAME_UP, lord.getNick(), String.valueOf(fameLv)));
		}

		return up;
	}

	/**
	 * 
	 * Method: addEquip
	 * 
	 * @Description: 加装备 @param player @param equipId @param pos @return @return
	 *               Equip @throws
	 */
	public Equip addEquip(Player player, int equipId, int pos) {
		Equip equip = new Equip(player.maxKey(), equipId, 1, 0, pos);
		player.equips.get(pos).put(equip.getKeyId(), equip);
		return equip;
	}

	/**
	 * 
	 * Method: addPart
	 * 
	 * @Description: 加配件 @param player @param partId @param pos @return @return
	 *               Part @throws
	 */
	public Part addPart(Player player, int partId, int pos) {
		Part part = new Part(player.maxKey(), partId, 0, 0, pos, false);
		player.parts.get(pos).put(part.getKeyId(), part);
		return part;
	}

	/**
	 * 
	 * Method: addChip
	 * 
	 * @Description: 加配件碎片 @param player @param chipId @param
	 *               count @return @return Chip @throws
	 */
	public Chip addChip(Player player, int chipId, int count) {
		Chip chip = player.chips.get(chipId);
		if (chip != null) {
			chip.setCount(count + chip.getCount());
		} else {
			chip = new Chip(chipId, count);
			player.chips.put(chipId, chip);
		}
		return chip;
	}

	/**
	 * 
	 * Method: addPartMaterial
	 * 
	 * @Description: 增加配件材料 @param player @param id @param count @return
	 *               void @throws
	 */
	public void addPartMaterial(Player player, int id, int count) {
		switch (id) {
		case 1:
			modifyFitting(player.lord, count);
			break;
		case 2:
			modifyMetal(player.lord, count);
			break;
		case 3:
			modifyPlan(player.lord, count);
			break;
		case 4:
			modifyMineral(player.lord, count);
			break;
		case 5:
			modifyTool(player.lord, count);
			break;
		case 6:
			modifyDraw(player.lord, count);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * Method: getResourceOut
	 * 
	 * @Description: 获取玩家每小时资源产量 @param player @param resourceId @throws
	 */
	public int getResourceOut(Player player, int resourceId) {
		Resource resource = player.resource;
		switch (resourceId) {
		case PartyType.RESOURCE_IRON://
			int ironOut = (int) (resource.getIronOut() * (100 + resource.getIronOutF()) / 100.0f);
			return ironOut;
		case PartyType.RESOURCE_OIL:
			int oilOut = (int) (resource.getOilOut() * (100 + resource.getOilOutF()) / 100.0f);
			return oilOut;
		case PartyType.RESOURCE_COPPER:
			int copperOut = (int) (resource.getCopperOut() * (100 + resource.getCopperOutF()) / 100.0f);
			return copperOut;
		case PartyType.RESOURCE_SILICON:
			int siliconOut = (int) (resource.getSiliconOut() * (100 + resource.getSiliconOutF()) / 100.0f);
			return siliconOut;
		case PartyType.RESOURCE_STONE:
			int stoneOut = (int) (resource.getStoneOut() * (100 + resource.getStoneOutF()) / 100.0f);
			return stoneOut;
		default:
			break;
		}
		return 0;
	}

	/**
	 * 
	 * Method: addAward
	 * 
	 * @Description: 通用加属性、物品、数据 @param player @param type @param id @param
	 *               count @param from @return void @throws
	 */
	public int addAward(Player player, int type, int id, long count, AwardFrom from) {
		switch (type) {
		case AwardType.EXP:
			addExp(player.lord, (int) count);
			break;
		case AwardType.PROS:

			break;
		case AwardType.FAME:
			addFame(player.lord, (int) count);
			break;
		case AwardType.HONOUR:

			break;
		case AwardType.PROP:
			addProp(player, id, (int) count);
			break;
		case AwardType.EQUIP:
			return addEquip(player, id, 0).getKeyId();
		case AwardType.PART:
			return addPart(player, id, 0).getKeyId();
		case AwardType.CHIP:
			addChip(player, id, (int) count);
			break;
		case AwardType.PART_MATERIAL:
			addPartMaterial(player, id, (int) count);
			break;
		case AwardType.SCORE:
			addArenaScore(player, (int) count);
			break;
		case AwardType.CONTRIBUTION: {
			long lordId = player.lord.getLordId();
			Member member = PartyDataManager.getInst().getMemberById(lordId);
			if (member != null) {
				member.setDonate(member.getDonate() + (int) count);
				member.setWeekAllDonate(member.getWeekAllDonate() + (int) count);
			}
			break;
		}
		case AwardType.HUANGBAO:
			addHunagbao(player.lord, (int) count);
			break;
		case AwardType.TANK:
			addTank(player, id, (int) count);
			break;
		case AwardType.HERO:
			return HeroManager.getInst().addHero(player, id, (int) count).getKeyId();
		case AwardType.GOLD:
			addGold(player.lord, (int) count, from);
			break;
		case AwardType.RESOURCE:
			addResource(player.resource, id, count);
			break;
		case AwardType.PARTY_BUILD:
			long lordId = player.lord.getLordId();
			PartyData partyData = PartyDataManager.getInst().getPartyByLordId(lordId);
			if (partyData != null) {
				addPartyBuild(partyData, (int) count);
			}
			break;
		case AwardType.POWER:
			addPower(player.lord, (int) count);
			break;
		case AwardType.MILITARY_MATERIAL:
			addMilitaryMaterial(player, id, count);
			break;
		default:
			break;
		}
		return 0;
	}

	/**
	 * Method: addMilitaryMaterial
	 * 
	 * @Description: TODO @param player @param id @param count @return
	 *               void @throws
	 */
	public MilitaryMaterial addMilitaryMaterial(Player player, int id, long count) {
		MilitaryMaterial m = player.militaryMaterials.get(id);
		if (count >= 0) {
			if (m != null) {
				m.setCount(m.getCount() + count);
			} else {
				m = new MilitaryMaterial(id, count);
				player.militaryMaterials.put(id, m);
			}
		}
		return m;
	}

	/**
	 * 
	 * Method: subProp
	 * 
	 * @Description: 消耗物品 @return void @throws
	 */
	public CommonPb.Atom2 subProp(Player player, int type, int id, long count, Object... obj) {
		CommonPb.Atom2.Builder b = CommonPb.Atom2.newBuilder();
		b.setKind(type).setId(id);
		switch (type) {
		case AwardType.RESOURCE:
			subResource(player.resource, id, count);
			b.setCount((int)getResourceNum(player.resource, id));
			break;
		case AwardType.MILITARY_MATERIAL:
			MilitaryMaterial m = subMilitaryMaterial(player, id, count);
			b.setCount((int)m.getCount());
			break;
		case AwardType.TANK:
			Tank tank = player.tanks.get(id);
			subTank(tank, (int) count);
			b.setCount(tank.getCount());
			break;
		case AwardType.GOLD:
			subGold(player.lord, (int) count, (GoldCost) obj[0]);
			b.setCount(player.lord.getGold());
			break;
		default:
			break;
		}
		return b.build();
	}

	private long getResourceNum(Resource resource, int id) {
		long ret = 0;
		switch (id) {
		case 1:
			ret = resource.getIron();
			break;
		case 2:
			ret = resource.getOil();
			break;
		case 3:
			ret = resource.getCopper();
			break;
		case 4:
			ret = resource.getSilicon();
			break;
		case 5:
			ret = resource.getStone();
			break;
		default:
			break;
		}
		return ret;
	}

	/**
	 * Method: subResource
	 * 
	 * @Description: TODO @param resource @param id @param count @return
	 *               void @throws
	 */
	private Resource subResource(Resource resource, int type, long count) {
		switch (type) {
		case 1:
			resource.setIron(resource.getIron() - count);
			break;
		case 2:
			resource.setOil(resource.getOil() - count);
			break;
		case 3:
			resource.setCopper(resource.getCopper() - count);
			break;
		case 4:
			resource.setSilicon(resource.getSilicon() - count);
			break;
		case 5:
			resource.setStone(resource.getStone() - count);
			break;
		default:
			break;
		}
		return resource;
	}

	/**
	 * Method: subMilitaryMaterial
	 * 
	 * @Description: TODO @param player @param id @param count @return
	 *               void @throws
	 */
	public MilitaryMaterial subMilitaryMaterial(Player player, int id, long count) {
		MilitaryMaterial m = player.militaryMaterials.get(id);
		if (m != null) {
			m.setCount(m.getCount() - count);
		}
		return m;
	}

	public int giveRedPacketGold(Player player, int propId, int count) {
		ConfProp staticProp = ConfPropProvider.getInst().getConfigById(propId);
		if (staticProp == null || staticProp.getEffectType() != 6) {
			return 0;
		}

		List<List<Integer>> effectValue = staticProp.getEffectValue();
		if (effectValue == null || effectValue.isEmpty()) {
			return 0;
		}

		List<Integer> one = effectValue.get(0);
		if (one.size() != 1) {
			return 0;
		}

		int gold = one.get(0) * count;

		addGold(player.lord, gold, AwardFrom.RED_PACKET);
		return gold;
	}

	/**
	 * 
	 * Method: addAwardList
	 * 
	 * @Description: 加一组物品 @param player @param awards @param from @return
	 *               void @throws
	 */
	public void addAwardList(Player player, List<List<Integer>> awards, AwardFrom from) {
		for (int i = 0; i < awards.size(); i++) {
			List<Integer> award = awards.get(i);
			if (award.size() != 3) {
				continue;
			}
			addAward(player, award.get(0), award.get(1), award.get(2), from);
		}
	}

	/**
	 * 
	 * Method: addAwardAndBack
	 * 
	 * @Description: 加一组物品,并返回pb数据 @param player @param drop @param
	 *               from @return @return List<Award> @throws
	 */
	public List<AwardPB> addAwardsBackPb(Player player, List<List<Integer>> drop, AwardFrom from) {
		List<AwardPB> awards = new ArrayList<>();
		if (drop != null && !drop.isEmpty()) {
			int type = 0;
			int id = 0;
			int count = 0;
			int keyId = 0;
			for (List<Integer> award : drop) {
				if (award.size() != 3) {
					continue;
				}

				type = award.get(0);
				id = award.get(1);
				count = award.get(2);
				keyId = addAward(player, type, id, count, from);
				awards.add(PbHelper.createAwardPb(type, id, count, keyId));
			}
		}
		return awards;
	}

	/**
	 * 
	 * Method: addArenaScore
	 * 
	 * @Description: 加竞技场积分 @param player @param count @return void @throws
	 */
	public void addArenaScore(Player player, int count) {
		Arena arena = ArenaProvider.getInst().getBeanById(player.roleId);
		if (arena != null) {
			arena.setScore(arena.getScore() + count);
		}
	}

	/**
	 * 
	 * Method: checkTank
	 * 
	 * @Description: 检查阵型中的坦克是否足够 @param player @param form @param
	 *               tankCount @return @return boolean @throws
	 */
	public boolean checkTank(Player player, Form form, int tankCount) {
		int totalTank = 0;
		int count = 0;
		Map<Integer, Integer> formTanks = new HashMap<Integer, Integer>();

		int[] p = form.p;
		int[] c = form.c;
		for (int i = 0; i < p.length; i++) {
			if (p[i] > 0) {
				count = addTankMapCount(formTanks, p[i], c[i], tankCount);
				totalTank += count;
				c[i] = count;
			}
		}

		Map<Integer, Tank> tanks = player.tanks;
		for (Map.Entry<Integer, Integer> entry : formTanks.entrySet()) {
			Tank tank = tanks.get(entry.getKey());
			if (tank == null || tank.getCount() < entry.getValue()) {
				return false;
			}
		}

		if (totalTank == 0) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * Method: createHomeDefendForm
	 * 
	 * @Description: 创建基地防守阵型，拿走坦克数量，战斗完返还 @param player @return @return
	 *               Form @throws
	 */
	public Form createHomeDefendForm(Player player) {
		Form formTemplate = player.forms.get(FormType.HOME_DEFEND);
		if (formTemplate == null) {
			return null;
		}

		Form form = new Form();
		ConfHero staticHero = null;
		if (formTemplate.getCommander() > 0) {
			Hero hero = player.heros.get(formTemplate.getCommander());
			if (hero != null && hero.getCount() > 0) {
				form.setCommander(formTemplate.getCommander());
				staticHero = ConfHeroProvider.getInst().getConfigById(form.getCommander());
			}
		}

		int totalTank = 0;
		int count = 0;
		Map<Integer, Integer> formTanks = new HashMap<Integer, Integer>();
		Map<Integer, Tank> tanks = player.tanks;
		int maxTankCount = formTankCount(player, staticHero);

		int[] p = formTemplate.p;
		int[] c = formTemplate.c;
		for (int i = 0; i < p.length; i++) {
			if (p[i] > 0) {
				count = addTankMapCount(formTanks, p[i], c[i], maxTankCount);
				if (count > 0) {
					Tank tank = tanks.get(p[i]);
					if (tank != null) {
						if (tank.getCount() < count) {
							count = tank.getCount();
						}

						tank.setCount(tank.getCount() - count);
						if (count > 0) {
							form.p[i] = p[i];
							form.c[i] = count;
							totalTank += count;
						}
					}
				}
			}
		}

		if (totalTank == 0) {
			return null;
		}

		return form;
	}

	/**
	 * 
	 * Method: getHomeDefendForm
	 * 
	 * @Description: 获取基地防守阵型,不扣除坦克 @param player @return @return Form @throws
	 */
	public Form getHomeDefendForm(Player player) {
		Form formTemplate = player.forms.get(FormType.HOME_DEFEND);
		if (formTemplate == null) {
			return null;
		}

		Form form = new Form();
		ConfHero staticHero = null;
		if (formTemplate.getCommander() > 0) {
			Hero hero = player.heros.get(formTemplate.getCommander());
			if (hero != null && hero.getCount() > 0) {
				form.setCommander(formTemplate.getCommander());
				staticHero = ConfHeroProvider.getInst().getConfigById(form.getCommander());
			}
		}

		int totalTank = 0;
		int count = 0;
		Map<Integer, Integer> formTanks = new HashMap<Integer, Integer>();
		Map<Integer, Tank> tanks = player.tanks;
		Map<Integer, RptTank> hadTank = new HashMap<>();
		int maxTankCount = formTankCount(player, staticHero);

		int[] p = formTemplate.p;
		int[] c = formTemplate.c;
		for (int i = 0; i < p.length; i++) {
			if (p[i] > 0) {
				count = addTankMapCount(formTanks, p[i], c[i], maxTankCount);
				if (count > 0) {
					RptTank had = hadTank.get(p[i]);
					if (had == null) {
						Tank tank = tanks.get(p[i]);
						if (tank == null) {
							had = new RptTank(p[i], 0);
						} else {
							had = new RptTank(p[i], tank.getCount());
						}
						hadTank.put(p[i], had);
					}

					if (had.getCount() < count) {
						count = had.getCount();
					}

					had.setCount(had.getCount() - count);
					if (count > 0) {
						form.p[i] = p[i];
						form.c[i] = count;
						totalTank += count;
					}
				}
			}
		}

		if (totalTank == 0) {
			return null;
		}

		return form;
	}

	/**
	 * 
	 * Method: calcHonorScore
	 * 
	 * @Description: 计算战损的荣誉点 @param map1 @param map2 @param
	 *               ratio @return @return int @throws
	 */
	public int calcHonor(Map<Integer, RptTank> map1, Map<Integer, RptTank> map2, double ratio) {
		int score1 = calcHonorScore(map1, ratio);
		int score2 = calcHonorScore(map2, ratio);
		int score = score1 + score2;
		if (score <= 0) {
			return 0;
		} else if (score < 101) {
			return 2;
		} else if (score < 501) {
			return 3;
		} else if (score < 2001) {
			return 4;
		} else if (score < 5001) {
			return 5;
		} else if (score < 10001) {
			return 7;
		} else if (score < 16001) {
			return 10;
		} else if (score < 25001) {
			return 15;
		} else {
			return 20;
		}
	}

	/**
	 * 
	 * Method: calcStaffingExp
	 * 
	 * @Description: 计算战损产生的编制经验 @param map @param ratio @return @return
	 *               int @throws
	 */
	public int calcStaffingExp(Map<Integer, RptTank> map, double ratio) {
		// 判定編制是否開啓
		// if (!TimeHelper.isStaffingOpen()) {
		// return 0;
		// }

		int exp = 0;
		int killed = 0;
		int lost = 0;
		ConfTank staticTank = null;
		Iterator<RptTank> it = map.values().iterator();
		while (it.hasNext()) {
			RptTank rptTank = (RptTank) it.next();
			killed = rptTank.getCount();
			lost = killed - (int) Math.ceil(ratio * killed);
			staticTank = ConfTankProvider.getInst().getConfigById(rptTank.getTankId());
			if (staticTank != null)
				exp += staticTank.getStaffingExp() * lost;
		}

		return exp;
	}

	private int calcHonorScore(Map<Integer, RptTank> map, double ratio) {
		Iterator<RptTank> it = map.values().iterator();
		int score = 0;

		int killed = 0;
		int lost = 0;
		ConfTank staticTank = null;

		while (it.hasNext()) {
			RptTank rptTank = (RptTank) it.next();
			killed = rptTank.getCount();
			lost = killed - (int) Math.ceil(ratio * killed);
			staticTank = ConfTankProvider.getInst().getConfigById(rptTank.getTankId());
			if (staticTank != null)
				score += staticTank.getHonorScore() * lost;
		}

		return score;
	}

	public int giveHonor(Player winner, Player loser, int honor) {
		int give = 0;
		if (loser.lord.getHonour() < honor) {
			give = loser.lord.getHonour();
		} else {
			give = honor;
		}

		winner.lord.setHonour(winner.lord.getHonour() + give);
		loser.lord.setHonour(loser.lord.getHonour() - give);

		// if (give != 0) {
		// rankDataManager.setHonour(winner.lord);
		// rankDataManager.setHonour(loser.lord);
		// }

		return give;
	}

	// public int giveStaffingExp(Player winner, Player loser, int exp) {
	// int loserExp = staticStaffingDataMgr.getTotalExp(loser.lord);
	// if (loserExp < exp) {
	// exp = loserExp;
	// }
	//
	// if (exp != 0) {
	// staticStaffingDataMgr.addStaffingExp(winner.lord, exp);
	// staticStaffingDataMgr.subStaffingExp(loser.lord, exp);
	//
	// rankDataManager.setStaffing(winner.lord);
	// rankDataManager.setStaffing(loser.lord);
	//
	// winner.lord.setStaffing(calcStaffing(winner));
	// loser.lord.setStaffing(calcStaffing(loser));
	//
	// synStaffingToPlayer(winner);
	// synStaffingToPlayer(loser);
	// }
	//
	// return exp;
	// }

	// public void addStaffingExp(Player player, int exp) {
	// staticStaffingDataMgr.addStaffingExp(player.lord, exp);
	//
	// rankDataManager.setStaffing(player.lord);
	//
	// player.lord.setStaffing(calcStaffing(player));
	//
	// synStaffingToPlayer(player);
	// }
	//
	// public int calcStaffing(Player player) {
	// // StaticStaffing staffing = staffingDataManager.calcStaffing(player);
	// // if (staffing != null) {
	// // return staffing.getStaffingId();
	// // }
	// //
	// // return 0;
	//
	// return staffingDataManager.calcStaffing(player);
	// }

	public void synInvasionToPlayer(Player target, March march) {
		if (target != null && target.isLogin) {
			SynInvasionRq.Builder builder = SynInvasionRq.newBuilder();
			builder.setInvasion(PbHelper.createInvasionPb(march));
			// Base.Builder msg =
			// PbHelper.createSynBase(SynInvasionRq.EXT_FIELD_NUMBER,
			// SynInvasionRq.ext, builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public void synArmyToPlayer(Player target, ArmyStatu armyStatu) {
		if (target != null && target.isLogin) {
			SynArmyRq.Builder builder = SynArmyRq.newBuilder();
			builder.setArmyStatu(PbHelper.createArmyStatuPb(armyStatu));
			// Base.Builder msg =
			// PbHelper.createSynBase(SynArmyRq.EXT_FIELD_NUMBER, SynArmyRq.ext,
			// builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public void synPartyOutToPlayer(Player target, int partyId) {
		if (target != null && target.isLogin) {
			SynPartyOutRq.Builder builder = SynPartyOutRq.newBuilder();
			builder.setPartyId(partyId);
			// Base.Builder msg =
			// PbHelper.createSynBase(SynPartyOutRq.EXT_FIELD_NUMBER,
			// SynPartyOutRq.ext, builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public void synPartyAcceptToPlayer(Player target, int partyId, int accept) {
		if (target != null && target.isLogin) {
			SynPartyAcceptRq.Builder builder = SynPartyAcceptRq.newBuilder();
			builder.setPartyId(partyId);
			builder.setAccept(accept);
			// Base.Builder msg =
			// PbHelper.createSynBase(SynPartyAcceptRq.EXT_FIELD_NUMBER,
			// SynPartyAcceptRq.ext, builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public void synBlessToPlayer(Player target, Lord lord) {
		if (target != null && target.isLogin) {
			SynBlessRq.Builder builder = SynBlessRq.newBuilder();

			long lordId = lord.getLordId();
			int sex = lord.getSex();
			String nick = lord.getNick();
			int icon = lord.getPortrait();
			int level = lord.getLevel();
			Man man = new Man(lordId, sex, nick, icon, level);
			builder.setMan(PbHelper.createManPb(man));
			// Base.Builder msg =
			// PbHelper.createSynBase(SynBlessRq.EXT_FIELD_NUMBER,
			// SynBlessRq.ext, builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public void synApplyPartyToPlayer(int partyId, int count) {
		List<Member> members = PartyDataManager.getInst().getMemberList(partyId);
		Iterator<Member> it = members.iterator();
		while (it.hasNext()) {
			Member member = it.next();
			if (member != null && member.getJob() >= PartyType.LEGATUS_CP) {
				Player target = getPlayer(member.getLordId());
				SynApplyRq.Builder builder = SynApplyRq.newBuilder();
				builder.setApplyCount(count);
				if (target != null && target.isLogin) {
					// Base.Builder msg =
					// PbHelper.createSynBase(SynApplyRq.EXT_FIELD_NUMBER,
					// SynApplyRq.ext, builder.build());
					// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
				}
			}
		}
	}

	public void synWarRecordToPlayer(Player target, SynWarRecordRq req) {
		if (target != null && target.isLogin) {

			// Base.Builder msg =
			// PbHelper.createSynBase(SynWarRecordRq.EXT_FIELD_NUMBER,
			// SynWarRecordRq.ext, req);
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public void synWarStateToPlayer(Player target, SynWarStateRq req) {
		if (target != null && target.isLogin) {
			// Base.Builder msg =
			// PbHelper.createSynBase(SynWarStateRq.EXT_FIELD_NUMBER,
			// SynWarStateRq.ext, req);
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public void synResourceToPlayer(Player target, int type, int count) {
		if (target != null && target.isLogin) {
			SynResourceRq.Builder builder = SynResourceRq.newBuilder();
			switch (type) {
			case 1:
				builder.setIron(count);
				break;
			case 2:
				builder.setOil(count);
				break;
			case 3:
				builder.setCopper(count);
				break;
			case 4:
				builder.setSilicon(count);
				break;
			case 5:
				builder.setStone(count);
				break;
			default:
				return;
			}

			// Base.Builder msg =
			// PbHelper.createSynBase(SynResourceRq.EXT_FIELD_NUMBER,
			// SynResourceRq.ext, builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public void synResourceToPlayer(Player target, int r1, int r2, int r3, int r4, int r5) {
		if (target != null && target.isLogin) {
			SynResourceRq.Builder builder = SynResourceRq.newBuilder();
			builder.setIron(r1);
			builder.setOil(r2);
			builder.setCopper(r3);
			builder.setSilicon(r4);
			builder.setStone(r5);

			// Base.Builder msg =
			// PbHelper.createSynBase(SynResourceRq.EXT_FIELD_NUMBER,
			// SynResourceRq.ext, builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public void synBuildToPlayer(Player target, BuildQue buildQue, int state) {
		if (target != null && target.isLogin) {
			SynBuildRq.Builder builder = SynBuildRq.newBuilder();
			builder.setQueue(PbHelper.createBuildQuePb(buildQue));
			builder.setState(state);

			// Base.Builder msg =
			// PbHelper.createSynBase(SynBuildRq.EXT_FIELD_NUMBER,
			// SynBuildRq.ext, builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public void synStaffingToPlayer(Player target) {
		if (target != null && target.isLogin) {
			SynStaffingRq.Builder builder = SynStaffingRq.newBuilder();
			builder.setStaffingLv(target.lord.getStaffingLv());
			builder.setStaffingExp(target.lord.getStaffingExp());
			builder.setStaffing(target.lord.getStaffing());

			// Base.Builder msg =
			// PbHelper.createSynBase(SynStaffingRq.EXT_FIELD_NUMBER,
			// SynStaffingRq.ext, builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	private int addTankMapCount(Map<Integer, Integer> formTanks, int tankId, int count, int maxCount) {
		if (count < 0) {
			return 0;
		}

		if (count > maxCount) {
			count = maxCount;
		}

		if (formTanks.containsKey(tankId)) {
			formTanks.put(tankId, formTanks.get(tankId) + count);
		} else {
			formTanks.put(tankId, count);
		}

		return count;
	}

	/**
	 * 
	 * Method: subTank
	 * 
	 * @Description: 扣除坦克 @param tank @param count @return void @throws
	 */
	public Tank subTank(Tank tank, int count) {
		tank.setCount(tank.getCount() - count);
		return tank;
	}

	public void updateFight(Player player) {
		// int fight = calcFight(player);
		// player.lord.setFight(fight);
	}

	/**
	 * 
	 * Method: formSlotCount
	 * 
	 * @Description: 阵容开启格子数量 @param lordLv @return @return int @throws
	 */
	public static int formSlotCount(int lordLv) {
		if (lordLv < 5) {
			return 2;
		} else if (lordLv < 6) {
			return 3;
		} else if (lordLv < 10) {
			return 4;
		} else if (lordLv < 15) {
			return 5;
		} else {
			return 6;
		}
	}

	/**
	 * 
	 * Method: formTankCount
	 * 
	 * @Description: 阵型中一个格子的带兵量上限 @param player @param
	 *               staticHero @return @return int @throws
	 */
	public int formTankCount(Player player, ConfHero staticHero) {
		int tankCount = 0;
		Lord lord = player.lord;
		// 等级兵力
		ConfLordLv staticLordLv = ConfLordLvProvider.getInst().getConfigById(lord.getLevel());
		tankCount += staticLordLv.getTankCount();

		// 繁荣度兵力
		if (lord.getPros() != 0) {
			ConfLordPros staticProsLv = ConfLordProsProvider.getInst().getStaticProsLv(lord.getPros());
			tankCount += staticProsLv.getTankCount();
		}

		// 统帅等级兵力
		if (lord.getCommand() != 0) {
			ConfLordCommand staticCommandLv = ConfLordCommandProvider.getInst().getConfigById(lord.getCommand());
			tankCount += staticCommandLv.getTankCount();
		}

		// 将领带兵量
		if (staticHero != null) {
			tankCount += staticHero.getTankCount();
		}

		return tankCount;
	}

	public void synGoldToPlayer(Player target, int addGold, int addTopup, String serialId) {
		if (target != null && target.isLogin) {
			SynGoldRq.Builder builder = SynGoldRq.newBuilder();
			builder.setGold(target.lord.getGold());
			builder.setAddGold(addGold);
			builder.setAddTopup(addTopup);
			builder.setVip(target.lord.getVip());
			builder.setSerialId(serialId);
			// Base.Builder msg =
			// PbHelper.createSynBase(SynGoldRq.EXT_FIELD_NUMBER, SynGoldRq.ext,
			// builder.build());
			// GameServer.getInstance().synMsgToPlayer(target.ctx, msg);
		}
	}

	public boolean checkAndSubTank(Player player, Form form, int tankCount) {
		int totalTank = 0;
		int count = 0;
		Map<Integer, Integer> formTanks = new HashMap<Integer, Integer>();
		int[] p = form.p;
		int[] c = form.c;
		for (int i = 0; i < p.length; i++) {
			if (p[i] > 0) {
				count = addTankMapCount(formTanks, p[i], c[i], tankCount);
				totalTank += count;
				c[i] = count;
			}
		}

		Map<Integer, Tank> tanks = player.tanks;
		for (Map.Entry<Integer, Integer> entry : formTanks.entrySet()) {
			Tank tank = tanks.get(entry.getKey());
			if (tank == null || tank.getCount() < entry.getValue()) {
				return false;
			}
		}

		if (totalTank == 0) {
			return false;
		}

		for (Map.Entry<Integer, Integer> entry : formTanks.entrySet()) {
			Tank tank = tanks.get(entry.getKey());
			subTank(tank, entry.getValue());
		}

		return true;
	}

	public List<Equip> getMinLvEquipById(Player player, int equipId) {
		List<Equip> rets = new ArrayList<Equip>();
		HashMap<Integer, Equip> equipMap = player.equips.get(0);
		Iterator<Equip> it = equipMap.values().iterator();
		while (it.hasNext()) {
			Equip next = it.next();
			if (next.getEquipId() == equipId) {
				rets.add(next);
			}
		}
		return rets;
	}

	public Part getMinLvPartById(Player player, int partId) {
		Part part = null;
		HashMap<Integer, Part> partMap = player.parts.get(0);
		Iterator<Part> it = partMap.values().iterator();
		while (it.hasNext()) {
			Part next = it.next();
			if (next.getPartId() == partId) {
				if (part == null) {
					part = next;
				} else if (next.getRefitLv() < part.getRefitLv()) {// 改造等级低的优先
					part = next;
				} else {
					if (next.getUpLv() < part.getUpLv()) {// 同改造等级,等级低的优先
						part = next;
					}
				}
			}
		}
		return part;
	}

	public Guy getGuy(long lordId) {
		Guy guy = guyMap.get(lordId);
		if (guy == null) {
			guy = new Guy(lordId);
			guyMap.put(lordId, guy);
		}
		return guy;
	}

	/**
	 * 
	 * Method: calcCollect
	 * 
	 * @Description: 计算当前部队携带的资源量 @param player @param army @param now @param
	 *               staticMine @param collect @return @return long @throws
	 */
	public long calcCollect(Player player, Army army, int now, ConfMine staticMine, int collect) {
		long get = 0;
		if (army.getGrab() != null) {
			get = army.getGrab().rs[staticMine.getType() - 1];
		}

		long payload = 0;
		Collect c = army.getCollect();
		if (c != null) {
			collect = (int) (collect * (1 + c.speed / 100.0f));
			payload = c.load;
		} else {
			payload = calcLoad(player, army.getForm());
		}

		get = get + (long) ((now - (army.getEndTime() - army.getPeriod())) / ((double) 3600) * collect);

		if (get > payload) {
			get = payload;
		}

		return get;
	}

	public int armyCount(Player player) {
		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(player.lord.getVip());
		if (staticVip != null) {
			return staticVip.getArmyCount();
		}
		return 3;
	}

}
