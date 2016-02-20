package backend.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

public class ExceptionUtils {

	public static String getDataIntegrityViolationExceptionCause (DataIntegrityViolationException pException){
		
		String message = "";
		
		ConstraintViolationException mConstraintViolationException = (ConstraintViolationException) pException.getCause();
		if(mConstraintViolationException == null)
			return message;
		
		//La CAUSE de un ConstraintViolationException usualmente será un (PSQLException), pero por motivos de seguridad, hasta no tener 
		//claros todos los posibles casos, usaremos Throwable.
		
		Throwable mCause 			= mConstraintViolationException.getCause();
		if(mCause == null)
			return message;
		
		message = mCause.getMessage();
		
		return message;
		
	}
	
	
}
