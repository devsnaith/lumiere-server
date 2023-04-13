package org.eu.lumiere.utils;

import org.eu.lumiere.net.http.HttpRequest;
import org.eu.lumiere.net.http.HttpRequestHandler;
import org.eu.lumiere.net.http.HttpResponse;

public class SimpleResponse implements HttpRequestHandler {
	
	private String body = "BasicResponse is running";
	private boolean html_response = false;
	
	public SimpleResponse(String body, boolean rhtml) {
		this.body = body == null ? this.body : body;
		this.html_response = rhtml;
	}
	
	@Override
	public void onRequestReceived(HttpRequest request, HttpResponse response) {
		response.setContentType(String.format("text/%s", html_response ? "html" : "plan"));
		response.push(body);
	}	
	
}
