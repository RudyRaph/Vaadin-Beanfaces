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
package rds.vaadin.beanfaces;

import java.util.Collection;

import javax.persistence.EntityManagerFactory;

import rds.vaadin.beanfaces.definitions.FieldDefinition;
import rds.vaadin.beanfaces.definitions.FormDefinition;
import rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding;
import rds.vaadin.beanfaces.utils.JPAContainerUtils;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.metadata.PropertyKind;
import com.vaadin.data.Container.Viewer;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Allows to fastly build form interfaces based on annotated JPA entities.
 * 
 * This approach is inspired by old vaadin 6 form and JPAContainer
 * master-details components. <br />
 * <br />
 * 
 * Once the form is build you can access associated components (onetomany and
 * manytomany) with {@link getField()}. <br />
 * 
 * @author Raphael Rougemont
 * 
 */
@SuppressWarnings("serial")
public class BeanFace extends FieldGroup implements Displayable {

	/**
	 * Available layout type for the form
	 */
	public enum LayoutType {
		HORIZONTAL, VERTICAL;
	}

	private final Layout layout;

	private final FormDefinition formDef;

	private Displayable.MODE mode;

	/**
	 * Create an ExtendedForm based on given {@FormDefinition}.
	 * 
	 * @param formDef
	 *            a FormDefinition instance
	 */
	public BeanFace(FormDefinition formDef) {
		this.formDef = formDef;

		// Build layout
		if (formDef.getLayoutType() == LayoutType.HORIZONTAL) {
			this.layout = new GridLayout(2, formDef.getFieldsDefinition()
					.size());
			((GridLayout) this.layout).setSpacing(true);
		} else {
			this.layout = new VerticalLayout();
		}

		// Create fields
		for (FieldDefinition fieldDef : formDef.getFieldsDefinition()) {

			final Object propertyName = fieldDef.getPropertyName();
			final PropertyKind propertyKind = fieldDef.getKind();
			final EntityManagerFactory emf = formDef.getContainer()
					.getEntityProvider().getEntityManager()
					.getEntityManagerFactory();

			Field<?> field = null;

			switch (propertyKind) {
			case MANY_TO_ONE:
				field = formDef.getFactoryHandler().getFactoryUtils()
						.createManyToOneField();
				attachContainer(formDef.getContainer().getType(propertyName),
						field);
				break;
			case ONE_TO_MANY:
				field = formDef.getFactoryHandler().getFactoryUtils()
						.createOneToManyField();
				attachContainer(JPAContainerUtils.getPropertyType(emf,
						propertyName, formDef.getContainer().getEntityClass()),
						field);
				break;
			case MANY_TO_MANY:
				field = formDef.getFactoryHandler().getFactoryUtils()
						.createManyToManyField();
				attachContainer(JPAContainerUtils.getPropertyType(emf,
						propertyName, formDef.getContainer().getEntityClass()),
						field);
				break;
			default:
				Class<?> type = fieldDef.getType();
				field = formDef.getFactoryHandler()
						.getFactory(formDef.getEntityClass())
						.createField(type, AbstractField.class);
				if (field instanceof AbstractTextField) {
					((AbstractTextField) field).setNullRepresentation("");
				}
				if (field instanceof RichTextArea) {
					((RichTextArea) field).setNullRepresentation("");
				}
				break;
			}
			// Bind properties
			bind(field, propertyName);
			// Append row
			appendRow(propertyKind, field, fieldDef.getLabel());
			// Activate validators by default
			activateValidator(fieldDef, true);
		}
	}

	/**
	 * Get the current mode for the form.
	 * 
	 * @return FormMode.MODE current mode
	 */
	public Displayable.MODE getMode() {
		return mode;
	}

