package org.eu.lumiere.net.http;

import java.util.HashMap;

import org.eu.lumiere.loggers.GlobalLogger;
import org.eu.lumiere.loggers.GlobalLogger.LogLevel;

public class HttpController implements HttpRequestHandler {
	
	private GlobalLogger l = GlobalLogger.getLogger();
	private HashMap<String, HttpRequestHandler> handlers = new HashMap<>();;
	
	public boolean addHandler(String path, HttpRequestHandler handler) {
		if(handler == null)
			return (boolean) l.printf(LogLevel.WARNING, "The handler ["+path+"] is null", false);			
		
		if(handler.equals(this))
			return (boolean) l.printf(LogLevel.WARNING, "The handler cannot be the HttpController", false);			
		l.printf(LogLevel.INFO, "A Handler for " + path + " has been created", null);
		handlers.put(path, handler);
		return true;
	}

	@Override
	public void onRequestReceived(HttpRequest request, HttpResponse response) {
		String clientUrl = request.getURL();
		if(handlers.containsKey(clientUrl))
			handlers.get(clientUrl).onRequestReceived(request, response);
	}
	
	public HttpRequestHandler getHandler(String path) {
		return handlers.get(path);
	}
	
	public HttpRequestHandler[] getHandlers() {
		return handlers.values().toArray(new HttpRequestHandler[0]);
	}
	
	public String[] getPaths() {
		return handlers.keySet().toArray(new String[0]);
	}
	
}
