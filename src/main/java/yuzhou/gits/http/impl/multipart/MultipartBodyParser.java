package yuzhou.gits.http.impl.multipart;

import java.util.HashMap;
import java.util.Map;

import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.HttpCommonConstants;
import yuzhou.gits.http.message.request.BodyParser;
import yuzhou.gits.http.message.request.HttpRequestException;

//a multiPart parser is abstract factory for creating stages parsing mutiPart HTTP
public abstract class MultipartBodyParser implements BodyParser {
	
	protected long contentLength;
	protected byte[] boundaryVal;
	protected byte[] bodyPartStart;
	protected byte[] lastBodyPart;
	protected ReqMsgParserContext ctx;
	protected int partBodyParseMinSizeRetain;
	public int getPartBodyParseMinSizeRetain() {
		return partBodyParseMinSizeRetain;
	}

	public void setPartBodyParseMinSizeRetain(int partBodyParseMinSizeRetain) {
		this.partBodyParseMinSizeRetain = partBodyParseMinSizeRetain;
	}
	protected Map<String,byte[]> currPartMIMEHeaders = new HashMap<String,byte[]>();
	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	public final static byte[] PART_START_CHARS = {0x2d,0x2d};
	public final static byte[] PART_END_CHARS = {'-','-'};

	public MultipartBodyParser(ReqMsgParserContext ctx,byte[] boundaryVal) {
		this.ctx = ctx;
		this.boundaryVal = boundaryVal;
		this.bodyPartStart = new byte[PART_START_CHARS.length+this.boundaryVal.length
		                         +HttpCommonConstants.CRLF.length];
		this.bodyPartStart[0] = PART_START_CHARS[0];
		this.bodyPartStart[1] = PART_START_CHARS[1];
		System.arraycopy(this.boundaryVal, 0, this.bodyPartStart, 2, this.boundaryVal.length);
		this.bodyPartStart[bodyPartStart.length-2] = HttpCommonConstants.CRLF[0];
		this.bodyPartStart[bodyPartStart.length-1] = HttpCommonConstants.CRLF[1];
		
		this.lastBodyPart = new byte[2+PART_START_CHARS.length+this.boundaryVal.length
		                         +HttpCommonConstants.CRLF.length];
		this.lastBodyPart[0] = HttpCommonConstants.CRLF[0];
		this.lastBodyPart[1] = HttpCommonConstants.CRLF[1];
		this.lastBodyPart[2] = PART_START_CHARS[0];
		this.lastBodyPart[3] = PART_START_CHARS[1];
		System.arraycopy(this.boundaryVal, 0, this.lastBodyPart, 4, this.boundaryVal.length);
		this.lastBodyPart[lastBodyPart.length-2] = PART_END_CHARS[0];
		this.lastBodyPart[lastBodyPart.length-1] = PART_END_CHARS[1];
		
		this.currStage = new FindBoundaryStage();
		this.partBodyParseMinSizeRetain = 
				Math.max(this.lastBodyPart.length,this.bodyPartStart.length);
		
	}
	
	/***********************************************
	  dash-boundary := "--" boundary 
		  	;boundary taken from the value of
		    ;boundary parameter of the 
		    ; Content-Type field. 
	  yuzhou.gits.http.multipart-body := [preamble CRLF] dash-boundary transport-padding CRLF 
	  body-part *encapsulation 
	  close-delimiter transport-padding [CRLF epilogue]
	  delimiter := CRLF dash-boundary 
	  close-delimiter := delimiter "--"
	  body-part := MIME-part-headers [CRLF *OCTET] 
		  ; Lines in a body-part must not start 
		  ; with the specified dash-boundary and 
		  ; the delimiter must not appear anywhere 
		  ; in the body part. Note that the 
		  ; semantics of a body-part differ from 
		  ; the semantics of a message, as 
		  ; described in the text.
	************************************************/
	public void parse(ReqMsgParserContext ctx) throws HttpRequestException {
		if(this.currStage != null){
			try {
				this.currStage.staging(ctx, this);
			} catch (Exception e) {
				throw new HttpRequestException(e);
			}
		}
	}
	
	protected MultipartParseStage currStage;

	public MultipartParseStage getCurrStage() {
		return currStage;
	}

	public void setCurrStage(MultipartParseStage currStage) {
		this.currStage = currStage;
	}
	
	
	public abstract PartDataStage partBodyStage(ReqMsgParserContext ctx);
}