package org.moobin.entityset.test;

import org.moobin.meta.annotation.Id;

public class TestEntity {

	@Id
	public String id;
	
	public int number;

	public TestEntity(int i) {
		this.id = "E" + i;
		this.number = i;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
}
