package test.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding;
import rds.vaadin.beanfaces.impl.introspection.annotations.UIBeanBinding;

@SuppressWarnings("serial")
@Entity
@UIBeanBinding(description = "<div style='color: grey;font-family: sans-serif;font-size:13px;margin-top:10px'>Canaux "
		+ "d'impression du document<br />(Map des association manyToMany)</div>")
public class Canal implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@UIFieldBinding(label = "Code du canal")
	private String cdecanal;

	@UIFieldBinding(label = "Libellé du canal")
	private String label;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "canals")
	private Set<Document> documents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCdecanal() {
		return cdecanal;
	}

	public void setCdecanal(String cdecanal) {
		this.cdecanal = cdecanal;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}
}
