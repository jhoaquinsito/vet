package backend.product.batch;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	@SequenceGenerator(name = "batch_id_seq", sequenceName = "batch_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "batch_id_seq")
	private Long iId;

	@Column(name = "stock")
	@Size(min=0, message= BatchConsts.cSTOCK_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = BatchConsts.cSTOCK_NOTNULL_VIOLATION_MESSAGE)
	private BigDecimal iStock;

	@Column(name = "iso_due_date")
	@NotNull(message = BatchConsts.cISODUEDATE_NOTNULL_VIOLATION_MESSAGE)
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
		} catch (ParseException bParseException) {
			throw new BusinessException(Batch.cDUEDATE_PARSING_EXCEPTION, bParseException);
		}
		
		return mDueDate;
	}

	public void setProduct(Product pProduct) {
		this.iProduct = pProduct;
	}

}
