package backend.person.children.legal_person;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import backend.person.Person;
import backend.product.Product;


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

	@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE})//, CascadeType.PERSIST
    @JoinTable(name="supplier", joinColumns=@JoinColumn(name="legal_person_id"), inverseJoinColumns=@JoinColumn(name="product_id"))
	@JsonManagedReference
	@JsonIgnore
	@Valid
	private Set<Product> iProducts; 
	
	
	public BigDecimal getCUIT() {
		return iCUIT;
	}

	public void setCUIT(BigDecimal pCUIT) {
		this.iCUIT = pCUIT;
	}

	@JsonManagedReference
	@JsonIgnore
	public Set<Product> getProducts() {
		return iProducts;
	}

	public void setProducts(Set<Product> pProducts) {
		this.iProducts = pProducts;
	}
	

}
