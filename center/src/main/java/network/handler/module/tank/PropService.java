package network.handler.module.tank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.domain.Player;
import com.game.util.EmojiHelper;
import com.game.util.LogHelper;
import com.game.util.PbHelper;
import com.game.util.RandomHelper;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import chat.Chat;
import config.bean.ConfMine;
import config.bean.ConfProp;
import config.bean.ConfVip;
import config.provider.ConfAwardProvider;
import config.provider.ConfEquipProvider;
import config.provider.ConfHeroProvider;
import config.provider.ConfPartProvider;
import config.provider.ConfPropProvider;
import config.provider.ConfVipProvider;
import data.bean.Army;
import data.bean.Effect;
import data.bean.Lord;
import data.bean.Prop;
import data.bean.PropQue;
import data.bean.Resource;
import define.ArmyState;
import define.AwardFrom;
import define.AwardType;
import define.BuildingId;
import define.GoldCost;
import define.MailType;
import define.PartyType;
import define.PropId;
import domain.Member;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import manager.MailManager;
import manager.PartyDataManager;
import manager.PlayerDataManager;
import manager.WorldDataManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.CommonPb.AwardPB;
import pb.GamePb.BuildPropRq;
import pb.GamePb.BuildPropRs;
import pb.GamePb.BuyPropRq;
import pb.GamePb.BuyPropRs;
import pb.GamePb.CancelQueRq;
import pb.GamePb.CancelQueRs;
import pb.GamePb.ComposeSantRq;
import pb.GamePb.ComposeSantRs;
import pb.GamePb.GetPropRs;
import pb.GamePb.SpeedQueRq;
import pb.GamePb.SpeedQueRs;
import pb.GamePb.UsePropRq;
import pb.GamePb.UsePropRs;
import protocol.s2c.ResponseCode;
import util.DateUtil;
import util.MsgHelper;

@Singleton
public class PropService implements IModuleMessageHandler {

