package util;

public class PlatformDependent {

	public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("win");
}
