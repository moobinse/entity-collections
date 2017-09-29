package com.moobin.util.impl;

import java.util.function.Predicate;

import com.moobin.util.EntitySet;

class SubCollection<K, V> extends EntityCollectionImpl<K, V> {

	/**
	 * 
	 */
	private final EntitySet<K, V> source;
	private Predicate<V> filter;

	SubCollection(EntitySet<K, V> source, Predicate<V> filter) {
		super(source.getValueType(), source.getKeyType(), source.getKeyFunction());
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