package org.moobin.meta;

public interface Metas {

	<T> Meta<T> getMeta(Class<T> clazz);

}
