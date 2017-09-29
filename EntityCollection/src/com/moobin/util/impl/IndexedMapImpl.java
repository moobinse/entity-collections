package com.moobin.util.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.moobin.util.IndexedEntitySet;

public class IndexedMapImpl<K extends Comparable<K>, V> implements IndexedEntitySet<K, V> {

	Entry root;
	private Function<V, K> keyProperty;
	Map<K, Entry> map = new HashMap<>();

	public IndexedMapImpl(Function<V, K> keyProperty) {
		this.keyProperty = keyProperty;
	}
	
	@Override
	public final V get(int index) {
		return root.get(index).value;
	}

	@Override
	public final int size() {
		return root.size;
	}

	@Override
	public final V getValue(K key) {
		Entry entry = map.get(key);
		return entry == null ? null : entry.value;
	}

	@Override
	public K getKey(V value) {
		return value == null ? null : keyProperty.apply(value);
	}

	@Override
	public int indexOf(K key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public V update(V value) {
		if (root == null) {
			root = new Entry(getKey(value), value);
			return null;
		}
		return root.add(getKey(value), value);
	}
	
	public void set(Collection<V> values) {
		List<V> list = new ArrayList<>(values);
		list.sort((a,b) -> getKey(a).compareTo(getKey(b)));
		root = new Entry(list);
	}

	@Override
	public V remove(K key) {
		V value = null;
		Entry entry = root.find(key);
		if (entry != null) {
			value = entry.value;
			entry.remove();
		}
		return value;
	}
                                                                                                                                                                                                                                                                                   
	protected class Entry {
		Entry parent;
		Entry left;
		Entry right;
		V value;
		K key;
		int size = 1;
		
		public Entry(List<V> values) {
			this(null, 0, values.size(), values);
		}
		
		public Entry(Entry parent, int from, int to, List<V> values) {
			this.parent = parent;
			size = from - to;
			int index = from + size / 2;
			value = values.get(index);
			if (index < from) {
				this.left = new Entry(this, from, index, values);
			}
			if (to > index) {
				this.right = new Entry(this, index + 1, to, values);
			}
		}
		
		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		void remove() {
			if (value != null) {
				value = null;
				resize(-1);
			}
		}
		
		int leftSize() {
			return left == null ? 0 : left.size;
		}
		
		int rightSize() {
			return right == null ? 0 : right.size;
		}
		
		Entry get(int index) {
			if (index < leftSize()) {
				return left.get(index);
			}
			if (index == leftSize()) {
				return this;
			}
			return right.get(index - leftSize() - 1);
		}
		
		Entry find(K key) {
			int comp = key.compareTo(this.key);
			if (comp == 0) return this;
			if (comp < 0) {
				return left.find(key);
			}
			return right.find(key);
		}
		
		V add(K key, V value) {
			int comp = key.compareTo(this.key);
			if (comp < 0) {
				if (left == null) {
					left = new Entry(key, value);
				    resize(1);
					return null;
				}
				return left.add(key, value);
			}
			if (comp > 0) {
				if (right == null) {
					right = new Entry(key, value);
					resize(1);
					return null;
				}
				return right.add(key, value);
			}
			// comp == 0
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}
		
		void resize(int diff) {
			size += 1;
			if (parent != null) {
				parent.resize(diff);
			}
		}

	}
	
}
