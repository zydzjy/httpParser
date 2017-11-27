package yuzhou.gits.http.impl;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.http.HttpReqMsgParserState;
import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.impl.multipart.AbstractFormMultipartFactory;
import yuzhou.gits.http.impl.multipart.DefaultMultipartFactory;
import yuzhou.gits.http.impl.multipart.MultipartFormDataBodyParser;
import yuzhou.gits.http.message.HttpCommonConstants;
import yuzhou.gits.http.message.request.HttpRequestException;

/*
field  =  field-name ":" [ field-body ] CRLF
field-name  =  1*<any CHAR, excluding CTLs, SPACE, and ":">
field-body  =  field-body-contents
[CRLF LWSP-char field-body]
     
field-body-contents =
	<the ASCII characters making up the field-body, as
	 defined in the following sections, and consisting
	of combinations of atom, quoted-string, and
	specials tokens, or else consisting of texts>
*/
public class HeaderParseState implements HttpReqMsgParserState {

	public void parse(ReqMsgParserContext ctx) throws HttpRequestException {
		BytesChunk aNewLine = null;
		while ((aNewLine = (ctx.readNextAvailable(HttpCommonConstants.CRLF))) != null) {
			if(aNewLine.getLength() == 0){// empty CRLF for headers end
				// setup body parser
				BodyParseState bodyParseState = new BodyParseState();
				String contentTypeStr = (String) ctx.getHeader("Content-Type");
				String contentLengthStr = (String) ctx.getHeader("Content-Length");
				long contentLength = Long.parseLong(contentLengthStr);
				if (contentTypeStr.startsWith(DefaultMultipartFactory.MULTIPART_FORM_DATA)) {
					int beginIndex = contentTypeStr.indexOf("=");
					String boundary = contentTypeStr.substring(beginIndex + 1);
					MultipartFormDataBodyParser _parser = null;
					AbstractFormMultipartFactory multipartFactory = 
							(AbstractFormMultipartFactory) ReqMsgParserContext
							.createMultipartFactory(
									AbstractFormMultipartFactory.MULTIPART_FORM_DATA);
					_parser = (MultipartFormDataBodyParser)
							multipartFactory.createMultipartBodyParser(ctx,boundary.getBytes());
					_parser.setContentLength(contentLength);
					bodyParseState.setParser(_parser);
				}
				ctx.setCurrState(bodyParseState);
				break;
			} else {
				String headerField = aNewLine.toString();
				int firstColon = headerField.indexOf(":");
				if (firstColon > 0) {
					String headerName = headerField.substring(0, firstColon);
					String headerVal = headerField.substring(firstColon + 1);
					if (headerVal != null)
						headerVal = headerVal.trim();
					ctx.putHeader(headerName, headerVal);
				} else {
					throw new HttpRequestException();
				}
			}
		}
	}

	public void enter(ReqMsgParserContext ctx) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}

	public void exit(ReqMsgParserContext ctx) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}
}