package backend.person;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

import backend.person.city.City;
import backend.person.iva_category.IVACategory;
import backend.person.settlement.Settlement;

// TODO documentar

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "person", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class Person  {

	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es la que usa postgresql por defecto
	@SequenceGenerator(name = "person_id_seq", sequenceName = "person_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_seq")
	private Long iId;

	@Column(name = "name")
	@Size(min=1, max=30, message= PersonConsts.cNAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = PersonConsts.cNAME_NOTNULL_VIOLATION_MESSAGE)
	private String iName;

	@Column(name = "address")
	@Size(min=1, max=100, message= PersonConsts.cADDRESS_SIZE_VIOLATION_MESSAGE)
	private String iAddress;

	@Column(name = "email")
	@Size(min=1, max=30, message= PersonConsts.cEMAIL_SIZE_VIOLATION_MESSAGE)
	private String iEmail;	

	@Column(name = "phone")
	@Size(max=50, message= PersonConsts.cPHONE_SIZE_VIOLATION_MESSAGE)
	private String iPhone;

	@Column(name = "mobile_phone")
	@Size(max=50, message= PersonConsts.cMOBILE_PHONE_SIZE_VIOLATION_MESSAGE)
	private String iMobilePhone;

	@Column(name="renspa")
	@Size(max=25, message= PersonConsts.cRENSPA_SIZE_VIOLATION_MESSAGE)
	private String iRENSPA;	
	
	@Column(name="zip_code")
	@Size(min=1, max=30, message= PersonConsts.cZIP_CODE_SIZE_VIOLATION_MESSAGE)
	private String iZipCode;

	@ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name="city")
	private City iCity;

	@ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name="IVA_category")
	private IVACategory iIVACategory;

	@Column(name="active", nullable = true)
	private Boolean iActive;
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="person")
	@Valid
	private Set<Settlement> iSettlements;
	
	public Long getId() {
		return iId;
	}

	public void setId(Long iId) {
		this.iId = iId;
	}

	public String getName() {
		return iName;
	}

	public void setName(String iName) {
		this.iName = iName;
	}

	public String getAddress() {
		return iAddress;
	}

	public void setAddress(String iAddress) {
		this.iAddress = iAddress;
	}

	public String getEmail() {
		return iEmail;
	}

	public void setEmail(String iEmail) {
		this.iEmail = iEmail;
	}

	public String getPhone() {
		return iPhone;
	}

	public void setPhone(String iPhone) {
		this.iPhone = iPhone;
	}

	public String getMobilePhone() {
		return iMobilePhone;
	}

	public void setMobilePhone(String iMobilePhone) {
		this.iMobilePhone = iMobilePhone;
	}

	public String getRENSPA() {
		return iRENSPA;
	}

	public void setRENSPA(String iRENSPA) {
		this.iRENSPA = iRENSPA;
	}
	
	public String getZipCode() {
		return iZipCode;
	}

	public void setZipCode(String iZipCode) {
		this.iZipCode = iZipCode;
	}

	public City getCity() {
		return iCity;
	}

	public void setCity(City iCity) {
		this.iCity = iCity;
	}

	public IVACategory getIVACategory() {
		return iIVACategory;
	}

	public void setIVACategory(IVACategory iIVACategory) {
		this.iIVACategory = iIVACategory;
	}
	
	public Boolean isActive() {
		return iActive;
	}
	
	public Boolean getActive() {
		return iActive;
	}
	
	public void setActive(Boolean pActive) {
		this.iActive = pActive;
	}

	public Set<Settlement> getSettlements() {
		return iSettlements;
	}

	public void setSettlements(Set<Settlement> pSettlements) {
		this.iSettlements = pSettlements;
	}
	
	/**
	 * Devuleve los pagos no descontados de la persona.
	 * @return mUndiscountedSettlements
	 */
	public List<Settlement> getUndiscountedSettlements() {
		
		List<Settlement> mUndiscountedSettlements = new ArrayList<Settlement>();
		
		if(this.getSettlements() != null){
			Iterator<Settlement> mSettlements = this.getSettlements().iterator();
			
			while(mSettlements.hasNext()){
				Settlement bSettlement =  mSettlements.next();
				if(!bSettlement.isDiscounted()){
					mUndiscountedSettlements.add(bSettlement);	
				}
			}
		}
		
		return mUndiscountedSettlements;
	}
	
	//==================== Métodos de dominio =========================
	
	/**
	 * Devuelve el total de los pagos no descontados realizados por el cliente.
	 * @return mTotal es el total de los pagos no descontados.
	 */
	public BigDecimal totalPaid(){
		BigDecimal mTotal = BigDecimal.ZERO;
		
		Iterator<Settlement> mSettlements = this.getUndiscountedSettlements().iterator();
		
		while(mSettlements.hasNext()){
			Settlement bSettlement =  mSettlements.next();
			mTotal = mTotal.add(bSettlement.getAmount());
		}
		
		return mTotal;
	}
	
	/**
	 * Marca todos los pagos del cliente como descontados.	
	 */
	public void discountSettlements(){
		Iterator<Settlement> mSettlements = this.getUndiscountedSettlements().iterator();
		
		while(mSettlements.hasNext()){
			Settlement bSettlement =  mSettlements.next();
			bSettlement.setDiscounted(true);;
		}
	}
	
	/**
	 * Agrega un Settlement a la lista de Settlements del objeto.
	 * @param settlement
	 */
	public void addSettlement(Settlement pSettlement){
		
		Set<Settlement> mSettlements = this.getSettlements();
		mSettlements.add(pSettlement);
		this.setSettlements(mSettlements);
	}

	/**
	 * Este metodo permite devolver un Settlement que es el excedente del
	 * total pagado por el cliente (Lista de Settlement NO descontados) y la deuda actual del cliente
	 * transmitida como parámetro.
	 * @param clientDebt
	 * @return
	 */
	public Set<Settlement> getSettlementListWithSurplus(BigDecimal pClientDebt){
		
		BigDecimal clientDebtUpdated = pClientDebt;
		
		Set<Settlement> mSettlementsBalanced = new TreeSet<Settlement>();
		
		
		//0. Calculo el monto total excedente
		BigDecimal surplusPaid = this.totalPaid().subtract(pClientDebt);
		
		Settlement surplusSettlement = new Settlement();
		surplusSettlement.setAmount(surplusPaid);
		surplusSettlement.setDate(new Date());
		surplusSettlement.setCheckNumber("");
		surplusSettlement.setConcept("Pago creado por excedente.");
		surplusSettlement.setDiscounted(false);
		
		//1. Obtengo la lista de Settlements NO descontados.
		List<Settlement> mSettlements = new ArrayList<Settlement>();
		mSettlements.addAll(this.getSettlements());
		
		//2. Los ordeno por fecha (ASC)
		Collections.sort(mSettlements);
		
		Boolean debtIsPaid = false;
		
		for(Settlement mSettlement : mSettlements){
			if(mSettlement.isDiscounted()){
				mSettlementsBalanced.add(mSettlement);
				continue;
			}
			
			if(!debtIsPaid){
				switch (clientDebtUpdated.compareTo(mSettlement.getAmount())) {
					//El resto de la deuda es MENOR que el pago que tenemos para descontarle
					case -1:  {
						//ACCION: Le substraemos la deuda al pago, la deuda queda en 0, la marcamos como pagada, 
						//y el resto del pago es parte del monto del Settlement de excedente
						mSettlement.setAmount(clientDebtUpdated);
						clientDebtUpdated = BigDecimal.ZERO;
				    	debtIsPaid = true;
				    	//Se agrega el settlement dejando el monto igual a lo que le falta a la deuda para ser pagada.
				    	mSettlementsBalanced.add(mSettlement);
				    	continue;
					}
							 
					//El resto de la deuda es IGUAL que el pago que tenemos para descontarle
				    case 0:   {
				    	//ACCION: Le substraemos a la deuda el pago, marcamos la deuda como salda y continuamos
				    	clientDebtUpdated =  clientDebtUpdated.subtract(mSettlement.getAmount());
				    	debtIsPaid = true;
				    	//Se agrega el settlement SIN modificaciones a la lista definitiva.
				    	mSettlementsBalanced.add(mSettlement);
				    	continue;
					}
		            //El resto de la deuda es MAYOR que el pago que tenemos para descontarle
				    case 1:   {
				    	//ACCION: Le substraemos a la deuda, el pago y continuamos
				    	clientDebtUpdated =  clientDebtUpdated.subtract(mSettlement.getAmount());
				    	//Se agrega el settlement SIN modificaciones a la lista definitiva.
				    	mSettlementsBalanced.add(mSettlement);
						continue;
					}
				}
			}else{
				continue;
			}
			
			
			
		}
		// Finalmente agrego a la lista, el Settlement de balance, con el excedente.
		mSettlementsBalanced.add(surplusSettlement);
		
		return mSettlementsBalanced;
		
		
		
	}
	
	//=========================== Fin Métodos de dominio =====================
	
	/** 
	  * Antes de una inserción verifica si la persona no tiene estado definido
	  * y en tal caso le asigna true por defecto
	  **/
	@PrePersist
	protected
	 void preInsert() {
	   if ( this.getActive() == null ) { this.setActive(true); }
	}
	
	/** 
	  * Antes de una actualización verifica si la persona no tiene estado definido
	  * y en tal caso le asigna true por defecto
	  **/
	@PreUpdate
	protected
	 void onPreUpdate() {
		 if ( this.getActive() == null ) { this.setActive(true); }
	 }

}
