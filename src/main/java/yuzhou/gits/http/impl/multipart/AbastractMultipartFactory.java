package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.request.HttpRequestException;

public interface AbastractMultipartFactory {
	public final static String MULTIPART_FORM_DATA = "multipart/form-data";
	
	public FindBoundaryStage createFindBoundaryStage(ReqMsgParserContext ctx,Object ...args) throws HttpRequestException;
	public FindMIMEHeadersStage createFindMIMEHeadersStage(ReqMsgParserContext ctx,Object ...args) throws HttpRequestException;
	public MultipartParseStage createMultipartParseStage(ReqMsgParserContext ctx,Object ...args) throws HttpRequestException;
	public MultipartBodyParser createMultipartBodyParser(ReqMsgParserContext ctx, Object... args);
	
}
