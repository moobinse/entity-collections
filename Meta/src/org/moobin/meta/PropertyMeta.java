package org.moobin.meta;

public interface PropertyMeta<V, P> {

	Meta<V> getOwner();
	
	String getName();
	
	Meta<P> getMeta();

	PropertyComposition getComposition();
	
	boolean isRequired();

	boolean isReadOnly();
	
}
