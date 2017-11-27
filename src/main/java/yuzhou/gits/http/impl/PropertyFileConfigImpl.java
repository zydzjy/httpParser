package yuzhou.gits.http.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import yuzhou.gits.http.ParserConfig;

public class PropertyFileConfigImpl implements ParserConfig {
	final static String URL_MAPPER_PRONAME = "urlMapper";
	
	
	
	protected Properties props = new Properties();
	
	public void addItem(String itemName, Object val) {
		// TODO Auto-generated method stub

	}
	
	public Object getItem(String itemName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Object[]> getUrlParseHandlerMapper(String url) {
		return this.urlMappers.get(url);
	}
	
	public void addUrlParseHandlerMapper(String url, String clzName, String method, 
			Object[] initParams) {
		List<Object[]> urlMapperList= this.urlMappers.get(url);
		if(urlMapperList ==  null){
			urlMapperList = new ArrayList<Object[]>();
			this.urlMappers.put(url, urlMapperList);
		}
		Object[] o = {clzName,initParams};
		urlMapperList.add(o);
	}
	
	protected Map<String,List<Object[]>> urlMappers = new HashMap<String,List<Object[]>>();
	
	public void initConfig(Object src) throws Exception {
		InputStream input = null;
		try {
			input = (FileInputStream)src;
			// load a properties file
			props.load(input);
			
			String strUrlMappers = props.getProperty(URL_MAPPER_PRONAME);
			String[] vals = strUrlMappers.split(",");
			for(int i=0;i<vals.length;i++){
				String val = vals[i];
				if(val != null){
					String[] _vals = val.split("@");
					this.addUrlParseHandlerMapper(_vals[0], _vals[1], _vals[2],null);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}