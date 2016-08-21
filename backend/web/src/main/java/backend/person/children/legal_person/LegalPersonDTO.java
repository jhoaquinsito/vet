package backend.person.children.legal_person;

import backend.person.PersonDTO;

public class LegalPersonDTO extends PersonDTO  {
	
	private String iCUIT;	
	private Boolean iClient;
	
	public String getCUIT() {
		return iCUIT;
	}
	public void setCUIT(String pCUIT) {
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
