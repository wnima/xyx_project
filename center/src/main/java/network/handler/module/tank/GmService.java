package network.handler.module.tank;
///**   
// * @Title: GmService.java    
// * @Package com.game.service    
// * @Description: TODO  
// * @author ZhangJun   
// * @date 2015年9月6日 下午7:11:51    
// * @version V1.0   
// */
//package com.game.module;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.game.constant.AwardFrom;
//import com.game.constant.AwardType;
//import com.game.constant.GameError;
//import com.game.constant.MailType;
//import com.game.constant.SysChatId;
//import com.game.domain.Player;
//import com.game.domain.p.Equip;
//import com.game.domain.p.Part;
//import com.game.manager.PlayerDataManager;
//import com.game.message.handler.ClientHandler;
//import com.game.pb.CommonPb;
//import com.game.pb.CommonPb.Mail;
//import com.game.pb.GamePb.DoSomeRq;
//import com.game.pb.GamePb.DoSomeRs;
//import com.game.util.LogHelper;
//import com.game.util.TimeHelper;
//
///**
// * @ClassName: GmService
// * @Description: TODO
// * @author ZhangJun
// * @date 2015年9月6日 下午7:11:51
// * 
// */
//@Service
//public class GmService {
//	@Autowired
//	private PlayerDataManager playerDataManager;
//
//	@Autowired
//	private BuildingService buildingService;
//	
//	@Autowired
//	private ChatService chatService;
//	
//	public void doSome(DoSomeRq req, ClientHandler handler) {
//
//		String str = req.getStr();
//		String[] words = str.split(" ");
//		int paramCount = words.length;
//		if (paramCount < 2 || paramCount > 4) {
//			handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//			return;
//		}
//
//		try {
//			String cmd = null;
//			String type = null;
//			String id = null;
//			String count = null;
//			if (paramCount == 2) {
//				cmd = words[0];
//				type = words[1];
//			} else if (paramCount == 3) {
//				cmd = words[0];
//				type = words[1];
//				count = words[2];
//				id = "0";
//			} else if (paramCount == 4) {
//				cmd = words[0];
//				type = words[1];
//				id = words[2];
//				count = words[3];
//			}
//
//			Player player = playerDataManager.getPlayer(handler.getRoleId());
//
//			if (player.account.getIsGm() <= 0) {
//				LogHelper.GM_LOGGER.trace("player {" + player.roleId + "} ilegal operating!");
//				handler.sendErrorMsgToPlayer(GameError.NO_AUTHORITY);
//				return;
//			}
//
//			LogHelper.GM_LOGGER.trace("gm {" + player.lord.getNick() + "|" + player.roleId + "} do operate {" + str + "}");
//
//			if ("add".equalsIgnoreCase(cmd)) {
//				gmAdd(player, type, Integer.valueOf(id), Integer.valueOf(count));
//			} else if ("del".equalsIgnoreCase(cmd)) {
//				delMail(Long.parseLong(type), Integer.parseInt(count));
//			} else if ("set".equalsIgnoreCase(cmd)) {
//				gmSet(player, type, Integer.valueOf(count));
//			} else if ("clear".equalsIgnoreCase(cmd)) {
//				gmClear(player, type);
//			} else if ("build".equalsIgnoreCase(cmd)) {
//				gmBuild(player, Integer.valueOf(type), Integer.valueOf(count), handler);
//				return;
//			} else if ("system".equalsIgnoreCase(cmd)) {
//				gmSystem(type);
//				return;
//			} else if ("mail".equalsIgnoreCase(cmd)) {
//				Mail mail = null;
//				if (req.hasMail()) {
//					mail = req.getMail();
//				} else {
//					handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//					return;
//				}
//
//				LogHelper.GM_LOGGER.trace("gm {" + player.roleId + "} send mail {" + mail + "}");
//				gmMail(mail, type);
//				return;
//			} else if ("platMail".equalsIgnoreCase(cmd)) {
//				Mail mail = null;
//				if (req.hasMail()) {
//					mail = req.getMail();
//				} else {
//					handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//					return;
//				}
//
//				LogHelper.GM_LOGGER.trace("gm {" + player.roleId + "} send plat mail {" + mail + "}");
//				gmPlatMail(mail, type);
//				return;
//			} else if ("kick".equalsIgnoreCase(cmd)) {
//				gmKick(type);
//				return;
//			} else if ("silence".equalsIgnoreCase(cmd)) {
//				gmSilence(type, Integer.valueOf(count));
//				return;
//			} else if ("ganVip".equalsIgnoreCase(cmd)) {
//				gmVip(type, Integer.valueOf(count));
//				return;
//			} else if ("clearPlayer".equalsIgnoreCase(cmd)) {
//				gmClearPlayer(type, count);
//				return;
//			} else if ("ganTopup".equalsIgnoreCase(cmd)) {
//				gmTopup(type, Integer.valueOf(count));
//				return;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//
//		DoSomeRs.Builder builder = DoSomeRs.newBuilder();
//		handler.sendMsgToPlayer(DoSomeRs.ext, builder.build());
//	}
//
//	private void gmAdd(Player player, String str, int id, long count) {
//		if ("gold".equalsIgnoreCase(str)) {
//			playerDataManager.addAward(player, AwardType.GOLD, id, count, AwardFrom.DO_SOME);
//		} else if ("prop".equalsIgnoreCase(str)) {
//			playerDataManager.addAward(player, AwardType.PROP, id, count, AwardFrom.DO_SOME);
//		} else if ("equip".equalsIgnoreCase(str)) {
//			playerDataManager.addAward(player, AwardType.EQUIP, id, count, AwardFrom.DO_SOME);
//		} else if ("chip".equalsIgnoreCase(str)) {
//			playerDataManager.addAward(player, AwardType.CHIP, id, count, AwardFrom.DO_SOME);
//		} else if ("part".equalsIgnoreCase(str)) {
//			playerDataManager.addAward(player, AwardType.PART, id, count, AwardFrom.DO_SOME);
//		} else if ("hero".equalsIgnoreCase(str)) {
//			playerDataManager.addAward(player, AwardType.HERO, id, count, AwardFrom.DO_SOME);
//		} else if ("tank".equalsIgnoreCase(str)) {
//			playerDataManager.addAward(player, AwardType.TANK, id, count, AwardFrom.DO_SOME);
//		} else if ("power".equalsIgnoreCase(str)) {
//			playerDataManager.addAward(player, AwardType.POWER, id, count, AwardFrom.DO_SOME);
//		} else if ("material".equalsIgnoreCase(str)) {
//			playerDataManager.addAward(player, AwardType.PART_MATERIAL, id, count, AwardFrom.DO_SOME);
//		}else if("military_material".equalsIgnoreCase(str)) {
//			playerDataManager.addAward(player, AwardType.MILITARY_MATERIAL, id, count, AwardFrom.DO_SOME);
//		}
//	}
//
//	private void gmSet(Player player, String str, int count) {
//		if ("combat".equalsIgnoreCase(str)) {
//			player.combatId = count;
//		} else if ("equip".equalsIgnoreCase(str)) {
//			player.equipEplrId = count;
//		} else if ("part".equalsIgnoreCase(str)) {
//			player.partEplrId = count;
//		} else if ("extr".equalsIgnoreCase(str)) {
//			player.extrEplrId = count;
//		} 
//		else if("military".equalsIgnoreCase(str)) {
//			player.militaryEplrId = count;
//		}
//		else if ("time".equalsIgnoreCase(str)) {
//			player.timePrlrId = count;
//		} else if ("vip".equalsIgnoreCase(str)) {
//			player.lord.setVip(count);
//			if(count>0) {
//				chatService.sendWorldChat(chatService.createSysChat(SysChatId.BECOME_VIP, player.lord.getNick(),""+count));
//			}
//		} else if ("lv".equalsIgnoreCase(str)) {
//			player.lord.setLevel(count);
//		} else if ("topup".equalsIgnoreCase(str)) {
//			player.lord.setTopup(count);
//		} else if ("staffing".equalsIgnoreCase(str)) {
//			player.lord.setStaffingLv(count);
//		}else if ("staffingExp".equalsIgnoreCase(str)) {
//			player.lord.setStaffingExp(count);
//		}else if ("ranks".equalsIgnoreCase(str)) {
//			player.lord.setRanks(count);
//		}
//	}
//
//	public void gmMail(Mail mail, String to) {
//		String content = null;
//		if (mail.hasContont()) {
//			content = mail.getContont();
//		}
//
//		int moldId = MailType.MOLD_SYSTEM_PUB_1;
//		if (mail.hasMoldId()) {
//			moldId = mail.getMoldId();
//		}
//
//		if (moldId == MailType.MOLD_SYSTEM_3 || moldId == MailType.MOLD_SYSTEM_2 || moldId == MailType.MOLD_SYSTEM_1 || moldId == MailType.MOLD_SYSTEM_PUB_2) {
//			if ("all".equalsIgnoreCase(to)) {
//				Iterator<Player> it = playerDataManager.getPlayers().values().iterator();
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player != null && player.isActive()) {
//						playerDataManager.sendAttachMail(player, mail.getAwardList(), moldId, TimeHelper.getCurrentSecond(), content);
//					}
//				}
//			} else if ("online".equalsIgnoreCase(to)) {
//				Iterator<Player> it = playerDataManager.getAllOnlinePlayer().values().iterator();
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player != null && player.isActive()) {
//						playerDataManager.sendAttachMail(player, mail.getAwardList(), moldId, TimeHelper.getCurrentSecond(), content);
//					}
//				}
//			} else {
//				String[] names = to.split("\\|");
//				if (names == null) {
//					System.out.println("gmMail to null");
//					return;
//				}
//
//				for (String name : names) {
//					System.out.println("gmMail to " + name);
//					Player player = playerDataManager.getPlayer(name);
//					if (player != null && player.isActive()) {
//						playerDataManager.sendAttachMail(player, mail.getAwardList(), moldId, TimeHelper.getCurrentSecond(), content);
//					}
//				}
//			}
//		} else if (moldId == MailType.MOLD_SYSTEM_PUB_1) {
//			if ("all".equalsIgnoreCase(to)) {
//				Iterator<Player> it = playerDataManager.getPlayers().values().iterator();
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player != null && player.isActive()) {
//						playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), content);
//					}
//				}
//			} else if ("online".equalsIgnoreCase(to)) {
//				Iterator<Player> it = playerDataManager.getAllOnlinePlayer().values().iterator();
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player != null && player.isActive()) {
//						playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), content);
//					}
//				}
//			} else {
//				String[] names = to.split("\\|");
//				if (names == null) {
//					System.out.println("gmMail to null");
//					return;
//				}
//
//				for (String name : names) {
//					System.out.println("gmMail to " + name);
//					Player player = playerDataManager.getPlayer(name);
//					if (player != null && player.isActive()) {
//						playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), content);
//					}
//				}
//			}
//		}
//	}
//
//	public void delMail(long playerId, int keyId) {
//		Player player = playerDataManager.getPlayer(playerId);
//		if (player != null && player.isActive()) {
//			if (player.mails.containsKey(keyId)) {
//				player.mails.remove(keyId);
//			}
//		}
//	}
//
//	/**
//	 * 给玩家邮件
//	 * 
//	 * @param to
//	 * @param moldId
//	 * @param content
//	 */
//	public void playerMail(String to, int moldId, List<CommonPb.Award> awards, String... content) {
//		String[] names = to.split("\\|");
//		if (names == null) {
//			System.out.println("gmMail to null");
//			return;
//		}
//		if (awards == null || awards.size() == 0) {
//			for (String name : names) {
//				System.out.println("gmMail to " + name);
//				Player player = playerDataManager.getPlayer(name);
//				if (player != null && player.isActive()) {
//					playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), content);
//				}
//			}
//		} else {
//			for (String name : names) {
//				System.out.println("gmMail to " + name);
//				Player player = playerDataManager.getPlayer(name);
//				if (player != null && player.isActive()) {
//					playerDataManager.sendAttachMail(player, awards, moldId, TimeHelper.getCurrentSecond(), content);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * @param moldId
//	 * @param online0全体1在线玩家
//	 * @param channelId0全渠道
//	 * @param awards
//	 * @param content
//	 */
//	public void toServerOrPlat(int moldId, int online, int channelId, List<CommonPb.Award> awards, String... content) {
//		Iterator<Player> it = playerDataManager.getPlayers().values().iterator();
//		if (awards == null || awards.size() == 0) {
//			while (it.hasNext()) {
//				Player player = (Player) it.next();
//				if (player != null && player.account != null && player.isActive()) {
//					if (channelId == 0 || player.account.getPlatNo() == channelId) {
//						if (online == 0) {// 全体成员
//							playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), content);
//						} else if (online == 1 && player.ctx != null) {// 在线玩家
//							playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), content);
//						}
//					}
//				}
//			}
//		} else {
//			while (it.hasNext()) {
//				Player player = (Player) it.next();
//				if (player != null && player.account != null && player.isActive()) {
//					if (channelId == 0 || player.account.getPlatNo() == channelId) {
//						if (online == 0) {// 全体成员
//							playerDataManager.sendAttachMail(player, awards, moldId, TimeHelper.getCurrentSecond(), content);
//						} else if (online == 1 && player.ctx != null) {// 在线玩家
//							playerDataManager.sendAttachMail(player, awards, moldId, TimeHelper.getCurrentSecond(), content);
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public void gmPlatMail(Mail mail, String to) {
//		String content = null;
//		if (mail.hasContont()) {
//			content = mail.getContont();
//		}
//
//		int moldId = MailType.MOLD_SYSTEM_PUB_1;
//		if (mail.hasMoldId()) {
//			moldId = mail.getMoldId();
//		}
//
//		if (moldId == MailType.MOLD_SYSTEM_3 || moldId == MailType.MOLD_SYSTEM_2 || moldId == MailType.MOLD_SYSTEM_1 || moldId == MailType.MOLD_SYSTEM_PUB_2) {
//			if ("all".equalsIgnoreCase(to)) {
//				Iterator<Player> it = playerDataManager.getPlayers().values().iterator();
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player != null && player.isActive()) {
//						playerDataManager.sendAttachMail(player, mail.getAwardList(), moldId, TimeHelper.getCurrentSecond(), content);
//					}
//				}
//			} else if ("online".equalsIgnoreCase(to)) {
//				Iterator<Player> it = playerDataManager.getAllOnlinePlayer().values().iterator();
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player != null && player.isActive()) {
//						playerDataManager.sendAttachMail(player, mail.getAwardList(), moldId, TimeHelper.getCurrentSecond(), content);
//					}
//				}
//			} else {
//				String[] plats = to.split("\\|");
//				if (plats == null) {
//					System.out.println("gmPlatMail to null");
//					return;
//				}
//
//				Set<Integer> set = new HashSet<Integer>();
//				for (String plat : plats) {
//					set.add(Integer.valueOf(plat));
//				}
//
//				// System.out.println("gmPlatMail to " + plat);
//
//				Iterator<Player> it = playerDataManager.getPlayers().values().iterator();
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player != null && player.isActive()) {
//						if (player.account != null && set.contains(player.account.getPlatNo())) {
//							playerDataManager.sendAttachMail(player, mail.getAwardList(), moldId, TimeHelper.getCurrentSecond(), content);
//						}
//					}
//				}
//			}
//		} else if (moldId == MailType.MOLD_SYSTEM_PUB_1) {
//			if ("all".equalsIgnoreCase(to)) {
//				Iterator<Player> it = playerDataManager.getPlayers().values().iterator();
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player != null && player.isActive()) {
//						playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), content);
//					}
//				}
//			} else if ("online".equalsIgnoreCase(to)) {
//				Iterator<Player> it = playerDataManager.getAllOnlinePlayer().values().iterator();
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player != null && player.isActive()) {
//						playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), content);
//					}
//				}
//			} else {
//				String[] plats = to.split("\\|");
//				if (plats == null) {
//					System.out.println("gmPlatMail to null");
//					return;
//				}
//
//				Set<Integer> set = new HashSet<Integer>();
//				for (String plat : plats) {
//					set.add(Integer.valueOf(plat));
//				}
//
//				// System.out.println("gmPlatMail to " + plat);
//
//				Iterator<Player> it = playerDataManager.getPlayers().values().iterator();
//				while (it.hasNext()) {
//					Player player = (Player) it.next();
//					if (player != null && player.isActive()) {
//						if (player.account != null && set.contains(player.account.getPlatNo())) {
//							playerDataManager.sendNormalMail(player, moldId, TimeHelper.getCurrentSecond(), content);
//						}
//					}
//				}
//			}
//		}
//	}
//
//	private void gmBuild(Player player, int buildingId, int lv, ClientHandler handler) {
//		buildingService.setBuildingLv(buildingId, lv, player, handler);
//	}
//
//	private void gmSystem(String str) {
//		if ("resource".equalsIgnoreCase(str)) {
//			buildingService.recalcResourceOut();
//		}
//	}
//
//	public void recalcResource() {
//		buildingService.recalcResourceOut();
//	}
//
//	public void gmKick(String name) {
//		Player player = playerDataManager.getPlayer(name);
//		if (player != null && player.isLogin && player.account.getIsGm() == 0) {
//			if (player.ctx != null) {
//				player.ctx.close();
//			}
//		}
//	}
//
//	public void gmSilence(String name, int s) {
//		Player player = playerDataManager.getPlayer(name);
//		if (player != null && player.account.getIsGm() == 0) {
//			player.lord.setSilence(s);
//		}
//	}
//
//	public void gmForbidden(String name, int s) {
//		Player player = playerDataManager.getPlayer(name);
//		if (player != null && player.account.getIsGm() == 0) {
//			player.account.setForbid(s);
//		}
//	}
//
//	public void gmVip(String name, int s) {
//		Player player = playerDataManager.getPlayer(name);
//		if (player != null && (s >= 0 && s <= 12)) {
//			player.lord.setVip(s);
//			if(s>0) {
//				chatService.sendWorldChat(chatService.createSysChat(SysChatId.BECOME_VIP, player.lord.getNick(),""+s));
//			}
//		}
//	}
//
//	private void gmTopup(String name, int s) {
//		Player player = playerDataManager.getPlayer(name);
//		if (player != null && (s >= 0)) {
//			player.lord.setTopup(s);
//		}
//	}
//
//	private void gmClear(Player player, String str) {
//		if ("prop".equalsIgnoreCase(str)) {
//			player.props.clear();
//		} else if ("equip".equalsIgnoreCase(str)) {
//			player.equips.put(0, new HashMap<Integer, Equip>());
//			player.equips.put(1, new HashMap<Integer, Equip>());
//			player.equips.put(2, new HashMap<Integer, Equip>());
//			player.equips.put(3, new HashMap<Integer, Equip>());
//			player.equips.put(4, new HashMap<Integer, Equip>());
//			player.equips.put(5, new HashMap<Integer, Equip>());
//			player.equips.put(6, new HashMap<Integer, Equip>());
//		} else if ("part".equalsIgnoreCase(str)) {
//			player.parts.put(0, new HashMap<Integer, Part>());
//			player.parts.put(1, new HashMap<Integer, Part>());
//			player.parts.put(2, new HashMap<Integer, Part>());
//			player.parts.put(3, new HashMap<Integer, Part>());
//			player.parts.put(4, new HashMap<Integer, Part>());
//		} else if ("hero".equalsIgnoreCase(str)) {
//			player.heros.clear();
//		} else if ("chip".equalsIgnoreCase(str)) {
//			player.chips.clear();
//		} else if ("skill".equalsIgnoreCase(str)) {
//			player.skills.clear();
//		} else if ("science".equalsIgnoreCase(str)) {
//			player.sciences.clear();
//		} else if ("tank".equalsIgnoreCase(str)) {
//			player.tanks.clear();
//		} else if ("mail".equalsIgnoreCase(str)) {
//			player.mails.clear();
//		} else if ("explore".equalsIgnoreCase(str)) {
//			player.lord.setExtrEplr(0);
//		}else if("military".equalsIgnoreCase(str)) {
//			player.militarySciences.clear();
//			player.militaryScienceGrids.clear();
//		}
//	}
//
//	private void gmClearPlayer(String name, String str) {
//		Player player = playerDataManager.getPlayer(name);
//		if (player != null) {
//			gmClear(player, str);
//		}
//	}
//}
