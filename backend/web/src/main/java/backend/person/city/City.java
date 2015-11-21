package backend.person.city;

// TODO documentar

import javax.persistence.*;
import javax.validation.constraints.*;

import backend.person.city.state.State;

@Entity
@Table(name = "city", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
public class City {

	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es la que usa postgresql por defecto
	@SequenceGenerator(name = "city_id_seq", sequenceName = "city_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_id_seq")
	private Long iId;

	@Column(name = "name")
	@Size(min=1, max=100, message= CityConsts.cNAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = CityConsts.cNAME_NOTNULL_VIOLATION_MESSAGE)
	private String iName;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="state")
	private State iState;

	public Long getId() {
		return iId;
	}

	public void setId(Long iId) {
		this.iId = iId;
	}

	public String getName() {
		return iName;
	}

	public void setName(String iName) {
		this.iName = iName;
	}

	public State getState() {
		return iState;
	}

	public void setState(State iState) {
		this.iState = iState;
	}

}