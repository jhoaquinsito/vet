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
