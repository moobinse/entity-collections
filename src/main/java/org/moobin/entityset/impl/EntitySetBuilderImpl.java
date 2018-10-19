/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Magnus Lenti (moobin@moobin.org)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.moobin.entityset.impl;

import java.util.function.Function;

import org.moobin.entityset.EntitySet;
import org.moobin.entityset.EntitySetBuilder;
import org.moobin.entityset.impl.EntitySetImpl;
import org.moobin.meta.EntityDescription;
import org.moobin.meta.impl.EntityMetaImpl;

public class EntitySetBuilderImpl implements EntitySetBuilder {

	@Override
	public <K extends Comparable<K>, V> EntitySet<K, V> create(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {

		EntityDescription<V, K> entityDef = new EntityMetaImpl<V, K>(valueType, keyType, keyFunction);
		return new EntitySetImpl<>(entityDef);
	}

}
