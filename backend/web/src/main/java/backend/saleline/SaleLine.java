package backend.saleline;

import java.math.BigDecimal;

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
import javax.validation.Valid;
import backend.product.batch.Batch;
import backend.sale.Sale;

@Entity
@Table(name = "saleline")

/**
* Una <code>SaleLine</code> es una representación de un ítem
* procesado en una Venta ( <code>Sale</code> )
* Una venta tiene: 
* un <strong>Id</strong>, 
* un identificador para la venta asociada<strong>sale_id</strong>, 
* un double identificiando la cantidad total del producto involucrado: <strong>quantity</strong>, 
* un numeric representando, a la fecha, el precio del producto en ese momento: <strong>unit_price</strong>
*/
public class SaleLine implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque es 
	// la que usa postgresql por defecto
    @SequenceGenerator(name="saleline_id_seq", sequenceName="saleline_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="saleline_id_seq")
	private Long iId;

	@Column(name="quantity")
	private float iQuantity ;
	
	@Column(name="unit_price")
	private BigDecimal iUnit_Price;
	
	@Column(name="discount")
	private BigDecimal iDiscount;

	@ManyToOne//(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="sale_id")
	@Valid
	private Sale iSale;
	
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name="batch")
	@Valid
	private Batch iBatch;

	public Long getId() {
		return iId;
	}

	public void setId(Long pId) {
		this.iId = pId;
	}

	public float getQuantity() {
		return iQuantity;
	}

	public void setQuantity(float pQuantity) {
		this.iQuantity = pQuantity;
	}

	public BigDecimal getUnit_Price() {
		return iUnit_Price;
	}

	public void setUnit_Price(BigDecimal pUnit_Price) {
		this.iUnit_Price = pUnit_Price;
	}

	public BigDecimal getDiscount() {
		return iDiscount;
	}

	public void setDiscount(BigDecimal pDiscount) {
		this.iDiscount = pDiscount;
	}

	public Sale getSale() {
		return iSale;
	}

	public void setSale(Sale pSale) {
		this.iSale = pSale;
	}

	public Batch getBatch() {
		return iBatch;
	}

	public void setBatch(Batch pBatch) {
		this.iBatch = pBatch;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}
