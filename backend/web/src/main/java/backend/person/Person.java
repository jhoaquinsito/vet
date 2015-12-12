package backend.person;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.*;

import backend.person.city.City;
import backend.person.iva_category.IVACategory;

// TODO documentar

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "person", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class Person  {

	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es la que usa postgresql por defecto
	@SequenceGenerator(name = "person_id_seq", sequenceName = "person_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_seq")
	private Long iId;

	@Column(name = "name")
	@Size(min=1, max=30, message= PersonConsts.cNAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = PersonConsts.cNAME_NOTNULL_VIOLATION_MESSAGE)
	private String iName;
	
	@Column(name = "lastname")
	@Size(min=1, max=30, message= PersonConsts.cNAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = PersonConsts.cNAME_NOTNULL_VIOLATION_MESSAGE)
	private String iLastName;

	@Column(name = "address")
	@Size(min=1, max=100, message= PersonConsts.cADDRESS_SIZE_VIOLATION_MESSAGE)
	private String iAddress;

	@Column(name = "email")
	@Size(min=1, max=30, message= PersonConsts.cEMAIL_SIZE_VIOLATION_MESSAGE)
	private String iEmail;	

	@Column(name = "phone")
	private Integer iPhone;

	@Column(name = "mobile_phone")
	private Integer iMobilePhone;

	@Column(name="renspa")
	@Digits(integer=17, fraction=0, message= PersonConsts.cRENSPA_DIGITS_VIOLATION_MESSAGE)
	private BigDecimal iRENSPA;	

	@Column(name="positive_balance")
	@Digits(integer=13, fraction=4, message= PersonConsts.cPOSITIVE_BALANCE_DIGITS_VIOLATION_MESSAGE)
	private BigDecimal iPositiveBalance;	

	@ManyToOne(cascade = {CascadeType.MERGE})//, CascadeType.PERSIST
    @JoinColumn(name="city")
	private City iCity;

	@ManyToOne(cascade = {CascadeType.MERGE})//, CascadeType.PERSIST
    @JoinColumn(name="IVA_category")
	private IVACategory iIVACategory;

	@Column(name="active")
	private Boolean iActive;
	
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
	
	public String getLastName() {
		return iLastName;
	}

	public void setLastName(String iLastName) {
		this.iLastName = iLastName;
	}

	public String getAddress() {
		return iAddress;
	}

	public void setAddress(String iAddress) {
		this.iAddress = iAddress;
	}

	public String getEmail() {
		return iEmail;
	}

	public void setEmail(String iEmail) {
		this.iEmail = iEmail;
	}

	public Integer getPhone() {
		return iPhone;
	}

	public void setPhone(Integer iPhone) {
		this.iPhone = iPhone;
	}

	public Integer getMobilePhone() {
		return iMobilePhone;
	}

	public void setMobilePhone(Integer iMobilePhone) {
		this.iMobilePhone = iMobilePhone;
	}

	public BigDecimal getRENSPA() {
		return iRENSPA;
	}

	public void setRENSPA(BigDecimal iRENSPA) {
		this.iRENSPA = iRENSPA;
	}

	public BigDecimal getPositiveBalance() {
		return iPositiveBalance;
	}

	public void setPositiveBalance(BigDecimal iPositiveBalance) {
		this.iPositiveBalance = iPositiveBalance;
	}

	public City getCity() {
		return iCity;
	}

	public void setCity(City iCity) {
		this.iCity = iCity;
	}

	public IVACategory getIVACategory() {
		return iIVACategory;
	}

	public void setIVACategory(IVACategory iIVACategory) {
		this.iIVACategory = iIVACategory;
	}
	
	public Boolean isActive() {
		return iActive;
	}

	public void setActive(Boolean pActive) {
		this.iActive = pActive;
	}

}
