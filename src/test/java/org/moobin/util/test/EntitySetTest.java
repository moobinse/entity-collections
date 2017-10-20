package org.moobin.util.test;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.moobin.util.EntitySet;
import org.moobin.util.EntitySetBuilder;
import org.moobin.util.ModifyibleEntitySet;
import org.moobin.util.impl.EntitySetBuilderImpl;

public class EntitySetTest {

	private static EntitySetBuilder factory;
	private static ModifyibleEntitySet<String, TestEntity> entities;
	private static EntitySet<String, TestEntity> subCollection;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		factory = new EntitySetBuilderImpl();
		entities = (ModifyibleEntitySet<String, TestEntity>) 
				factory.create(TestEntity.class, String.class, e -> e.id);
		subCollection = entities.filter(e -> e.number % 10 == 3);
	}

	@Test
	public void testFactory() {
		
		for (int i = 0; i < 100; i++) {
			entities.update(new TestEntity(i));
		}
		assertEquals(100, entities.getSize());
		assertEquals(10, subCollection.getSize());

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
		
		for (int i = 0; i < 100; i++) {
			entities.update(new TestEntity(i));
		}
		assertEquals(100, entities.getSize());
		assertEquals(10, subCollection.getSize());
		
		for (int i = 50; i < 200; i++) {
			entities.update(new TestEntity(i));
		}
		assertEquals(200, entities.getSize());
		assertEquals(20, subCollection.getSize());
		
		Assert.assertNotNull(entities.getValue("E19"));
		Assert.assertNull(subCollection.getValue("E19"));
	}

	@Test
	public void testFactory2() {
		
		
	}

}