	public static PropService getInst() {
		return BeanManager.getBean(PropService.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

	/**
	 * 
	 * Method: getProp
	 * 
	 * @Description: 客户端获取道具数据 @param handler @return void @throws
	 */
	public void getProp(ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Iterator<Prop> it = player.props.values().iterator();
		GetPropRs.Builder builder = GetPropRs.newBuilder();
		while (it.hasNext()) {
			builder.addProp(PbHelper.createPropPb(it.next()));
		}

		for (PropQue propQue : player.propQue) {
			builder.addQueue(PbHelper.createPropQuePb(propQue));
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.GetPropRs, builder.build());
	}

	/**
	 * 
	 * Method: buyProp
	 * 
	 * @Description: 玩家购买道具 @param req @param handler @return void @throws
	 */
	public void buyProp(BuyPropRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int propId = req.getPropId();
		int count = req.getCount();
		if (count <= 0 || count > 100) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		ConfProp staticProp = ConfPropProvider.getInst().getConfigById(propId);
		if (staticProp == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		if (staticProp.getCanBuy() != 1) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Lord lord = player.lord;
		int cost = staticProp.getPrice() * count;
		if (lord.getGold() < cost) {
//			handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
			return;
		}

		if (!PlayerDataManager.getInst().subGold(lord, cost, GoldCost.BUY_PROP)) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Prop prop = PlayerDataManager.getInst().addProp(player, propId, count);

		BuyPropRs.Builder builder = BuyPropRs.newBuilder();
		builder.setGold(lord.getGold());
		builder.setCount(prop.getCount());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.BuyPropRs, builder.build());
	}

	/**
	 * 
	 * Method: composeSant
	 * 
	 * @Description: 合成将神魂 @param req @param handler @return void @throws
	 */
	public void composeSant(ComposeSantRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Prop prop = player.props.get(PropId.SANT_HERO_CHIP);
		if (prop == null || prop.getCount() < 50) {
//			handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
			return;
		}

		PlayerDataManager.getInst().subProp(prop, 50);
		PlayerDataManager.getInst().addProp(player, PropId.SANT_HERO, 1);

		ComposeSantRs.Builder builder = ComposeSantRs.newBuilder();
		builder.addAward(PbHelper.createAwardPb(AwardType.PROP, PropId.SANT_HERO, 1));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.ComposeSantRs, builder.build());
	}

	/**
	 * 
	 * Method: useProp
	 * 
	 * @Description: 玩家使用道具 @param req @param handler @return void @throws
	 */
	public void useProp(UsePropRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int propId = req.getPropId();
		int count = req.getCount();
		if (count <= 0 || count > 100) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		Prop prop = player.props.get(propId);
		if (prop == null || prop.getCount() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.NO_PROP);
			return;
		}

		if (prop.getCount() < count) {
//			handler.sendErrorMsgToPlayer(GameError.PROP_NOT_ENOUGH);
			return;
		}

		ConfProp staticProp = ConfPropProvider.getInst().getConfigById(propId);
		if (staticProp == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		UsePropRs.Builder builder = UsePropRs.newBuilder();
		int type = staticProp.getEffectType();
		// 1.获得资源、物品 2.获得效果加成 3.加速 4.随机箱子 5.普通道具物品 6.特殊功能道具
		for (int i = 0; i < count; i++) {
			if (type == 1) {
				List<List<Integer>> effectValue = staticProp.getEffectValue();
				if (effectValue == null || effectValue.isEmpty()) {
//					handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
					return;
				}
				builder.addAllAward(PlayerDataManager.getInst().addAwardsBackPb(player, effectValue, AwardFrom.USE_PROP));
			} else if (type == 2) {
				List<List<Integer>> effectValue = staticProp.getEffectValue();
				if (effectValue == null || effectValue.isEmpty()) {
//					handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
					return;
				}

				for (List<Integer> one : effectValue) {
					if (one.size() != 2) {
						continue;
					}

					Effect effect = PlayerDataManager.getInst().addEffect(player, one.get(0), one.get(1));
					if (effect != null) {
						builder.addEffect(PbHelper.createEffectPb(effect));
					}
				}
			} else if (type == 3) {// 加速类道具一般在相关模块调用

			} else if (type == 4) {
				List<List<Integer>> effectValue = staticProp.getEffectValue();
				if (effectValue == null || effectValue.isEmpty()) {
//					handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
					return;
				}
				List<Integer> one = effectValue.get(0);
				if (one.size() != 1) {
//					handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
					return;
				}
				List<List<Integer>> awards = ConfAwardProvider.getInst().getAwards(one.get(0));

				List<AwardPB> pbAward = PlayerDataManager.getInst().addAwardsBackPb(player, awards, AwardFrom.USE_PROP);
				builder.addAllAward(pbAward);

				// 发送开宝箱的消息
				sendOpenBoxMsg(propId, player, pbAward);

			} else if (type == 6) {
//				if (!req.hasParam()) {
//					handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//					return;
//				}

				String param = req.getParam();

				if (propId >= PropId.HORN_1 && propId <= PropId.HORN_4) {// 喇叭
					if (player.lord.getSilence() > 0) {
//						handler.sendErrorMsgToPlayer(GameError.CHAT_SILENCE);
						return;
					}

					Chat chat = ChatService.getInst().createManChat(player, param);
					ChatService.getInst().sendHornChat(chat, propId - 59);
				} else if (propId == PropId.CHANGE_NAME) {// 身份铭牌
					if (param == null || param.isEmpty() || param.length() >= 12) {
//						handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
						return;
					}

					if (EmojiHelper.containsEmoji(param)) {
//						handler.sendErrorMsgToPlayer(GameError.INVALID_CHAR);
						return;
					}

					if (!PlayerDataManager.getInst().takeNick(param)) {
//						handler.sendErrorMsgToPlayer(GameError.SAME_NICK);
						return;
					}

					PlayerDataManager.getInst().rename(player, param);
				} else if (propId == PropId.PARTY_RENAME_CARD) {// 军团改名卡
					if (param == null || param.isEmpty() || param.length() >= 12) {
//						handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
						return;
					}

					if (EmojiHelper.containsEmoji(param)) {
//						handler.sendErrorMsgToPlayer(GameError.INVALID_CHAR);
						return;
					}

					Member member = PartyDataManager.getInst().getMemberById(playerId);
					if (member == null || member.getPartyId() == 0) {
//						handler.sendErrorMsgToPlayer(GameError.PARTY_HAD);
						return;
					}

					// 判断是否是军团长
					if (member.getJob() != PartyType.LEGATUS) {
//						handler.sendErrorMsgToPlayer(GameError.AUTHORITY_ERR);
						return;
					}

					// 判断是否名字已存在
					if (PartyDataManager.getInst().isExistPartyName(param)) {
//						handler.sendErrorMsgToPlayer(GameError.SAME_PARTY_NAME);
						return;
					}

					PartyService.getInst().rename(member.getPartyId(), param, ctx, packet, msg);
				}

				else if (propId == PropId.SCOUT) {// 矿点侦查
					Player target = PlayerDataManager.getInst().getPlayer(param);
					if (target == null || !target.isActive() || target.roleId == player.roleId) {
//						handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
						return;
					}

					Army e = null;
					int index = 0;
					for (Army army : target.armys) {
						if (army.getState() == ArmyState.COLLECT && !army.getSenior()) {
							// e = army;
							// break;
							index++;
						}
					}

					if (index != 0) {
						int which = RandomHelper.randomInSize(index);
						index = 0;
						for (Army army : target.armys) {
							if (army.getState() == ArmyState.COLLECT && !army.getSenior()) {
								if (index == which) {
									e = army;
									break;
								}
								index++;
							}
						}
					}

					if (e != null) {
						ConfMine staticMine = WorldDataManager.getInst().evaluatePos(e.getTarget());
						MailManager.getInst().sendNormalMail(player, MailType.MOLD_SCOUT_SUCCESS, DateUtil.getSecondTime(), target.lord.getNick(), String.valueOf(staticMine.getLv()), String.valueOf(staticMine.getType()), String.valueOf(e.getTarget()));
					} else {
						MailManager.getInst().sendNormalMail(player, MailType.MOLD_SCOUT_FAIL, DateUtil.getSecondTime(), target.lord.getNick());
					}

				} else if (propId == PropId.INDICATOR) {// 定位仪
					Player target = PlayerDataManager.getInst().getPlayer(param);
					if (target == null || !target.isActive() || target.roleId == player.roleId) {
//						handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
						return;
					}

					MailManager.getInst().sendNormalMail(player, MailType.MOLD_SCOUT, DateUtil.getSecondTime(), target.lord.getNick(), String.valueOf(target.lord.getPos()));
				}
			}
		}

		if (propId >= PropId.RED_PACKET_1 && propId <= PropId.RED_PACKET_4) {// 红包
//			if (!req.hasParam()) {
////				handler.sendErrorMsgToPlayer(GameError.PARAM_ERROR);
//				return;
//			}

			String param = req.getParam();
			String[] nicks = param.split("&");
			if (count % nicks.length != 0) {
//				handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
				return;
			}

			int everyGet = count / nicks.length;

			List<Player> targets = new ArrayList<>();
			for (String nick : nicks) {
				Player target = PlayerDataManager.getInst().getPlayer(nick);
				if (target == null || !target.isActive() || target.roleId == player.roleId) {
//					handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
					return;
				}
				targets.add(target);
			}

			List<AwardPB> awards = new ArrayList<>();
			awards.add(PbHelper.createAwardPb(AwardType.RED_PACKET, propId, everyGet));

			int now = DateUtil.getSecondTime();
			for (Player target : targets) {
				MailManager.getInst().sendAttachMail(target, awards, MailType.MOLD_RED_PACKET, now, player.lord.getNick());
			}
		}

		PlayerDataManager.getInst().subProp(prop, count);

		LogHelper.logProp(player.lord, propId, count);
		builder.setCount(prop.getCount());
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.UsePropRs, builder.build());
	}

