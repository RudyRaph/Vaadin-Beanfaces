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
package rds.vaadin.beanfaces.impl.introspection;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding;
import rds.vaadin.beanfaces.impl.introspection.annotations.UIBeanBinding;

/**
 * Allows to introspect a bean according to some defined annotations.
 * 
 * Statically holds a cache map for each known beans. If bean metadata for a
 * bean already exists it retreives it from the cache.
 * 
 * @author Raphael Rougemont
 * 
 */
@SuppressWarnings("serial")
public class DefaultBeanIntrospector implements Serializable {

	private static final Map<Class<?>, DefaultBeanIntrospector> REFS = new HashMap<Class<?>, DefaultBeanIntrospector>();

	private static final Class<UIFieldBinding> FIELD_BINDING_ANNOTATION = UIFieldBinding.class;

	private final List<Field> annotatedProperties = new ArrayList<Field>();

	private final Class<?> beanClass;

	public static DefaultBeanIntrospector get(Class<?> beanClass) {
		if (!REFS.containsKey(beanClass)) {
			REFS.put(beanClass, new DefaultBeanIntrospector(beanClass));
		}
		return REFS.get(beanClass);
	}

	protected DefaultBeanIntrospector(Class<?> beanClass) {
		this.beanClass = beanClass;
		for (Field field : beanClass.getDeclaredFields()) {
			field.setAccessible(true);
			UIFieldBinding binding = field
					.getAnnotation(FIELD_BINDING_ANNOTATION);
			if (binding != null) {
				annotatedProperties.add(field);
			}
		}
	}

	public String findFormDescription() {
		if (this.beanClass.getAnnotation(UIBeanBinding.class) != null) {
			return this.beanClass.getAnnotation(UIBeanBinding.class)
					.description();
		}
		return null;
	}

	public String[] findEligibleTableHeaders() {
		List<String> eligibles = new ArrayList<String>();
		for (Field f : annotatedProperties) {
			UIFieldBinding annot = findAnnotation(f.getName());
			if (!annot.hiddenInTable()) {
				eligibles.add(!annot.headerLabelInTable().equals("") ? annot
						.headerLabelInTable() : annot.label());
			}
		}
		return eligibles.toArray(new String[] {});
	}

	public Object[] findEligibleTableProperties() {
		List<Object> eligibles = new ArrayList<Object>();
		for (Field f : annotatedProperties) {
			UIFieldBinding annot = findAnnotation(f.getName());
			if (!annot.hiddenInTable()) {
				eligibles.add(f.getName());
			}
		}
		return eligibles.toArray(new Object[] {});
	}

	public Object[] findEligibleHiddenColumn() {
		List<Object> eligibles = new ArrayList<Object>();
		for (Field f : annotatedProperties) {
			UIFieldBinding annot = findAnnotation(f.getName());
			if (annot.hiddenColumnInTable()) {
				eligibles.add(f.getName());
			}
		}
		return eligibles.toArray(new Object[] {});
	}

	public Object[] findEligibleBooleanColumn() {
		List<Object> eligibles = new ArrayList<Object>();
		for (Field f : annotatedProperties) {
			UIFieldBinding annot = findAnnotation(f.getName());
			if (annot.booleanConvertedInTable()) {
				eligibles.add(f.getName());
			}
		}
		return eligibles.toArray(new Object[] {});
	}

	public List<Field> getAnnotatedFields() {
		return annotatedProperties;
	}

	public UIFieldBinding findAnnotation(String propertyName) {
		Field property = getAnnotatedProperty(propertyName);
		return property.getAnnotation(FIELD_BINDING_ANNOTATION);
	}

	public Field getAnnotatedProperty(String propertyName) {
		for (Field member : annotatedProperties) {
			if (propertyName.equals(member.getName())) {
				return member;
			}
		}
		throw new RuntimeException("Annotated bean member not found :"
				+ propertyName);
	}
}
