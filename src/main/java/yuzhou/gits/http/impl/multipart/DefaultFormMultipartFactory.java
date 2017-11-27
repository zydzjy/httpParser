package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.request.HttpRequestException;

public class DefaultFormMultipartFactory extends DefaultMultipartFactory implements AbstractFormMultipartFactory  {

	public MultipartFormDataBodyParser createMultipartFormDataBodyParser() throws HttpRequestException {
		return null;
	}

	public CommonFormDataStage createCommonFormDataStage() throws HttpRequestException {
		return null;
	}

	public FormFileStage createFormFileStage() throws HttpRequestException {
		return null;
	}

	public FormFileOutputStream createFormFileOutputStream() throws HttpRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	public MultipartFormDataParseListener createMultipartFormDataParseListener() throws HttpRequestException {
		return null;
	}

	@Override
	public MultipartParseStage createMultipartParseStage(ReqMsgParserContext ctx, Object... args)
			throws HttpRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	public MultipartBodyParser createMultipartBodyParser(ReqMsgParserContext ctx, Object... args) {
		 return new MultipartFormDataBodyParser(ctx,(byte[])args[0]);
	}
}