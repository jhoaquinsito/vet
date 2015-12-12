package backend.utils;

import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.batch.Batch;
import backend.product.batch.BatchDTO;
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
import backend.person.city.City;
import backend.person.city.CityDTO;
import backend.person.city.state.State;
import backend.person.city.state.StateDTO;
import backend.person.children.legal_person.LegalPerson;
import backend.person.children.legal_person.LegalPersonDTO;
import backend.person.children.natural_person.NaturalPerson;
import backend.person.children.natural_person.NaturalPersonDTO;
import backend.person.Person;
import backend.person.PersonDTO;
import backend.person.iva_category.IVACategory;
import backend.person.iva_category.IVACategoryDTO;
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
		
		// BacthDTO to BatchUnit
		mapperFactory.classMap(BatchDTO.class, Batch.class).byDefault().register();

		// ProductDTO to Product
		mapperFactory.classMap(ProductDTO.class, Product.class).byDefault().register();
		
		// StateDTO to State
		mapperFactory.classMap(StateDTO.class, State.class).byDefault().register();
		
		// CityDTO to City
		mapperFactory.classMap(CityDTO.class, City.class).byDefault().register();
		
		// IVACategoryDTO to IVACategory
		mapperFactory.classMap(IVACategoryDTO.class, IVACategory.class).byDefault().register();
		
		// PersonDTO to Person
		mapperFactory.classMap(PersonDTO.class, Person.class).byDefault().register();
		
		// LegalPersonDTO to LegalPerson
		mapperFactory.classMap(LegalPersonDTO.class, LegalPerson.class).use(PersonDTO.class, Person.class).byDefault().register();
		
		// RealPersonDTO to RealPerson
		mapperFactory.classMap(NaturalPersonDTO.class, NaturalPerson.class).use(PersonDTO.class, Person.class).byDefault().register();

		return mapperFactory.getMapperFacade();
	}

}
