package backend.exception;

/**
 * Un <code>ErrorDTO</code> es una clase de tipo DTO, que sirve para almacenar
 * la informacion pertinente a una excepcion generada en alguna de las distintas capas de la arquitectura.
 * <br>
 * Su mision es almacenar y transmitir esta informacion al cliente, quien debera debidamente mostrarla.
 * <br><br>
 * Un <code>ExceptionJSONInfo</code> contiene:
 * <strong>Message</strong>, el mensaje de tipo user-friendly para informar al usuario final.<br>
 * <strong>StackTrace</strong>, el <code>stackTrace</code> (opcional) que se comunica. Este es opcional
 * 								pues solo estara completo en el caso de excepciones no controladas.<br>
 * 
 * @author genesis
 *
 */
public class ErrorDTO {

    private String iMessage;
    private String iStackTrace;
    
    public String getMessage() {
        return iMessage;
    }
    public void setMessage(String pMessage) {
        this.iMessage = pMessage;
    }
	public String getStackTrace() {
		return iStackTrace;
	}
	public void setStackTrace(String iStackTrace) {
		this.iStackTrace = iStackTrace;
	}
}