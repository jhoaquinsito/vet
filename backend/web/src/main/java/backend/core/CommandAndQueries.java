package backend.core;

import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.ProductService;
import backend.utils.CustomMapper;

/**
 * <code>CommandAndQueries</code> representa el conjunto de comandos y consultas posibles de realizar al dominio.
 * Los comandos y consultas son utilizados a nivel de presentación (RestController) y esta clase se encarga
 * de convertir los DTO recibidos en objetos de dominio, implementar el comando o la consulta deseada utilizando
 * objetos del dominio y luego convertir la respuesta a DTO para responder a la capa de presentación (si
 * es necesario).
 * 
 * El conjunto de comandos y consultas tiene asociado:
 * un mappeador de objetos (DTO vs dominio, o viceversa): <strong>Mapper</strong>.
 * 
 * @author tomas
 *
 */
public class CommandAndQueries {
	// TODO implementar un mapeador mejor (librería quizás)
	private CustomMapper iMapper;
	
	
	/**
	 * Constructor.
	 */
	public CommandAndQueries() {
		super();
		this.iMapper = new CustomMapper();
	}
	
	/**
	 * Este método es un comando que permite guardar un producto.
	 * 
	 * @param pProductDTO producto a guardar
	 * @return identificador del producto guardado
	 */
	public Long saveProduct(ProductDTO pProductDTO) {
		
		ProductService mProdSer = new ProductService();
		
		// map dto to domain object
		Product mProduct = this.iMapper.productDTOToProduct(pProductDTO);
		
		mProduct = mProdSer.save(mProduct);
		
		return mProduct.getId();
	}

}
