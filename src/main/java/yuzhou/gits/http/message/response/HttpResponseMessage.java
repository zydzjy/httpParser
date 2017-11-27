package yuzhou.gits.http.message.response;

import java.util.Date;

public class HttpResponseMessage {

	public static byte[] responseMsg_dummy = ("HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 0\r\n"
			+ "\r\n\r\n").getBytes();

	public static byte[] responseMsg_400 = ("HTTP/1.1 400 bad request!\r\nConnection: close\r\nContent-Length: 0\r\n"
			+ "\r\n\r\n").getBytes();

	public final static byte[] responseMsg_internalError = ("HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 70\r\nContent-Type: application/json; charset=gbk\r\n"
			+ "\r\n{\"code\":\"999\",\"msg\":\"server internal failure to handle this request!\"}\r\n").getBytes();

	public final static byte[] responseMsg_noHandler = ("HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 3\r\nContent-Type: application/json; charset=gbk\r\n"
			+ "\r\n789\r\n").getBytes();

	
	public static byte[] createSimpleResponseWithChunkedEncode
					(String respCode,String responseData,String bodyDataEncode) throws Exception {
		StringBuffer resp = new StringBuffer();
		String respBody = "{\"code\":\""+respCode+"\",\"msg\":" + responseData + "}";
		resp.append("HTTP/1.1 200 ")
			.append("Content-Type: application/json; charset=gb2312\r\n" 
					+"Transfer-Encoding: chunked\r\n"
					+"Date: "+new Date()+"\r\n"
					+ "\r\n"
					+ Integer.toHexString(respBody.length())+"\r\n"
					+ respBody + "\r\n"
					+"0\r\n"
					+ "\r\n");
		return resp.toString().getBytes(bodyDataEncode);
	}

}
