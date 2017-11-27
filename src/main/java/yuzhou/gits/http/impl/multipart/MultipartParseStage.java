package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.http.ReqMsgParserContext;

public interface MultipartParseStage {

	public Object staging(ReqMsgParserContext ctx,
			MultipartBodyParser parser) throws Exception;
}
