package backend.exceptions;

public class ProductNotFoundException extends RuntimeException {
	private long productId;

	public ProductNotFoundException(long productId) {
	this.productId = productId;
	}

	public long getProductId() {
		return productId;
	}
}
