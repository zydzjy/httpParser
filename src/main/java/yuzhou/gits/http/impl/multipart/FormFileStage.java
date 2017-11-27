package yuzhou.gits.http.impl.multipart;

import java.util.Iterator;
import java.util.List;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.commonUtils.BytesUtils;
import yuzhou.gits.http.HttpRequestParseListener;
import yuzhou.gits.http.impl.ParseEvent;
import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.request.HttpRequestException;

public class FormFileStage extends PartDataStage {
	
	protected byte[] formFileNameBytes = null;
	
	public FormFileStage(byte[] formFileNameBytes) {
		this.formFileNameBytes = formFileNameBytes;
	}
	
	/*protected int checkNextPartStart(ReqMsgParserContext ctx, 
			MultipartBodyParser parser){
		return -1;
	}
	
	protected int checkLastPartEnd(ReqMsgParserContext ctx, 
			MultipartBodyParser parser){
		int idx = -1;
		if(ctx.getCurrTotalSizeReq() + ctx.getAvailableLen() + parser.lastBodyPart.length
				>= parser.contentLength){
			idx = ctx.getDataToDeal().length - parser.lastBodyPart.length - 2;
		}
		return idx;
	}*/
	
	
	protected void notifyFileReadEnd(ReqMsgParserContext ctx, MultipartBodyParser parser) 
			throws HttpRequestException{
		if(formFileNameBytes == null || formFileNameBytes.length== 0){
			return;
		}
		ParseEvent<Object> event = new ParseEvent<Object>(ctx,null);
		List<HttpRequestParseListener> listensers = ctx.getListeners();
		if(listensers != null){
			Iterator<HttpRequestParseListener> it = 
					listensers.iterator();
			while(it.hasNext()){
				HttpRequestParseListener _listener = it.next();
				if(_listener instanceof MultipartFormDataParseListener){
					MultipartFormDataParseListener formDataListener = 
							(MultipartFormDataParseListener)_listener;
					formDataListener.formFileDataReadEnd(event);
				}
			}
		} 
	}
	
	protected void notifyFileReadStart(ReqMsgParserContext ctx, MultipartBodyParser parser) throws HttpRequestException{
		if(formFileNameBytes == null || formFileNameBytes.length== 0){
			return;
		}
		//TODO: adopt to windows file name
		int lastSlash = BytesUtils.lastIndexOf(formFileNameBytes, "\\".getBytes());
		byte[] tempName = formFileNameBytes;
		if(lastSlash > -1){
			tempName = new byte[formFileNameBytes.length - lastSlash - 1];
			System.arraycopy(formFileNameBytes, lastSlash+1, tempName, 0, tempName.length);
		}
		ParseEvent<byte[]> event = new ParseEvent<byte[]>(ctx,tempName);
		List<HttpRequestParseListener> listensers = ctx.getListeners();
		if(listensers != null){
			Iterator<HttpRequestParseListener> it = 
					listensers.iterator();
			while(it.hasNext()){
				HttpRequestParseListener _listener = it.next();
				if(_listener instanceof MultipartFormDataParseListener){
					MultipartFormDataParseListener formDataListener = 
							(MultipartFormDataParseListener)_listener;
					formDataListener.formFileDataReadStart(event);
				}
			}
		}
	}
	
	@Override
	protected void processPartBodyData(ReqMsgParserContext ctx, MultipartBodyParser parser, BytesChunk data)
			throws Exception {
		if(formFileNameBytes == null || formFileNameBytes.length== 0){
			return;
		}
		 this.processFileDataRead(ctx, data);
	}
	
	protected void processFileDataRead(ReqMsgParserContext ctx,BytesChunk data) throws Exception {
		
		ParseEvent<BytesChunk> event = new ParseEvent<BytesChunk>(ctx,data);
		List<HttpRequestParseListener> listensers = ctx.getListeners();
		if(listensers != null){
			Iterator<HttpRequestParseListener> it = 
					listensers.iterator();
			while(it.hasNext()){
				HttpRequestParseListener _listener = it.next();
				if(_listener instanceof MultipartFormDataParseListener){
					MultipartFormDataParseListener formDataListener = 
							(MultipartFormDataParseListener)_listener;
					formDataListener.formFileDataRead(event);
				}
			}
		}  
	}

	@Override
	public void partStart(ReqMsgParserContext ctx, MultipartBodyParser parser)
			throws HttpRequestException {
		this.notifyFileReadStart(ctx, parser);
	}

	@Override
	public void partEnd(ReqMsgParserContext ctx, MultipartBodyParser parser)
			throws HttpRequestException {
		this.notifyFileReadEnd(ctx, parser);
	}
}