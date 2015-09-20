package backend.utils;

import backend.product.Product;
import backend.product.ProductDTO;
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
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Un <code>OrikaMapperFactory</code> es una fabrica de mappers Orika. Orika es
 * una librería externa que tiene mappers para mappear entre objetos.
 * 
 * En esta clase se realiza la configuración de los mappings que se usarán en la
 * aplicación.
 * 
 * @author tomas
 *
 */
public class OrikaMapperFactory {

	/**
	 * Método genera un MapperFacade configurado con los diferentes mappings que
	 * se utilizarán en la aplicación.
	 * 
	 * @return MapperFacade configurado
	 */
	public static MapperFacade getMapperFacade() {

		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

		// PresentationDTO to Presentation
		mapperFactory.classMap(PresentationDTO.class, Presentation.class).byDefault().register();

		// CategoryDTO to Category
		mapperFactory.classMap(CategoryDTO.class, Category.class).byDefault().register();

		// DrugDTO to Drug
		mapperFactory.classMap(DrugDTO.class, Drug.class).byDefault().register();

		// ManufacturerDTO to Manufacturer
		mapperFactory.classMap(ManufacturerDTO.class, Manufacturer.class).byDefault().register();

		// MeasureUnitDTO to MeasureUnit
		mapperFactory.classMap(MeasureUnitDTO.class, MeasureUnit.class).byDefault().register();

		// ProductDTO to Product
		mapperFactory.classMap(ProductDTO.class, Product.class).byDefault().register();

		return mapperFactory.getMapperFacade();
	}

}