	/**
	 * Set the visual appearance for the form. For more details about available
	 * modes see {@link MODE}
	 */
	@Override
	public void setMode(MODE mode) {
		this.mode = mode;

		final Collection<Object> properties = getBoundPropertyIds();

		switch (mode) {
		case INSERT:
			for (Object property : properties) {
				Field field = getField(property);
				field.setReadOnly(false);
				activateValidator(
						formDef.getFieldDefinition(property.toString()), true);
			}
			break;
		case UPDATE:
			for (Object property : properties) {
				FieldDefinition def = formDef.getFieldDefinition(property
						.toString());
				Field field = getField(property);
				if (def.getDisplayMode() == UIFieldBinding.MODES.WRITE_UPDATE) {
					field.setReadOnly(false);
					activateValidator(
							formDef.getFieldDefinition(property.toString()),
							true);
				} else {
					field.setReadOnly(true);
					activateValidator(
							formDef.getFieldDefinition(property.toString()),
							false);
				}
			}
			break;
		case READONLY:
			for (Object property : properties) {
				Field field = getField(property);
				field.setReadOnly(true);
				activateValidator(
						formDef.getFieldDefinition(property.toString()), false);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * After setting new datasource to the form, it refreshs / filter oneToMany
	 * associated field.
	 */
	@Override
	public void setItemDataSource(Item itemDataSource) {
		super.setItemDataSource(itemDataSource);
		refreshOneToMany();
	}

	/**
	 * Add an new oneToMany associated field.
	 * 
	 * @param propertyName
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	public <E> void addEntity(String propertyName, EntityItem<E> item) {
		JPAContainerUtils.<E> associateEntity(
				(EntityItem<E>) getItemDataSource(), propertyName, item);
	}

	/**
	 * Remove a oneToMany associated field.
	 * 
	 * @param propertyName
	 *            property name in your entity
	 * @param itemId
	 *            id of the associated entity to remove
	 */
	public <E> void removeEntity(String propertyName, Object itemId) {
		Table table = (Table) getField(propertyName);
		JPAContainerUtils.<E> removeAssociatedEntity(
				(JPAContainer<E>) table.getContainerDataSource(), itemId);
	}

	/**
	 * Return the layout for the form. For custom layout, simply override this
	 * method with yours to provide a new layout type. In that case the method
	 * {@link appendRow()} must be overriden too.
	 * 
	 * @return the layout component associated to this form
	 */
	public Layout getLayout() {
		return layout;
	}

	/**
	 * Overide to provide custom validators for field.
	 * 
	 * @param fieldConfig
	 * @param activate
	 */
	protected void activateValidator(FieldDefinition fieldConfig,
			boolean activate) {
		PropertyKind propertyKind = fieldConfig.getKind();
		// Keep only non associative fields
		if (!formDef.isExcludable(propertyKind)) {
			if (activate) {
				getField(fieldConfig.getPropertyName()).addValidator(
						new BeanValidator(formDef.getEntityClass(), fieldConfig
								.getPropertyName().toString()));
			} else {
				getField(fieldConfig.getPropertyName()).removeAllValidators();
			}
		}
	}

	/**
	 * Append row to the form in a label:field layout style. Can be overriden to
	 * customize component display.
	 * 
	 * @param propertyKind
	 * @param field
	 * @param caption
	 */
	protected void appendRow(PropertyKind propertyKind, Component field,
			String caption) {
		if (formDef.isExcludable(propertyKind)) {
			return;
		}
		Label label = new Label(caption.toString());
		getLayout().addComponent(label);
		getLayout().addComponent(field);
		if (getLayout() instanceof GridLayout) {
			((GridLayout) getLayout()).setComponentAlignment(label,
					Alignment.MIDDLE_RIGHT);
		}
	}

	private void refreshOneToMany() {
		EntityItem<?> masterEntity = (EntityItem<?>) getItemDataSource();

		// Exclude transient instances
		if (masterEntity.isPersistent()) {
			for (FieldDefinition fieldConfig : formDef.getFieldsDefinition()) {
				switch ((PropertyKind) fieldConfig.getKind()) {
				case ONE_TO_MANY:
					Table table = (Table) getField(fieldConfig
							.getPropertyName());
					JPAContainer<?> container = (JPAContainer<?>) table
							.getContainerDataSource();
					JPAContainerUtils.filterOneToMany(container, fieldConfig
							.getPropertyName().toString(), masterEntity
							.getEntity());
					break;
				default:
					break;
				}
			}
		}
	}

	private void attachContainer(Class<?> type, Field<?> field) {
		JPAContainer<?> containerForField = JPAContainerUtils.buildContainer(
				formDef.getContainer().getEntityProvider().getEntityManager(),
				type);
		((Viewer) field).setContainerDataSource(containerForField);
	}
}