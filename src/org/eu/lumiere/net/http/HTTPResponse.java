package org.eu.lumiere.net.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Set;

import org.eu.lumiere.loggers.GlobalLogger;
import org.eu.lumiere.loggers.GlobalLogger.LogLevel;

public class HTTPResponse {
	
	private GlobalLogger l = GlobalLogger.getLogger();
	private String status_line = "HTTP/1.0 200 OK";
	private String content_type = "text/html";
	private LinkedHashMap<String, String> httph;
	private OutputStream os;
	
	public HTTPResponse(OutputStream os, String status_line, String content_type) {
		this.content_type = content_type == null ? this.content_type : content_type;
		if(status_line != null && !status_line.isEmpty())
			this.status_line = status_line;
		httph = httph == null ? new LinkedHashMap<>() : httph;
		this.os = os;
	}
	
	public void push(String body) {
		try {
			StringBuilder build = new StringBuilder(status_line+"\r\n");
			build.append("Server: Lumiere Server").append("\r\n");
			for(String key : httph.keySet()) {
				build.append(String.format("%s: %s\r\n", key, getProperty(key)));
			}
			build.append("Content-Length: " + body.length()).append("\r\n");
			os.write(build.append("\r\n").append(body).toString().getBytes());
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
}
