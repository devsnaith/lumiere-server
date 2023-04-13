package org.eu.lumiere.net.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Properties;
import java.util.StringTokenizer;

import org.eu.lumiere.loggers.GlobalLogger;
import org.eu.lumiere.loggers.GlobalLogger.LogLevel;

public class HttpRequest {
	private GlobalLogger l = GlobalLogger.getLogger();
	private String reqMethod, reqPath, reqVersion;
	private Properties properties;
	public HttpRequest(Socket client) {
		try {
			InputStreamReader stream = new InputStreamReader(client.getInputStream());
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
			
		} catch (IOException e) {
			l.printf(LogLevel.ERROR, e.getMessage(), null);
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
