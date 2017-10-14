package com.moobin.util.test;

import com.moobin.ann.Id;

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
