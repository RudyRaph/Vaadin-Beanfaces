/**
 * Copyright 2013 RudyRaph
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rds.vaadin.beanfaces.utils;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;

/**
 * Default field factory handler.
 * 
 * Custom factories can be added for a specific entity by using {@link
 * addFactory(Class<?> key, FieldGroupFieldFactory value)}.
 * 
 * @author Raphael Rougemont
 * 
 */
public class FieldFactoryHandler {

	private final static FactoryUtils factoryUtils = new FactoryUtils();

	private final Map<Class<?>, Object> factories = new HashMap<Class<?>, Object>();

	public FactoryUtils getFactoryUtils() {
		return factoryUtils;
	}

	/**
	 * Adds factory for a specific entity.
	 * 
	 * @param key
	 *            entity class
	 * @param value
	 *            FieldGroupFieldFactory instance
	 */
	public void addFactory(Class<?> key, FieldGroupFieldFactory value) {
		factories.put(key, value);
	}

	/**
	 * Returns factories associated with the given entity
	 * 
	 * @param key
	 *            entity class
	 * @return a custom FieldGroupFieldFactory
	 */
	public FieldGroupFieldFactory getFactory(Class<?> key) {
		return (FieldGroupFieldFactory) factories.get(key);
	}
}