package util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
//	private static Randomizer instance = new Randomizer();
//	private SecureRandom secureRandom;
	private ThreadLocalRandom rand;

	public static Randomizer getInst() {
		return new Randomizer();
	}

	private Randomizer() {
//		secureRandom = new SecureRandom();
//		rand = new Random(secureRandom.nextLong());
		rand = ThreadLocalRandom.current(); 
	}

	public static int nextInt() {
		int ret = getInst().rand.nextInt();
		if (ret < 0) {
			ret = -ret;
		}
		return ret;
	}

	public static boolean randomOk(int seed, int ratio) {
		if (seed <= 0) {
			throw new IllegalArgumentException("the seed of random can't be zero");
		}
		return nextInt(seed) < ratio;
	}


	public static int nextInt(int min, int max) {
		if (max == min) {
			return min;
		}
		return min + nextInt(max - min);
	}

	public static int nextInt(int mod) {
		int ret = getInst().rand.nextInt(mod);
		return ret;
	}
	
	public static void main(String[] args) {
		System.out.println(nextInt(1));
	}

	public static double nextDouble() {
		return getInst().rand.nextDouble();
	}

	public static Random getRandom() {
		return getInst().rand;
	}

	public static double nextGaussian() {
		return getInst().rand.nextGaussian();
	}
	
	public static long nextLong(long min, long max) {
		if (max == min) {
			return min;
		}
		return min + nextLong(max - min);
	}

	public static long nextLong(long mod) {
		long ret = getInst().rand.nextLong(mod);
		return ret;
	}
}
