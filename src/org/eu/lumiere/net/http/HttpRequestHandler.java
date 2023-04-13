package org.eu.lumiere.net.http;

public interface HttpRequestHandler{
	public void onRequestReceived(HttpRequest request, HttpResponse response);
}
