package org.moobin.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;
import org.moobin.util.EntitySet;
import org.moobin.util.EntitySetBuilder;
import org.moobin.util.ModifyibleEntitySet;
import org.moobin.util.impl.EntitySetBuilderImpl;
import org.moobin.util.impl.SortedEntitySetImpl;

public class IndexedEntitySetTest {

	private static EntitySetBuilder factory;
	private static ModifyibleEntitySet<String, TestEntity> entities;
	private static EntitySet<String, TestEntity> subCollection;
	private static SortedEntitySetImpl<String, TestEntity> sorted;	
	
	@Test
	public void testIndexedSet() {
		factory = new EntitySetBuilderImpl();
		entities = (ModifyibleEntitySet<String, TestEntity>) factory.create(TestEntity.class, String.class, e -> e.id);
		
		List<Integer> list = new ArrayList<>();
		randomInts(1000).forEach(list::add);
		list.forEach(i -> entities.update(new TestEntity(i)));
		sorted = new SortedEntitySetImpl<>(entities, (a, b) -> a.number - b.number);
		System.out.println(list.size());
		entities.removeByKey("E302");
		System.out.println(entities.getSize());
		System.out.println(sorted.getSize());
		System.out.println(sorted.get(300, 10));
		System.out.println(sorted.get(200));

		assertEquals(IndexOutOfBoundsException.class, assertException(() -> sorted.get(-1, 10)));
		assertEquals(IndexOutOfBoundsException.class, assertException(() -> sorted.get(998, 2)));
		assertEquals(IndexOutOfBoundsException.class, assertException(() -> sorted.get(999)));
		
	
	}
	
	private Class<?> assertException(Supplier<?> supplier) {
		try {
			supplier.get();
		}
		catch (Exception e) {
			return e.getClass();
		}
		return null;
	}

	@Test
	public void testFactory() {
		
		factory = new EntitySetBuilderImpl();
		entities = (ModifyibleEntitySet<String, TestEntity>) factory.create(TestEntity.class, String.class, e -> e.id);
		subCollection = entities.filter(e -> e.number % 10 == 3);

		randomInts(100).forEach(i -> entities.update(new TestEntity(i)));
		sorted = new SortedEntitySetImpl<>(entities, (a, b) -> a.number - b.number);
		System.out.println(sorted.get(0));
		System.out.println(sorted.get(1));
		System.out.println(sorted.get(2));
		System.out.println(sorted.get(3));
		System.out.println(sorted.get(4));
		System.out.println(sorted.get(30,10));
		
		assertEquals(100, entities.getSize());
		assertEquals(10, subCollection.getSize());
		assertEquals(100, sorted.getSize());

		for (int i = 50; i < 200; i++) {
			entities.update(new TestEntity(i));
		}
		assertEquals(200, entities.getSize());
		assertEquals(20, subCollection.getSize());
		


		Assert.assertNotNull(entities.getValue("E19"));
		Assert.assertNull(subCollection.getValue("E19"));
		
		entities.clear();
		assertEquals(0, entities.getSize());
		assertEquals(0, subCollection.getSize());
		assertEquals(0, sorted.getSize());
		
		randomInts(100).forEach(i -> entities.update(new TestEntity(i)));
		assertEquals(100, entities.getSize());
		assertEquals(10, subCollection.getSize());
		
		for (int i = 50; i < 200; i++) {
			entities.update(new TestEntity(i));
		}
		assertEquals(200, entities.getSize());
		assertEquals(20, subCollection.getSize());
		
		Assert.assertNotNull(entities.getValue("E19"));
		Assert.assertNull(subCollection.getValue("E19"));
		
		Assert.assertEquals(62, sorted.indexByKey("E62"));
		Assert.assertEquals("E0", sorted.get(0).id);
		Assert.assertEquals("E33", sorted.get(33).id);
		
		for (int i = 0; i < 200; i++) {
			String key = "E" + i;
			Assert.assertNotNull(entities.getValue(key));
			if (i % 10 == 3) {
				Assert.assertNotNull(subCollection.getValue(key));
			}
			else {
				Assert.assertNull(subCollection.getValue(key));
			}
			Assert.assertNotNull(sorted.getValue(key));
			Assert.assertEquals(i, sorted.indexByKey(key));
		}
		
	}

	private List<Integer> randomInts(int size) {
		List<Integer> list = new ArrayList<>();
	    IntStream.range(0, size).forEach(list::add);
	    Collections.shuffle(list);
	    return list;
	}

}
