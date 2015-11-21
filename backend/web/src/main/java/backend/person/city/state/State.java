package backend.person.city.state;

// TODO documentar
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "state", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
public class State {

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

	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es la que usa postgresql por defecto
	@SequenceGenerator(name = "state_id_seq", sequenceName = "state_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "state_id_seq")
	private Long iId;

	@Column(name = "name")
	@Size(min=1, max=100, message= StateConsts.cNAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = StateConsts.cNAME_NOTNULL_VIOLATION_MESSAGE)
	private String iName;

}
