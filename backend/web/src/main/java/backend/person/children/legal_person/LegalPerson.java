package backend.person.children.legal_person;

import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

import backend.person.Person;


/**
 * Un <code>LegalPerson</code> es una representación de una persona legal. 
 * Una persona legal tiene: 
 * un <strong>CUIT (único)</strong>, 
 * una lista de <strong>Product</strong>
 * y todos los atritubos que hereda de la clase <code>Person</code>
 * @author gonzalo
 *
 */
@Entity
@PrimaryKeyJoinColumn(name="person_id")
@Table(name = "legal_person", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class LegalPerson extends Person {

	@Column(name="cuit", unique=true)
	@NotNull(message = LegalPersonConsts.cCUIT_NOTNULL_VIOLATION_MESSAGE)
	@Digits(integer=11, fraction=0, message= LegalPersonConsts.cCUIT_DIGITS_VIOLATION_MESSAGE)
	@Valid
	private BigDecimal iCUIT; 
	
	@Column(name="client", nullable=true)
	private Boolean iClient;
	
	public BigDecimal getCUIT() {
		return iCUIT;
	}

	public void setCUIT(BigDecimal pCUIT) {
		this.iCUIT = pCUIT;
	}
	
	public Boolean getClient() {
		return iClient;
	}

	public void setClient(Boolean pClient) {
		this.iClient = pClient;
	}
	
	/**
	 * Determina si una persona jurídica es cliente.
	 * Se pueden dar 2 casos: que sea solo cliente y que sea cliente y preveedor.
	 */
	public Boolean isClient() {
		return (iClient == null) || iClient;
	}
	
	/**
	 * Determina si una persona jurídica es proveedor.
	 * Se pueden dar 2 casos: que sea solo proveedor y que sea cliente y preveedor.
	 */
	public Boolean isSupplier() {
		return (iClient == null) || !iClient;
	}
	
	/**
	 * Determina si una persona jurídica es cliente y proveedor a la vez.
	 */
	public Boolean isClientAndSupplier() {
		return (iClient == null);
	}
}
