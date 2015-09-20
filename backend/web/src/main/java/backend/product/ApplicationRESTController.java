package backend.product;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import backend.core.CommandAndQueries;
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
	
	
	@RequestMapping(value = "product/list",method = RequestMethod.GET)
	public @ResponseBody List<Product> getList() {
		
		List<Product> list = (List<Product>) gProdRepo.findAll();
		
		return list;
	}
	
	@RequestMapping(value = "product/first",method = RequestMethod.GET)
	public @ResponseBody Product getFirst() {
		
		
		List<Product> list = (List<Product>) gProdRepo.findAll();
		
		return list.get(0);
	}

	@RequestMapping(value = "product/{id}", method = RequestMethod.GET)
	public Product getProductById(@PathVariable int id) throws Exception {
		Product product = gProdRepo.findOne((long) id);
		if (product == null) {
			//BusinessException(String pClassName, String pMethodName, String pExMessage, String pRequestUrl)
			throw new BusinessException("Product","getProductById","Entidad no encontrada", "url",HttpStatus.NOT_FOUND);
		}
		return product;
	}
	
	@RequestMapping(value = "product/product", method = RequestMethod.POST)
	public String insertProduct(@RequestBody Product product)  {
		return product.getName();
	}
	
	@RequestMapping(value = "product/error/{id}", method = RequestMethod.POST)
	public Product errorProduct (@PathVariable int id) throws Exception
	{
		try {
			switch (id) {
			case 0:
			{
				List<Product> list = (List<Product>) gProdRepo.findAll();
				
				if(list == null || list.get(0) == null)
					throw new BusinessException("Product","getProductById","No hay entidades", "url",HttpStatus.FAILED_DEPENDENCY);
				return list.get(0);
			}
			case 1:  
				{
					//Hacemos una excepcion por Division de Zero
					int i = 0;
					i = -1/i;
			        break;
				}
			case 2:  
				{
					throw new BusinessException("Product","errorProduct","Porque seleccionaste el 2!!! ESTE NO ANDAAA KAPOW", "url",HttpStatus.NO_CONTENT);
					
				}
			}
			
		} catch (Exception e) {
			//Capturamos la excepcion Generica.
			throw new BusinessException("Product","errorProduct",e.getMessage(), "url",HttpStatus.BAD_GATEWAY);
		}
		return null;
		

	}
	
	@ExceptionHandler(BusinessException.class)
	public @ResponseBody ResponseEntity<ExceptionJSONInfo> handleBusinessException(HttpServletRequest request, BusinessException ex){
		
		// Obtenemos datos del Request. - Ahora no lo uso par anada.
		String requestURL 		= request.getRequestURL().toString();
		String exceptionMessage = ex.getExMessage();
		String className 		= ex.getClassName();
		String methodName		= ex.getMethodName();
		
		//Creamos el objeto json que sera el que viaje al cliente.
	    ExceptionJSONInfo exceptionDTO = new ExceptionJSONInfo();
	    exceptionDTO.setUrl(request.getRequestURL().toString());
	    exceptionDTO.setMessage(ex.getMessage());
	    exceptionDTO.setiStackTrace(ex.getStackTrace().toString());
	    exceptionDTO.setiDetail(ex.getExMessage());
	    ResponseEntity<ExceptionJSONInfo> response = new ResponseEntity<ExceptionJSONInfo>(exceptionDTO,ex.getiStatusCode());
	    
	    return response;
	}
	

}
