package define;

public interface EffectType {
	// 增加5种资源50%的基础产量
	final int ALL_PRODUCT = 1;

	// 增加宝石50%的基础产量
	final int STONE_PRODUCT = 2;

	// 增加铁50%的基础产量
	final int IRON_PRODUCT = 3;

	// 增加石油50%的基础产量
	final int OIL_PRODUCT = 4;

	// 增加铜50%的基础产量
	final int COPPER_PRODUCT = 5;

	// 增加硅50%的基础产量
	final int SILICON_PRODUCT = 6;

	// 增加己方部队20%伤害
	final int ADD_HURT = 7;

	// 降低敌方部队20%伤害
	final int REDUCE_HURT = 8;

	// 部队在世界地图行军速度提升100%
	final int MARCH_SPEED = 9;

	// 保护基地免受攻击/侦查.攻击他人后状态取消
	final int ATTACK_FREE = 10;

	// 使用改变基地外观.命中+15%.闪避暴击抗暴+5%
	final int CHANGE_SURFACE_1 = 11;

	// 使用改变基地外观.5种资源基础产量+25%
	final int CHANGE_SURFACE_2 = 12;

	// 使用改变基地外观.增加1个建筑位
	final int CHANGE_SURFACE_3 = 13;

	// 使用获得一个暗黑风格的基地外观(有效时间7天)
	final int CHANGE_SURFACE_4 = 14;

	// 使用获得一个荒漠风格的基地外观(有效时间7天)
	final int CHANGE_SURFACE_5 = 15;

	// 使用获得一个茅草屋基地外观(有效时间7天)
	final int CHANGE_SURFACE_6 = 16;

	// 使用获得一个宝石基地外观(有效时间7天)
	final int CHANGE_SURFACE_7 = 17;

	// 增加5种资源20%的基础产量
	final int WAR_CHAMPION = 18;

	// 增加己方部队30%伤害
	final int ADD_HURT_SUPUR = 19;

	// 降低敌方部队30%伤害
	final int REDUCE_HURT_SUPER = 20;

	// 部队在世界地图行军速度提升150%
	final int MARCH_SPEED_SUPER = 21;
}
