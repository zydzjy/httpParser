package yuzhou.gits.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.http.exceptionHandler.ExceptionHandler;
import yuzhou.gits.http.impl.DefaultReqURIHandler;
import yuzhou.gits.http.impl.ParseEvent;
import yuzhou.gits.http.message.request.HttpRequestException;
import yuzhou.gits.http.message.response.HttpResponseMessage;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * https://tools.ietf.org/html/rfc7230#section-3
 HTTP-message   = start-line
									( header-field CRLF )
									CRLF
									[ message-body ]
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public class SimpleReqMsgParser implements HTTPRequestMessageParser {
	public static ExceptionHandler DEFAULT_EXCEPTION_HANDLER = null;
	public final static DefaultReqURIHandler DEFAULT_REQ_LINE_HANDLER = new DefaultReqURIHandler();

	protected boolean parse = true;

	public boolean isParse() {
		return parse;
	}

	public void setParse(boolean parse) {
		this.parse = parse;
	}

	// use a state machine to manage request message parse procedure
	public void parse(ReqMsgParserContext parseCtx) throws HttpRequestException {
		try {
			while(parseCtx.isParse()){
				HttpReqMsgParserState currState = parseCtx.getCurrState();
				currState.parse(parseCtx);
			}
		} catch (HttpRequestException e) {
			handleException(parseCtx, e);
		}
	}

	protected void handleException(ReqMsgParserContext parseCtx, HttpRequestException e) {
		// TODO: handle any exception
		e.printStackTrace();
		// find an exception handler for this exception
		ExceptionHandler expHandler = this.getGlobalExceptionHandler(e.getErrorCode());
		try {
			if (expHandler == null) {
				expHandler = DEFAULT_EXCEPTION_HANDLER;
			}
			expHandler.handleException(parseCtx, e);
		} catch (Exception e1) {
			e1.printStackTrace();
			// TODO:if exception can't handle properly??
		} finally {
			try {
				this.end(parseCtx);
			} catch (HttpRequestException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void writeAndFlushResponse(ReqMsgParserContext parseCtx, byte[] data) throws HttpRequestException {
		// TODO:avoiding reply repeatedly!
		if (parseCtx.requestResponsed) {
			throw new HttpRequestException("THIS REQUEST HAS BEEN REPLIED!");
		} else {
			parseCtx.setRequestResponsed(true);
		}
	}

	public void end(ReqMsgParserContext parseCtx) throws HttpRequestException {
		/*if (parseCtx.hasNextAvailable())
			throw new BadHttpRequestException();*/
		
		// TODO:end all
		parseCtx.setReadMuch(false);
		parseCtx.setCurrState(null);
		parseCtx.destroyAllListeners();
		
		if (!parseCtx.isRequestResponsed()) {
			try {
				parseCtx.getParser().writeAndFlushResponse(parseCtx, 
						HttpResponseMessage.responseMsg_dummy);
			} catch (Exception e) {
				throw new HttpRequestException(e);
			}
		}
		
		parseCtx = null;
		
	}

	public List<Object[]> getListenerClz(Object key) throws HttpRequestException {
		// return this.uriParseListenerMap.get(key);
		return this.config.getUrlParseHandlerMapper((String) key);
	}

	public List<HttpRequestParseListener> globalUriParseListeners = new ArrayList<HttpRequestParseListener>();

	public void addGlobalURIListener(HttpRequestParseListener listener) throws HttpRequestException {
		this.globalUriParseListeners.add(listener);
	}

	public void notifyReqLineEndEvent(ParseEvent<BytesChunk[]> event) throws Exception {
		BytesChunk[] eventData = event.getEventData();
		// BytesChunk method = (BytesChunk) eventData[0];
		BytesChunk uri = eventData[1];
		ReqMsgParserContext ctx = event.getReqParseCxt();
		ctx.setCurrURI(uri);
		// notify all global URI listeners
		Iterator<HttpRequestParseListener> it = globalUriParseListeners.iterator();
		while (it.hasNext()) {
			HttpRequestParseListener listener = it.next();
			listener.onReqLineEnd(event);
		}
		// use Default request line listener to new all listener instance
		DEFAULT_REQ_LINE_HANDLER.onReqLineEnd(event);
	}

	protected Map<String, ExceptionHandler> globalExpHandlerMap = new HashMap<String, ExceptionHandler>();

	public ExceptionHandler getGlobalExceptionHandler(String errorCode) {
		return this.globalExpHandlerMap.get(errorCode);
	}

	public void setGlobalExceptionHandler(String errorCode, ExceptionHandler expHandler) {
		this.globalExpHandlerMap.put(errorCode, expHandler);
	}

	ParserConfig config;

	public void init(ParserConfig config) throws HttpRequestException {
		this.config = config;

		SimpleReqMsgParser.DEFAULT_EXCEPTION_HANDLER = new ExceptionHandler() {
			public void handleException(ReqMsgParserContext context, HttpRequestException exception) throws Exception {
				// TODO:write an internal error message
				writeAndFlushResponse(context, HttpResponseMessage.responseMsg_internalError);
			}
		};
	}

	public void destroy() throws HttpRequestException {
		// TODO destroy all resources
		
	}
}