package backend.product;

import java.util.List;

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

		// ItemRepository mItemRepo = gContext.getBean(ItemRepository.class);

		Product prod1 = new Product();
		prod1.setName("nombre de test");

		prod1 = gProdRepo.save(prod1);

		return "Que comience el juego......" + prod1.toString();
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
