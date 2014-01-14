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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import rds.vaadin.beanfaces.BeanFace.LayoutType;
import rds.vaadin.beanfaces.definitions.FieldDefinition;
import rds.vaadin.beanfaces.definitions.FormDefinition;
import rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding;
import rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding.MODES;
import rds.vaadin.beanfaces.utils.FieldFactoryHandler;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.metadata.PropertyKind;

/**
 * Default implementation of {@FormDefinition} using
 * {@link rds.vaadin.beanfaces.impl.introspection.DefaultBeanIntrospector} to
 * find out properties.
 * 
 * Keeps references on {@link rds.vaadin.beanfaces.definitions.FieldDefinition}
 * describing every properties of a form.
 * 
 * @author Raphael Rougemont
 * 
 */
public class AnnotatedBeanFormDefinition implements FormDefinition {

	private final LayoutType layoutType;
	private final FieldFactoryHandler factoryHandler;
	private final JPAContainer<?> jpaContainer;
	private final List<FieldDefinition> fieldsConfig;

	public AnnotatedBeanFormDefinition(JPAContainer<?> jpaContainer,
			FieldFactoryHandler factoryHandler, LayoutType layout,
			boolean withAssociations) {
		this.jpaContainer = jpaContainer;
		this.layoutType = layout;
		this.factoryHandler = factoryHandler;
		this.fieldsConfig = new ArrayList<FieldDefinition>();

		// Introspect bean
		DefaultBeanIntrospector bi = DefaultBeanIntrospector.get(jpaContainer
				.getEntityClass());

		// Loop annotated fields
		List<Field> annotatedFields = bi.getAnnotatedFields();

		for (Field f : annotatedFields) {
			PropertyKind propertyKind = jpaContainer.getPropertyKind(f
					.getName());
			UIFieldBinding binding = f.getAnnotation(UIFieldBinding.class);

			if (!withAssociations) {
				if (!isExcludable(propertyKind)) {
					fieldsConfig.add(new FieldDefinitionImpl(f.getName(), f
							.getType(), bi.findAnnotation(f.getName()).label(),
							propertyKind, binding.mode()));
				}
			} else {
				fieldsConfig.add(new FieldDefinitionImpl(f.getName(), f
						.getType(), bi.findAnnotation(f.getName()).label(),
						propertyKind, binding.mode()));
			}
		}
	}

	public LayoutType getLayoutType() {
		return layoutType;
	}

	public List<FieldDefinition> getFieldsDefinition() {
		return fieldsConfig;
	}

	public FieldDefinition getFieldDefinition(String propertyName) {
		for (FieldDefinition c : fieldsConfig) {
			if (c.getPropertyName().toString().equals(propertyName)) {
				return c;
			}
		}
		return null;
	}

	public FieldFactoryHandler getFactoryHandler() {
		return factoryHandler;
	}

	public Class<?> getEntityClass() {
		return jpaContainer.getEntityClass();
	}

	public JPAContainer<?> getContainer() {
		return jpaContainer;
	}

	/*
	 * Return true if this property is given as an association and must be
	 * excluded from plain form.
	 */
	public boolean isExcludable(PropertyKind propertyKind) {
		if (propertyKind == PropertyKind.ONE_TO_MANY
				|| propertyKind == PropertyKind.MANY_TO_MANY) {
			return true;
		}
		return false;
	}

	class FieldDefinitionImpl implements FieldDefinition {

		private final Object propertyName;
		private final String label;
		private final PropertyKind kind;
		private final MODES mode;
		private final Class<?> type;

		public FieldDefinitionImpl(Object propertyName, Class<?> type,
				String label, PropertyKind kind, MODES mode) {
			this.propertyName = propertyName;
			this.type = type;
			this.label = label;
			this.kind = kind;
			this.mode = mode;
		}

		@Override
		public PropertyKind getKind() {
			return kind;
		}

		@Override
		public Object getPropertyName() {
			return propertyName;
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public MODES getDisplayMode() {
			return mode;
		}

		@Override
		public Class<?> getType() {
			return type;
		}
	}
}
