package yuzhou.gits.http;

import yuzhou.gits.http.ParserConfig;
import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.request.HttpRequestException;

public interface HTTPRequestMessageParser {
	
	public void init(ParserConfig config) throws HttpRequestException;
	public void destroy() throws HttpRequestException;
	
	public void parse(ReqMsgParserContext parseCtx) throws HttpRequestException;
	public void end(ReqMsgParserContext parseCtx) throws HttpRequestException;
}
