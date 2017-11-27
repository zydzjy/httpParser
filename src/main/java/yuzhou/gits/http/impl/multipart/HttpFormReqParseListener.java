package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.http.HttpRequestParseListener;
import yuzhou.gits.http.ParseEvent;

public interface HttpFormReqParseListener extends HttpRequestParseListener{
	
	public <T> void formDataRead(ParseEvent<T> event) throws Exception;
	
}
