package backend.core;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

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
import backend.exception.ErrorDTO;
import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.category.CategoryDTO;
import backend.product.drug.DrugDTO;
import backend.product.manufacturer.ManufacturerDTO;
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
	 * @return ProductDTO : producto buscado.
	 * @throws BusinessException
	 *             el producto estaba eliminado lógicamente
	 * @throws Exception
	 *             : Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "product/{id}", method = RequestMethod.GET)
	public ProductDTO getProductById(@PathVariable Long id) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.getProduct(id);
	}

	/**
	 * Metodo que permite eliminar un producto a partir de su identificador. Al
	 * eliminar el producto, sus los lotes asociados son eliminados físicamente.
	 * 
	 * @param id
	 *            identificador del producto a eliminar
	 * @throws BusinessException
	 *             errores al intentar realizar la operación
	 */
	@RequestMapping(value = "product/{id}", method = RequestMethod.DELETE)
	public void deleteProduct(@PathVariable Long id) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		mCNQ.deleteProduct(id);
	}

	/**
	 * Método que permite recuperar la lista completa de Presentaciones.
	 * 
	 * @return Lista de presentaciones.
	 */
	@RequestMapping(value = "presentation", method = RequestMethod.GET)
	public List<PresentationDTO> listPresentations() {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<PresentationDTO> mPresentationDTOList = mCNQ.getPresentations();

		return mPresentationDTOList;
	}

	@RequestMapping(value = "category", method = RequestMethod.POST)
	public long saveCategory(@RequestBody CategoryDTO pCategory) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.saveCategory(pCategory);

	}

	

	@RequestMapping(value = "category", method = RequestMethod.GET)
	public List<CategoryDTO> listCategorys() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<CategoryDTO> mCategoryDTOList = mCNQ.getCategorys();

		return mCategoryDTOList;
	}

	@RequestMapping(value = "manufacturer", method = RequestMethod.POST)
	public long saveManufacturer(@RequestBody ManufacturerDTO pManufacturer) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.saveManufacturer(pManufacturer);

	}

	

	@RequestMapping(value = "manufacturer", method = RequestMethod.GET)
	public List<ManufacturerDTO> listManufacturers() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<ManufacturerDTO> mManufacturerDTOList = mCNQ.getManufacturers();

		return mManufacturerDTOList;
	}

	/**
	 * Método API que permite crear una <code>Presentation</code>
	 * 
	 * @param presentation
	 *            presentación a guardar
	 * @return identificador de la presentación guardada
	 * @throws BusinessException
	 *             errores de negocio producidos
	 */
	@RequestMapping(value = "presentation", method = RequestMethod.POST)
	public Long createPresentation(@RequestBody PresentationDTO presentation) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		Long mId = mCNQ.createPresentation(presentation);

		return mId;
	}

	/**
	 * Método que permite recuperar la lista completa de drogas.
	 * 
	 * @return Lista de drogas.
	 */
	@RequestMapping(value = "drug", method = RequestMethod.GET)
	public List<DrugDTO> listDrugs() {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<DrugDTO> mDrugDTOList = mCNQ.getDrugs();

		return mDrugDTOList;
	}

	/**
	 * Método API que permite crear una <code>Drug</code>
	 * 
	 * @param drug
	 *            droga a guardar
	 * @return identificador de la droga guardada
	 * @throws BusinessException
	 *             errores de negocio producidos
	 */
	@RequestMapping(value = "drug", method = RequestMethod.POST)
	public Long createDrug(@RequestBody DrugDTO drug) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		Long mId = mCNQ.createDrug(drug);

		return mId;
	}

	/**
	 * Método maneja las excepciones que se producen en el controlador.
	 * 
	 * Solamente maneja las excepciones cuyo tipo son: BusinessException
	 * 
	 * Los parámetros de entrada a este metodo llegan de forma automática.
	 * 
	 * @param mRequest
	 *            : Este parámetro contiene la información del request generado
	 *            desde el cliente. Ejemplo: request.getRequestURL() devuelve la
	 *            URL del servicio consumido. Ej.: "www.genesis.com/product/1"
	 * @param mBusinessException
	 *            : Este parámetro contiene la excepción que se generó durante
	 *            la ejecución de un método en el controlador. Tiene información
	 *            sobre la clase, método y detalles de la excepción.
	 * @return ResponseEntity<ErrorDTO> El ResponseEntity contiene la
	 *         información del código HTTP retornado al cliente (EJ: 404, 500,
	 *         etc.) También contiene la información de la clase T que envuelve,
	 *         en este caso ErrorDTO. En conclusion, el cliente recibe
	 *         no solamente el codigo de error, sino tambien detalles gracias a
	 *         la entidad envuelta.
	 */
	@ExceptionHandler(BusinessException.class)
	public @ResponseBody ResponseEntity<ErrorDTO> handleBusinessException(HttpServletRequest mRequest,
			BusinessException mBusinessException) {

		// Creamos el objeto DTO que sera el que viaje al cliente.
		ErrorDTO mErrorDTO = new ErrorDTO();
		mErrorDTO.setMessage(mBusinessException.getMessage());
		mErrorDTO.setStackTrace(mBusinessException.getStackTraceString());

		// creación de la respuesta
		ResponseEntity<ErrorDTO> mResponse = new ResponseEntity<ErrorDTO>(mErrorDTO, HttpStatus.CONFLICT);

		return mResponse;
	}

}
