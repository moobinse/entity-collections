package com.moobin.meta.impl;

import com.moobin.meta.ValueMeta;

public class ValueMetaImpl<V> implements ValueMeta<V> {

	private Class<V> type;
 
	public ValueMetaImpl(Class<V> type) {
		this.type = type;
	}

	@Override
	public Class<V> getType() {
		return type;
	}

}
