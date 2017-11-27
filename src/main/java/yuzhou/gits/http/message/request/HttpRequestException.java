package yuzhou.gits.http.message.request;

public class HttpRequestException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public HttpRequestException() {
	}
	
	public HttpRequestException(Throwable e) {
		super(e);
	}

	public HttpRequestException(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public HttpRequestException(Throwable cause,String errorCode) {
		super(cause);
		setErrorCode(errorCode);
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	private String errorCode = "";

}
