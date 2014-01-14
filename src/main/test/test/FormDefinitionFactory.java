package test;

import rds.vaadin.beanfaces.BeanFace;
import rds.vaadin.beanfaces.definitions.FormDefinition;
import rds.vaadin.beanfaces.impl.introspection.AnnotatedBeanFormDefinition;
import rds.vaadin.beanfaces.utils.FieldFactoryHandler;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.util.HibernateLazyLoadingDelegate;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;

/**
 * Default factory for {@link rds.vaadin.beanfaces.definitions.FormDefinition}.
 * 
 * @author Raphael Rougemont
 * 
 */
public class FormDefinitionFactory {

	private static final String PERSISTENCE_UNIT = "rds-persistenceunit";

	public static FormDefinition buildWithoutAssociations(Class<?> entityClass) {
		JPAContainer<?> container = JPAContainerFactory.make(entityClass,
				PERSISTENCE_UNIT);
		container.getEntityProvider().setLazyLoadingDelegate(
				new HibernateLazyLoadingDelegate());
		FieldFactoryHandler factoryHandler = new FieldFactoryHandler();
		factoryHandler.addFactory(entityClass,
				new DefaultFieldGroupFieldFactory());
		return new AnnotatedBeanFormDefinition(container, factoryHandler,
				BeanFace.LayoutType.HORIZONTAL, false);
	}

	public static FormDefinition buildWithAssociations(Class<?> entityClass) {
		JPAContainer<?> container = JPAContainerFactory.make(entityClass,
				PERSISTENCE_UNIT);
		container.getEntityProvider().setLazyLoadingDelegate(
				new HibernateLazyLoadingDelegate());
		FieldFactoryHandler factoryHandler = new FieldFactoryHandler();
		factoryHandler.addFactory(entityClass,
				new DefaultFieldGroupFieldFactory());
		return new AnnotatedBeanFormDefinition(container, factoryHandler,
				BeanFace.LayoutType.HORIZONTAL, true);
	}
}
