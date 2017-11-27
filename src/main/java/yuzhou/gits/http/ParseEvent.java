package yuzhou.gits.http;

import yuzhou.gits.http.ReqMsgParserContext;

public class ParseEvent<T> {

	protected T eventData;
	protected ReqMsgParserContext reqParseCxt;
	
	public ParseEvent(ReqMsgParserContext cxt,T eventData){
		this.reqParseCxt = cxt;
		this.eventData = eventData;
	}
	
	public T getEventData() {
		return eventData;
	}

	public void setEventData(T eventData) {
		this.eventData = eventData;
	}

	public ReqMsgParserContext getReqParseCxt() {
		return reqParseCxt;
	}

	public void setReqParseCxt(ReqMsgParserContext reqParseCxt) {
		this.reqParseCxt = reqParseCxt;
	}
}
