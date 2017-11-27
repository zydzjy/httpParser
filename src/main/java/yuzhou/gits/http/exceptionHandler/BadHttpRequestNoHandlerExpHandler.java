package yuzhou.gits.http.exceptionHandler;

import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.request.HttpRequestException;
import yuzhou.gits.http.message.response.HttpResponseMessage;

public class BadHttpRequestNoHandlerExpHandler implements ExceptionHandler {

	public void handleException(ReqMsgParserContext context, HttpRequestException exception) throws Exception {
		//write a error response to client
		context.getParser()
			.writeAndFlushResponse(context, HttpResponseMessage.responseMsg_400);
	}
}