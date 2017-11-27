package yuzhou.gits.http.exceptionHandler;

import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.request.HttpRequestException;

public interface ExceptionHandler {
	
	public void handleException(ReqMsgParserContext context,
			HttpRequestException exception) throws Exception;
}