	/**
	 * Method: sendOpenBoxMsg @Description: TODO @param propId @param player @param
	 * pbAward @return void @throws
	 */
	public void sendOpenBoxMsg(int propId, Player player, List<AwardPB> pbAward) {
		// 判断是否符合发送消息条件
//		Map<Integer, List<EndConditionItem>> map = staticActionMsgDataMgr.getMsgMap(ActionMsgConst.OPEN_BOX);
//		List<EndConditionItem> list = map.get(propId);
//		if (list != null) {
//			for (AwardPB award : pbAward) {
//				for (EndConditionItem item : list) {
//					boolean flag = true;
//					if (item.getItemType() != 0) {
//						flag = (item.getItemType() == award.getType());
//					}
//					if (flag && item.getItemId() != 0) {
//						flag = (item.getItemId() == award.getId());
//					}
//					if (flag && item.getQuality() != 0) {
//						// 判断是否正确的品质
//						flag = isCorrectQuality(award.getType(), award.getId(), item.getQuality());
//					}
//					if (flag && item.getStar() != 0) {
//						flag = isCorrectStar(award.getType(), award.getId(), item.getStar());
//					}
//					if (flag) {
//						// 发送消息
//						ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(item.getChatId(), player.lord.getNick(), AwardType.PROP + ":" + propId, award.getType() + ":" + award.getId()));
//					}
//				}
//			}
//		}
	}

