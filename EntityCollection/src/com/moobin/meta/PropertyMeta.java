package com.moobin.meta;

public interface PropertyMeta<E, P> {

	String getName();
	
	ValueMeta<P> getMeta();

	Composition getComposition();
	
}
