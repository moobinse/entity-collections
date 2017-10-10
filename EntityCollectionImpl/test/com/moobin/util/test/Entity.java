package com.moobin.util.test;

public class Entity {

	public String id;
	
	public int number;

	public Entity(int i) {
		this.id = "E" + i;
		this.number = i;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
}
