/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Magnus Lenti (moobin@moobin.org)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.moobin.entityset.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.moobin.entityset.EntitySet;
import org.moobin.entityset.EntitySetListener;
import org.moobin.entityset.EntitySetState;
import org.moobin.entityset.SortedEntitySet;
import org.moobin.meta.EntityDescription;

/**
 * 
 * @author Magnus Lenti
 *
 * @param <K>
 * @param <V>
 */
public class EntitySetImpl<K extends Comparable<K>, V> implements ModifyibleEntitySet<K, V> {

	protected final Map<K, V> map = new HashMap<>();
	private final EntityDescription<V, K> entityDef;
	
	private final Map<Predicate<V>, EntitySubSet<K, V>> subSets = new HashMap<>();
	private final List<EntitySetListener<V>> listeners = new ArrayList<>();
	
	private final Object mutex = new Object();
	private EntitySetState state = EntitySetState.INIT;
	
	/**
	 * 
	 * @param entityDef
	 */
	public EntitySetImpl(EntityDescription<V, K> entityDef) {
		this.entityDef = entityDef;
	}
		
    /**
     * {@inheritDoc} 
     */
	@Override
	public EntityDescription<V, K> getEntityMeta() {
		return entityDef;
	}

    /**
     * {@inheritDoc} 
     */
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
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public V getValue(K key) {
		return map.get(key);
	}

    /**
     * {@inheritDoc} 
     */
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
	
    /**
     * {@inheritDoc} 
     */
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
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public void clear() {
		synchronized (mutex) {
			map.clear();
			subSets.values().forEach(ModifyibleEntitySet::clear);
			listeners.forEach(EntitySetListener::onClear);
		}
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public void destroy() {
		synchronized (mutex) {
			map.clear();
			subSets.values().forEach(ModifyibleEntitySet::destroy);
			listeners.forEach(EntitySetListener::onDestroy);
		}
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	public int size() {
		return map.size();
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	public Collection<V> getValues() {
		return map.values();
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public Collection<V> addListener(EntitySetListener<V> listener) {
		synchronized (mutex) {
			Collection<V> values = new ArrayList<>(getValues());
			listeners.add(listener);
			return values;
		}
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public void removeListener(EntitySetListener<V> listener) {
		listeners.remove(listener);
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public SortedEntitySet<K, V> sort(Comparator<V> comparator) {
		return new SortedEntitySetImpl<>(this, comparator);
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public EntitySetState getState() {
		return state;
	}
}
