package yuzhou.gits.http.message.request;

import yuzhou.gits.http.ReqMsgParserContext;

public interface BodyParser {
 
	public void parse(ReqMsgParserContext ctx) throws HttpRequestException;
	
}
