package yuzhou.gits.http.impl.multipart;

public interface FormFileOutputStream {
	
	public void write(byte[] data,int offset,int length) throws Exception;
	public void flush() throws Exception;
	
	public void open(Object config) throws Exception;
	public void close() throws Exception;
	public boolean isClosed();
}
