package mxw;

import java.util.Comparator;

class ComparatorStar implements Comparator<UserRank> {

	@Override
	public int compare(UserRank o1, UserRank o2) {
		int d1 = o1.getStarLv();
		int d2 = o2.getStarLv();

		// 星际由大到小
		if (d1 < d2)
			return 1;
		else if (d1 > d2) {
			return -1;
		} else {// 通关时长由短到长
			long p1 = o1.getPassTime();
			long p2 = o2.getPassTime();
			if (p1 > p2) {
				return 1;
			} else if (p1 < p2) {
				return -1;
			} else {// 通关时间由小到大
				int c1 = o1.getCreateTime();
				int c2 = o2.getCreateTime();
				if (c1 > c2) {
					return 1;
				} else if (c1 < c2) {
					return -1;
				} else {
					long u1 = o1.getUserId();
					long u2 = o2.getUserId();
					if (u1 > u2) {
						return 1;
					} else if (u1 < u2) {
						return -1;
					}
				}
			}
		}
		return 0;
	}
}