	/**
	 * Method: sendOpenBoxMsg @Description: TODO @param propId @param player @param
	 * pbAward @return void @throws
	 */
	public void sendJoinActivityMsg(int acitivityId, Player player, List<AwardPB> pbAward) {
//		// 判断是否符合发送消息条件
//		Map<Integer, List<EndConditionItem>> map = staticActionMsgDataMgr.getMsgMap(ActionMsgConst.JOIN_ACTION);
//		List<EndConditionItem> list = map.get(acitivityId);
//		if (list != null) {
//			for (AwardPB award : pbAward) {
//				for (EndConditionItem item : list) {
//					boolean flag = true;
//					if (item.getItemType() != 0) {
//						flag = (item.getItemType() == award.getType());
//					}
//					if (flag && item.getItemId() != 0) {
//						flag = (item.getItemId() == award.getId());
//					}
//					if (flag && item.getQuality() != 0) {
//						// 判断是否正确的品质
//						flag = isCorrectQuality(award.getType(), award.getId(), item.getQuality());
//					}
//					if (flag && item.getStar() != 0) {
//						flag = isCorrectStar(award.getType(), award.getId(), item.getStar());
//					}
//					if (flag) {
//						// 发送消息
//						ChatService.getInst().sendWorldChat(ChatService.getInst().createSysChat(item.getChatId(), player.lord.getNick(), award.getType() + ":" + award.getId(), "" + acitivityId));
//					}
//				}
//			}
//		}
	}

	/**
	 * Method: isCorrectStar @Description: TODO @param type @param id @param
	 * star @return @return boolean @throws
	 */
	private boolean isCorrectStar(int type, int id, int star) {
		if (type == AwardType.HERO) {
			return ConfHeroProvider.getInst().getConfigById(id).getStar() == star;
		}
		return false;
	}

	/**
	 * @param quality Method: isCorrectQuality @Description: TODO @param type @param
	 *                id @return @return boolean @throws
	 */
	private boolean isCorrectQuality(int type, int id, int quality) {
		if (type == AwardType.PART || type == AwardType.CHIP) {
			return ConfPartProvider.getInst().getConfigById(id).getQuality() == quality;
		} else if (type == AwardType.EQUIP) {
			return ConfEquipProvider.getInst().getConfigById(id).getQuality() == quality;
		}
		return false;
	}

	private int getPropQueWaitCount(Lord lord) {
		ConfVip staticVip = ConfVipProvider.getInst().getConfigById(lord.getVip());
		if (staticVip != null) {
			return staticVip.getWaitQue();
		}
		return 0;
	}

	private PropQue createQue(Player player, int propId, int count, int period, int endTime) {
		PropQue propQue = new PropQue(player.maxKey(), propId, count, 1, period, endTime);
		return propQue;
	}

	private PropQue createWaitQue(Player player, int propId, int count, int period, int endTime) {
		PropQue propQue = new PropQue(player.maxKey(), propId, count, 0, period, endTime);
		return propQue;
	}

