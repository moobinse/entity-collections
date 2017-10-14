package com.moobin.util.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.moobin.meta.EntityMeta;
import com.moobin.util.EntitySet;
import com.moobin.util.EntitySetListener;
import com.moobin.util.IndexedEntitySet;

public class IndexedEntitySetImpl<K, V> implements IndexedEntitySet<K, V>, EntitySetListener<V> {

	private final EntitySet<K, V> source;
	private Map<K, Entry> map = new HashMap<>();
	private Entry root;
	private Comparator<V> comparator;

	public IndexedEntitySetImpl(EntitySet<K, V> source, Comparator<V> comparator) {
		this.source = source;
		this.comparator = comparator;
		source.addListener(this);
		root = new Entry(source.getValues());
	}

	@Override
	public EntityMeta<V, K> getEntityMety() {
		return source.getEntityMety();
	}

	@Override
	public V getValue(K key) {
		return source.getValue(key);
	}

	@Override
	public int getSize() {
		return source.getSize();
	}

	@Override
	public Collection<V> getValues() {
		return source.getValues();
	}

	@Override
	public EntitySet<K, V> filter(Predicate<V> filter) {
		return source.filter(filter);
	}

	@Override
	public V get(int index) {
		return root.get(index);
	}

	@Override
	public int indexByKey(K key) {
		Entry entry = map.get(key);
		return entry == null ? -1 : entry.getIndex();
	}

	@Override
	public void addListener(EntitySetListener<V> listener) {
		source.addListener(listener);
	}

	@Override
	public void onAdd(V value) {
		if (root == null) {
			root = new Entry(value);
			map.put(root.key, root);
		} else {
			root.add(value);
		}
		// TODO notify
	}

	@Override
	public void onUpdate(V value, V oldValue) {
		assert getKey(value).equals(getKey(oldValue));
		Entry entry = map.get(getKey(value));
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

	@Override
	public void onRemove(V value) {
		map.get(getKey(value)).remove();
	}

	@Override
	public void onClear() {
		root = null;
		map.clear();
	}

	public void dump() {
		dump(root);
		System.out.println();
	}
	
	private void dump(Entry e) {
		if (e == null) {
			System.out.print("-");
			return;
		}
		System.out.print(e.value);
		if (e.left == null && e.right == null) {
			return;
		}
		if (e.size == 1) return;
		System.out.print("(");
		dump(e.left);
		System.out.print(",");
		dump(e.right);
		System.out.print(")");
	}
	
	private class Entry {
		Entry parent;
		Entry left;
		Entry right;
		private V value;
		private K key;
		private int size = 1;

		Entry(V value) {
			this.value = value;
			this.key = getKey(value);
		}

		public Entry(List<V> list, int i, int size2) {
			// TODO Auto-generated constructor stub
		}

		public Entry(Collection<V> values) {
			List<V> list = new ArrayList<>(source.getValues());
			Collections.sort(list, comparator);
			int index = list.size() / 2;
			left = new Entry(list, 0, index);
			right = new Entry(list, index + 1, list.size());
		}
		
		public int getIndex() {
			if (parent == null) {
				return leftSize();
			}
			if (this == parent.right) {
				return parent.getIndex() + leftSize() + 1;
			}
			return parent.getIndex() - size + leftSize();
		}

		public V get(int index) {
			if (index == leftSize()) return value;
			if (index < leftSize()) return left.get(index);
			return right.get(index - leftSize() - 1);
		}
		
		private int leftSize() {
			return left == null ? 0 : left.size;
		}
		
		private void remove() {
			parent.size--;
			map.remove(key);
			
			if (parent == null) {
				// TODO: root
				return;
			}
			Entry e = null;
			if (size == 0) {
				e = null;
			}
			else if (left == null) {
				e = right;
			}
			else if (right == null) {
				e = left;
			}
			else {
				e = left;
				while (e.right != null) {
					e.size--;
					e = e.right;
				}
				setRight(e.parent, e.left);
				setRight(e, right);
			}
			if (parent.left == this) {
				setLeft(parent, e);
			}
			else {
				assert parent.right == this;
				setRight(parent, e);
			}

		}
		
		void setRight(Entry parent, Entry right) {
			parent.right = right;
			right.parent = parent;
		}
		
		void setLeft(Entry parent, Entry left) {
			parent.left = left;
			left.parent = parent;
		}

		@SuppressWarnings("unchecked")
		public void add(V value) {
			size++;
			int cmp = comparator.compare(this.value, value);
			if (cmp == 0) {
				cmp = ((Comparable<K>) key).compareTo(getKey(value));
			}
			assert cmp != 0;
			if (cmp > 0) {
				if (left == null) {
					left = new Entry(value);
					left.parent = this;
					map.put(left.key, left);
				} else {
					left.add(value);
				}
			} else {
				if (right == null) {
					right = new Entry(value);
					right.parent = this;
					map.put(right.key, right);
				} else {
					right.add(value);
				}
			}
		}

	}
}
