package com.moobin.util;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Factory {

	<K, V> EntityCollection<K, V> create(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction);
	
	<K, V> EntityCollection<K, V> filter(EntityCollection<K, V> source, Predicate<V> filter);
	
}
