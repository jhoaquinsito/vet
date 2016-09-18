package backend.form_of_sale;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;


@SuppressWarnings("serial")
@Entity
@Table(name = "form_of_sale", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class FormOfSale {
	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es la que usa postgresql por defecto
	@SequenceGenerator(name="form_of_sale_id_seq", sequenceName="form_of_sale_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="form_of_sale_id_seq")
	private Long iId;

	@Column(name = "description")
	@Size(min=1, max=100, message= FormOfSaleConsts.cNAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = FormOfSaleConsts.cNAME_NOTNULL_VIOLATION_MESSAGE)
	private String iDescription;
	
	public Long getId() {
		return iId;
	}

	public void setId(Long iId) {
		this.iId = iId;
	}

	public String getDescription() {
		return iDescription;
	}

	public void setDescription(String iDescription) {
		this.iDescription = iDescription;
	}
}
