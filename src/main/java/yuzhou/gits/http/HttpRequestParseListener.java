package yuzhou.gits.http;

import yuzhou.gits.http.message.request.HttpRequestException;
import yuzhou.gits.http.ParseEvent;

public interface HttpRequestParseListener {

	public void init(Object initParams) throws HttpRequestException;

	public void destory() throws HttpRequestException;

	public <T> void onReqLineStart(ParseEvent<T> e) throws HttpRequestException;
	public <T> void onReqLineEnd(ParseEvent<T> e) throws HttpRequestException;

	public <T> void onHeaderStart(ParseEvent<T> e) throws HttpRequestException;
	public <T> void onHeaderEnd(ParseEvent<T> e) throws HttpRequestException;

	public <T> void onBodyStart(ParseEvent<T> e) throws HttpRequestException;
	public <T> void onBodyEnd(ParseEvent<T> e) throws HttpRequestException;

}
