package com.moobin.util.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.moobin.meta.EntityMeta;
import com.moobin.util.EntitySet;
import com.moobin.util.EntitySetListener;
import com.moobin.util.ModifyibleEntitySet;

public class EntitySetImpl<K, V> implements ModifyibleEntitySet<K, V> {

	private Map<K, V> map;
	private EntityMeta<V, K> entityDef;
	
	private Map<Predicate<V>, ModifyibleEntitySet<K, V>> subSets = new HashMap<>();
	private List<EntitySetListener<V>> listeners = new ArrayList<>();
	
	public EntitySetImpl(EntityMeta<V, K> entityDef) {
		map = new HashMap<>();
		this.entityDef = entityDef;
	}
	
	@Override
	public EntityMeta<V, K> getEntity() {
		return entityDef;
	}

	@Override
	public EntitySet<K, V> filter(Predicate<V> filter) {
		ModifyibleEntitySet<K, V> subSet = subSets.get(filter);
		if (subSet == null) {
			subSet = new EntitySetImpl<K, V>(entityDef) {
				@Override
				public V update(V value) {
					if (!filter.test(value)) {
						return removeByKey(getKey(value));
					}
					return super.update(value);
				}
			};
			getValues().forEach(subSet::update);
			subSets.put(filter, subSet);
		}
		return subSet;
	}
	
	@Override
	public V getValue(K key) {
		return map.get(key);
	}

	@Override
	public V update(V value) {
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
	
	@Override
	public V removeByKey(K key) {
		V oldValue = map.remove(key);
		if (oldValue != null) {
			subSets.forEach((p,s) -> s.removeByKey(key));
			listeners.forEach(l -> l.onRemove(oldValue));
		}
		return oldValue;
	}
	
	@Override
	public void clear() {
		map.clear();
		subSets.values().forEach(ModifyibleEntitySet::clear);
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
	public void addListener(EntitySetListener<V> listener) {
		listeners.add(listener);
	}
	
}
