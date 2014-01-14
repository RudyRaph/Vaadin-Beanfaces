package test.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding;
import rds.vaadin.beanfaces.impl.introspection.annotations.UIBeanBinding;
import rds.vaadin.beanfaces.impl.introspection.annotations.UIFieldBinding.MODES;

@SuppressWarnings("serial")
@Entity
@UIBeanBinding(description = "<div style='color: grey;font-family: sans-serif;font-size:13px;margin-top:10px'>Formulaire d'édition "
		+ "des propriétés d'un document sortants<br />(Map des prop simples + association manyToOne)</div>")
public class Document implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@UIFieldBinding(label = "Code du document", mode = MODES.WRITE)
	@NotNull(message = "Le code du document ne peut être null ni vide.")
	@Size(min = 5)
	private String cdeDocNom;

	@UIFieldBinding(label = "Modèle du document", mode = MODES.WRITE)
	@NotNull(message = "Le code du Model ne peut être null ni vide.")
	@ManyToOne
	@JoinColumn(name = "idMod")
	private Model idMod;

	@UIFieldBinding(label = "Type du document", hiddenColumnInTable = true)
	@NotNull(message = "Le Type du document ne peut être null ni vide.")
	@ManyToOne
	@JoinColumn(name = "idtype")
	private Type type;

	@UIFieldBinding(label = "Visible en production", booleanConvertedInTable = true)
	private boolean visProd;

	@UIFieldBinding(label = "Document complexe", hiddenColumnInTable = true, booleanConvertedInTable = true)
	private boolean complex;

	@UIFieldBinding(label = "Questions de perso", hiddenColumnInTable = true, booleanConvertedInTable = true)
	private boolean qstPerso;

	@UIFieldBinding(label = "Perso sensible", hiddenColumnInTable = true, booleanConvertedInTable = true)
	private boolean qstPersoSensible;

	@UIFieldBinding(label = "Paragraphes du document", hiddenInTable = true)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "document")
	private Set<Paragraphe> paragraphes;

	@UIFieldBinding(label = "Canaux du document", hiddenInTable = true)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "documentcanal", joinColumns = { @JoinColumn(name = "iddoc", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "idcanal", nullable = false, updatable = false) })
	private Set<Canal> canals = new HashSet<Canal>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Model getIdMod() {
		return idMod;
	}

	public void setIdMod(Model idMod) {
		this.idMod = idMod;
	}

	public String getCdeDocNom() {
		return cdeDocNom;
	}

	public void setCdeDocNom(String cdeDocNom) {
		this.cdeDocNom = cdeDocNom;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isVisProd() {
		return visProd;
	}

	public void setVisProd(boolean visProd) {
		this.visProd = visProd;
	}

	public boolean isComplex() {
		return complex;
	}

	public void setComplex(boolean complex) {
		this.complex = complex;
	}

	public boolean isQstPerso() {
		return qstPerso;
	}

	public void setQstPerso(boolean qstPerso) {
		this.qstPerso = qstPerso;
	}

	public boolean isQstPersoSensible() {
		return qstPersoSensible;
	}

	public void setQstPersoSensible(boolean qstPersoSensible) {
		this.qstPersoSensible = qstPersoSensible;
	}

	public Set<Canal> getCanals() {
		return canals;
	}

	public void setCanals(Set<Canal> canals) {
		this.canals = canals;
	}

	public Set<Paragraphe> getParagraphes() {
		return paragraphes;
	}

	public void setParagraphes(Set<Paragraphe> paragraphes) {
		this.paragraphes = paragraphes;
	}
}
