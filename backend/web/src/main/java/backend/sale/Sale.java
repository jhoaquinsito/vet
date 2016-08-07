package backend.sale;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import org.springframework.data.domain.Persistable;
import backend.person.Person;
import backend.person.settlement.Settlement;
import backend.product.Product;
import backend.saleline.SaleLine;
import backend.utils.DateHelper;

/**
* Una <code>Sale</code> es una representación de una Venta. 
* Una venta tiene: 
* un <strong>Id</strong>, 
* un <strong>date</strong>, 
* un boolean para identificar si está <strong>invoiced</strong>, 
* un boolean para identificar si está pago (<strong>paied_out</strong>), 
* un  <strong>person</strong>
*/
@Entity
@Table(name = "sale", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class Sale implements Persistable<Long>{


	private static final long serialVersionUID = 1L;


	@Id
	@Column(name="id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque es 
	// la que usa postgresql por defecto
    @SequenceGenerator(name="sale_id_seq", sequenceName="sale_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="sale_id_seq")
	private Long iId;


	@Column(name="date")
	private Date iDate;
	  

	@Column(name="invoiced")
	private boolean iInvoiced ;
	

	@Column(name="paied_out")
	private boolean iPaiedOut ;
	
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name="person")
	private Person iPerson;
	  
	@OneToMany(mappedBy="iSale", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER, orphanRemoval=true)
	@Valid
	private Set<SaleLine> iSaleLines;
	
	private String iPayForm;	
	
	public Long getId() {
		return iId;
	}

	public void setId(Long iId) {
		this.iId = iId;
	}

	public Date getDate() {
		return iDate;
	}

	public void setDate(Date iDate) {
		this.iDate = iDate;
	}

	public boolean isInvoiced() {
		return iInvoiced;
	}

	public void setInvoiced(boolean iInvoiced) {
		this.iInvoiced = iInvoiced;
	}

	public boolean isPaiedOut() {
		return iPaiedOut;
	}

	public void setPaiedOut(boolean pPaiedOut) {
		this.iPaiedOut = pPaiedOut;
	}

	public Person getPerson() {
		return iPerson;
	}

	public void setPerson(Person iPerson) {
		this.iPerson = iPerson;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "iSaleLineId.sale")
	public Set<SaleLine> getSaleLines() {
		return iSaleLines;
	}

	public void setSaleLines(Set<SaleLine> iSaleLines) {
		this.iSaleLines = iSaleLines;
	}


	public String getPayForm() {
		return iPayForm;
	}

	public void setPayForm(String pPayForm) {
		this.iPayForm = pPayForm;
	}

	public Set<Product> getProducts(){
		Set<Product> bProducts = new HashSet<Product>(); 
		for(SaleLine bSaleLine : getSaleLines())
		{
			bProducts.add(bSaleLine.getBatch().getProduct());
		}
		return bProducts;
	}
	
	/**
	 * Método interno a Sale para saber si la venta ha sido realizada 
	 * en EFECTIVO o bien en CUENTA CORRIENTE.
	 * 
	 * Se determina de la siguiente manera:
	 * - Una venta se considera pagada en efectivo, si y solo si, 
	 *   la venta tiene un Settlement (pago)
	 *   cuya fecha y monto coinciden. 
	 * - Una venta se considera pagada bajo Cuenta Corriente cuando sucede lo contrario 
	 *   (múltiples pagos, o pago diferido en fecha)
	 * @return
	 */
	public boolean isSalePaidedInCash(){
		
		Iterator<Settlement> bSettlementsIterator =  getPerson().getSettlements().iterator();
		
		while(bSettlementsIterator.hasNext()){
			Settlement bSettlement = bSettlementsIterator.next();
			
			Boolean isSameDate = DateHelper.isSameDateWithoutTime(getDate(),bSettlement.getDate());
			
			if(isSameDate ){
				Boolean isSameAmout = getSaleAmount().compareTo(bSettlement.getAmount()) == 0;
				if(isSameAmout)
				return true;
			}
		}
		return false;
	}

	/***
	 * Este método permite calcular y devolver el monto total de la venta
	 * sin considerar los precios actualizados.
	 * @return
	 */
	public BigDecimal getSaleAmount(){
		BigDecimal mDebt = BigDecimal.ZERO;
		
		Iterator<SaleLine> bSaleLinesIterator = getSaleLines().iterator();
		while(bSaleLinesIterator.hasNext()){//Por cada línea de venta
			SaleLine bSaleLine = bSaleLinesIterator.next();
					
			//Convierto la cantidad de la línea a Bigdecimal
			BigDecimal bProductQuantity = BigDecimal.valueOf(bSaleLine.getQuantity());
					
			//Obtengo el precio unitario actual del producto de la línea
			BigDecimal bProductPrice = bSaleLine.getUnit_Price();

					
			//Sumo a la deuda la cantidad del producto por su precio unitario
			mDebt = mDebt.add(bProductQuantity.multiply(bProductPrice));
			
			//Resto el descuento hecho en la saleline
			//Obtengo el valor BigDecimal de 100
			BigDecimal bBigDecimal100 = BigDecimal.valueOf(100);
			//Multiplico la cantidad del producto por el precio guardado en el saleline 
			BigDecimal bSaleLineTotal = bProductQuantity.multiply(bProductPrice);
			//Calculo el descuento como: (cantidad*precio*descuento)/100
			BigDecimal bDiscount = bSaleLineTotal.multiply(bSaleLine.getDiscount()).divide(bBigDecimal100); 
			//Resto el descuento
			mDebt = mDebt.subtract(bDiscount);
		}
		
		return mDebt;
	}
	
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}	

}
