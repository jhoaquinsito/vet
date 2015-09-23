package backend.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import backend.product.presentation.PresentationDTO;

/**
 * Este <code>Controlador</code> es el encargado de recibir los request desde la
 * interfaz, procesarlos, y devolver la respuesta al cliente.
 * 
 * @author genesis
 *
 */
@RestController
@RequestMapping("/")
public class ApplicationRESTController {

	/**
	 * Metodo API que permite crear un Product.
	 * 
	 * @param ProductDTO
	 *            : datos del producto a crear
	 * @return Long : Identificador del nuevo producto en la BD.
	 * @throws BusinessException 
	 */
	@RequestMapping(value = "product", method = RequestMethod.POST)
	public Long createProduct(@RequestBody ProductDTO product) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		Long mId = mCNQ.saveProduct(product);

		return mId;
	}

	/**
	 * Metodo API que permite recuperar la lista de los distintos Product.
	 * 
	 * @return Lista de productos.
	 */
	@RequestMapping(value = "product", method = RequestMethod.GET)
	public @ResponseBody List<Product> listProducts() {
		throw new UnsupportedOperationException("La operación que intentaste realizar aún no está implementada."); 
	}

	/**
	 * Metodo API que permite recuperar un Product especificando su ID
	 * 
	 * @param id
	 *            : Identificador de la entidad buscada.
	 * @return Product : producto buscado.
	 * @throws Exception
	 *             : Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "product/{id}", method = RequestMethod.GET)
	public Product getProductById(@PathVariable int id) throws Exception {
		throw new UnsupportedOperationException("La operación que intentaste realizar aún no está implementada.");
	}
	
	/**
	 * Método que permite recuperar la lista completa de Presentaciones.
	 * @return Lista de presentaciones.
	 */
	@RequestMapping(value = "presentation", method = RequestMethod.GET)
	public List<PresentationDTO> listPresentations() {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<PresentationDTO> mPresentationDTOList = mCNQ.getPresentations();

		return mPresentationDTOList;
	}

	/**
	 * Este metodo es un "error handling method", uno que permite manejar las
	 * excepciones que se producen en los distintos metodos del controllador.
	 * 
	 * Solamente maneja las excepciones cuyo tipo son: BusinessException
	 * 
	 * Los parametros de entrada a este metodo llegan de forma automatica.
	 * 
	 * @param request
	 *            : Este parametro contiene la informacion del request generado
	 *            desde el cliente Ejemplo: request.getRequestURL() devuelve la
	 *            URL del servicio consumido ej ("www.genesis.com/product/1")
	 * @param ex
	 *            : Este parametro contiene la excepcion que se genero durante
	 *            la ejecucion de un metodo en el controlador. Tiene informacion
	 *            sobre la clase, metodo, y detalles de la excepcion.
	 * @return ResponseEntity<ExceptionJSONInfo> El ResponseEntity contiene la
	 *         informacion del codigo HTTP retornado al cliente (EJ: 404, 500,
	 *         etc.) Tambien contiene la informacion de la clase T que envuelve,
	 *         en este caso ExceptionJSONInfo En conclusion, el cliente recibe
	 *         no solamente el codigo de error, sino tambien detalles gracias a
	 *         la entidad envuelta.
	 */
	@ExceptionHandler(BusinessException.class)
	public @ResponseBody ResponseEntity<BusinessExceptionDTO> handleBusinessException(HttpServletRequest request,
			BusinessException ex) {

		// Creamos el objeto json que sera el que viaje al cliente.
		BusinessExceptionDTO exceptionDTO = new BusinessExceptionDTO();
		exceptionDTO.setUrl(request.getRequestURL().toString());
		exceptionDTO.setiDetail(ex.getExMessage());
		exceptionDTO.setMessage(ex.getFriendlyMessage());
		exceptionDTO.setiDetail(ex.getExMessage());

		// Obtengo el StackTrace para pasarlo como String.
		StringWriter stackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(stackTrace));
		exceptionDTO.setStackTrace(stackTrace.toString());

		ResponseEntity<BusinessExceptionDTO> response = new ResponseEntity<BusinessExceptionDTO>(exceptionDTO,
				ex.getiStatusCode());

		return response;
	}

}
