package org.eu.lumiere.net.http;

import java.net.Socket;
import java.util.HashMap;

import org.eu.lumiere.error.ErrorHandler;
import org.eu.lumiere.error.HTTPException;
import org.eu.lumiere.loggers.GlobalLogger;
import org.eu.lumiere.loggers.GlobalLogger.LogLevel;
import org.eu.lumiere.net.RequestHandler;

public class HTTPController implements RequestHandler {
	
	private GlobalLogger l = GlobalLogger.getLogger();
	private HashMap<String, RequestHandler> handlers = new HashMap<>();
	private HTTPException hException = ErrorHandler.getHandler();
	
	public boolean addHandler(String path, RequestHandler handler) {
		if(handler == null)
			return (boolean) l.printf(LogLevel.WARNING, "The handler ["+path+"] is null", false);			
		
		if(handler.equals(this))
			return (boolean) l.printf(LogLevel.WARNING, "The handler cannot be the HttpController", false);			
		l.printf(LogLevel.INFO, "A Handler for " + path + " has been created", null);
		handlers.put(path, handler);
		return true;
	}
	
	public void setHTTPException(HTTPException ex) {
		hException = ex == null ? ErrorHandler.getHandler() : ex;
	}
	
	@Override
	public void onRequestReceived(Socket socket, HTTPRequest request, HTTPResponse response) {
		String clientUrl = request.getURL();
		if(handlers.containsKey(clientUrl)) {
			handlers.get(clientUrl).onRequestReceived(socket, request, response);
		} else {
			hException.onException(response, 404, "where are you going dude?");
		}
	}
	
	public RequestHandler getHandler(String path) {
		return handlers.get(path);
	}
	
	public RequestHandler[] getHandlers() {
		return handlers.values().toArray(new RequestHandler[0]);
	}
	
	public String[] getPaths() {
		return handlers.keySet().toArray(new String[0]);
	}
	
}
