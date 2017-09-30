package com.moobin.meta;

public interface PropertyMeta<E, P> {

	Class<P> getComponentType();
	
	String getName();
	
}
