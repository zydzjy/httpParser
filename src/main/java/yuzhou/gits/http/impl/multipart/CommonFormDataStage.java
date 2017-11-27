package yuzhou.gits.http.impl.multipart;

import java.util.ArrayList;
import java.util.List;

import yuzhou.gits.commonUtils.BytesChunk;
import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.message.request.HttpRequestException;

public class CommonFormDataStage extends PartDataStage {
	@Override
	protected void processPartBodyData(ReqMsgParserContext ctx, MultipartBodyParser parser, BytesChunk data)
			throws Exception {
		MultipartFormDataBodyParser formBodyParser = (MultipartFormDataBodyParser)parser;
		byte[] fieldName = formBodyParser.getFormParam("name=\"".getBytes());
		Object[] formData = {fieldName,data};
		//System.out.println("form name:"+new String(fieldName)+",form value:"+data.getString("gbk"));
		//formBodyParser.commonFormData.add(formData);
		@SuppressWarnings("unchecked")
		List<Object[]> formDataList = (List<Object[]>) ctx.getExterParam(
				MultipartFormDataBodyParser.FORM_DATA);
		if(formDataList == null){
			formDataList = new ArrayList<Object[]>();
			ctx.setExterParam(MultipartFormDataBodyParser.FORM_DATA, formDataList);
		}
		formDataList.add(formData);
	}

	@Override
	public void partStart(ReqMsgParserContext ctx, MultipartBodyParser parser) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partEnd(ReqMsgParserContext ctx, MultipartBodyParser parser) throws HttpRequestException {
		// TODO Auto-generated method stub
		
	}
}