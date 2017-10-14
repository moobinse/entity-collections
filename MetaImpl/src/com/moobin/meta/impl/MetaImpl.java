package com.moobin.meta.impl;

import java.util.Map;

import com.moobin.meta.PropertyMeta;
import com.moobin.meta.Meta;

public class MetaImpl<V> implements Meta<V> {

	private Class<V> type;
	private Map<String, PropertyMeta<V, ?>> properties;
 
	public MetaImpl(Class<V> type) {
		this.type = type;
	}

	@Override
	public Class<V> getType() {
		return type;
	}

	@Override
	public Map<String, PropertyMeta<V, ?>> getProperties() {
		return properties;
	}

}
