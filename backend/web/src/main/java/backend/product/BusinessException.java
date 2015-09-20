package backend.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Employee Not Found") //404
public class BusinessException extends Exception{
	private static final long serialVersionUID = -3332292346834265371L;
	 
	public String iClassName;
	public String iMethodName;
	public String iExMessage;
	public String iRequestUrl;
	public HttpStatus iStatusCode;
	
    public BusinessException(String p, int id ){
        super("NotFoundException with Entity: "+ p + ", id="+id);
    }

    
    
    
	/**
	 * @param iClassName
	 * @param iMethodName
	 * @param iExMessage
	 * @param iRequestUrl
	 */
	public BusinessException(String pClassName, String pMethodName, String pExMessage, String pRequestUrl, HttpStatus pStatusCode) {
		super("CLASS: " + pClassName + " - METHOD: " + pMethodName +" - EX MESSAGE: " + pExMessage  );
		
		this.iClassName 	= pClassName;
		this.iMethodName 	= pMethodName;
		this.iExMessage 	= pExMessage;
		this.iRequestUrl 	= pRequestUrl;
		this.iStatusCode	= pStatusCode;
		
	}




	public String getClassName() {
		return iClassName;
	}


	public void setClassName(String pClassName) {
		this.iClassName = pClassName;
	}


	public String getMethodName() {
		return iMethodName;
	}


	public void setMethodName(String pMethodName) {
		this.iMethodName = pMethodName;
	}


	public String getExMessage() {
		return iExMessage;
	}


	public void setExMessage(String pExMessage) {
		this.iExMessage = pExMessage;
	}


	public String getRequestUrl() {
		return iRequestUrl;
	}


	public void setRequestUrl(String pRequestUrl) {
		this.iRequestUrl = pRequestUrl;
	}




	public HttpStatus getiStatusCode() {
		return iStatusCode;
	}




	public void setiStatusCode(HttpStatus iStatusCode) {
		this.iStatusCode = iStatusCode;
	}
}
