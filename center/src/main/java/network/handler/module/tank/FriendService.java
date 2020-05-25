package network.handler.module.tank;
//package com.game.module;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.game.constant.GameError;
//import com.game.constant.MailType;
//import com.game.dataMgr.StaticLordDataMgr;
//import com.game.domain.Player;
//import com.game.domain.p.Bless;
//import com.game.domain.p.Friend;
//import com.game.domain.p.Lord;
//import com.game.domain.p.Man;
//import com.game.domain.p.Mine;
//import com.game.domain.p.Store;
//import com.game.domain.s.StaticLordLv;
//import com.game.manager.PartyDataManager;
//import com.game.manager.PlayerDataManager;
//import com.game.manager.SmallIdManager;
//import com.game.manager.WorldDataManager;
//import com.game.message.handler.ClientHandler;
//import com.game.pb.CommonPb;
//import com.game.pb.CommonPb.Award;
//import com.game.pb.GamePb.AcceptBlessRq;
//import com.game.pb.GamePb.AcceptBlessRs;
//import com.game.pb.GamePb.AddFriendRq;
//import com.game.pb.GamePb.AddFriendRs;
//import com.game.pb.GamePb.AddTipFriendsRq;
//import com.game.pb.GamePb.AddTipFriendsRs;
//import com.game.pb.GamePb.BlessFriendRq;
//import com.game.pb.GamePb.BlessFriendRs;
//import com.game.pb.GamePb.DelFriendRq;
//import com.game.pb.GamePb.DelFriendRs;
//import com.game.pb.GamePb.DelStoreRq;
//import com.game.pb.GamePb.DelStoreRs;
//import com.game.pb.GamePb.GetBlessRs;
//import com.game.pb.GamePb.GetFriendRs;
//import com.game.pb.GamePb.GetStoreRs;
//import com.game.pb.GamePb.GetTipFriendsRs;
//import com.game.pb.GamePb.MarkStoreRq;
//import com.game.pb.GamePb.MarkStoreRs;
//import com.game.pb.GamePb.RecordStoreRq;
//import com.game.pb.GamePb.RecordStoreRs;
//import com.game.pb.GamePb.SeachPlayerRq;
//import com.game.pb.GamePb.SeachPlayerRs;
//import com.game.util.PbHelper;
//import com.game.util.RandomHelper;
//import com.game.util.TimeHelper;
//
///**
// * @author ChenKui
// * @version 创建时间：2015-9-3 上午10:52:08
// * @declare
// */
//@Service
//public class FriendService {
//	@Autowired
//	private PlayerDataManager playerDataManager;
//
//	@Autowired
//	private PartyDataManager partyDataManager;
//
//	@Autowired
//	private WorldDataManager worldDataManager;
//
//	@Autowired
//	private StaticLordDataMgr lordDataMgr;
//
//	@Autowired
//	private SmallIdManager smallIdManager;
//
//	@Autowired
//	private MilitaryScienceService militaryScienceService;
//
//	public void getFriend(ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		Map<Long, Friend> friendMap = player.friends;
//		GetFriendRs.Builder builder = GetFriendRs.newBuilder();
//		Iterator<Friend> it = friendMap.values().iterator();
//		int today = TimeHelper.getCurrentDay();
//		while (it.hasNext()) {
//			Friend friend = it.next();
//			if (friend.getBlessTime() != today) {
//				friend.setBless(0);
//			}
//			long lordId = friend.getLordId();
//			if (smallIdManager.isSmallId(lordId)) {
//				continue;
//			}
//
//			Friend entity = new Friend(lordId, friend.getBless(), friend.getBlessTime());
//			Player ee = playerDataManager.getPlayer(lordId);
//			if (ee == null) {
//				continue;
//			}
//			Lord lord = ee.lord;
//			if (lord == null) {
//				continue;
//			}
//			String partyName = partyDataManager.getPartyNameByLordId(lordId);
//			Man man = new Man();
//			man.setLordId(lordId);
//			man.setIcon(lord.getPortrait());
//			man.setLevel(lord.getLevel());
//			man.setNick(lord.getNick());
//			man.setSex(lord.getSex());
//			man.setPros(lord.getPros());
//			man.setFight(lord.getFight());
//			man.setProsMax(lord.getProsMax());
//			man.setPartyName(partyName);
//			builder.addFriend(PbHelper.createFriendPb(man, entity));
//		}
//		handler.sendMsgToPlayer(GetFriendRs.ext, builder.build());
//	}
//
//	public void addFriend(AddFriendRq req, ClientHandler handler) {
//		long friendId = req.getFriendId();
//		if (friendId == handler.getRoleId().longValue()) {
//			handler.sendErrorMsgToPlayer(GameError.FRIEND_HAD);
//			return;
//		}
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		Map<Long, Friend> friendMap = player.friends;
//		if (friendMap.containsKey(friendId)) {
//			handler.sendErrorMsgToPlayer(GameError.FRIEND_HAD);
//			return;
//		}
//		Player toFriend = playerDataManager.getPlayer(friendId);
//		if (toFriend == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_LORD);
//			return;
//		}
//
//		Friend friend = new Friend(friendId, 0, 0);
//		friendMap.put(friendId, friend);
//
//		playerDataManager.sendNormalMail(toFriend, MailType.MOLD_FRIEND_ADD, TimeHelper.getCurrentSecond(), player.lord.getNick());
//
//		AddFriendRs.Builder builder = AddFriendRs.newBuilder();
//		handler.sendMsgToPlayer(AddFriendRs.ext, builder.build());
//	}
//
//	public void seachPlayer(SeachPlayerRq req, ClientHandler handler) {
//		String nick = req.getNick();
//
//		Player player = playerDataManager.getPlayer(nick);
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		SeachPlayerRs.Builder builder = SeachPlayerRs.newBuilder();
//		Man man = new Man();
//		man.setLordId(lord.getLordId());
//		man.setSex(lord.getSex());
//		man.setIcon(lord.getPortrait());
//		man.setLevel(lord.getLevel());
//		man.setRanks(lord.getRanks());
//		man.setFight(lord.getFight());
//		man.setPros(lord.getPros());
//		man.setProsMax(lord.getProsMax());
//		man.setPartyName(partyDataManager.getPartyNameByLordId(lord.getLordId()));
//		builder.setMan(PbHelper.createManPb(man));
//
//		handler.sendMsgToPlayer(SeachPlayerRs.ext, builder.build());
//	}
//
//	public void delFriend(DelFriendRq req, ClientHandler handler) {
//		long friendId = req.getFriendId();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		Map<Long, Friend> friendMap = player.friends;
//		if (!friendMap.containsKey(friendId)) {
//			handler.sendErrorMsgToPlayer(GameError.FRIEND_NOT_EXIST);
//			return;
//		}
//		friendMap.remove(friendId);
//		DelFriendRs.Builder builder = DelFriendRs.newBuilder();
//		handler.sendMsgToPlayer(DelFriendRs.ext, builder.build());
//	}
//
//	public void blessFriendRq(BlessFriendRq req, ClientHandler handler) {
//		long friendId = req.getFriendId();
//		long lordId = handler.getRoleId();
//		Player player = playerDataManager.getPlayer(lordId);
//		Lord lord = player.lord;
//		StaticLordLv staticLordLv = lordDataMgr.getStaticLordLv(lord.getLevel());
//		if (staticLordLv == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_LORD);
//			return;
//		}
//		int currentDay = TimeHelper.getCurrentDay();
//		Map<Long, Friend> friendMap = player.friends;
//		int blessCount = lord.getBlessCount();
//		if (lord.getBlessTime() != currentDay) {
//			blessCount = 0;
//			lord.setBlessTime(currentDay);
//		}
//		int exp = 0;
//		if (friendId != 0) {// 单独对好友进行祝福
//			if (!friendMap.containsKey(friendId) || smallIdManager.isSmallId(friendId)) {
//				handler.sendErrorMsgToPlayer(GameError.FRIEND_NOT_EXIST);
//				return;
//			}
//			Player fplayer = playerDataManager.getPlayer(friendId);
//			if (fplayer == null) {
//				handler.sendErrorMsgToPlayer(GameError.NO_LORD);
//				return;
//			}
//			boolean flag = addBless(fplayer, lordId, currentDay);
//
//			Friend friend = friendMap.get(friendId);
//			friend.setBless(1);// 祝福
//			friend.setBlessTime(currentDay);
//			if (blessCount < 10) {
//				exp = staticLordLv.getBlessExp();
//				playerDataManager.addExp(lord, staticLordLv.getBlessExp());
//				blessCount++;
//			}
//			if (flag)
//				playerDataManager.synBlessToPlayer(fplayer, lord);
//
//		} else {// 一键全祝福
//			Iterator<Friend> it = friendMap.values().iterator();
//			int count = 0;
//			while (it.hasNext()) {
//				Friend next = it.next();
//				if (smallIdManager.isSmallId(next.getLordId())) {
//					continue;
//				}
//				if (next.getBlessTime() == currentDay) {// 已经祝福过
//					continue;
//				}
//				next.setBless(1);
//				next.setBlessTime(currentDay);
//
//				Player fplayer = playerDataManager.getPlayer(next.getLordId());
//				boolean flag = addBless(fplayer, lordId, currentDay);
//				if (flag) {
//					playerDataManager.synBlessToPlayer(fplayer, lord);
//				}
//
//				if (blessCount + count < 10) {
//					count++;
//				}
//			}
//			blessCount += count;
//			if (count > 0) {
//				exp = count * staticLordLv.getBlessExp();
//				playerDataManager.addExp(lord, exp);
//			}
//		}
//		lord.setBlessCount(blessCount);
//		lord.setBlessTime(currentDay);
//		BlessFriendRs.Builder builder = BlessFriendRs.newBuilder();
//		builder.setExp(exp);
//		handler.sendMsgToPlayer(BlessFriendRs.ext, builder.build());
//	}
//
//	public void getBlessRq(ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		Map<Long, Bless> blessMap = player.blesses;
//		GetBlessRs.Builder builder = GetBlessRs.newBuilder();
//		Iterator<Bless> it = blessMap.values().iterator();
//		int currentDay = TimeHelper.getCurrentDay();
//		int blessCount = 0;
//		while (it.hasNext()) {
//			Bless bless = it.next();
//			if (bless.getBlessTime() != currentDay) {
//				continue;
//			}
//			long lordId = bless.getLordId();
//			if (smallIdManager.isSmallId(lordId)) {
//				continue;
//			}
//			Bless entity = new Bless(lordId, bless.getBlessTime());
//			entity.setState(bless.getState());
//			Player blessPlayer = playerDataManager.getPlayer(lordId);
//			if (blessPlayer == null) {
//				continue;
//			}
//			Lord lord = blessPlayer.lord;
//			if (lord == null) {
//				continue;
//			}
//			if (++blessCount > 10) {
//				break;
//			}
//			int sex = lord.getSex();
//			String nick = lord.getNick();
//			int level = lord.getLevel();
//			int icon = lord.getPortrait();
//			Man man = new Man(lordId, sex, nick, icon, level);
//			builder.addBless(PbHelper.createBlessPb(man, entity));
//		}
//		handler.sendMsgToPlayer(GetBlessRs.ext, builder.build());
//	}
//
//	public void acceptBless(AcceptBlessRq req, ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		Map<Long, Bless> blessMap = player.blesses;
//		long friendId = req.getFriendId();
//		int addEnergy = 0;
//		int currentDay = TimeHelper.getCurrentDay();
//		Iterator<Bless> beenIt = blessMap.values().iterator();
//		int blessCount = 0;
//		while (beenIt.hasNext()) {
//			Bless bless = beenIt.next();
//			if (bless != null && bless.getBlessTime() == currentDay && bless.getState() != 0) {
//				blessCount++;
//				if (smallIdManager.isSmallId(bless.getLordId())) {
//					continue;
//				}
//			}
//		}
//		if (blessCount >= 10) {
//			handler.sendErrorMsgToPlayer(GameError.COUNT_NOT_ENOUGH);
//			return;
//		}
//
//		if (friendId == 0) {
//			Iterator<Bless> it = blessMap.values().iterator();
//			while (it.hasNext()) {
//				Bless bless = it.next();
//				if (bless != null && bless.getBlessTime() == currentDay && bless.getState() == 0) {
//					if (smallIdManager.isSmallId(bless.getLordId())) {
//						continue;
//					}
//					bless.setState(1);
//					addEnergy++;
//				}
//			}
//		} else {
//			Bless bless = blessMap.get(friendId);
//			if (bless != null && bless.getBlessTime() == currentDay && bless.getState() == 0) {
//				if (!smallIdManager.isSmallId(bless.getLordId())) {
//					bless.setState(1);
//					addEnergy++;
//				}
//			}
//		}
//		// 军工科技材料
//		List<Award> awards = new ArrayList<Award>();
//
//		if (addEnergy > 0) {
//			addEnergy = addEnergy + blessCount >= 10 ? 10 - blessCount : addEnergy;
//			addEnergy = addEnergy < 0 ? 0 : addEnergy;
//
//			for (int i = 0; i < addEnergy; i++) {
//				List<Award> a = militaryScienceService.getMilitaryBlessAward(player);
//				if (a != null && a.size() > 0) {
//					awards.addAll(a);
//				}
//			}
//			playerDataManager.addPower(player.lord, addEnergy);
//		}
//
//		AcceptBlessRs.Builder builder = AcceptBlessRs.newBuilder();
//		builder.setEnergy(player.lord.getPower());
//		if (awards.size() > 0) {
//			builder.addAllAward(awards);
//		}
//		handler.sendMsgToPlayer(AcceptBlessRs.ext, builder.build());
//	}
//
//	public void getStoreRq(ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		GetStoreRs.Builder builder = GetStoreRs.newBuilder();
//		List<Store> storeList = player.coords;
//		for (Store store : storeList) {
//			builder.addStore(PbHelper.createStorePb(store));
//		}
//		handler.sendMsgToPlayer(GetStoreRs.ext, builder.build());
//	}
//
//	public void recordStoreRq(RecordStoreRq req, ClientHandler handler) {
//		int pos = req.getPos();
//		int enemy = req.getEnemy();
//		int friend = req.getFriend();
//		int isMine = req.getIsMine();
//		int type = req.getType();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		RecordStoreRs.Builder builder = RecordStoreRs.newBuilder();
//		List<Store> storeList = player.coords;
//		Store store = new Store();
//		store.setPos(pos);
//		store.setType(type);
//		store.setEnemy(enemy);
//		store.setFriend(friend);
//		store.setIsMine(isMine);
//		if (type == 1) {
//			Player friendPlayer = worldDataManager.getPosData(pos);
//			if (friendPlayer != null) {
//				Lord lord = friendPlayer.lord;
//				Man man = new Man(lord.getLordId(), lord.getSex(), lord.getNick(), lord.getPortrait(), lord.getLevel());
//				store.setMan(man);
//			} else {
//				Lord lord = player.lord;
//				Man man = new Man(lord.getLordId(), lord.getSex(), lord.getNick(), lord.getPortrait(), lord.getLevel());
//				store.setMan(man);
//			}
//		} else {
//			int mineId = RandomHelper.randomInSize(5) + 1;
//			int mineLv = RandomHelper.randomInSize(90) + 1;
//			Mine mine = new Mine();
//			mine.setMineId(mineId);
//			mine.setMineLv(mineLv);
//			store.setMine(mine);
//		}
//		storeList.add(store);
//		builder.setStore(PbHelper.createStorePb(store));
//		handler.sendMsgToPlayer(RecordStoreRs.ext, builder.build());
//	}
//
//	public void markStoreRq(MarkStoreRq req, ClientHandler handler) {
//		CommonPb.Store store = req.getStore();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		MarkStoreRs.Builder builder = MarkStoreRs.newBuilder();
//		List<Store> storeList = player.coords;
//		for (Store entity : storeList) {
//			if (store.getPos() == entity.getPos()) {
//				entity.setMark(store.getMark());
//				entity.setFriend(store.getFriend());
//				entity.setEnemy(store.getEnemy());
//			}
//		}
//		handler.sendMsgToPlayer(MarkStoreRs.ext, builder.build());
//	}
//
//	public void delStoreRq(DelStoreRq req, ClientHandler handler) {
//		int pos = req.getPos();
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		DelStoreRs.Builder builder = DelStoreRs.newBuilder();
//		List<Store> storeList = player.coords;
//		Iterator<Store> it = storeList.iterator();
//		while (it.hasNext()) {
//			Store next = it.next();
//			if (next.getPos() == pos) {
//				it.remove();
//				break;
//			}
//		}
//		handler.sendMsgToPlayer(DelStoreRs.ext, builder.build());
//	}
//
//	private boolean addBless(Player player, long friendId, int now) {
//		Map<Long, Bless> blessMap = player.blesses;
//		Iterator<Bless> it = blessMap.values().iterator();
//		int blessCount = 0;
//		while (it.hasNext()) {
//			Bless next = it.next();
//			if (next.getBlessTime() != now || smallIdManager.isSmallId(next.getLordId())) {
//				it.remove();
//				continue;
//			}
//			blessCount++;
//		}
//		if (blessCount >= 10) {
//			return false;
//		}
//		Bless bless = blessMap.get(friendId);
//		if (bless == null) {
//			bless = new Bless(friendId, now);
//			blessMap.put(friendId, bless);
//			return true;
//		} else {
//			if (bless.getBlessTime() != now) {
//				bless.setState(0);// 祝福
//				bless.setBlessTime(now);
//				blessMap.put(friendId, bless);
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * 
//	 * @param handler
//	 */
//	public void getTipFriendsRq(ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		long roleId = player.lord.getLordId();
//		int playerLv = player.lord.getLevel();
//		int minLv = Math.abs(playerLv - 10);
//		int maxLv = playerLv + 10;
//
//		GetTipFriendsRs.Builder builder = GetTipFriendsRs.newBuilder();
//
//		Map<String, Player> playerMap = playerDataManager.getAllOnlinePlayer();
//
//		Iterator<Player> it = playerMap.values().iterator();
//		int count = 0;
//		while (it.hasNext()) {
//			Player next = it.next();
//			Lord lord = next.lord;
//			if (lord == null) {
//				continue;
//			}
//
//			if (lord.getLordId() == roleId) {
//				continue;
//			}
//			int level = lord.getLevel();
//			if (level >= minLv && level <= maxLv && count < 8) {
//				count++;
//				Man man = new Man();
//				man.setLordId(lord.getLordId());
//				man.setNick(lord.getNick());
//				man.setIcon(lord.getPortrait());
//				builder.addMan(PbHelper.createManPb(man));
//			}
//		}
//		handler.sendMsgToPlayer(GetTipFriendsRs.ext, builder.build());
//	}
//
//	/**
//	 * 一键添加好友
//	 * 
//	 * @param req
//	 * @param handler
//	 */
//	public void addTipFriendsRq(AddTipFriendsRq req, ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		AddTipFriendsRs.Builder builder = AddTipFriendsRs.newBuilder();
//		Map<Long, Friend> friendMap = player.friends;
//
//		List<Long> lordList = req.getLordIdList();
//		for (Long lordId : lordList) {
//			if (friendMap.containsKey(lordId)) {
//				continue;
//			}
//			Player toFriend = playerDataManager.getPlayer(lordId);
//			if (toFriend == null) {
//				continue;
//			}
//			playerDataManager.sendNormalMail(toFriend, MailType.MOLD_FRIEND_ADD, TimeHelper.getCurrentSecond(), player.lord.getNick());
//			Friend friend = new Friend(lordId, 0, 0);
//			friendMap.put(lordId, friend);
//
//		}
//		handler.sendMsgToPlayer(AddTipFriendsRs.ext, builder.build());
//	}
//}
