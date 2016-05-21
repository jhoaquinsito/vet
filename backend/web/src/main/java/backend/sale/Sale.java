package backend.sale;

import java.util.Date;
import java.util.HashSet;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import backend.person.Person;
import backend.product.Product;
import backend.saleline.SaleLine;

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
	private boolean iPaied_out ;
	
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name="person")
	private Person iPerson;
	  
	@OneToMany(mappedBy="iSale", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER, orphanRemoval=true)
	@Valid
	private Set<SaleLine> iSaleLines;
	
	
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

	public boolean isPaied_out() {
		return iPaied_out;
	}

	public void setPaied_out(boolean iPaied_out) {
		this.iPaied_out = iPaied_out;
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


	public Set<Product> getProducts(){
		Set<Product> bProducts = new HashSet<Product>(); 
		for(SaleLine bSaleLine : getSaleLines())
		{
			bProducts.add(bSaleLine.getBatch().getProduct());
		}
		return bProducts;
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}	

}
