package backend.person.settlement;

public class SettlementConsts {
	
	public static final String cDATE_NOTNULL_VIOLATION_MESSAGE = "El pago debe contener una fecha de realización.";
	public static final String cAMOUNT_NOTNULL_VIOLATION_MESSAGE = "El pago debe contener un monto.";
	public static final String cAMOUNT_SIZE_VIOLATION_MESSAGE = "El monto del pago debe ser mayor o igual a 0 (cero).";
	public static final String cCONCEPT_SIZE_VIOLATION_MESSAGE = "El concepto del pago no puede contener mas de 250 (doscientos cincuenta) caracteres.";
	public static final String cCHEK_NUMBER_SIZE_VIOLATION_MESSAGE = "El número de cheque no puede contener mas de 8 (ocho) dígitos.";

}
