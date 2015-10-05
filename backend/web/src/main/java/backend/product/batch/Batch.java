package backend.product.batch;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.http.HttpStatus;

import backend.exception.BusinessException;
import backend.product.Product;

/**
 * Un <code>Batch</code> es una representación de un lote.
 * Un lote tiene: un <strong>Id</strong>, una fecha de vencimiento: <strong>DueDate</strong>,
 *  una fecha de vencimiento en formato Iso: <strong>IsoDueDate</strong>,
 *  una cantidad en stock: <strong>Stock</strong>.
 * @author tomas
 *
 */
@Entity
@Table(name = "batch", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
public class Batch {

	private static final String cDUEDATE_PARSING_EXCEPTION = "Error al intentar obtener una fecha de vencimiento a partir de la fecha de vencimiento en formato ISO.";



	public Batch() {
		super();
	}

	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es
	// la que usa postgresql por defecto
	@SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
	private Long iId;

	@Column(name = "stock")
	private BigDecimal iStock;

	@Column(name = "iso_due_date")
	private Integer iIsoDueDate;

	@ManyToOne()
	@JoinColumn(name = "product", nullable = false)
	private Product iProduct;



	public BigDecimal getStock() {
		return iStock;
	}

	public void setStock(BigDecimal iStock) {
		this.iStock = iStock;
	}

	public Integer getIsoDueDate() {
		return iIsoDueDate;
	}

	public void setIsoDueDate(Integer iIsoDueDate) {
		this.iIsoDueDate = iIsoDueDate;
	}

	public Long getId() {
		return iId;
	}
	
	public void setId(Long pId) {
		this.iId = pId;
	}
	
	/**
	 * Método que devuelve la fecha en formato Date a partir de la fecha ISO del lote.
	 * @return fecha en formato Date
	 * @throws BusinessException error al parsear la fecha
	 */
	public Date getDueDate() throws BusinessException{
		
		// iso due date tiene el formato yyyymmdd
		SimpleDateFormat mDateFormatter = new SimpleDateFormat("yyyyMMdd");

		Date mDueDate;

		try {
			// uso el formatter para parsear la fecha en formato ISO (si hay un error, lanza una excepción)
			mDueDate = mDateFormatter.parse(this.iIsoDueDate.toString());
		} catch (ParseException e) {
			// TODO mejorar el uso de excepciones, no debería porque aclarar la clase y el método, tampoco el HTTP status acá
			// TODO la excepción BusinessException debería encapsular la otra excepción ParseException
			throw new BusinessException(Batch.class.toString(), "getDueDate", Batch.cDUEDATE_PARSING_EXCEPTION, "nose", HttpStatus.CONFLICT);
		}
		
		return mDueDate;
	}

	public void setProduct(Product pProduct) {
		this.iProduct = pProduct;
	}

}