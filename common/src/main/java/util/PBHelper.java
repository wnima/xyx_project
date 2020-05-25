package util;

import pb.MxwPb.PropPB;
import pb.MxwPb.RankPB;
import pb.MxwPb.SectionPB;
import pb.MxwPb.ShopPB;
import pb.MxwPb.SignPB;

public class PBHelper {

	public static SectionPB createSectionPB(int id, boolean pass, int starLv) {
		SectionPB.Builder pb = SectionPB.newBuilder();
		pb.setId(id);
		pb.setPass(pass);
		pb.setStarLv(starLv);
		return pb.build();
	}

	public static RankPB crateRankPB(int rankId, String name, String portrait, int chapterId, int sectionId, int rankValue) {
		RankPB.Builder builder = RankPB.newBuilder();
		builder.setRankId(rankId);
		builder.setName(name);
		builder.setPortrait(portrait);
		builder.setProcess(chapterId + "-" + sectionId);
		builder.setRankValue(rankValue);
		return builder.build();
	}

	public static PropPB cratePropPB(int propId, int propNum) {
		PropPB.Builder builder = PropPB.newBuilder();
		builder.setItemId(propId);
		builder.setItemNum(propNum);
		return builder.build();
	}

	public static ShopPB crateShopPB(int shopId, int itemId, int itemNum, int gold, int coin, int state) {
		ShopPB.Builder builder = ShopPB.newBuilder();
		builder.setShopId(shopId);
		builder.setItemId(itemId);
		builder.setItemNum(itemNum);
		builder.setGold(gold);
		builder.setCoin(coin);
		builder.setState(state);
		return builder.build();
	}

	public static SignPB crateSignPB(int date, int state, int itemId, int itemNum) {
		SignPB.Builder builder = SignPB.newBuilder();
		builder.setDate(date);
		builder.setState(state);
		builder.setItemId(itemId);
		builder.setItemNum(itemNum);
		return builder.build();
	}
}
