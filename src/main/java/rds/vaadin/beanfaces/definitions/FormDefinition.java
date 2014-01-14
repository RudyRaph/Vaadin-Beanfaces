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
package rds.vaadin.beanfaces.definitions;

import java.util.List;

import rds.vaadin.beanfaces.BeanFace.LayoutType;
import rds.vaadin.beanfaces.utils.FieldFactoryHandler;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.metadata.PropertyKind;

/**
 * Base interface to describe metadatas needed for Bean-UI generation.
 * 
 * 
 * @author Raphael Rougemont
 * 
 */
public interface FormDefinition {

	/**
	 * Returns the layout of the form
	 * 
	 * @return
	 */
	LayoutType getLayoutType();

	/**
	 * Returns a list of {@FieldDefinition} describing each
	 * available fields of the form.
	 * 
	 * @return
	 */
	List<FieldDefinition> getFieldsDefinition();

	/**
	 * Returns the factory handler avalaible for the form
	 * 
	 * @return
	 */
	FieldFactoryHandler getFactoryHandler();

	/**
	 * Returns entity class of the form
	 * 
	 * @return
	 */
	Class<?> getEntityClass();

	/**
	 * Returns assiociated {@JPAContainer}
	 * 
	 * @return
	 */
	<T> JPAContainer<T> getContainer();

	/**
	 * Returns the {@FieldDefinition} according to the given
	 * propertyName
	 * 
	 * @param propertyName
	 * @return
	 */
	FieldDefinition getFieldDefinition(String propertyName);

	/**
	 * Indicated if the given @{PropertyKind} should be considered as
	 * association, ie excluded from plain form.
	 * 
	 * @param propertyKind
	 * @return
	 */
	boolean isExcludable(PropertyKind propertyKind);

}
