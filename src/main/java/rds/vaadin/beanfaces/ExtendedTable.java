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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rds.vaadin.beanfaces.impl.introspection.DefaultBeanIntrospector;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;

/**
 * Provides a simple way to build a vaadin Table with some custom
 * functionalities offered by annotations.
 * 
 * For more informations about functionnalities see
 * {@link rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding}
 * and {@link rds.vaadin.beanfaces.impl.introspection.annotations.UIBeanBinding}
 * .
 * 
 * @author Raphael Rougemont
 * 
 */
@SuppressWarnings("serial")
public class ExtendedTable extends Table {

	private final boolean addCheckboxColumn;
	private boolean initialized = false;

	public ExtendedTable() {
		this(false);
	}

	public ExtendedTable(boolean generateCheckboxColumn) {
		setCaption(null);
		setColumnCollapsingAllowed(true);
		setBuffered(true);
		setImmediate(true);
		setSelectable(true);
		this.addCheckboxColumn = generateCheckboxColumn;
	}

	@Override
	public void setContainerDataSource(Container newDataSource) {
		super.setContainerDataSource(newDataSource);
		if (!initialized) {
			init(newDataSource);
		}
	}

	@Override
	public void setContainerDataSource(Container newDataSource,
			Collection<?> visibleIds) {
		if (newDataSource == null) {
			throw new IllegalArgumentException("Container cannot be null");
		}
		super.setContainerDataSource(newDataSource, visibleIds);
		if (!initialized) {
			init(newDataSource);
		}
	}

	private void init(Container container) {
		if (container instanceof JPAContainer) {
			// Get entity class
			Class<?> entityClass = ((JPAContainer<?>) container)
					.getEntityClass();

			// Init columns
			setVisibleColumns(DefaultBeanIntrospector.get(entityClass)
					.findEligibleTableProperties());

			// Init headers
			setColumnHeaders(DefaultBeanIntrospector.get(entityClass)
					.findEligibleTableHeaders());

			// Hide some collumns
			for (Object o : DefaultBeanIntrospector.get(entityClass)
					.findEligibleHiddenColumn()) {
				setColumnCollapsed(o, true);
			}

			// Convert boolean values
			for (Object o : DefaultBeanIntrospector.get(entityClass)
					.findEligibleBooleanColumn()) {
				setConverter(o, new StringToBoolean());
			}
		}

		/*
		 * Generate addional column
		 */
		if (addCheckboxColumn) {
			addGeneratedCheckboxColumn();
		}

		initialized = true;
	}

	private void addGeneratedCheckboxColumn() {

		// Keep references on all Checkbox
		final List<CheckBox> data = new ArrayList<CheckBox>();

		addItemClickListener(new ItemClickEvent.ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				if (ExtendedTable.this.isReadOnly()) {
					return;
				}
				boolean selected = ExtendedTable.this.isSelected(event
						.getItemId());
				for (CheckBox c : data) {
					if (c.getData().toString()
							.equals(event.getItemId().toString())) {
						c.setReadOnly(false);
						if (!selected) {
							c.setValue(Boolean.TRUE);
						} else {
							c.setValue(Boolean.FALSE);
						}
						c.setReadOnly(true);
					}
				}
			}
		});

		addGeneratedColumn("", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {
				final CheckBox checkBox = new CheckBox();
				checkBox.setImmediate(false);
				checkBox.setBuffered(true);
				checkBox.setData(itemId);
				data.add(checkBox);
				// Update
				if (source.isSelected(itemId)) {
					checkBox.setValue(Boolean.TRUE);
				}
				checkBox.setReadOnly(true);
				return checkBox;
			}
		});
		// Attach the list of checkbox in order to be linked
		// with the component, and accessible outside.
		setData(data);
	}
}

@SuppressWarnings("serial")
class StringToBoolean extends StringToBooleanConverter {

	@Override
	protected String getFalseString() {
		return "non";
	}

	@Override
	protected String getTrueString() {
		return "oui";
	}
}
