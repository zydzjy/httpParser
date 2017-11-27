package yuzhou.gits.http;

import java.util.List;

public interface ParserConfig {

	public void addItem(String itemName,Object val);
	public Object getItem(String itemName);
	
	public void addUrlParseHandlerMapper(String url,String method,String clzName
			,Object[] initParmas);
	public List<Object[]> getUrlParseHandlerMapper(String url);
	
	public void initConfig(Object src) throws Exception;
}
