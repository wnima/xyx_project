package com.game.util;

import org.apache.commons.lang3.RandomUtils;

public class RandomHelper {
	static public boolean isHitRangeIn100(final int prob) {
		final int seed = randomInSize(100);
		boolean bool = false;
		if (seed < prob) {
			bool = true;
		}
		return bool;
	}

	static public boolean isHitRangeIn1000(final int prob) {
		final int seed = randomInSize(1000);
		boolean bool = false;
		if (seed < prob) {
			bool = true;
		}
		return bool;
	}
	
	static public boolean isHitRangeIn100000(final int prob) {
		final int seed = randomInSize(100000);
		boolean bool = false;
		if (seed < prob) {
			bool = true;
		}
		return bool;
	}

	static public int randomInSize(final int size) {
		return RandomUtils.nextInt(0, size);
	}
	
	static public long randomInSize(final long size) {
		return RandomUtils.nextLong(0, size);
	}

}
