package backend.report.person_balance;

import java.math.BigDecimal;

public class PersonBalanceDTO {

	private String iPersonName;
	private BigDecimal iDebtTotal;
	private BigDecimal iCreditTotal;

	public String getPersonName(){
		return iPersonName;
	}
	public BigDecimal getDebtTotal(){
		return iDebtTotal;
	}
	public BigDecimal getCreditTotal(){
		return iCreditTotal;
	}

	public void setPersonName(String pPersonName){
		this.iPersonName = pPersonName;
	}
	public void setDebtTotal(BigDecimal pDebtTotal){
		this.iDebtTotal = pDebtTotal;
	}
	public void setCreditTotal(BigDecimal pCreditTotal){
		this.iCreditTotal = pCreditTotal;
	}

}