	/**
	 * 
	 * Method: buildProp
	 * 
	 * @Description: 制造车间生产道具 @param req @param handler @return void @throws
	 */
	public void buildProp(BuildPropRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int propId = req.getPropId();
		int count = req.getCount();
		if (count <= 0 || count > 100) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		int buildingLv = PlayerDataManager.getBuildingLv(BuildingId.WORKSHOP, player.building);
		if (buildingLv < 1) {
//			handler.sendErrorMsgToPlayer(GameError.BUILD_LEVEL);
			return;
		}

		ConfProp staticProp = ConfPropProvider.getInst().getConfigById(propId);
		if (staticProp == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_CONFIG);
			return;
		}

		if (staticProp.getCanBuild() != 1) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		int stoneCost = staticProp.getStoneCost() * count;
		int skillBook = staticProp.getSkillBook() * count;
		int heroChip = staticProp.getHeroChip() * count;

		Resource resource = player.resource;
		Prop bookProp = null;
		Prop chipProp = null;
		BuildPropRs.Builder builder = BuildPropRs.newBuilder();
		if (stoneCost > 0) {
			if (resource.getStone() < stoneCost) {
//				handler.sendErrorMsgToPlayer(GameError.RESOURCE_NOT_ENOUGH);
				return;
			}
		}

		if (skillBook > 0) {
			bookProp = player.props.get(PropId.SKILL_BOOK);
			if (bookProp == null || bookProp.getCount() < skillBook) {
//				handler.sendErrorMsgToPlayer(GameError.BOOK_NOT_ENOUGH);
				return;
			}
		}

		if (heroChip > 0) {
			chipProp = player.props.get(PropId.HERO_CHIP);
			if (chipProp == null || chipProp.getCount() < heroChip) {
//				handler.sendErrorMsgToPlayer(GameError.HERO_CHIP_NOT_ENOUGH);
				return;
			}
		}

		List<PropQue> propQue = player.propQue;
		int queSize = propQue.size();
		PropQue que = null;
		int now = DateUtil.getSecondTime();
		int haust = staticProp.getBuildTime() * count;
		if (queSize == 0) {
			que = createQue(player, propId, count, haust, now + haust);
			propQue.add(que);
		} else {
			if (queSize > 0 && queSize < getPropQueWaitCount(player.lord) + 1) {
				que = createWaitQue(player, propId, count, haust, now + haust);
				propQue.add(que);
			} else {
//				handler.sendErrorMsgToPlayer(GameError.MAX_PROP_QUE);
				return;
			}
		}

		if (stoneCost > 0) {
			PlayerDataManager.getInst().modifyStone(resource, -stoneCost);
			builder.setStone(resource.getStone());
		}

		if (skillBook > 0) {
			PlayerDataManager.getInst().subProp(bookProp, skillBook);
			builder.setSkillBook(bookProp.getCount());
		}

		if (heroChip > 0) {
			PlayerDataManager.getInst().subProp(chipProp, heroChip);
			builder.setHeroChip(chipProp.getCount());
		}

