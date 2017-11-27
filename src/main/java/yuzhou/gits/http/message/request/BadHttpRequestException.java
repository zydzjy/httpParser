package yuzhou.gits.http.message.request;

public class BadHttpRequestException extends HttpRequestException {
	
	/**
	 * 
	 */
	public BadHttpRequestException(){
		super(ERROR_CODE_BAD_REQUEST);
	}
	private static final long serialVersionUID = 1L;
	public final static String ERROR_CODE_BAD_REQUEST = "badRequest";
}
