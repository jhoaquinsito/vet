package backend.person;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
				if(!bSettlement.isMarkedAsDiscounted()){
					mUndiscountedSettlements.add(bSettlement);	
				}
			}
		}
		
		return mUndiscountedSettlements;
	}
	
	/**
	 * Devuleve los pagos no descontados de la persona ordenados de forma
	 * ascendente
	 * @return mUndiscountedSettlements
	 */
	public List<Settlement> getUndiscountedSettlementsOrderByDateAsc() {
		
		List<Settlement> mUndiscountedSettlements = getUndiscountedSettlements();
		
		Collections.sort(mUndiscountedSettlements, new Comparator<Settlement>() {
		    public int compare(Settlement m1, Settlement m2) {
		        return m1.getDate().compareTo(m2.getDate());
		    }
		});
		
		return mUndiscountedSettlements;
	}
	
	
	
	//==================== Métodos de dominio =========================
	/***
	 * Este método permite actualizar el <discount> de un Settlement
	 * y actualizarlo internamente en la lista de pagos (Settlement) del cliente.
	 * 
	 * 
	 * @param pSettlementToUpdate:
	 * 								Settlement objetivo a actualizar (Comparación por ID)
	 * @param pNewDiscountValue
	 * 								Valor de <discount> a actualizar en el cliente
	 * 
	 * Resultado: Lista (Set) de pagos (Settlement) actualizada.
	 */
	public void UpdateSettlement(Settlement pSettlementToUpdate, BigDecimal pNewDiscountValue){
		System.out.println("*****UpdateSettlement******");
		//Obtengo lista original de Settlements. Servirá de guía para iterar y actualizar un (1) settlement
		Iterator<Settlement> mActualSettlements = this.getSettlements().iterator();
		
		//Lista auxiliar en donde iremos almacenando todos los settlements, con la particularidad que habrá uno actualizado.
		List<Settlement> mSettlementsUpdated = new ArrayList<Settlement>();
		
		while(mActualSettlements.hasNext()){
			Settlement bSettlement =  mActualSettlements.next();
			System.out.println("bSettlement " + bSettlement.getDiscounted().toString() );
			if(bSettlement.getId() == pSettlementToUpdate.getId()){
				bSettlement.setDiscounted(pNewDiscountValue);
				System.out.println("bSettlement " + bSettlement.getDiscounted().toString() + " (Updated)" );
			}	
			mSettlementsUpdated.add(bSettlement);
		}
		
		Set<Settlement> mSettlementsUpdatedSet = new HashSet<Settlement>(mSettlementsUpdated);
		
		this.setSettlements(mSettlementsUpdatedSet);
		System.out.println("*********************");
	}
	
	/**
	 * Este método se encarga de realizar la actualización masiva
	 * de Settlements ( NO DESCONTADOS ) del cliente, tomando en cuenta:
	 * - El monto total de su deuda
	 * - Los pagos no descontados (ordenados cronológicamente de forma ascendente)
	 * @param pTotalClientDebtAmount
	 */
	public void UpdateUndiscountedSettlements(BigDecimal pTotalClientDebtAmount){
		
		List<Settlement> mUndiscountedSettlements = this.getUndiscountedSettlementsOrderByDateAsc();
		for(Settlement mUndiscountedSettlement : mUndiscountedSettlements ){
			
			//Caso 1: Si el monto del pago no supera la deuda -> Se actualiza el pago: El atributo <discounted> pasa ser igual al monto del pago.
			// 		   Luego se resta a la deudda el monto del pago procesado (es decir: se va reduciendo la deuda)
			
			if(mUndiscountedSettlement.getAmount().compareTo(pTotalClientDebtAmount) <= 0){
				UpdateSettlement(mUndiscountedSettlement,mUndiscountedSettlement.getAmount());
				pTotalClientDebtAmount =  pTotalClientDebtAmount.subtract(mUndiscountedSettlement.getAmount());
			}
			//Caso 2: El monto del pago SUPERA la deuda -> Se actualiza el pago: El atributo <discounted> pasa ser igual al monto del pago (lo que restaba).
			//			Luego la deuda queda en cero (0) pues se termino de descontar (es decir: se ha cancelado completamente la deuda)
			else{
				UpdateSettlement(mUndiscountedSettlement,pTotalClientDebtAmount);
				pTotalClientDebtAmount = BigDecimal.ZERO;
			}
			
			if(pTotalClientDebtAmount.compareTo(BigDecimal.ZERO) == 0)
				return;
		}
		
	}
	
	/**
	 * Devuelve el total de los pagos no descontados realizados por el cliente.
	 * @return mTotal es el total de los pagos no descontados.
	 */
	public BigDecimal totalAmountOfUndiscountedSettlements(){
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
	public void markAsDiscountedAllSettlements(){
		Iterator<Settlement> mSettlements = this.getUndiscountedSettlements().iterator();
		
		while(mSettlements.hasNext()){
			Settlement bSettlement =  mSettlements.next();
			bSettlement.markAsDiscounted();
		}
	}
	
	/**
	 * Agrega un Settlement a la lista de Settlements del objeto.
	 * @param settlement
	 */
	public void addSettlement(Settlement pSettlement){
		
		// si el settlement no tiene fecha, entonces le asigno la fecha de hoy
		if(pSettlement.getDate() == null ) pSettlement.setDate(new Date());
		
		Set<Settlement> mSettlements = this.getSettlements();
		mSettlements.add(pSettlement);
		this.setSettlements(mSettlements);
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
