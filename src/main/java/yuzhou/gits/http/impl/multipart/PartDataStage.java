package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.commonUtils.BytesUtils;
import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.request.HttpRequestException;

public abstract class PartDataStage implements MultipartParseStage {

	public abstract void partStart(ReqMsgParserContext ctx, MultipartBodyParser parser) throws HttpRequestException;

	public abstract void partEnd(ReqMsgParserContext ctx, MultipartBodyParser parser) throws HttpRequestException;
	
	protected int checkNextPartStart(ReqMsgParserContext ctx, 
			MultipartBodyParser parser){
		int idx = -1;
		byte[] partStart = { parser.bodyPartStart[0], parser.bodyPartStart[1], parser.bodyPartStart[2],
				parser.bodyPartStart[3] };
		idx = BytesUtils.indexOf(ctx.getDataToDeal(), ctx.getCurrReadIdx(), partStart);
		if (idx > -1) {// peek some data,attempt to check the next
						// boundary start
			idx = BytesUtils.indexOf(ctx.getDataToDeal(), ctx.getCurrReadIdx(), parser.bodyPartStart); 
		}
		return idx;
	}
	
	protected int checkLastPartEnd(ReqMsgParserContext ctx, 
			MultipartBodyParser parser){
		int idx = -1;
		byte[] partEnd = { parser.lastBodyPart[0], parser.lastBodyPart[1], parser.lastBodyPart[2],
				parser.lastBodyPart[3] };
		idx = BytesUtils.indexOf(ctx.getDataToDeal(), ctx.getCurrReadIdx(), partEnd);
		if (idx > -1) {
			idx = BytesUtils.indexOf(ctx.getDataToDeal(), ctx.getCurrReadIdx(), parser.lastBodyPart);
		}
		return idx;
	}
	
	public Object staging(ReqMsgParserContext ctx, MultipartBodyParser parser) throws Exception {
		if (ctx.getAvailableLen() <= parser.partBodyParseMinSizeRetain) {
			ctx.setReadMuch(true);
			return null;
		} else {
			while (ctx.hasNextAvailable()) {
				int idx = this.checkNextPartStart(ctx, parser);
				if (idx > -1) {// maybe next part start,this part is end
					// TODO:IMPORTANT!!! reduce CRLF??
					if (idx - ctx.getCurrReadIdx() - 2 > 0) {
						int len = idx - ctx.getCurrReadIdx() - 2;// TODO:reduce
																	// CRLF??
						BytesChunk data = ctx.readNextAvailable(ctx.getCurrReadIdx(), len);
						this.processPartBodyData(ctx, parser, data);
						// TODO:notify part body end
						this.partEnd(ctx, parser);
					}
					// TODO:skip the next boundary and CRLF??
					ctx.skip(parser.bodyPartStart.length + 2);

					FindMIMEHeadersStage nextStage = new FindMIMEHeadersStage();
					parser.setCurrStage(nextStage);
				} else {
					idx = this.checkLastPartEnd(ctx, parser);
					if (idx > -1) {// current part is last part
						if (idx - ctx.getCurrReadIdx() > 0) {
							int len = idx - ctx.getCurrReadIdx();
							BytesChunk data = ctx.readNextAvailable(ctx.getCurrReadIdx(), len);
							// TODO:fire form data event
							this.processPartBodyData(ctx, parser, data);
							this.partEnd(ctx, parser);
						}
						ctx.skip(parser.lastBodyPart.length + 2);
						// TODO:body end,what next to do
						ctx.getCurrState().exit(ctx);
						/*ParseEvent event = new ParseEvent(ctx,null);
						ctx.notifyBodyEndEvent(event);
						
						ctx.getParser().end(ctx);
						ctx.setNextState(null);*/
						return null;
					} else {
						// data = new byte[this.ctx.getAvailableLen()];
						BytesChunk data = ctx.readNextAvailable(ctx.getCurrReadIdx(), ctx.getAvailableLen());
						this.processPartBodyData(ctx, parser, data);
					}
				}
				return null;
			}
		}
		return null;
	}

	protected abstract void processPartBodyData(ReqMsgParserContext ctx, MultipartBodyParser parser, BytesChunk data)
			throws Exception;
}
