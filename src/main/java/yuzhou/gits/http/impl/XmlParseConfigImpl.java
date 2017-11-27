package yuzhou.gits.http.impl;

import java.io.InputStream;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import yuzhou.gits.http.ParserConfig;

public class XmlParseConfigImpl implements ParserConfig {

	public void addItem(String itemName, Object val) {
		// TODO Auto-generated method stub

	}

	public Object getItem(String itemName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addUrlParseHandlerMapper(String url, String method, String clzName, Object[] initParmas) {
		// TODO Auto-generated method stub

	}

	public List<Object[]> getUrlParseHandlerMapper(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	class XmlHandler extends DefaultHandler {
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			System.out.println("localName:"+localName+",qName:"+qName);
		}
	}
	
	/** Default parser name. */
    protected static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";

    
	public void initConfig(Object src) throws Exception {
		XMLReader parser = null;
		// create parser
        try {
            parser = XMLReaderFactory.createXMLReader(DEFAULT_PARSER_NAME);
            parser.setContentHandler(new XmlHandler());
            InputSource input = new InputSource((InputStream)src);
            parser.parse(input);
        }catch (Exception e) {
            e.printStackTrace();
        }
	}

}
