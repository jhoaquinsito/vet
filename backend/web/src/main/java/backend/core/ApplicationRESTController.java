package backend.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import backend.exception.BusinessException;
import backend.exception.BusinessExceptionDTO;
import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.ProductRepository;
import backend.product.category.CategoryDTO;
import backend.product.drug.DrugDTO;
import backend.product.manufacturer.ManufacturerDTO;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.product.presentation.PresentationDTO;

/**
 * Este <code>Controlador</code> es el encargado de recibir los request
 * desde la interfaz, procesarlos, y devolver la respuesta al cliente. 
 * @author genesis
 *
 */

@RestController
@RequestMapping("/")
public class ApplicationRESTController {

	@Autowired
	ProductRepository gProdRepo;

	@RequestMapping(value = "home",method = RequestMethod.GET)
	public String home() {

		ProductDTO prod1 = new ProductDTO();
		prod1.setName("Producto1 nombre");
		prod1.setDescription("Descripción 1");
		prod1.setCost(new BigDecimal("1"));
		prod1.setDeletedOn(Timestamp.valueOf(LocalDateTime.now()));
		prod1.setLastUpdateOn(Timestamp.valueOf(LocalDateTime.now()));
		prod1.setLastUpdateUser("current user");
		prod1.setMinimumStock(new BigDecimal("1"));
		prod1.setUnitPrice(new BigDecimal("1"));
		prod1.setUtility(new BigDecimal("1"));
		
		ManufacturerDTO mManufacturer = new ManufacturerDTO();
		mManufacturer.setName("manufacturer1 name");
		prod1.setManufacturer(mManufacturer);
		
		MeasureUnitDTO mMeasureUnit = new MeasureUnitDTO();
		mMeasureUnit.setName("measure unit1 name");
		prod1.setMeasureUnit(mMeasureUnit);
		
		PresentationDTO mPresentation = new PresentationDTO();
		mPresentation.setName("presentation1 name");
		prod1.setPresentation(mPresentation);
		
		CategoryDTO mCategory = new CategoryDTO();
		mCategory.setName("category name");
		prod1.setCategory(mCategory);

		Set<DrugDTO> drugs = new HashSet<DrugDTO>();
		DrugDTO droga1 = new DrugDTO();
		droga1.setName("droga1-prod.creation-1");
		DrugDTO droga2 = new DrugDTO();
		droga2.setName("droga2-prod.creation-1");
		DrugDTO droga3 = new DrugDTO();
		droga3.setName("droga3-prod.creation-1");
		drugs.add(droga1);
		drugs.add(droga2);
		drugs.add(droga3);
		prod1.setDrugs(drugs);
		
		CommandAndQueries mCNQ = new CommandAndQueries();
		Long mId = mCNQ.saveProduct(prod1);

		return prod1.toString() + "--------> ID =" + mId.toString();
	}
	
	/**
	 * Metodo API que permite recuperar la lista de los distintos Product
	 * @return Lista de productos.
	 */
	@RequestMapping(value = "product/",method = RequestMethod.GET)
	public @ResponseBody List<Product> getList() {
		
		List<Product> list = (List<Product>) gProdRepo.findAll();
		
		return list;
	}
	
	/**
	 * Metodo API que permite recuperar un Product especificando su ID
	 * @param id : Identificador de la entidad buscada.
	 * @return Product : producto buscado.
	 * @throws Exception : Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "product/{id}", method = RequestMethod.GET)
	public Product getProductById(@PathVariable int id) throws Exception {
		Product product = gProdRepo.findOne((long) id);
		if (product == null) {
			throw new BusinessException("Product","getProductById","Entidad no encontrada", "url",HttpStatus.NOT_FOUND);
		}
		return product;
	}
	
	
	/**
	 * Metodo API que permite recuperar un Product especificando su ID
	 * @param product : Producto especificado para guardar en la BD.
	 * @return long   : Identificador del nuevo producto en la BD.
	 */
	@RequestMapping(value = "product/product", method = RequestMethod.POST)
	public long insertProduct(@RequestBody Product product)  {
		return gProdRepo.save(product).getId();
	}
	
	
	/**
	 * Este metodo es un "error handling method", uno que permite manejar las excepciones
	 * que se producen en los distintos metodos del controllador.
	 * 
	 * Solamente maneja las excepciones cuyo tipo son: BusinessException
	 * 
	 * Los parametros de entrada a este metodo llegan de forma automatica.
	 * 
	 * @param request : Este parametro contiene la informacion del request generado desde el cliente
	 * 					Ejemplo: request.getRequestURL() devuelve la URL del servicio consumido ej ("www.genesis.com/product/1")
	 * @param ex	  : Este parametro contiene la excepcion que se genero durante la ejecucion de un metodo 
	 *  				en el controlador. Tiene informacion sobre la clase, metodo, y detalles de la excepcion.
	 * @return ResponseEntity<ExceptionJSONInfo>
	 * 					El ResponseEntity contiene la informacion del codigo HTTP retornado al cliente (EJ: 404, 500, etc.)
	 * 					Tambien contiene la informacion de la clase T que envuelve, en este caso ExceptionJSONInfo
	 * 					En conclusion, el cliente recibe no solamente el codigo de error, sino tambien detalles gracias a la
	 * 					entidad envuelta.
	 */
	@ExceptionHandler(BusinessException.class)
	public @ResponseBody ResponseEntity<BusinessExceptionDTO> handleBusinessException(HttpServletRequest request, BusinessException ex){
		

		//Creamos el objeto json que sera el que viaje al cliente.
	    BusinessExceptionDTO exceptionDTO = new BusinessExceptionDTO();
	    exceptionDTO.setUrl(request.getRequestURL().toString());
	    exceptionDTO.setiDetail(ex.getExMessage());
	    exceptionDTO.setMessage(ex.getFriendlyMessage());
	    exceptionDTO.setiDetail(ex.getExMessage());
	    
	    //Obtengo el StackTrace para pasarlo como String.
		    StringWriter stackTrace = new StringWriter();
			ex.printStackTrace(new PrintWriter(stackTrace));
	    exceptionDTO.setStackTrace(stackTrace.toString());
		
	    ResponseEntity<BusinessExceptionDTO> response = new ResponseEntity<BusinessExceptionDTO>(exceptionDTO,ex.getiStatusCode());
	    
	    return response;
	}
	

}
