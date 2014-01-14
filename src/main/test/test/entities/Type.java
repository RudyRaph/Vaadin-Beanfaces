package test.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class Type implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idtype;

	private String cdeType;

	private String label;

	@OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
	private Set<Document> documents;

	public Long getId() {
		return idtype;
	}

	public void setId(Long id) {
		this.idtype = id;
	}

	public String getCdeType() {
		return cdeType;
	}

	public void setCdeType(String cdeType) {
		this.cdeType = cdeType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
