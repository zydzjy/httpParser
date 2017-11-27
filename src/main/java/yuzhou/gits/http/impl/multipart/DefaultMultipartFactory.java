package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.request.HttpRequestException;

public abstract class DefaultMultipartFactory implements AbastractMultipartFactory {
	
	public FindBoundaryStage createFindBoundaryStage(ReqMsgParserContext ctx, Object... args)
			throws HttpRequestException {
		return new FindBoundaryStage();
	}

	public FindMIMEHeadersStage createFindMIMEHeadersStage(ReqMsgParserContext ctx, Object... args)
			throws HttpRequestException {
		return new FindMIMEHeadersStage();
	}

	public abstract MultipartParseStage createMultipartParseStage(ReqMsgParserContext ctx, Object... args)
			throws HttpRequestException;

}
