package org.moobin.util.test;

import org.moobin.ann.Id;

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
