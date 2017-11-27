package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.http.HttpFormReqParseListener;
import yuzhou.gits.http.impl.ParseEvent;
import yuzhou.gits.http.message.request.HttpRequestException;

public interface MultipartFormDataParseListener extends HttpFormReqParseListener{
	
	public void formFileDataReadStart(ParseEvent<byte[]> event) throws HttpRequestException;
	public void formFileDataRead(ParseEvent<BytesChunk> event) throws HttpRequestException;
	public <T> void formFileDataReadEnd(ParseEvent<T> event) throws HttpRequestException;
}