package org.eu.lumiere.net.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.StringTokenizer;

public class HTTPRequest {
	private String reqMethod, reqPath, reqVersion;
	private Properties properties;
	public HTTPRequest(InputStream os) throws IOException {
		InputStreamReader stream = new InputStreamReader(os);
		BufferedReader reader = new BufferedReader(stream);
		StringTokenizer t = new StringTokenizer(reader.readLine());			
		reqMethod = t.nextToken();
		reqPath = t.nextToken();
		reqVersion = t.nextToken();
		String line;
		properties = new Properties();
		while((line = reader.readLine()) != null) {
			String[] split;
			if((split = line.split(":", 2)).length >= 2) {
				properties.setProperty(split[0], split[1].replaceFirst ("^ *", ""));
			}else {
				break;
			}
		}
	}
	
	public String[] getKeys()  {
		return properties.keySet().toArray(new String[0]);
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public String getMethod() {
		return reqMethod;
	}
	
	public String getURL() {
		return reqPath;
	}
	
	public String getHttpVersion() {
		return reqVersion;
	}
}
