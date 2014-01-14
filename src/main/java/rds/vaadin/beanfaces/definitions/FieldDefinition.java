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

import rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding;

import com.vaadin.addon.jpacontainer.metadata.PropertyKind;

/**
 * Base interface for describing a field (a bean property).
 * 
 * @author Raphael Rougemont
 * 
 */
public interface FieldDefinition {

	/**
	 * Returns the displayable label associated to the field
	 * 
	 * @return
	 */
	String getLabel();

	/**
	 * Returns the (java) property name associated to the field
	 * 
	 * @return
	 */
	Object getPropertyName();

	/**
	 * Returns the kind of the field
	 * 
	 * @return
	 */
	PropertyKind getKind();

	/**
	 * Returns the display mode
	 * 
	 * @return
	 */
	UIFieldBinding.MODES getDisplayMode();

	/**
	 * Returns relative java type
	 */
	Class<?> getType();
}
