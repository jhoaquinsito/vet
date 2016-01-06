package backend.saleline;

import backend.product.ProductDTO;

public class SaleLineDTO {

	private Long iId;

	private float iQuantity ;

	private float iUnit_Price;
	
	private float iDiscount;

//	private SaleDTO iSale;
		
	private ProductDTO iProduct;
	
	public float getQuantity() {
		return iQuantity;
	}

	public void setQuantity(float pQuantity) {
		this.iQuantity = pQuantity;
	}

	public float getUnit_Price() {
		return iUnit_Price;
	}

	public void setUnit_Price(float pUnit_Price) {
		this.iUnit_Price = pUnit_Price;
	}

//	public SaleDTO getSale() {
//		return iSale;
//	}
//
//	public void setSale(SaleDTO pSale) {
//		this.iSale = pSale;
//	}

	public float getDiscount() {
		return iDiscount;
	}

	public void setDiscount(float pDiscount) {
		this.iDiscount = pDiscount;
	}

	public ProductDTO getProduct() {
		return iProduct;
	}

	public void setProduct(ProductDTO pProduct) {
		this.iProduct = pProduct;
	}

	public Long getId() {
		return iId;
	}

	public void setId(Long pId) {
		this.iId = pId;
	}
	
}
