package backend.sale;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import backend.saleline.SaleLineForReportDTO;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SaleForReportDTO {

	private Long iId;

	private Date iDate;
	  
	private boolean iInvoiced ;

	private boolean iPaied_out;

	private Long iPersonId;
	
	private String iPersonName;

	private Long iPersonIVACategoryId;

	private BigDecimal iPersonIVACategoryPercentage;

	private Set<SaleLineForReportDTO> iSaleLines;
	
	public Long getId() {
		return iId;
	}

	public void setId(Long pId) {
		this.iId = pId;
	}

	public Date getDate() {
		return iDate;
	}

	public void setDate(Date pDate) {
		this.iDate = pDate;
	}

	public boolean isInvoiced() {
		return iInvoiced;
	}

	public void setInvoiced(boolean pInvoiced) {
		this.iInvoiced = pInvoiced;
	}

	public boolean isPaied_out() {
		return iPaied_out;
	}

	public void setPaied_out(boolean pPaied_out) {
		this.iPaied_out = pPaied_out;
	}

	public Set<SaleLineForReportDTO> getSaleLines() {
		return iSaleLines;
	}

	public void setSaleLines(Set<SaleLineForReportDTO> pSaleLines) {
		this.iSaleLines = pSaleLines;
	}

	public Long getPersonId() {
		return iPersonId;
	}

	public void setPersonId(Long pPersonId) {
		this.iPersonId = pPersonId;
	}

	public String getPersonName() {
		return iPersonName;
	}

	public void setPersonName(String pPersonName) {
		this.iPersonName = pPersonName;
	}

	public Long getPersonIVACategoryId() {
		return iPersonIVACategoryId;
	}

	public void setPersonIVACategoryId(Long pPersonIVACategoryId) {
		this.iPersonIVACategoryId = pPersonIVACategoryId;
	}

	public BigDecimal getPersonIVACategoryPercentage() {
		return iPersonIVACategoryPercentage;
	}

	public void setPersonIVACategoryPercentage(BigDecimal pPersonIVACategoryPercentage) {
		this.iPersonIVACategoryPercentage = pPersonIVACategoryPercentage;
	}
	
	
}
