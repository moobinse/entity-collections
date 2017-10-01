package com.moobin.meta;

public interface PropertyMeta<V, P> {

	ValueMeta<V> getOwner();
	
	String getName();
	
	ValueMeta<P> getMeta();

	Composition getComposition();
	
	boolean required();

}