		builder.setQueue(PbHelper.createPropQuePb(que));
		MsgHelper.sendResponse(ctx, playerId, ResponseCode.BuildPropRs, builder.build());
	}

	private void dealPropQue(Map<Integer, Prop> props, PropQue propQue) {
		Prop prop = props.get(propQue.getPropId());
		if (prop == null) {
			prop = new Prop(propQue.getPropId(), propQue.getCount());
			prop.setPropId(propQue.getPropId());
			prop.setCount(propQue.getCount());
			props.put(prop.getPropId(), prop);
		} else {
			prop.setCount(prop.getCount() + propQue.getCount());
		}
	}

	private void dealPropQue(Player player, List<PropQue> list, int now) {
		Map<Integer, Prop> props = player.props;
		Iterator<PropQue> it = list.iterator();
		int endTime = 0;
		while (it.hasNext()) {
			PropQue propQue = it.next();
			if (propQue.getState() == 1) {
				endTime = propQue.getEndTime();
				if (now >= endTime) {
					dealPropQue(props, propQue);
					it.remove();
					continue;
				}
				break;
			} else {
				if (endTime == 0) {
					endTime = now;
				}

				endTime += propQue.getPeriod();
				if (now >= endTime) {
					dealPropQue(props, propQue);
					it.remove();
					continue;
				}

				propQue.setState(1);
				propQue.setEndTime(endTime);
				break;
			}
		}
	}

	/**
	 * 
	 * Method: speedPropQue
	 * 
	 * @Description: 加速道具生产 @param req @param handler @return void @throws
	 */
	public void speedPropQue(SpeedQueRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int keyId = req.getKeyId();
		int cost = req.getCost();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		List<PropQue> list = player.propQue;
		PropQue que = null;
		for (PropQue e : list) {
			if (e.getKeyId() == keyId) {
				que = e;
				break;
			}
		}

		if (que == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EXIST_QUE);
			return;
		}

		if (que.getState() == 0) {
//			handler.sendErrorMsgToPlayer(GameError.SPEED_WAIT_QUE);
			return;
		}

		int now = DateUtil.getSecondTime();
		int leftTime = que.getEndTime() - now;
		if (leftTime <= 0) {
			leftTime = 1;
		}

		if (cost == 1) {// 金币
			int sub = (int) Math.ceil(leftTime / 60.0);
			Lord lord = player.lord;
			if (lord.getGold() < sub) {
//				handler.sendErrorMsgToPlayer(GameError.GOLD_NOT_ENOUGH);
				return;
			}
			PlayerDataManager.getInst().subGold(lord, sub, GoldCost.SPEED_BUILD);
			que.setEndTime(now);

			dealPropQue(player, list, now);

			SpeedQueRs.Builder builder = SpeedQueRs.newBuilder();
			builder.setGold(lord.getGold());
			MsgHelper.sendResponse(ctx, playerId, ResponseCode.SpeedQueRs, builder.build());
			return;
		} else {// 道具
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
//			return;
		}
	}

	/**
	 * 
	 * Method: cancelPropQue
	 * 
	 * @Description: 取消道具生产 @param req @param handler @return void @throws
	 */
	public void cancelPropQue(CancelQueRq req, ChannelHandlerContext ctx, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> msg) {
		long playerId = packet.getPlayerId();
		int keyId = req.getKeyId();
		Player player = PlayerDataManager.getInst().getPlayer(playerId);
		List<PropQue> list = player.propQue;
		PropQue que = null;

		for (PropQue e : list) {
			if (e.getKeyId() == keyId) {
				que = e;
				break;
			}
		}

		if (que == null) {
//			handler.sendErrorMsgToPlayer(GameError.NO_EXIST_QUE);
			return;
		}

		int propId = que.getPropId();
		int count = que.getCount();
		ConfProp staticProp = ConfPropProvider.getInst().getConfigById(propId);
		if (staticProp == null) {
//			handler.sendErrorMsgToPlayer(GameError.INVALID_PARAM);
			return;
		}

		list.remove(que);

		int stoneCost = staticProp.getStoneCost() * count / 2;
		int bookCount = staticProp.getSkillBook() * count / 2;
		int heroChip = staticProp.getHeroChip() * count / 2;

		if (bookCount > 0) {
			PlayerDataManager.getInst().addProp(player, PropId.SKILL_BOOK, bookCount);
		}

		if (heroChip > 0) {
			PlayerDataManager.getInst().addProp(player, PropId.HERO_CHIP, heroChip);
		}

		Resource resource = player.resource;
		CancelQueRs.Builder builder = CancelQueRs.newBuilder();
		if (stoneCost > 0) {
			PlayerDataManager.getInst().modifyStone(resource, stoneCost);
			builder.setStone(resource.getStone());
		}

		MsgHelper.sendResponse(ctx, playerId, ResponseCode.CancelQueRs, builder.build());
	}

	/**
	 * 
	 * Method: propQueTimerLogic
	 * 
	 * @Description: 制造车间制造道具队列定时器逻辑 @return void @throws
	 */
	public void propQueTimerLogic() {
		Iterator<Player> iterator = PlayerDataManager.getInst().getPlayers().values().iterator();
		int now = DateUtil.getSecondTime();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			if (!player.isActive()) {
				continue;
			}

			if (!player.propQue.isEmpty()) {
				dealPropQue(player, player.propQue, now);
			}
		}
	}
}
