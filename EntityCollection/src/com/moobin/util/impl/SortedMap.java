package com.moobin.util.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.moobin.util.impl.IndexedEntitySetImpl.Entry;

public class SortedMap<K extends Comparable<K>, V, S> extends IndexedEntitySetImpl<K, V> {

	private Function<V, S> sortProperty;

	public SortedMap(Function<V, K> keyProperty, Function<V, S> sortProperty) {
		super(keyProperty);
		this.sortProperty = sortProperty;
	}
	
	@Override
	public int indexOf(K key) {
		return 0;
	}

	@Override
	public V update(V value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V remove(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	class SortEntry extends Entry {
		public SortEntry(K key, V value) {
			super(key, value);
			// TODO Auto-generated constructor stub
		}

		Entry left;
		Entry right;
		V value;
		K key;
		S sortValue;
		int size;
		
		void set(V value) {
			this.value = value;
			key = getKey(value);
			sortValue = sortProperty.apply(value);
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
		
	}
	
}
