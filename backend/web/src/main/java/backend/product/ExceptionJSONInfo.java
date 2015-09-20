package backend.product;

public class ExceptionJSONInfo {
	 
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
	public String getiStackTrace() {
		return iStackTrace;
	}
	public void setiStackTrace(String iStackTrace) {
		this.iStackTrace = iStackTrace;
	}
}