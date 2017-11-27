package yuzhou.gits.http.impl;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.commonUtils.BytesUtils;
import yuzhou.gits.http.HttpReqMsgParserState;
import yuzhou.gits.http.ParseEvent;
import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.SimpleReqMsgParser;
import yuzhou.gits.http.message.HttpCommonConstants;
import yuzhou.gits.http.message.request.HttpRequestException;
import yuzhou.gits.http.message.request.HttpRequestNoHandlerException;




/*
request-line   = method SP request-target SP HTTP-version CRLF
*/
public class ReqLineParseState implements HttpReqMsgParserState {
	
	protected BytesChunk method = null;
	protected BytesChunk uri = null;
	
	public void parse(ReqMsgParserContext ctx) throws HttpRequestException {
		try {
			BytesChunk data = ctx.readNextAvailable(HttpCommonConstants.CRLF);
			// extract method
			int methodEndIdx = BytesUtils.indexOf(data.getBytesBuf(), 0, HttpCommonConstants.SP) - 1;
			method = new BytesChunk(data.getBytesBuf(), 0, methodEndIdx);
			// extract uri
			int uriStartIdx = methodEndIdx + 2;// plus SP
			int uriEndIdx = BytesUtils.indexOf(data.getBytesBuf(), uriStartIdx, HttpCommonConstants.SP) - 1;
			uri = new BytesChunk(data.getBytesBuf(), uriStartIdx, uriEndIdx);
			this.exit(ctx);
			ctx.setCurrState(new HeaderParseState());
		} catch (Exception e) {
			HttpRequestException _e = new HttpRequestNoHandlerException();
			_e.setErrorCode(HttpRequestNoHandlerException.ERROR_CODE_NO_HANDLER_FOR_URI);
			throw _e;
		}
	}

	protected void notifyReqLineEndEvent(BytesChunk method, BytesChunk uri, ReqMsgParserContext ctx) throws Exception {
		BytesChunk[] eventData = { method, uri };
		ParseEvent<BytesChunk[]> event = new ParseEvent<BytesChunk[]>(ctx,eventData);
		event.setReqParseCxt(ctx);
		SimpleReqMsgParser parser = ctx.getParser();
		parser.notifyReqLineEndEvent(event);
	}
	
	public void enter(ReqMsgParserContext ctx) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}

	public void exit(ReqMsgParserContext ctx) throws HttpRequestException {
		try {
			this.notifyReqLineEndEvent(method, uri, ctx);
		} catch (Exception e) {
			throw new HttpRequestException(e);
		}
	}
}