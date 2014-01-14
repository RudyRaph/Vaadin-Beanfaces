package test.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding;

@SuppressWarnings("serial")
@Entity
public class Model implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idMod;

	@UIFieldBinding(label = "Code modèle")
	private String cde;

	@UIFieldBinding(label = "Libellé du modèle")
	private String label;

	@OneToMany(mappedBy = "idMod", fetch = FetchType.LAZY)
	private Set<Document> documents;

	public Long getId() {
		return idMod;
	}

	public void setId(Long id) {
		this.idMod = id;
	}

	public String getCde() {
		return cde;
	}

	public void setCde(String cde) {
		this.cde = cde;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return label + " [" + cde + "]";
	}
}
