package com.moobin.util.impl;

import java.util.function.Predicate;

import com.moobin.util.EntitySet;

class EntitySubSet<K, V> extends EntitySetImpl<K, V> {

	/**
	 * 
	 */
	private final EntitySet<K, V> source;
	private Predicate<V> filter;

	EntitySubSet(EntitySet<K, V> source, Predicate<V> filter) {
		super(source.getEntity());
		this.source = source;
		this.filter = filter;
		source.getValues().forEach(this::update);
	}

	public Predicate<V> getFilter() {
		return filter;
	}
	
	@Override
	public V update(V value) {
		if (!filter.test(value)) {
			return removeByKey(getKey(value));
		}
		return super.update(value);
	}
	
}