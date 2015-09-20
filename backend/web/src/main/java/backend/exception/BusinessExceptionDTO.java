package backend.exception;

/**
 * Un <code>ExceptionJSONInfo</code> es una clase de tipo DTO, que sirve para almacenar
 * la informacion pertinente a una excepcion generada en alguna de las distintas capas de la arquitectura.
 * <br>
 * Su mision es almacenar y transmitir esta informacion al cliente, quien debera debidamente mostrarla.
 * <br><br>
 * Un <code>ExceptionJSONInfo</code> contiene:
 * <strong>Url</strong>, la direccion a la API a la cual se hizo el request del lado del cliente.<br>
 * <strong>Message</strong>, el mensaje de tipo user-friendly para informar al usuario final.<br>
 * <strong>Detail</strong>, el detalle, de un nivel mas developer, para detallar al mismo que sucedio. 
 * 							El detalle contiene el formato CLASE - METODO - EXCEPCION <br>
 * <strong>StackTrace</strong>, el <code>stackTrace</code> (opcional) que se comunica. Este es opcional
 * 								pues solo estara completo en el caso de excepciones no controladas.<br>
 * 
 * @author genesis
 *
 */
public class BusinessExceptionDTO {
	 
    private String iUrl;
    private String iMessage;			//amigable
    private String iDetail;				//El no amigable.
    private String iStackTrace;
    
     
    public String getUrl() {
        return iUrl;
    }
    public void setUrl(String pUrl) {
        this.iUrl = pUrl;
    }
    public String getMessage() {
        return iMessage;
    }
    public void setMessage(String pMessage) {
        this.iMessage = pMessage;
    }
	public String getiDetail() {
		return iDetail;
	}
	public void setiDetail(String iDetail) {
		this.iDetail = iDetail;
	}
	public String getStackTrace() {
		return iStackTrace;
	}
	public void setStackTrace(String iStackTrace) {
		this.iStackTrace = iStackTrace;
	}
}