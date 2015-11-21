package backend.person;

import java.math.BigDecimal;

import backend.person.city.CityDTO;
import backend.person.iva_category.IVACategoryDTO;

public class PersonDTO  {

	private Long iId;
	private String iName;
	private String iAddress;
	private String iEmail;	
	private Integer iPhone;
	private Integer iMobilePhone;
	private BigDecimal iRENSPA;	
	private BigDecimal iPositiveBalance;	
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
	public Integer getPhone() {
		return iPhone;
	}
	public void setPhone(Integer pPhone) {
		this.iPhone = pPhone;
	}
	public Integer getMobilePhone() {
		return iMobilePhone;
	}
	public void setMobilePhone(Integer pMobilePhone) {
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