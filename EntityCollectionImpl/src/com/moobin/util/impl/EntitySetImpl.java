package com.moobin.util.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.moobin.meta.EntityMeta;
import com.moobin.util.EntitySet;
import com.moobin.util.ModifyibleEntitySet;
import com.moobin.util.SortedEntitySet;

public class EntitySetImpl<K extends Comparable<K>, V> implements ModifyibleEntitySet<K, V> {

	protected final Map<K, V> map = new HashMap<>();
	private final EntityMeta<V, K> entityDef;
	
	private final Map<Predicate<V>, EntitySubSet<K, V>> subSets = new HashMap<>();
	private final List<Listener<V>> listeners = new ArrayList<>();
	
	private final Object mutex = new Object();
	
	public EntitySetImpl(EntityMeta<V, K> entityDef) {
		this.entityDef = entityDef;
	}
		
	@Override
	public EntityMeta<V, K> getEntityMety() {
		return entityDef;
	}

	@Override
	public EntitySet<K, V> filter(Predicate<V> filter) {
		EntitySubSet<K, V> subSet = subSets.get(filter);
		if (subSet == null) {
			synchronized (mutex) {
				subSets.put(filter, subSet = new EntitySubSet<>(this, filter));
			}
		}
		return subSet;
	}
	
	@Override
	public V getValue(K key) {
		return map.get(key);
	}

	@Override
	public V update(V value) {
		synchronized (mutex) {
			V oldValue = map.put(getKey(value), value);
			subSets.forEach((p,s) -> s.update(value));
			if (oldValue == null) {
				listeners.forEach(l -> l.onAdd(value));
			}
			else {
				listeners.forEach(l -> l.onUpdate(value, oldValue));
			}
			return oldValue;
		}
	}
	
	@Override
	public V removeByKey(K key) {
		synchronized (mutex) {
			V oldValue = map.remove(key);
			if (oldValue != null) {
				subSets.forEach((p,s) -> s.removeByKey(key));
				listeners.forEach(l -> l.onRemove(oldValue));
			}
			return oldValue;
		}
	}
	
	@Override
	public void clear() {
		synchronized (mutex) {
			map.clear();
			subSets.values().forEach(ModifyibleEntitySet::clear);
			listeners.forEach(Listener::onClear);
		}
	}
	
	@Override
	public void destroy() {
		synchronized (mutex) {
			map.clear();
			subSets.values().forEach(ModifyibleEntitySet::destroy);
			listeners.forEach(Listener::onDestroy);
		}
	}

	@Override
	public int getSize() {
		return map.size();
	}

	@Override
	public Collection<V> getValues() {
		return map.values();
	}
	
	@Override
	public Collection<V> addListener(Listener<V> listener) {
		synchronized (mutex) {
			Collection<V> values = new ArrayList<>(getValues());
			listeners.add(listener);
			return values;
		}
	}
	
	@Override
	public void removeListener(Listener<V> listener) {
		listeners.remove(listener);
	}
	
	@Override
	public SortedEntitySet<K, V> sort(Comparator<V> comparator) {
		return new SortedEntitySetImpl<>(this, comparator);
	}
	
}
