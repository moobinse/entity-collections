package org.moobin.entityset.test;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.moobin.entityset.EntitySet;
import org.moobin.entityset.EntitySetBuilder;
import org.moobin.entityset.impl.EntitySetBuilderImpl;
import org.moobin.entityset.impl.ModifyibleEntitySet;

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
		assertEquals(100, entities.size());
		assertEquals(10, subCollection.size());

		for (int i = 50; i < 200; i++) {
			entities.update(new TestEntity(i));
		}
		assertEquals(200, entities.size());
		assertEquals(20, subCollection.size());

		Assert.assertNotNull(entities.getValue("E19"));
		Assert.assertNull(subCollection.getValue("E19"));
		
		entities.clear();
		assertEquals(0, entities.size());
		assertEquals(0, subCollection.size());
		
		for (int i = 0; i < 100; i++) {
			entities.update(new TestEntity(i));
		}
		assertEquals(100, entities.size());
		assertEquals(10, subCollection.size());
		
		for (int i = 50; i < 200; i++) {
			entities.update(new TestEntity(i));
		}
		assertEquals(200, entities.size());
		assertEquals(20, subCollection.size());
		
		Assert.assertNotNull(entities.getValue("E19"));
		Assert.assertNull(subCollection.getValue("E19"));
	}

	@Test
	public void testFactory2() {
		
		
	}

}
