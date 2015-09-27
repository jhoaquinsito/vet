package backend.product.batch;

import java.math.BigDecimal;
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

	@Column(name = "due_date")
	private Date iDueDate;

	@Column(name = "stock")
	private BigDecimal iStock;

	@Column(name = "iso_due_date")
	private Integer iIsoDueDate;

	// TODO revisar si corresponden PERSIST and MERGE
	// TODO revisar porque debo usar EAGER acá
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "product", nullable = false)
	private Product iProduct;

	public Date getDueDate() {
		return iDueDate;
	}

	public void setDueDate(Date iDueDate) {
		this.iDueDate = iDueDate;
	}

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

}
