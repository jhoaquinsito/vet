package backend.core;

import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.ProductService;
import backend.product.category.Category;
import backend.product.category.CategoryDTO;
import backend.product.drug.Drug;
import backend.product.drug.DrugDTO;
import backend.product.manufacturer.Manufacturer;
import backend.product.manufacturer.ManufacturerDTO;
import backend.product.measure_unit.MeasureUnit;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.product.presentation.Presentation;
import backend.product.presentation.PresentationDTO;
import backend.utils.CustomMapper;
import backend.utils.OrikaMapperFactory;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

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
	// MapperFacade de Orika (librería para mapping)
	private MapperFacade iMapper; 
	
	/**
	 * Constructor.
	 */
	public CommandAndQueries() {
		super();
		// obtengo un mapper facade de la factory Orika
		this.iMapper = OrikaMapperFactory.getMapperFacade();
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
		Product mProduct = iMapper.map(pProductDTO, Product.class);
		
		mProduct = mProdSer.save(mProduct);
		
		return mProduct.getId();
	}

}
