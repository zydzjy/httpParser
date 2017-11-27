package yuzhou.gits.http.impl;

import yuzhou.gits.http.HttpReqMsgParserState;
import yuzhou.gits.http.impl.ParseEvent;
import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.request.BodyParser;
import yuzhou.gits.http.message.request.HttpRequestException;

public class BodyParseState implements HttpReqMsgParserState {
	long len = 0;
 
	protected BodyParser parser;
	
	
	public BodyParser getParser() {
		return parser;
	}

	public void setParser(BodyParser parser) {
		this.parser = parser;
	}

	public void parse(ReqMsgParserContext ctx) throws HttpRequestException {
		this.parser.parse(ctx);
	}

	public void enter(ReqMsgParserContext ctx) throws HttpRequestException {
		
	}

	public void exit(ReqMsgParserContext ctx) throws HttpRequestException {
		ParseEvent<Object> event = new ParseEvent<Object>(ctx,null);
		try {
			ctx.notifyBodyEndEvent(event);
		} catch (Exception e) {
			throw new HttpRequestException(e);
		}finally{
			ctx.getParser().end(ctx);
			ctx.setNextState(null);
		}
	}
}