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

import rds.vaadin.beanfaces.ExtendedTable;

import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

public class FactoryUtils {

	public Field<?> createManyToOneField() {
		ComboBox combobox = new ComboBox();
		combobox.setImmediate(true);
		combobox.setBuffered(true);
		combobox.setNullSelectionAllowed(false);
		combobox.setItemCaptionMode(ItemCaptionMode.ITEM);
		combobox.setConverter(new SingleSelectConverter<Object>(combobox));
		return combobox;
	}

	public Field<?> createManyToManyField() {
		final ExtendedTable table = new ExtendedTable(true);
		table.setConverter(new BaseMultiSelectConverter(table));
		table.setMultiSelect(true);
		table.setMultiSelectMode(MultiSelectMode.SIMPLE);
		table.setWidth("100%");
		return table;
	}

	@SuppressWarnings("serial")
	public Field<?> createOneToManyField() {
		final ExtendedTable table = new ExtendedTable() {
			@Override
			public void commit() throws SourceException, InvalidValueException {
				// Master table is not supposed to be commited with the "plain"
				// form.
				// Master table is supposed to have its own internal commit
				// implementation.
				// super.commit();
			}
		};
		table.setWidth("100%");
		return table;
	}
}
