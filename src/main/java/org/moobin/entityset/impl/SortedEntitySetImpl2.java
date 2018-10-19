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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.moobin.entityset.EntitySet;
import org.moobin.entityset.EntitySetListener;
import org.moobin.entityset.EntitySetState;
import org.moobin.entityset.SortedEntitySet;
import org.moobin.entityset.TreeNode;

/**
 * 
 * @author Magnus Lenti
 *
 * @param <K>
 * @param <V>
 */
public class SortedEntitySetImpl2<K extends Comparable<K>, V> implements SortedEntitySet<K, V>, EntitySetListener<V> {

	private final EntitySet<K, V> source;
	private Map<K, Entry> map = new HashMap<>();
	private Entry root;
	private Comparator<V> comparator;
	
	private Object mutex = new Object();
	private EntitySetState state = EntitySetState.INIT;

	/**
	 * 
	 * @param source
	 * @param comparator
	 */
	public SortedEntitySetImpl2(EntitySet<K, V> source, Comparator<V> comparator) {
		this.source = source;
		init(comparator);
	}
	
	/**
	 * 
	 * @param source
	 * @param method
	 * @param comparator
	 */
	public <T> SortedEntitySetImpl2(EntitySet<K, V> source, Function<V, T> method, Comparator<T> comparator) {
		this.source = source;
		init((a, b) ->  comparator.compare(method.apply(a), method.apply(b)));
	}
	
	/**
	 * Creates a sorted set sorted on the unique key
	 * 
	 * @param source
	 */
	public SortedEntitySetImpl2(EntitySet<K, V> source) {
		this.source = source;
		init((a, b) -> getKey(a).compareTo(getKey(b)));
	}
	
	private void init(Comparator<V> comparator) {
		this.comparator = comparator;
		synchronized (mutex) {
			Collection<V> snapshot = source.addListener(this);
			root = parse(snapshot);
		}
		state = source.getState();
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public EntitySetState getState() {
		return state ;
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public void onAdd(V value) {
		synchronized (mutex) {
			if (root == null) {
				root = new Entry(value);
				map.put(root.key, root);
			} else {
				root.add(value);
			}
		}
		// TODO notify
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	public void onUpdate(V value, V oldValue) {
		synchronized (mutex) {
			assert source.getKey(value).equals(source.getKey(oldValue));
			Entry entry = map.get(source.getKey(value));
			assert oldValue == entry.value;
			if (comparator.compare(value, entry.value) == 0) {
				entry.value = value;
				// TODO notify
			} else {
				// TODO: handle as one event
				onRemove(oldValue);
				onAdd(value);
			}
		}
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	public void onRemove(V value) {
		synchronized (mutex) {
			map.get(source.getKey(value)).remove();
		}
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	public void onClear() {
		synchronized (mutex) {
			root = null;
			map.clear();
		}
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public void onDestroy() {
		throw new RuntimeException("Not implemented");
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public EntitySet<K, V> getSource() {
		return source;
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	public V get(int index) {
		synchronized (mutex) {
			if (index < 0 || index >= size()) {
				throw new IndexOutOfBoundsException();
			}
			return ((Entry) root.get(index)).value;
		}
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	public List<V> get(int from, int count) {
		List<V> list = new ArrayList<>();
		synchronized (mutex) {
			if (from < 0 || from + count > size()) {
				throw new IndexOutOfBoundsException();
			}
			Entry entry = root.get(from);
			for (int i = 0; entry != null && i < count; i++) {
				list.add(entry.value);
				entry = (Entry) entry.getNext();
			}
		}
		return list;
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	public int indexByKey(K key) {
		Entry entry = map.get(key);
		return entry == null ? -1 : entry.getIndex();
	}
	
	private Entry parse(List<V> list, int from, int to) {

		int mid = (to + from) / 2;
		Entry entry = new Entry(list.get(mid));
		entry.size = to - from + 1;
		if (mid > from) {
			entry.setLeft(parse(list, from, mid -1));
		}
		if (to > mid) {
			entry.setRight(parse(list, mid + 1, to));
		}
		map.put(entry.key, entry);
		return entry;
	}

	private Entry parse(Collection<V> values) {
		if (values.isEmpty()) {
			return null;
		}
		List<V> list = new ArrayList<>(values);
		Collections.sort(list, comparator);
		return parse(list, 0, list.size() - 1);
	}

	
	private class Entry extends TreeNodeImpl {

		private V value;
		private K key;

		Entry(V value) {
			this.value = value;
			this.key = source.getKey(value);
		}
		
		@Override
		public void setRight(TreeNode right) {
			assert right.getParent() == null;
			this.right = (Entry) right;
			if (right != null) {
				((Entry) right).parent = this;
			}
		}
		
		@Override
		public void setLeft(TreeNode left) {
			assert left.getParent() == null;
			this.left = (Entry) left;
			if (left != null) {
				((Entry) left).parent = this;
			}
		}

		void add(V value) {
			size++;
			int cmp = comparator.compare(this.value, value);
			if (cmp == 0) {
				cmp = ((Comparable<K>) key).compareTo(source.getKey(value));
			}
			assert cmp != 0;
			if (cmp > 0) {
				if (left == null) {
					Entry tNew = new Entry(value);
					tNew.parent = this;
					map.put(tNew.key, tNew);
				} else {
					((Entry) left).add(value);
				}
			} else {
				if (right == null) {
					Entry tNew = new Entry(value);
					tNew.parent = this;
					map.put(tNew.key, tNew);
				} else {
					((Entry) right).add(value);
				}
			}
		}
		
		@Override
		public Entry get(int index) {
			return (Entry) super.get(index);
		}
		
		@Override
		public String toString() {
			return "<" + value + ">";
		}

	}

}
