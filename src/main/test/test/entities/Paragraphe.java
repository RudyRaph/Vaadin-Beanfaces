package test.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding;
import rds.vaadin.beanfaces.impl.introspection.annotations.UIBeanBinding;

@SuppressWarnings("serial")
@UIBeanBinding(description = "<div style='color: grey;font-family: sans-serif;font-size:13px;margin-top:10px'>Liste "
		+ "des paragraphes personnalisables<br />(Map des association oneToMany)</div>")
@Entity
public class Paragraphe implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@UIFieldBinding(label = "Libellé du paragraphe", headerLabelInTable = "Libellé du paragraphe")
	private String label;

	@UIFieldBinding(label = "Contenu du paragraphe", headerLabelInTable = "Contenu du paragraphe")
	private String content;

	@ManyToOne()
	@JoinColumn(name = "document")
	private Document document;

	public Paragraphe() {
	}

	public Paragraphe(String label, String content) {
		super();
		this.label = label;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
