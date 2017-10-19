package org.moobin.util.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.moobin.util.EntitySet;
import org.moobin.util.SortedEntitySet;
import org.moobin.util.EntitySet.Listener;

public class SortedEntitySetImpl<K extends Comparable<K>, V> implements SortedEntitySet<K, V>, Listener<V> {

	private final EntitySet<K, V> source;
	private Map<K, Entry> map = new HashMap<>();
	private Entry root;
	private Comparator<V> comparator;
	
	private Object mutex = new Object();

	public SortedEntitySetImpl(EntitySet<K, V> source, Comparator<V> comparator) {
		this.source = source;
		this.comparator = comparator;
		synchronized (mutex) {
			source.addListener(this);
			if (!source.getValues().isEmpty()) {
				root = parse(source.getValues());
			}
		}
	}
	
	public <T> SortedEntitySetImpl(EntitySet<K, V> source, Function<V, T> method, Comparator<T> comparator) {
		this(source, (o1, o2) ->  comparator.compare(method.apply(o1), method.apply(o2)));
	}
	
	public SortedEntitySetImpl(EntitySet<K, V> source) {
		this.source = source;
		this.comparator = (a, b) -> getKey(a).compareTo(getKey(b));
		source.addListener(this);
		if (!source.getValues().isEmpty()) {
			root = parse(source.getValues());
		}
	}
	
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

	@Override
	public void onRemove(V value) {
		synchronized (mutex) {
			map.get(source.getKey(value)).remove();
		}
	}

	@Override
	public void onClear() {
		synchronized (mutex) {
			root = null;
			map.clear();
		}
	}
	
	@Override
	public void onDestroy() {
		throw new RuntimeException("Not implemented");
	}
	
	@Override
	public EntitySet<K, V> getSource() {
		return source;
	}

	@Override
	public V get(int index) {
		synchronized (mutex) {
			if (index < 0 || index >= getSize()) {
				throw new IndexOutOfBoundsException();
			}
			return root.get(index).value;
		}
	}
	
	public List<V> get(int from, int count) {
		List<V> list = new ArrayList<>();
		synchronized (mutex) {
			if (from < 0 || from + count > getSize()) {
				throw new IndexOutOfBoundsException();
			}
			Entry entry = root.get(from);
			for (int i = 0; entry != null && i < count; i++) {
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

	
	private class Entry {
		Entry parent;
		Entry left;
		Entry right;
		private V value;
		private K key;
		private int size = 1;

		Entry(V value) {
			this.value = value;
			this.key = source.getKey(value);
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
				cmp = ((Comparable<K>) key).compareTo(source.getKey(value));
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


	}

}
