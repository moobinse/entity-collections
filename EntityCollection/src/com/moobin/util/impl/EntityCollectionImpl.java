package com.moobin.util.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import com.moobin.util.EntityCollection;
import com.moobin.util.EntityCollectionListener;

public class EntityCollectionImpl<K, V> implements EntityCollection<K, V> {

	private Map<K, V> map;
	Class<K> keyType;
	Class<V> valueType;
	Function<V, K> keyFunction;
	
	private List<SubCollection<K, V>> subCollections = new ArrayList<>();
	private List<EntityCollectionListener<V>> listeners = new ArrayList<>();
	
	public EntityCollectionImpl(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {
		this.keyFunction = keyFunction;
		this.valueType = valueType;
		this.keyType = keyType;
		map = new HashMap<>();
	}

	@Override
	public EntityCollection<K, V> filter(Predicate<V> filter) {
		for (SubCollection<K, V> subCollection : subCollections) {
			if (filter.equals(subCollection.getFilter())) {
				return subCollection;
			}
		}
		SubCollection<K, V> subCollection = new SubCollection<K, V>(this, filter);
		subCollections.add(subCollection);
		return subCollection;
	}
	
	@Override
	public Class<K> getKeyType() {
		return keyType;
	}
	
	@Override
	public Function<V, K> getKeyFunction() {
		return keyFunction;
	}

	@Override
	public Class<V> getValueType() {
		return valueType;
	}

	@Override
	public V getValue(K key) {
		return map.get(key);
	}

	@Override
	public K getKey(V value) {
		return keyFunction.apply(value);
	}

	@Override
	public V update(V value) {
		V oldValue = map.put(getKey(value), value);
		subCollections.forEach(s -> s.update(value));
		if (oldValue == null) {
			listeners.forEach(l -> l.onAdd(value));
		}
		else {
			listeners.forEach(l -> l.onUpdate(value, oldValue));
		}
		return oldValue;
	}
	
	@Override
	public V removeByKey(K key) {
		V oldValue = map.remove(key);
		if (oldValue != null) {
			subCollections.forEach(s -> s.removeByKey(key));
			listeners .forEach(l -> l.onRemove(oldValue));
		}
		return oldValue;
	}
	
	@Override
	public void clear() {
		map.clear();
		subCollections.forEach(EntityCollection::clear);
	}

	@Override
	public int getSize() {
		return map.size();
	}

	@Override
	public Collection<V> getValues() {
		return map.values();
	}
	
}
