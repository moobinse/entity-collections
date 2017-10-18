package com.moobin.util.impl;

import java.util.function.Predicate;

class EntitySubSet<K extends Comparable<K>, V> extends EntitySetImpl<K, V> {

	private final Predicate<V> filter;

	public EntitySubSet(EntitySetImpl<K, V> source, Predicate<V> filter) {
		super(source.getEntityMety());
		this.filter = filter;
		source.map.forEach((k, v) -> { if (filter.test(v)) map.put(k, v); });
	}
	
	@Override
	public V update(V value) {
		if (!filter.test(value)) {
			return removeByKey(getKey(value));
		}
		return super.update(value);
	}
	
}