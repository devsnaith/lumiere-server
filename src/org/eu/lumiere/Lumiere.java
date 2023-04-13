package org.eu.lumiere;

import java.io.IOException;
import java.net.Socket;

import org.eu.lumiere.loggers.GlobalLogger;
import org.eu.lumiere.loggers.GlobalLogger.LogLevel;
import org.eu.lumiere.net.ServerEvents;
import org.eu.lumiere.net.WebServer;
import org.eu.lumiere.net.http.HttpRequest;
import org.eu.lumiere.net.http.HttpRequestHandler;
import org.eu.lumiere.net.http.HttpResponse;

public class Lumiere implements ServerEvents{
	
	private GlobalLogger l = GlobalLogger.getLogger();
	
	private int server_port = 8080;
	private WebServer server;
	
	private HttpResponse header;
	private HttpRequestHandler request;
	
	public Lumiere(HttpRequestHandler rHandler) {
		if((this.request = rHandler) == null) {
			l.printf(LogLevel.ERROR, "Lumiere 'requestHandler' CANNOT BE NULL", null);
			return;
		}
		header = new HttpResponse(null, "HTTP/1.1 200 OK", "text/html");
		header.setProperty("Server", "Lumiere Server");
	}

	@Override
	public void onConnection(Socket socket) {
		
		if(header == null)
			return;
		
		HttpResponse httpH = new HttpResponse(socket, header.getStatusLine(), header.getContentType());
		header.getKeySet().forEach(key -> {
			httpH.setProperty(key, header.getProperty(key));
		});
		
		request.onRequestReceived(new HttpRequest(socket), httpH);
		try {			
			if(socket.isConnected())
				socket.close();
		}catch(IOException ex) {
			l.printf(LogLevel.ERROR, ex.getMessage(), null);
		}
	}
	
	public void bootServer(int port) {
		if(server != null) {
			l.printf(LogLevel.ERROR, "Lumiere is already running", null);
			return;
		}
		
		server_port = port;
		server = new WebServer(this.getClass().getSimpleName(), this, server_port);
		server.start();
	}
	
	public HttpResponse getSharedResponseHeader() {
		return header;
	}
	
	public int getCurrentPort() {
		return server_port;
	}
}
