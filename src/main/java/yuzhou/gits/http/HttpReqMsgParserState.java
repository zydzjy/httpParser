package yuzhou.gits.http;


import yuzhou.gits.http.message.request.HttpRequestException;

public interface HttpReqMsgParserState {
	
	public void enter(ReqMsgParserContext ctx) throws HttpRequestException;
	public void parse(ReqMsgParserContext ctx) throws HttpRequestException;
	public void exit(ReqMsgParserContext ctx) throws HttpRequestException;
}