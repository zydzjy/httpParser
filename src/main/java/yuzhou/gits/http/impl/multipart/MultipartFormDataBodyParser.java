package yuzhou.gits.http.impl.multipart;

import yuzhou.gits.commonUtils.BytesUtils;
import yuzhou.gits.http.ReqMsgParserContext;

public class MultipartFormDataBodyParser extends MultipartBodyParser {
	public final static String FORM_DATA = "FORM_DATA";
	 
	public MultipartFormDataBodyParser(ReqMsgParserContext ctx, byte[] boundaryVal) {
		super(ctx, boundaryVal);
	}

	@Override
	public PartDataStage partBodyStage(ReqMsgParserContext ctx) {
		byte[] formFileName = this.getFormParam("filename=\"".getBytes());	
		if(formFileName != null){
			return new FormFileStage(formFileName);
		}else{ 
			 return new CommonFormDataStage();
		}
	}
	
	//protected List<Object[]> commonFormData = new ArrayList<Object[]>();
	
	protected byte[] getFormParam(byte[] param){
		byte[] contentDispositionData = (byte[])this.currPartMIMEHeaders.get("Content-Disposition");
		
		int formFieldNameStartIdx = BytesUtils.indexOf(contentDispositionData, 0, param);
		if(formFieldNameStartIdx > -1){
			int formFieldNameEndIdx = BytesUtils.indexOf(contentDispositionData, formFieldNameStartIdx +
					param.length , "\"".getBytes());
			if(formFieldNameEndIdx > -1){
				byte[] fieldName = new byte[formFieldNameEndIdx - (formFieldNameStartIdx+param.length)];
				System.arraycopy(contentDispositionData, formFieldNameStartIdx+param.length
						, fieldName, 0, fieldName.length);
	
				return fieldName;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
}
