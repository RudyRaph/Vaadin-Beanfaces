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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingMutableLocalEntityProvider;
import com.vaadin.addon.jpacontainer.util.HibernateLazyLoadingDelegate;
import com.vaadin.addon.jpacontainer.util.HibernateUtil;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;

/**
 * Utility class for JPAContainer.
 * 
 * Strongly inspired by logic from
 * com.vaadin.addon.jpacontainer.fieldfactory.MasterDetailEditor and
 * com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory.
 * 
 * @author Raphael Rougemont
 * 
 */
public class JPAContainerUtils {

	private JPAContainerUtils() {
	}

	@SuppressWarnings("unchecked")
	public static <T> JPAContainer<T> buildContainer(EntityManager em_,
			Class<T> clazz) {

		CachingMutableLocalEntityProvider<T> entityProvider = new CachingMutableLocalEntityProvider<T>(
				clazz, em_);
		entityProvider
				.setLazyLoadingDelegate(new HibernateLazyLoadingDelegate());
		JPAContainer<T> container = new JPAContainer<T>(clazz);
		container.setEntityProvider(entityProvider);
		return container;
	}

	public static Class<?> getPropertyType(EntityManagerFactory emf,
			Object propertyId, Class<?> entityClass) {
		for (EntityType<?> entityType : emf.getMetamodel().getEntities()) {
			Class<?> javaType = entityType.getJavaType();
			if (javaType == entityClass) {
				Attribute<?, ?> attribute = entityType.getAttribute(propertyId
						.toString());
				if (attribute instanceof PluralAttribute) {
					PluralAttribute pAttribute = (PluralAttribute) attribute;
					return pAttribute.getElementType().getJavaType();
				} else {
					SingularAttribute pAttribute = (SingularAttribute) attribute;
					return pAttribute.getType().getJavaType();
				}
			}
		}
		return null;
	}

	public static <T> void associateEntity(EntityItem<T> masterEntity,
			String propertyName, EntityItem<T> item) {
		String backReferencePropertyId = HibernateUtil.getMappedByProperty(
				masterEntity.getEntity(), propertyName);
		Property<T> backProp = item.getItemProperty(backReferencePropertyId);
		backProp.setValue(masterEntity.getEntity());
		JPAContainer<T> container = (JPAContainer<T>) item.getContainer();
		T entity = item.getEntity();
		container.addEntity(entity);
	}

	public static <T> void removeAssociatedEntity(JPAContainer<T> container,
			Object itemId) {
		container.removeItem(itemId);
	}

	public static <T> void filterOneToMany(JPAContainer container,
			String propertyName, T masterEntity) {
		final String backReferencePropertyId = HibernateUtil
				.getMappedByProperty(masterEntity, propertyName);
		Filter filter = new Compare.Equal(backReferencePropertyId, masterEntity);
		container.removeAllContainerFilters();
		container.addContainerFilter(filter);
		container.applyFilters();
	}
}
