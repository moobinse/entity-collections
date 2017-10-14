package com.moobin.util.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.moobin.util.EntitySet;
import com.moobin.util.EntitySetBuilder;
import com.moobin.util.ModifyibleEntitySet;
import com.moobin.util.impl.EntitySetBuilderImpl;
import com.moobin.util.impl.IndexedEntitySetImpl;

public class IndexedEntitySetTest {

	private static EntitySetBuilder factory;
	private static ModifyibleEntitySet<String, Entity> entities;
	private static EntitySet<String, Entity> subCollection;
	private static IndexedEntitySetImpl<String, Entity> sorted;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		factory = new EntitySetBuilderImpl();
		entities = (ModifyibleEntitySet<String, Entity>) 
				factory.create(Entity.class, String.class, e -> e.id);
		subCollection = entities.filter(e -> e.number % 10 == 3);
		sorted = new IndexedEntitySetImpl<>(entities, (a, b) -> a.number - b.number);
	}

	@Test
	public void testFactory() {
		
		randomInts(100).forEach(i -> entities.update(new Entity(i)));
		assertEquals(100, entities.getSize());
		assertEquals(10, subCollection.getSize());
		assertEquals(100, sorted.getSize());

		for (int i = 50; i < 200; i++) {
			entities.update(new Entity(i));
		}
		assertEquals(200, entities.getSize());
		assertEquals(20, subCollection.getSize());

		Assert.assertNotNull(entities.getValue("E19"));
		Assert.assertNull(subCollection.getValue("E19"));
		
		entities.clear();
		assertEquals(0, entities.getSize());
		assertEquals(0, subCollection.getSize());
		assertEquals(0, sorted.getSize());
		
		randomInts(100).forEach(i -> entities.update(new Entity(i)));
		assertEquals(100, entities.getSize());
		assertEquals(10, subCollection.getSize());
		
		for (int i = 50; i < 200; i++) {
			entities.update(new Entity(i));
		}
		assertEquals(200, entities.getSize());
		assertEquals(20, subCollection.getSize());
		
		Assert.assertNotNull(entities.getValue("E19"));
		Assert.assertNull(subCollection.getValue("E19"));
		
		Assert.assertEquals(62, sorted.indexByKey("E62"));
		Assert.assertEquals("E0", sorted.get(0).id);
		Assert.assertEquals("E33", sorted.get(33).id);
	}

	private List<Integer> randomInts(int size) {
		List<Integer> list = new ArrayList<>();
	    IntStream.range(0, 100).forEach(list::add);
	    Collections.shuffle(list);
	    return list;
	}

}
