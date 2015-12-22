package backend.person.children.legal_person;

import java.math.BigDecimal;
import java.util.Set;

import backend.person.PersonDTO;
import backend.product.ProductDTO;

public class LegalPersonDTO extends PersonDTO  {
	
	private BigDecimal iCUIT;
	
	private Set<ProductDTO> iProducts;
	
	private Boolean iClient;
	
	public BigDecimal getCUIT() {
		return iCUIT;
	}
	public void setCUIT(BigDecimal pCUIT) {
		this.iCUIT = pCUIT;
	}
	public Set<ProductDTO> getProducts() {
		return iProducts;
	}
	public void setProducts(Set<ProductDTO> pProducts) {
		this.iProducts = pProducts;
	}
	
	public Boolean getClient() {
		return iClient;
	}

	public void setClient(Boolean pClient) {
		this.iClient = pClient;
	}
	
}
