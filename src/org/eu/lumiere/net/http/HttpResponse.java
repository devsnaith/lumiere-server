package org.eu.lumiere.net.http;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Set;

import org.eu.lumiere.loggers.GlobalLogger;
import org.eu.lumiere.loggers.GlobalLogger.LogLevel;

public class HttpResponse {
	
	private GlobalLogger l = GlobalLogger.getLogger();
	private String status_line = "HTTP/1.0 200 OK";
	private String content_type = "text/html";
	private LinkedHashMap<String, String> httph;
	private Socket client;
	
	public HttpResponse(Socket client, String status_line, String content_type) {
		this.content_type = content_type == null ? this.content_type : content_type;
		if(status_line != null && !status_line.isEmpty())
			this.status_line = status_line;
		httph = httph == null ? new LinkedHashMap<>() : httph;
		this.client = client;
	}
	
	public void push(String body) {
		try {
			StringBuilder build = new StringBuilder(status_line+"\r\n");
			for(String key : httph.keySet()) {
				build.append(String.format("%s: %s\r\n", key, getProperty(key)));
			}
			client.getOutputStream().write(build.append("\r\n").append(body).toString().getBytes());
		} catch (IOException e) {
			l.printf(LogLevel.ERROR, e.getMessage(),  null);
		}
	}
	
	public void setProperty(String key, String value) {
		if(!httph.containsKey(key))
			httph.put(key, value);
	}
	
	public void setStatus(String sl) {
		this.status_line = sl;
	}
	
	public void setContentType(String ct) {
		this.content_type = ct;
	}
	
	public String getStatus() {
		return this.status_line;
	}
	
	public String getContentType() {
		return this.content_type;
	}
	
	public String getProperty(String key) {
		return httph.get(key);
	}
	
	public Set<String> getKeySet() {
		return httph.keySet();
	}
	
	public String getStatusLine() {
		return status_line;
	}
	
}
