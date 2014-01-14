package test;

import rds.vaadin.beanfaces.Displayable;
import rds.vaadin.beanfaces.BeanFace;
import rds.vaadin.beanfaces.definitions.FormDefinition;
import rds.vaadin.beanfaces.impl.introspection.DefaultBeanIntrospector;
import test.entities.Canal;
import test.entities.Document;
import test.entities.Paragraphe;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.annotations.Theme;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("reindeer")
@SuppressWarnings("serial")
public class BeanViewUI extends UI {

	@Override
	protected void init(VaadinRequest request) {

		// Build definition
		final FormDefinition formDef = FormDefinitionFactory
				.buildWithAssociations(Document.class);

		// Add custom field factory for Paragraphe
		formDef.getFactoryHandler().addFactory(Paragraphe.class,
				new FieldFactory());

		// Build form
		final BeanFace form = new BeanFace(formDef);

		// Create item
		EntityItem<Document> item = formDef.<Document> getContainer()
				.createEntityItem(new Document());

		// Set datasource to form
		form.setItemDataSource(item);

		// ///////////////////////////// Layout and controls
		// ////////////////////////

		final HorizontalLayout rootLayout = new HorizontalLayout();
		final VerticalLayout controlsLayout = new VerticalLayout();
		controlsLayout.setMargin(true);
		controlsLayout.setSpacing(true);
		controlsLayout.setWidth("400px");
		final Button modeInsert = new Button("Mode insert");
		modeInsert.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				form.setMode(Displayable.MODE.INSERT);
			}
		});
		controlsLayout.addComponent(modeInsert);

		final Button modeUpdate = new Button("Mode update");
		modeUpdate.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				form.setMode(Displayable.MODE.UPDATE);
			}
		});
		controlsLayout.addComponent(modeUpdate);

		final Button modeReadonly = new Button("Mode readonly");
		modeReadonly.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				form.setMode(Displayable.MODE.READONLY);
			}
		});
		controlsLayout.addComponent(modeReadonly);

		Button commit = new Button("Commit");
		controlsLayout.addComponent(commit);

		final Button addAssociatedEntity = new Button("Ajouter un paragraphe");
		addAssociatedEntity.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Table table = (Table) form.getField("paragraphes");
				JPAContainer<Paragraphe> container = (JPAContainer<Paragraphe>) table
						.getContainerDataSource();
				form.addEntity("paragraphes", container
						.createEntityItem(new Paragraphe("La loi des séries",
								"Il était une fois...")));
			}
		});
		addAssociatedEntity.setEnabled(false);
		controlsLayout.addComponent(addAssociatedEntity);

		final Button removeAssociatedEntity = new Button(
				"Supprimer le paragraphe sélectionné");
		removeAssociatedEntity.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Table table = (Table) form.getField("paragraphes");
				JPAContainer<Paragraphe> container = (JPAContainer<Paragraphe>) table
						.getContainerDataSource();
				Object itemId = ((Table) form.getField("paragraphes"))
						.getValue();
				if (itemId != null) {
					form.removeEntity("paragraphes", itemId);
				}
			}
		});
		removeAssociatedEntity.setEnabled(false);
		controlsLayout.addComponent(removeAssociatedEntity);

		commit.addClickListener(new ClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					if (form.isValid()) {
						form.commit();
						Object id = formDef.getContainer().addEntity(
								((EntityItem<Document>) form
										.getItemDataSource()).getEntity());
						EntityItem<Document> it = formDef
								.<Document> getContainer().getItem(id);
						form.setItemDataSource(it);
						Notification.show("Les données sont sauvegardées");
						addAssociatedEntity.setEnabled(true);
						removeAssociatedEntity.setEnabled(true);
					}
				} catch (CommitException e) {
					Notification.show(e.getMessage());
					e.printStackTrace();
				}
			}
		});

		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		formLayout.setWidth("400px");
		if (DefaultBeanIntrospector.get(Document.class).findFormDescription() != null) {
			formLayout.addComponent(new Label(DefaultBeanIntrospector.get(
					Document.class).findFormDescription(), ContentMode.HTML));
		}
		formLayout.addComponent(form.getLayout());

		if (DefaultBeanIntrospector.get(Canal.class).findFormDescription() != null) {
			formLayout.addComponent(new Label(DefaultBeanIntrospector.get(
					Canal.class).findFormDescription(), ContentMode.HTML));
		}
		form.getField("canals").setHeight("130px");
		formLayout.addComponent(form.getField("canals"));

		if (DefaultBeanIntrospector.get(Paragraphe.class).findFormDescription() != null) {
			formLayout.addComponent(new Label(DefaultBeanIntrospector.get(
					Paragraphe.class).findFormDescription(), ContentMode.HTML));
		}
		form.getField("paragraphes").setHeight("100px");
		formLayout.addComponent(form.getField("paragraphes"));

		rootLayout.addComponent(controlsLayout);
		rootLayout.addComponent(formLayout);

		setContent(rootLayout);
	}
}

@SuppressWarnings("serial")
class FieldFactory extends DefaultFieldGroupFieldFactory {
	int counter = 0;

	@Override
	public <T extends Field> T createField(Class<?> dataType, Class<T> fieldType) {
		Field<?> field = null;
		if (counter == 1) {
			field = new RichTextArea();
			field.setWidth("100%");
			field.setHeight("170px");
		} else {
			field = super.createField(dataType, fieldType);
		}
		counter++;
		return (T) field;
	}
}