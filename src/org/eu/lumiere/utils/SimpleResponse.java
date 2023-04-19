package org.eu.lumiere.utils;

import java.net.Socket;

import org.eu.lumiere.net.RequestHandler;
import org.eu.lumiere.net.http.HTTPRequest;
import org.eu.lumiere.net.http.HTTPResponse;

public class SimpleResponse implements RequestHandler {
	
	private String body = "BasicResponse is running";
	private boolean html_response = false;
	
	public SimpleResponse(String body, boolean rhtml) {
		this.body = body == null ? this.body : body;
		this.html_response = rhtml;
	}
	
	@Override
	public void onRequestReceived(Socket socket, HTTPRequest request, HTTPResponse response) {
		response.setContentType(String.format("text/%s", html_response ? "html" : "plan"));
		response.push(body);
	}	
	
}
