package backend.person;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import backend.person.city.CityDTO;
import backend.person.iva_category.IVACategoryDTO;
import backend.person.settlement.Settlement;
import backend.person.settlement.SettlementDTO;
import backend.sale.SaleDTO;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PersonDTO  {

	private Long iId;
	private String iName;
	private String iAddress;
	private String iEmail;	
	private String iPhone;
	private String iMobilePhone;
	private String iRENSPA;	
	private String iZipCode;
	private CityDTO iCity;
	private IVACategoryDTO iIVACategory;
	private Boolean iActive;
	private Set<SettlementDTO> iSettlements;
	
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
	public String getPhone() {
		return iPhone;
	}
	public void setPhone(String pPhone) {
		this.iPhone = pPhone;
	}
	public String getMobilePhone() {
		return iMobilePhone;
	}
	public void setMobilePhone(String pMobilePhone) {
		this.iMobilePhone = pMobilePhone;
	}
	public String getRENSPA() {
		return iRENSPA;
	}
	public void setRENSPA(String pRENSPA) {
		this.iRENSPA = pRENSPA;
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
	public void setActive(Boolean pActive) {
		this.iActive = pActive;
	}
	public Boolean getActive() {
		return iActive;
	}
	public Set<SettlementDTO> getSettlements() {
		return iSettlements;
	}
	public void setSettlements(Set<SettlementDTO> pSettlements) {
		this.iSettlements = pSettlements;
	}

}