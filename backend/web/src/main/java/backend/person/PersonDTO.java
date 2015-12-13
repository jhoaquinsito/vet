package backend.person;

import java.math.BigDecimal;
import java.math.BigInteger;

import backend.person.city.CityDTO;
import backend.person.iva_category.IVACategoryDTO;

public class PersonDTO  {

	private Long iId;
	private String iName;
	private String iLastName;
	private String iAddress;
	private String iEmail;	
	private BigInteger iPhone;
	private BigInteger iMobilePhone;
	private BigDecimal iRENSPA;	
	private BigDecimal iPositiveBalance;
	private String iZipCode;
	private CityDTO iCity;
	private IVACategoryDTO iIVACategory;
	
	public Long getId() {
		return iId;
	}
	public void setId(Long pId) {
		this.iId = pId;
	}
	public String getName() {
		return iName;
	}
	public void setName(String pName) {
		this.iName = pName;
	}
	public String getLastName() {
		return iLastName;
	}
	public void setLastName(String pLastName) {
		this.iLastName = pLastName;
	}
	public String getAddress() {
		return iAddress;
	}
	public void setAddress(String pAddress) {
		this.iAddress = pAddress;
	}
	public String getEmail() {
		return iEmail;
	}
	public void setEmail(String pEmail) {
		this.iEmail = pEmail;
	}
	public BigInteger getPhone() {
		return iPhone;
	}
	public void setPhone(BigInteger pPhone) {
		this.iPhone = pPhone;
	}
	public BigInteger getMobilePhone() {
		return iMobilePhone;
	}
	public void setMobilePhone(BigInteger pMobilePhone) {
		this.iMobilePhone = pMobilePhone;
	}
	public BigDecimal getRENSPA() {
		return iRENSPA;
	}
	public void setRENSPA(BigDecimal pRENSPA) {
		this.iRENSPA = pRENSPA;
	}
	public BigDecimal getPositiveBalance() {
		return iPositiveBalance;
	}
	public void setPositiveBalance(BigDecimal pPositiveBalance) {
		this.iPositiveBalance = pPositiveBalance;
	}
	public String getZipCode() {
		return iZipCode;
	}
	public void setZipCode(String pZipCode) {
		this.iZipCode = pZipCode;
	}
	public CityDTO getCity() {
		return iCity;
	}
	public void setCity(CityDTO pCity) {
		this.iCity = pCity;
	}
	public IVACategoryDTO getIVACategory() {
		return iIVACategory;
	}
	public void setIVACategory(IVACategoryDTO pIVACategory) {
		this.iIVACategory = pIVACategory;
	}

}