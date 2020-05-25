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
//import com.game.chat.domain.Chat;
//import com.game.constant.SysChatId;
//import com.game.domain.Player;
//import com.game.domain.p.Equip;
//import com.game.domain.p.Hero;
//import com.game.domain.p.Lord;
//import com.game.domain.p.Part;
//import com.game.domain.p.Prop;
//import com.game.domain.p.Science;
//import com.game.domain.p.Tank;
//import com.game.manager.PlayerDataManager;
//import com.game.message.handler.DealType;
//import com.game.message.handler.ServerHandler;
//import com.game.pb.BasePb.Base;
//import com.game.pb.CommonPb;
//import com.game.pb.InnerPb.BackBuildingRq;
//import com.game.pb.InnerPb.BackEquipRq;
//import com.game.pb.InnerPb.BackLordBaseRq;
//import com.game.pb.InnerPb.BackPartRq;
//import com.game.pb.InnerPb.CensusBaseRq;
//import com.game.pb.InnerPb.ForbiddenRq;
//import com.game.pb.InnerPb.GetLordBaseRq;
//import com.game.pb.InnerPb.ModLordRq;
//import com.game.pb.InnerPb.ModVipRq;
//import com.game.pb.InnerPb.NoticeRq;
//import com.game.pb.InnerPb.SendToMailRq;
//import com.game.server.GameServer;
//import com.game.server.ICommand;
//import com.game.util.DateHelper;
//import com.game.util.LogHelper;
//import com.game.util.PbHelper;
//import com.game.util.TimeHelper;
//
///**
// * 
// * @author ChenKui
// * 
// */
//@Service
//public class GmToolService {
//	@Autowired
//	private GmService gmService;
//	@Autowired
//	private PlayerDataManager playerDataManager;
//	@Autowired
//	private ChatService chatService;
//
//	public void forbidden(final ForbiddenRq req, final ServerHandler handler) {
//		GameServer.getInstance().mainLogicServer.addCommand(new ICommand() {
//			@Override
//			public void action() {
//				int forbiddenId = req.getForbiddenId();
//				if (req.hasNick()) {
//					String nick = req.getNick();
//					forbiddenLogic(forbiddenId, nick);
//				} else if (req.hasLordId()) {
//					long lordId = req.getLordId();
//					forbiddenLogic(forbiddenId, lordId);
//				}
//
//			}
//		}, DealType.MAIN);
//
//	}
//
//	public void modVip(final ModVipRq req, final ServerHandler handler) {
//		GameServer.getInstance().mainLogicServer.addCommand(new ICommand() {
//			@Override
//			public void action() {
//				long lordId = req.getLordId();
//				int type = req.getType();
//				int value = req.getValue();
//				modVipLogic(lordId, type, value);
//			}
//		}, DealType.MAIN);
//
//	}
//
//	public void modLord(final ModLordRq req, final ServerHandler handler) {
//		GameServer.getInstance().mainLogicServer.addCommand(new ICommand() {
//			@Override
//			public void action() {
//				long lordId = req.getLordId();
//				int type = req.getType();
//				int keyId = req.getKeyId();
//				int value = req.getValue();
//				modLordLogic(lordId, type, keyId, value);
//			}
//		}, DealType.MAIN);
//
//	}
//
//	public void getLordBase(final GetLordBaseRq req, final ServerHandler handler) {
//		GameServer.getInstance().mainLogicServer.addCommand(new ICommand() {
//			@Override
//			public void action() {
//				String marking = req.getMarking();
//				long lordId = req.getLordId();
//				int type = req.getType();
//				backLordBaseLogic(marking, lordId, type);
//			}
//		}, DealType.MAIN);
//
//	}
//
//	public void censusBase(final CensusBaseRq req, final ServerHandler handler) {
//		GameServer.getInstance().mainLogicServer.addCommand(new ICommand() {
//			@Override
//			public void action() {
//				String marking = req.getMarking();
//				int alv = req.getAlv();
//				int blv = req.getBlv();
//				int vip = req.getVip();
//				int type = req.getType();
//				int id = req.getId();
//				int count = req.getCount();
//				censusBaseLogic(marking, alv, blv, vip, type, id, count);
//			}
//		}, DealType.MAIN);
//
//	}
//
//	public void sendMail(final SendToMailRq req, final ServerHandler handler) {
//		GameServer.getInstance().mainLogicServer.addCommand(new ICommand() {
//			@Override
//			public void action() {
//				int moldId = Integer.parseInt(req.getMoldId());
//				String title = req.getTitle();
//				String content = req.getContont();
//				String award = req.getAward();
//				String to = req.getTo();
//				int type = req.getType();
//				int channelNo = req.getChannelNo();// 0全体 1-N渠道
//				int online = req.getOnline();// 0全体 1在线
//				String making = req.getMarking();
//				int avip = req.getAvip();
//				int bvip = req.getBvip();
//				int alv = req.getAlv();
//				int blv = req.getBlv();
//				sendMailLogic(making, type, channelNo, online, moldId, title, content, award, to, alv, blv, avip, bvip);
//			}
//		}, DealType.MAIN);
//
//	}
//
//	public void sendNotice(final NoticeRq req, final ServerHandler handler) {
//		GameServer.getInstance().mainLogicServer.addCommand(new ICommand() {
//			@Override
//			public void action() {
//				sendNoticeLogic(req.getContent());
//			}
//		}, DealType.MAIN);
//	}
//
//	public boolean backLordBaseLogic(String markging, long lordId, int type) {
//		Player player = playerDataManager.getPlayer(lordId);
//		BackLordBaseRq.Builder builder = BackLordBaseRq.newBuilder();
//		builder.setMarking(markging);
//		builder.setType(type);
//		if (player == null) {
//			builder.setCode(1);
//			Base.Builder baseBuilder = PbHelper.createRqBase(BackLordBaseRq.EXT_FIELD_NUMBER, null, BackLordBaseRq.ext, builder.build());
//			GameServer.getInstance().sendMsgToPublic(baseBuilder, 1);
//		} else {
//			builder.setCode(200);
//			switch (type) {
//			case 1: {// 背包道具
//				Iterator<Prop> it = player.props.values().iterator();
//				while (it.hasNext()) {
//					Prop next = it.next();
//					if (next.getCount() > 0) {
//						builder.addTowInt(PbHelper.createTwoIntPb(next.getPropId(), next.getCount()));
//					}
//				}
//				break;
//			}
//			case 2: {// 武将
//				Iterator<Hero> it = player.heros.values().iterator();
//				while (it.hasNext()) {
//					Hero next = it.next();
//					if (next.getCount() > 0) {
//						builder.addTowInt(PbHelper.createTwoIntPb(next.getHeroId(), next.getCount()));
//					}
//				}
//				break;
//			}
//			case 3: {// 坦克
//				Iterator<Tank> it = player.tanks.values().iterator();
//				while (it.hasNext()) {
//					Tank next = it.next();
//					if (next.getCount() > 0) {
//						builder.addTowInt(PbHelper.createTwoIntPb(next.getTankId(), next.getCount()));
//					}
//				}
//				break;
//			}
//			case 4: {// 建筑
//				BackBuildingRq.Builder buildingBuilder = BackBuildingRq.newBuilder();
//				buildingBuilder.setMarking(markging);
//				buildingBuilder.setType(type);
//				buildingBuilder.setCode(200);
//
//				buildingBuilder.setWare1(player.building.getWare1());
//				buildingBuilder.setWare2(player.building.getWare2());
//				buildingBuilder.setTech(player.building.getTech());
//				buildingBuilder.setFactory1(player.building.getFactory1());
//				buildingBuilder.setFactory2(player.building.getFactory2());
//				buildingBuilder.setRefit(player.building.getRefit());
//				buildingBuilder.setCommand(player.building.getCommand());
//				buildingBuilder.setWorkShop(player.building.getWorkShop());
//
//				Base.Builder baseBuilder = PbHelper.createRqBase(BackBuildingRq.EXT_FIELD_NUMBER, null, BackBuildingRq.ext, buildingBuilder.build());
//				GameServer.getInstance().sendMsgToPublic(baseBuilder, 1);
//				return true;
//			}
//			case 5: {// 科技
//				Iterator<Science> it = player.sciences.values().iterator();
//				while (it.hasNext()) {
//					Science next = it.next();
//					builder.addTowInt(PbHelper.createTwoIntPb(next.getScienceId(), next.getScienceLv()));
//				}
//				break;
//			}
//			case 6: {// 配件
//				BackPartRq.Builder partBuilder = BackPartRq.newBuilder();
//				partBuilder.setMarking(markging);
//				partBuilder.setType(type);
//
//				partBuilder.setCode(200);
//				for (int i = 0; i < 5; i++) {
//					Map<Integer, Part> map = player.parts.get(i);
//					if (map != null) {
//						Iterator<Part> it = map.values().iterator();
//						while (it.hasNext()) {
//							partBuilder.addPart(PbHelper.createPartPb(it.next()));
//						}
//					}
//				}
//				Base.Builder baseBuilder = PbHelper.createRqBase(BackPartRq.EXT_FIELD_NUMBER, null, BackPartRq.ext, partBuilder.build());
//				GameServer.getInstance().sendMsgToPublic(baseBuilder, 1);
//				return true;
//			}
//			case 7: {// 装备
//				BackEquipRq.Builder equipBuilder = BackEquipRq.newBuilder();
//				equipBuilder.setMarking(markging);
//				equipBuilder.setType(type);
//
//				equipBuilder.setCode(200);
//				for (int i = 0; i < 7; i++) {
//					Map<Integer, Equip> equipMap = player.equips.get(i);
//					Iterator<Equip> it = equipMap.values().iterator();
//					while (it.hasNext()) {
//						equipBuilder.addEquip(PbHelper.createEquipPb(it.next()));
//					}
//				}
//				Base.Builder baseBuilder = PbHelper.createRqBase(BackEquipRq.EXT_FIELD_NUMBER, null, BackEquipRq.ext, equipBuilder.build());
//				GameServer.getInstance().sendMsgToPublic(baseBuilder, 1);
//				return true;
//			}
//			default:
//				break;
//			}
//			Base.Builder baseBuilder = PbHelper.createRqBase(BackLordBaseRq.EXT_FIELD_NUMBER, null, BackLordBaseRq.ext, builder.build());
//			GameServer.getInstance().sendMsgToPublic(baseBuilder, 1);
//		}
//		return true;
//	}
//
//	public boolean censusBaseLogic(String markging, int alv, int blv, int vip, int type, int id, int count) {
//		Map<Long, Player> playerCache = playerDataManager.getPlayers();
//
//		String time = DateHelper.displayNowDateTime();
//
//		switch (type) {
//		case 1: {// 道具
//			Iterator<Player> it = playerCache.values().iterator();
//
//			while (it.hasNext()) {
//				Player next = it.next();
//				Lord lord = next.lord;
//				if (lord == null) {
//					continue;
//				}
//				if (alv != 0 && lord.getLevel() < alv) {
//					continue;
//				}
//				if (blv != 0 && lord.getLevel() > blv) {
//					continue;
//				}
//				Prop prop = next.props.get(id);
//				if (prop != null && prop.getCount() >= count) {
//					StringBuffer sb = new StringBuffer();
//					sb.append(time + "|" + lord.getLordId()).append("|").append(lord.getNick()).append("|").append(lord.getLevel()).append("|")
//							.append(lord.getVip()).append("|").append(prop.getPropId()).append("|").append(prop.getCount());
//					LogHelper.PROP_LOGGER.error(sb.toString());
//				}
//			}
//			break;
//		}
//		case 2: {// 武将
//			Iterator<Player> it = playerCache.values().iterator();
//			while (it.hasNext()) {
//				Player next = it.next();
//				Lord lord = next.lord;
//				if (lord == null) {
//					continue;
//				}
//				if (alv != 0 && lord.getLevel() < alv) {
//					continue;
//				}
//				if (blv != 0 && lord.getLevel() > blv) {
//					continue;
//				}
//				Hero hero = next.heros.get(id);
//				if (hero != null && hero.getCount() >= count) {
//					StringBuffer sb = new StringBuffer();
//					sb.append(time + "|" + lord.getLordId()).append("|").append(lord.getNick()).append("|").append(lord.getLevel()).append("|")
//							.append(lord.getVip()).append("|").append(hero.getHeroId()).append("|").append(hero.getCount());
//					LogHelper.HERO_LOGGER.error(sb.toString());
//				}
//			}
//			break;
//		}
//		case 16: {// 金币
//			Iterator<Player> it = playerCache.values().iterator();
//			while (it.hasNext()) {
//				Player next = it.next();
//				Lord lord = next.lord;
//				if (lord == null) {
//					continue;
//				}
//				if (alv != 0 && lord.getLevel() < alv) {
//					continue;
//				}
//				if (blv != 0 && lord.getLevel() > blv) {
//					continue;
//				}
//				if (lord.getGold() >= count) {
//					StringBuffer sb = new StringBuffer();
//					sb.append(time + "|" + lord.getLordId()).append("|").append(lord.getNick()).append("|").append(lord.getLevel()).append("|")
//							.append(lord.getVip()).append("|").append(lord.getGold());
//					LogHelper.GOLD_LOGGER.error(sb.toString());
//				}
//			}
//			break;
//		}
//
//		default:
//			break;
//		}
//		// BackLordBaseRq.Builder builder = BackLordBaseRq.newBuilder();
//		// builder.setMarking(markging);
//		// builder.setType(type);
//		// if (player == null) {
//		// builder.setCode(1);
//		// Base.Builder baseBuilder =
//		// PbHelper.createRqBase(BackLordBaseRq.EXT_FIELD_NUMBER, null,
//		// BackLordBaseRq.ext, builder.build());
//		// GameServer.getInstance().sendMsgToPublic(baseBuilder);
//		// } else {
//		// builder.setCode(200);
//		// switch (type) {
//		// case 1: {// 背包道具
//		// Iterator<Prop> it = player.props.values().iterator();
//		// while (it.hasNext()) {
//		// Prop next = it.next();
//		// builder.addTowInt(PbHelper.createTwoIntPb(next.getPropId(),
//		// next.getCount()));
//		// }
//		// break;
//		// }
//		// case 2: {// 武将
//		// Iterator<Hero> it = player.heros.values().iterator();
//		// while (it.hasNext()) {
//		// Hero next = it.next();
//		// builder.addTowInt(PbHelper.createTwoIntPb(next.getHeroId(),
//		// next.getCount()));
//		// }
//		// break;
//		// }
//		// case 3: {// 坦克
//		// Iterator<Tank> it = player.tanks.values().iterator();
//		// while (it.hasNext()) {
//		// Tank next = it.next();
//		// builder.addTowInt(PbHelper.createTwoIntPb(next.getTankId(),
//		// next.getCount()));
//		// }
//		// break;
//		// }
//		// case 4: {// 建筑
//		// BackBuildingRq.Builder buildingBuilder = BackBuildingRq.newBuilder();
//		// buildingBuilder.setMarking(markging);
//		// buildingBuilder.setType(type);
//		// buildingBuilder.setCode(200);
//		//
//		// buildingBuilder.setWare1(player.building.getWare1());
//		// buildingBuilder.setWare2(player.building.getWare2());
//		// buildingBuilder.setTech(player.building.getTech());
//		// buildingBuilder.setFactory1(player.building.getFactory1());
//		// buildingBuilder.setFactory2(player.building.getFactory2());
//		// buildingBuilder.setRefit(player.building.getRefit());
//		// buildingBuilder.setCommand(player.building.getCommand());
//		// buildingBuilder.setWorkShop(player.building.getWorkShop());
//		//
//		// Base.Builder baseBuilder =
//		// PbHelper.createRqBase(BackBuildingRq.EXT_FIELD_NUMBER, null,
//		// BackBuildingRq.ext, buildingBuilder.build());
//		// GameServer.getInstance().sendMsgToPublic(baseBuilder);
//		// return true;
//		// }
//		// case 5: {// 科技
//		// Iterator<Science> it = player.sciences.values().iterator();
//		// while (it.hasNext()) {
//		// Science next = it.next();
//		// builder.addTowInt(PbHelper.createTwoIntPb(next.getScienceId(),
//		// next.getScienceLv()));
//		// }
//		// break;
//		// }
//		// case 6: {// 配件
//		// BackPartRq.Builder partBuilder = BackPartRq.newBuilder();
//		// partBuilder.setMarking(markging);
//		// partBuilder.setType(type);
//		//
//		// partBuilder.setCode(200);
//		// for (int i = 0; i < 5; i++) {
//		// Map<Integer, Part> map = player.parts.get(i);
//		// Iterator<Part> it = map.values().iterator();
//		// while (it.hasNext()) {
//		// partBuilder.addPart(PbHelper.createPartPb(it.next()));
//		// }
//		// }
//		// Base.Builder baseBuilder =
//		// PbHelper.createRqBase(BackPartRq.EXT_FIELD_NUMBER, null,
//		// BackPartRq.ext, partBuilder.build());
//		// GameServer.getInstance().sendMsgToPublic(baseBuilder);
//		// return true;
//		// }
//		// default:
//		// break;
//		// }
//		// Base.Builder baseBuilder =
//		// PbHelper.createRqBase(BackLordBaseRq.EXT_FIELD_NUMBER, null,
//		// BackLordBaseRq.ext, builder.build());
//		// GameServer.getInstance().sendMsgToPublic(baseBuilder);
//		// }
//		return true;
//	}
//
//	public boolean forbiddenLogic(int forbiddenId, String nick) {
//		if (forbiddenId == 1) {
//			gmService.gmSilence(nick, 1);
//		} else if (forbiddenId == 2) {
//			gmService.gmSilence(nick, 0);
//		} else if (forbiddenId == 3) {
//			gmService.gmForbidden(nick, 1);
//			gmService.gmKick(nick);
//		} else if (forbiddenId == 4) {
//			gmService.gmForbidden(nick, 0);
//		} else if (forbiddenId == 5) {
//			gmService.gmKick(nick);
//		}
//		return true;
//	}
//
//	public boolean forbiddenLogic(int forbiddenId, long lordId) {
//		if (forbiddenId == 1) {
//			Player player = playerDataManager.getPlayer(lordId);
//			if (player != null && player.account.getIsGm() == 0) {
//				player.lord.setSilence(1);
//			}
//		} else if (forbiddenId == 2) {
//			Player player = playerDataManager.getPlayer(lordId);
//			if (player != null && player.account.getIsGm() == 0) {
//				player.lord.setSilence(0);
//			}
//		} else if (forbiddenId == 3) {
//			Player player = playerDataManager.getPlayer(lordId);
//			if (player != null && player.account.getIsGm() == 0) {
//				player.account.setForbid(1);
//				if (player.isLogin && player.account.getIsGm() == 0 && player.ctx != null) {
//					player.ctx.close();
//				}
//			}
//		} else if (forbiddenId == 4) {
//			Player player = playerDataManager.getPlayer(lordId);
//			if (player != null && player.account.getIsGm() == 0) {
//				player.account.setForbid(0);
//			}
//		} else if (forbiddenId == 5) {
//			Player player = playerDataManager.getPlayer(lordId);
//			if (player != null && player.account.getIsGm() == 0) {
//				if (player.isLogin && player.account.getIsGm() == 0 && player.ctx != null) {
//					player.ctx.close();
//				}
//			}
//		}
//		return true;
//	}
//
//	public boolean modVipLogic(long lordId, int type, int value) {
//		Player player = playerDataManager.getPlayer(lordId);
//		System.out.println("lordId" + lordId + "type" + type + "value" + value);
//		if (type == 1) {// 修改VIP
//			if (player != null && value >= 0 && value <= 12) {
//				player.lord.setVip(value);
//				if (value > 0) {
//					chatService.sendWorldChat(chatService.createSysChat(SysChatId.BECOME_VIP, player.lord.getNick(), "" + value));
//				}
//			}
//		} else if (type == 2) {
//			if (player != null && value >= 0) {
//				player.lord.setTopup(value);
//			}
//		}
//		return true;
//	}
//
//	public boolean modLordLogic(long lordId, int type, int keyId, int value) {
//		Player player = playerDataManager.getPlayer(lordId);
//		if (player == null) {
//			return true;
//		}
//		switch (type) {
//		case 1: {// 背包道具
//			return true;
//		}
//		case 2: {// 武将
//			return true;
//		}
//		case 3: {// 坦克
//			return true;
//		}
//		case 4: {// 建筑
//			return true;
//		}
//		case 5: {// 科技
//			return true;
//		}
//		case 6: {// 配件
//			return true;
//		}
//		case 7: {// 装备
//			for (int i = 0; i < 7; i++) {
//				Map<Integer, Equip> map = player.equips.get(i);
//				if (map != null) {
//					Iterator<Equip> it = map.values().iterator();
//					while (it.hasNext()) {
//						Equip equip = it.next();
//						if (equip.getKeyId() == keyId) {
//							equip.setLv(value);
//							equip.setExp(0);
//						}
//					}
//				}
//			}
//			return true;
//		}
//		default:
//			break;
//		}
//		return true;
//	}
//
//	public boolean sendMailLogic(String marking, int type, int channelNo, int online, int moldId, String title, String content, String award, String to,
//			int alv, int blv, int avip, int bvip) {
//		// 邮件内容
//		String[] params = null;
//		if (!title.equals("") && !content.equals("")) {
//			params = new String[] { title, content };
//		} else if (title.equals("") && !content.equals("")) {
//			params = new String[] { content };
//		} else if (!title.equals("") && content.equals("")) {
//			params = new String[] { title };
//		}
//
//		// 附件内容
//		List<CommonPb.Award> awardList = new ArrayList<CommonPb.Award>();
//		if (!award.equals("")) {
//			String[] awards = award.split("\\|");
//			if (awards.length % 3 == 0) {
//				for (int i = 0; i < awards.length; i += 3) {
//					CommonPb.Award en = PbHelper.createAwardPb(Integer.parseInt(awards[i]), Integer.parseInt(awards[i + 1]), Integer.parseInt(awards[i + 2]));
//					awardList.add(en);
//				}
//			}
//		}
//
//		// 发送对象
//		if (type == 1) {// 按玩家发放
//			gmService.playerMail(to, moldId, awardList, params);
//			return true;
//		} else if (type == 2) {// 按服渠道在线发放
//			Iterator<Player> it = playerDataManager.getPlayers().values().iterator();
//			if (awardList.size() == 0) {
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player == null || player.account == null || !player.isActive() || player.lord == null) {
//						continue;
//					}
//					if (channelNo == 0 || player.account.getPlatNo() == channelNo) {
//						Lord lord = player.lord;
//						if (alv != 0 && lord.getLevel() < alv) {
//							continue;
//						}
//						if (blv != 0 && lord.getLevel() > blv) {
//							continue;
//						}
//						if (avip != 0 && lord.getVip() < avip) {
//							continue;
//						}
//						if (bvip != 0 && lord.getVip() > bvip) {
//							continue;
//						}
//
//						if (online == 0) {// 全体成员
//							playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), params);
//						} else if (online == 1 && player.ctx != null) {// 在线玩家
//							playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), params);
//						}
//					}
//				}
//			} else {
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player == null || player.account == null || !player.isActive() || player.lord == null) {
//						continue;
//					}
//
//					if (channelNo == 0 || player.account.getPlatNo() == channelNo) {
//
//						Lord lord = player.lord;
//						if (alv != 0 && lord.getLevel() < alv) {
//							continue;
//						}
//						if (blv != 0 && lord.getLevel() > blv) {
//							continue;
//						}
//						if (avip != 0 && lord.getVip() < avip) {
//							continue;
//						}
//						if (bvip != 0 && lord.getVip() > bvip) {
//							continue;
//						}
//
//						if (online == 0) {// 全体成员
//							playerDataManager.sendAttachMail(player, awardList, moldId, TimeHelper.getCurrentSecond(), params);
//						} else if (online == 1 && player.ctx != null) {// 在线玩家
//							playerDataManager.sendAttachMail(player, awardList, moldId, TimeHelper.getCurrentSecond(), params);
//						}
//					}
//				}
//			}
//
//			return true;
//		}
//		return true;
//	}
//
//	public boolean sendNoticeLogic(String content) {
//		Chat chat = chatService.createSysChat(SysChatId.SYS_HORN, content);
//		chatService.sendHornChat(chat, 1);
//		return true;
//	}
//
//}
