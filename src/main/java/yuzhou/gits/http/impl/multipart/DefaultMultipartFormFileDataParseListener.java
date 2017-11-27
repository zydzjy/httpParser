package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.http.impl.ParseEvent;
import yuzhou.gits.http.message.request.HttpRequestException;

public class DefaultMultipartFormFileDataParseListener implements MultipartFormDataParseListener {

	private FormFileOutputStream outputStream;
	
	public FormFileOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(FormFileOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void formFileDataReadStart(ParseEvent<byte[]> event) throws HttpRequestException {
		// TODO:handle this event to set up file processor
		String fileName;
		try {
			byte[] eventData = event.getEventData();
			fileName = new String(eventData,"gbk");
			String configParams = fileName;
			this.outputStream.open(configParams);
		} catch (Exception e) {
			throw new HttpRequestException(e);
		}
	}

	public void formFileDataRead(ParseEvent<BytesChunk> event) throws HttpRequestException {
		try{
			/*byte[] dataRead = (byte[]) event.getEventData();
			this.outputStream.write(dataRead,0,dataRead.length);*/
			BytesChunk dataRead = (BytesChunk) event.getEventData();
			this.outputStream.write(dataRead.getBytesBuf(),dataRead.getStart(),dataRead.getLength());
		}catch(Exception e){
			throw new HttpRequestException(e);
		}
	}

	public <T> void formFileDataReadEnd(ParseEvent<T> event) throws HttpRequestException {
		try {
			this.outputStream.close();
		} catch (Exception e) {
			throw new HttpRequestException(e);
		}
	}

	public <T> void formDataRead(ParseEvent<T> event) throws Exception {
	
	}

	public void init(Object initParams) throws HttpRequestException {
		
	}

	public void destory() throws HttpRequestException {
		
	}

	public <T> void onReqLineStart(ParseEvent<T> e) throws HttpRequestException {
	
	}

	public <T> void onReqLineEnd(ParseEvent<T> e) throws HttpRequestException {
	
	}

	public <T> void onHeaderStart(ParseEvent<T> e) throws HttpRequestException {
		
	}

	public <T> void onHeaderEnd(ParseEvent<T> e) throws HttpRequestException {
	
	}

	public <T> void onBodyStart(ParseEvent<T> e) throws HttpRequestException {
		
	}

	public <T> void onBodyEnd(ParseEvent<T> e) throws HttpRequestException {
		
	}
}
