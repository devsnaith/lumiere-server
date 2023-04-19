package org.eu.lumiere;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.TimeZone;

import org.eu.lumiere.error.ErrorHandler;
import org.eu.lumiere.error.HTTPException;
import org.eu.lumiere.loggers.GlobalLogger;
import org.eu.lumiere.loggers.GlobalLogger.LogLevel;
import org.eu.lumiere.net.RequestHandler;
import org.eu.lumiere.net.RequestListener;
import org.eu.lumiere.net.ServerEvents;
import org.eu.lumiere.net.http.HTTPController;
import org.eu.lumiere.net.http.HTTPRequest;
import org.eu.lumiere.net.http.HTTPResponse;
import org.eu.lumiere.utils.SimpleResponse;

public class Lumiere implements ServerEvents {
	
	private GlobalLogger l = GlobalLogger.getLogger();
	private HTTPException hEx;
	
	private int server_port = 8080;
	private RequestListener server;
	
	private HTTPResponse header;
	private RequestHandler request;
	
	private HashMap<HTTPRequest, Socket> requests;
	
	public Lumiere(RequestHandler rHandler) {
		initialize(rHandler, null);
	}
	
	public Lumiere(RequestHandler rHandler, HTTPException ex) {
		initialize(rHandler, ex == null ? ErrorHandler.getHandler() : ex);
	}
	
	private void initialize(RequestHandler rHandler, HTTPException ex) {
		this.hEx = ex == null ? ErrorHandler.getHandler() : ex;
		if((this.request = rHandler) == null) {
			l.printf(LogLevel.ERROR, "Lumiere 'requestHandler' CANNOT BE NULL", null);
			return;
		}
		header = new HTTPResponse(null, "HTTP/1.1 200 OK", "text/html");
		header.setProperty("Accept-Ranges", "bytes");
		ErrorHandler.getHandler().debug = false;
		ErrorHandler.getHandler().initialize();
		requests = new HashMap<>();
	}
	
	@Override
	public void onConnection(Socket socket) {
		OutputStream s_os = null;
		HTTPRequest http_in = null;
		HTTPResponse http_out = null;
		
		try {			
			s_os = socket.getOutputStream();
			http_out = new HTTPResponse(s_os, header.getStatus(), header.getContentType());
			for(String key : header.getKeySet()) {				
				http_out.setProperty(key, header.getProperty(key));
			}			
			SimpleDateFormat gmtDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
			gmtDate.setTimeZone(TimeZone.getTimeZone("GMT"));
			http_out.setProperty("Date", gmtDate.format(new Date()));

			http_in = new HTTPRequest(socket.getInputStream());
		}catch(NoSuchElementException ex) {
			hEx.onException(http_out, 400, "The request you sent is not a valid request.");
		}catch(IOException ex) {
			l.printf(LogLevel.ERROR, ex.getMessage(), null);
			hEx.onException(http_out, 503, "");
		}
		
		if(http_in != null)
			request.onRequestReceived(socket, http_in, http_out);

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
		double lTIme = System.nanoTime();
		server = new RequestListener(this.getClass().getSimpleName(), this, server_port);
		server.start();
		
		if(server.isRunning())
			l.printf(LogLevel.INFO, "Lumiere server restarted after %f seconds", null, (double) ((System.nanoTime() - lTIme) / 1000000000));
		
	}
	
	public Socket getClient(HTTPRequest request) {
		return requests.get(request);
	}
	
	public HTTPResponse getSharedResponseHeader() {
		return header;
	}
	
	public int getCurrentPort() {
		return server_port;
	}
	
	public static void main(String[] args) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Lumiere-server version 1.0").append("\n");
		sb.append("lumiere-server at github https://github.com/DevSnaith/lumiere-server").append("\n");
		System.out.write(sb.toString().getBytes());
	}
}
