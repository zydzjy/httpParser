package yuzhou.gits.http;

import yuzhou.gits.http.impl.ParseEvent;

public interface HttpFormReqParseListener extends HttpRequestParseListener{
	
	public <T> void formDataRead(ParseEvent<T> event) throws Exception;
	
}
