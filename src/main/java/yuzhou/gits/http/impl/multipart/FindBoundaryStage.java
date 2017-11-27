package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.commonUtils.BytesUtils;
import yuzhou.gits.http.ReqMsgParserContext;

public class FindBoundaryStage implements MultipartParseStage {

	public Object staging(ReqMsgParserContext ctx, MultipartBodyParser parser) throws Exception {
		int idxChecked = BytesUtils.indexOf(ctx.getDataToDeal(),
				ctx.getCurrReadIdx(), parser.bodyPartStart);
		if(idxChecked > -1){//read part start
			FindMIMEHeadersStage nextStage = new FindMIMEHeadersStage();
			parser.setCurrStage(nextStage);
			ctx.skip(parser.bodyPartStart.length);
		}
		return null;
	}
}
