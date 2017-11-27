package yuzhou.gits.http.impl.multipart;
import yuzhou.gits.http.message.request.HttpRequestException;

public interface AbstractFormMultipartFactory extends AbastractMultipartFactory {

	public MultipartFormDataBodyParser createMultipartFormDataBodyParser() throws HttpRequestException;
	
	public CommonFormDataStage createCommonFormDataStage() throws HttpRequestException;
	public FormFileStage createFormFileStage() throws HttpRequestException;
	public FormFileOutputStream createFormFileOutputStream() throws HttpRequestException;
	public MultipartFormDataParseListener createMultipartFormDataParseListener() throws HttpRequestException;
}