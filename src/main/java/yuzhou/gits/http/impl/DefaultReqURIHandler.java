package yuzhou.gits.http.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.http.HttpRequestParseListener;
import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.SimpleReqMsgParser;
import yuzhou.gits.http.impl.ParseEvent;
import yuzhou.gits.http.message.request.HttpRequestException;
import yuzhou.gits.http.message.request.HttpRequestNoHandlerException;

public class DefaultReqURIHandler implements HttpRequestParseListener {

	public <T> void onReqLineEnd(ParseEvent<T>	 e) throws HttpRequestException {
		Object[] eventData = (Object[]) e.getEventData();
		// TODO:notify parser the current URI is invalidate
		//BytesChunk method = (BytesChunk) eventData[0];
		BytesChunk uri = (BytesChunk) eventData[1];
		try {
			System.out.println(new String(uri.getString("gbk")));
		} catch (UnsupportedEncodingException e1) {
		}
		// TODO:set up event handlers for this URI
		ReqMsgParserContext parseCxt = e.getReqParseCxt();
		SimpleReqMsgParser parser = parseCxt.getParser();
		List<Object[]> listeners = null;
		try {
			listeners = parser.getListenerClz(uri.getString("gbk"));
		} catch (UnsupportedEncodingException e1) {
			throw new HttpRequestNoHandlerException();
		}
		if(listeners == null || listeners.size() == 0){
			throw new HttpRequestNoHandlerException();
		}else{
			Iterator<Object[]> it = listeners.iterator();
			try{
				List<HttpRequestParseListener> listenerInstances = 
						new ArrayList<HttpRequestParseListener>();
				while(it.hasNext()){
					Object[] elem = it.next();
					Class<?> clz = Class.forName((String) elem[0]);
					Object param = (Object) elem[1];
					HttpRequestParseListener listenerInstance = 
							(HttpRequestParseListener) clz.newInstance();
					listenerInstance.init(param);
					listenerInstances.add(listenerInstance);
				}

				parseCxt.setListeners(listenerInstances);
			}catch(Exception _e){
				throw new HttpRequestNoHandlerException();
			}
		}
	}

	public void init(Object initParams) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}

	public void destory() throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}

	public <T> void onReqLineStart(ParseEvent<T> e) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}

	public <T> void onHeaderStart(ParseEvent<T> e) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}

	public <T> void onHeaderEnd(ParseEvent<T> e) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}

	public <T> void onBodyStart(ParseEvent<T> e) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}

	public <T> void onBodyEnd(ParseEvent<T> e) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}

}
