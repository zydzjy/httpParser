package yuzhou.gits.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.commonUtils.BytesUtils;
import yuzhou.gits.http.ParseEvent;
import yuzhou.gits.http.impl.ReqLineParseState;
import yuzhou.gits.http.impl.multipart.AbastractMultipartFactory;
import yuzhou.gits.http.impl.multipart.DefaultFormMultipartFactory;
import yuzhou.gits.http.impl.multipart.DefaultMultipartFactory;
import yuzhou.gits.http.message.request.HttpRequestException;

public class ReqMsgParserContext {

	protected HttpReqMsgParserState currState;
	protected byte[] dataToDeal;
	protected int currReadIdx = 0;
	protected Map<String, Object> exterParams = new HashMap<String, Object>();
	protected BytesChunk reqMethod;
	protected BytesChunk reqURI;
	protected long httpRequestStartTime = 0;
	protected boolean parse;
	
	
	public static AbastractMultipartFactory createMultipartFactory(String multipartType)
		throws HttpRequestException{
		if(DefaultMultipartFactory.MULTIPART_FORM_DATA.equals(multipartType)){
			return new DefaultFormMultipartFactory();
		}else{
			throw new HttpRequestException("can not create a decret factory for this multipart type");
		}
	}
	
	public boolean isParse() {
		return this.hasNextAvailable();
	}

	public void setParse(boolean parse) {
		this.parse = parse;
	}

	public long getHttpRequestStartTime() {
		return httpRequestStartTime;
	}

	public void setHttpRequestStartTime(long httpRequestStartTime) {
		this.httpRequestStartTime = httpRequestStartTime;
	}

	public <T> void notifyBodyEndEvent(ParseEvent<T> event) throws Exception {
		// notify all listener
		Iterator<HttpRequestParseListener> listenerIt = this.listeners.iterator();
		while (listenerIt.hasNext()) {
			HttpRequestParseListener listener = listenerIt.next();
			listener.onBodyEnd(event);
		}
	}

	public ReqMsgParserContext() {
		this.currState = new ReqLineParseState();
	}

	public Object getExterParam(String paramKey) {
		return this.exterParams.get(paramKey);
	}

	public void setExterParam(String paramKey, Object param) {
		this.exterParams.put(paramKey, param);
	}

	public HttpReqMsgParserState getCurrState() {
		return currState;
	}

	public void setCurrState(HttpReqMsgParserState currState) {
		this.currState = currState;
	}

	public byte[] getDataToDeal() {
		return dataToDeal;
	}

	
	public void setDataToDeal(byte[] _dataToDeal) {
		// connect the previous
		if (this.dataToDeal != null && this.currReadIdx < this.dataToDeal.length) {
			int remainSize = this.dataToDeal.length - this.currReadIdx;
			byte[] _newDataBuff = new byte[_dataToDeal.length+ remainSize];
			System.arraycopy(this.dataToDeal, this.currReadIdx, _newDataBuff, 0, remainSize);
			System.arraycopy(_dataToDeal, 0, _newDataBuff, remainSize, _dataToDeal.length);
			this.dataToDeal = null;
			this.dataToDeal = _newDataBuff;
		} else {
			this.dataToDeal = _dataToDeal;
		}
		this.readMuch = false;
		this.currReadIdx = 0;
	}

	private SimpleReqMsgParser parser;

	public SimpleReqMsgParser getParser() {
		return parser;
	}

	public void setParser(SimpleReqMsgParser parser) {
		this.parser = parser;
	}
	protected long currTotalSizeReq = 0L;
	public void skip(int length) {
		this.currReadIdx += length;
		currTotalSizeReq += length;
	}

	public int getAvailableLen() {
		return this.dataToDeal.length - this.currReadIdx;
	}

	public boolean hasNextAvailable() {
		return this.dataToDeal != null && this.currReadIdx < this.dataToDeal.length && this.readMuch == false;
	}

	public BytesChunk readNextAvailable(int start,int length) {
		if (this.dataToDeal.length - this.currReadIdx < length) {
			this.readMuch = true;
			return null;
		}
		//System.arraycopy(this.dataToDeal.getBytesBuf(), start + this.dataToDeal.getStart(), buff, 0, buff.length);
		this.currReadIdx += length;
		BytesChunk data = new BytesChunk(this.dataToDeal,start,this.currReadIdx-1);
		
		currTotalSizeReq += length;
		return data;
	}

	public int getCurrReadIdx() {
		return this.currReadIdx;
	}

	private boolean readMuch = false;

	public boolean getReadMuch() {
		return this.readMuch;
	}

	public void setReadMuch(boolean readMuch) {
		this.readMuch = readMuch;
	}
	
	public BytesChunk readNextAvailable(byte[] end) {
		BytesChunk readData = new BytesChunk();

		int idx = BytesUtils.indexOf(this.dataToDeal, this.currReadIdx, end);
		if (idx < 0) {
			readMuch = true;
			return null;
		} else {
			if(idx == this.currReadIdx){
				readData = BytesChunk.EMPTY_CHUNK;
			}else{
				readData = new BytesChunk(this.dataToDeal,this.currReadIdx,idx -1);
			}
			this.currReadIdx = (idx+end.length);
		}
		
		currTotalSizeReq += readData.getLength()+end.length;
		return readData;
	}

	public long getCurrTotalSizeReq() {
		return currTotalSizeReq;
	}

	private Map<String, Object> reqHeaders = new HashMap<String, Object>();

	public void putHeader(String key, Object val) {
		this.reqHeaders.put(key, val);
	}

	public Object getHeader(String key) {
		return this.reqHeaders.get(key);
	}

	private BytesChunk uri;

	public void setCurrURI(BytesChunk uri) {
		this.uri = uri;
	}

	public BytesChunk getCurrURI() {
		return this.uri;
	}

	protected List<HttpRequestParseListener> listeners = null;

	public void setListeners(List<HttpRequestParseListener> listeners) {
		this.listeners = listeners;
	}

	public List<HttpRequestParseListener> getListeners() {
		return this.listeners;
	}
	
	protected boolean requestResponsed = false;
	
	public boolean isRequestResponsed() {
		return requestResponsed;
	}

	public void setRequestResponsed(boolean requestResponsed) {
		this.requestResponsed = requestResponsed;
	}

	public void destroyAllListeners() {
		if (this.listeners != null) {
			Iterator<HttpRequestParseListener> it = this.listeners.iterator();
			while (it.hasNext()) {
				HttpRequestParseListener listener = it.next();
				try {
					listener.destory();
				} catch (HttpRequestException e) {
					// just ignore
				}
			}
		}
	}
}
