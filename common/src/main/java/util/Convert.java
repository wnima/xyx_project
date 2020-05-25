package util;

import java.util.function.Function;

public interface Convert<K, V> extends Function<K, V> {

	@Override
	V apply(K k);
}
