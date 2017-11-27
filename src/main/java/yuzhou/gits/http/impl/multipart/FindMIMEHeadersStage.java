package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.commonUtils.BytesUtils;
import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.HttpCommonConstants;
import yuzhou.gits.http.message.request.HttpRequestException;

public class FindMIMEHeadersStage implements MultipartParseStage {

	public Object staging(ReqMsgParserContext ctx, MultipartBodyParser parser) throws Exception {
		BytesChunk data = ctx.readNextAvailable(HttpCommonConstants.CRLF);
		if (data != null) {// MIME headers found,next read body payLoad
			if (data.getLength() == 0) {// a blank line indicate that MIME headers end
				PartDataStage nextStage = parser.partBodyStage(ctx);
				//TODO:notify part body
				nextStage.partStart(ctx, parser);
				parser.setCurrStage(nextStage);
			}else  {
				this.parseMIMEHeaders(parser, data);
			}
		}
		return null;
	}
	
	public final static byte[] formFileHeaderParamName = "filename=\"".getBytes();
	
	protected void parseMIMEHeaders(MultipartBodyParser parser,
			BytesChunk mimeHeaderData) throws HttpRequestException {
		int idx = BytesUtils.indexOf(mimeHeaderData, 0, ": ".getBytes());
		if(idx <= -1){
			throw new HttpRequestException("BAD MULTIPART");
		}
		byte[] headerName = new byte[idx];
		BytesUtils.bytesChunkCopy(mimeHeaderData, 0, headerName, 0, headerName.length);
		byte[] headerVal = new byte[mimeHeaderData.getLength()- idx-2];
		BytesUtils.bytesChunkCopy(mimeHeaderData, idx+2, headerVal, 0, headerVal.length);
		
		parser.currPartMIMEHeaders.put(new String(headerName), headerVal);
	}
}
