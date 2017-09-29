package com.moobin.util.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.moobin.util.Entity;
import com.moobin.util.EntitySet;
import com.moobin.util.EntitySetListener;

public class EntitySetImpl<K, V> implements EntitySet<K, V> {

	private Map<K, V> map;
	private Entity<V, K> entityDef;
	
	private List<EntitySubSet<K, V>> subCollections = new ArrayList<>();
	private List<EntitySetListener<V>> listeners = new ArrayList<>();
	
	public EntitySetImpl(Entity<V, K> entityDef) {
		map = new HashMap<>();
		this.entityDef = entityDef;
	}
	
	@Override
	public Entity<V, K> getEntity() {
		return entityDef;
	}

	@Override
	public EntitySet<K, V> filter(Predicate<V> filter) {
		for (EntitySubSet<K, V> subCollection : subCollections) {
			if (filter.equals(subCollection.getFilter())) {
				return subCollection;
			}
		}
		EntitySubSet<K, V> subCollection = new EntitySubSet<K, V>(this, filter);
		subCollections.add(subCollection);
		return subCollection;
	}
	
	@Override
	public V getValue(K key) {
		return map.get(key);
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
		subCollections.forEach(EntitySet::clear);
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
