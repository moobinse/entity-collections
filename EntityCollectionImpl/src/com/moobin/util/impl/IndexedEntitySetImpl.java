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
		if (!source.getValues().isEmpty()) {
			root = parse(source.getValues());
		}
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
		return root.get(index).value;
	}
	
	public List<V> get(int from, int count) {
		List<V> list = new ArrayList<>();
		Entry entry = root.get(from);
		for (int i = 0; i < count; i++) {
			if (entry != null) {
				list.add(entry.value);
				entry = entry.next();
			}
		}
		return list;
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
	
	@Override
	public IndexedEntitySet<K, V> sort(Comparator<V> comparator) {
		return source.sort(comparator);
	}

	public void dump() {
		if (root != null) {
			dump(root);
		}
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
	
	private Entry parse(List<V> list, int from, int to) {

		int mid = (to + from) / 2;
//		System.out.println(from + "-" + to + "  (" + mid + ")");
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

		public Entry next() {
			Entry e = right;
			if (e != null) {
				while (e.left != null) {
					e = e.left;
				}
				return e;
			}
			for (e = this; e != null && !e.isLeft(); e = e.parent);
			return e == null ? null : e.parent;
			
		}
		
		public boolean isLeft() {
			return parent != null && parent.left == this;
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

		public Entry get(int index) {
			if (index == leftSize()) return this;
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
				e.parent.setRight(e.left);
				e.setRight(right);
			}
			if (parent.left == this) {
				parent.setLeft(e);
			}
			else {
				assert parent.right == this;
				parent.setRight(e);
			}

		}
		
		void setRight(Entry right) {
			this.right = right;
			if (right != null) {
				right.parent = this;
			}
		}
		
		void setLeft(Entry left) {
			this.left = left;
			if (left != null) {
				left.parent = this;
			}
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
		
		@Override
		public String toString() {
			return "<" + value + ">";
		}
		
		private void dump() {
			System.out.print(value);
			if (left == null && right == null) {
				return;
			}
			System.out.print("(");
			if (left != null) {
				left.dump();
			}
			System.out.print(",");
			if (right != null) {
				right.dump();
			}
			System.out.print(")");
		}
		


	}
}
