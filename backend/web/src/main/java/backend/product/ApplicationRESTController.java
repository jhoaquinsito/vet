package backend.product;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
			//throw new ProductNotFoundException(id);
		}
		return product;
	}
	
	@RequestMapping(value = "product/product", method = RequestMethod.POST)
	public String insertProduct(@RequestBody Product product)  {
		return product.getName();
	}

	
	
//	@ExceptionHandler(ProductNotFoundException.class)
//	public ResponseEntity<MyError> businessExceptionHandler(ProductNotFoundException e) {
//		/*
//		 * BusinessExcepcion : Clase 
//		 */
//		
//		long spittleId = e.getProductId();
//		MyError error = new MyError(4, "Product [" + spittleId + "] not found");
//		return new ResponseEntity<MyError>(error, HttpStatus.NOT_FOUND);
//	}
}
