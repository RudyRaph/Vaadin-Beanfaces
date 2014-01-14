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
package rds.vaadin.beanfaces.impl.introspection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for binding a field (property of a bean).
 * 
 * Used to hold metadatas used for UI generation.
 * 
 * @author Raphael Rougemont
 * 
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UIFieldBinding {

	/**
	 * Available display type for a field.
	 * 
	 * WRITE : the field value can be only inserted WRITE_UPDATE : the field
	 * value can be inserted and updated
	 * 
	 * @author Raphael Rougemont
	 * 
	 */
	public enum MODES {
		WRITE, WRITE_UPDATE;
	}

	/**
	 * Default label used for the field
	 * 
	 * @return
	 */
	String label() default "";

	/**
	 * Default description used for the field
	 * 
	 * @return
	 */
	String description() default "";

	/**
	 * Default label used when in table column
	 * 
	 * @return
	 */
	String headerLabelInTable() default "";

	/**
	 * Should this field be hidden when displayed in a table
	 * 
	 * @return
	 */
	boolean hiddenColumnInTable() default false;

	/**
	 * Should this field value be translated when displayed in a table
	 * 
	 * @return
	 */
	boolean booleanConvertedInTable() default false;

	/**
	 * Should the field be hidden when displayed in a table
	 * 
	 * @return
	 */
	boolean hiddenInTable() default false;

	/**
	 * Default display policy applied to this field
	 * 
	 * @return
	 */
	MODES mode() default MODES.WRITE_UPDATE;

}
