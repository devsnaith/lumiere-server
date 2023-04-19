package org.eu.lumiere.net;

import java.net.Socket;

import org.eu.lumiere.net.http.HTTPRequest;
import org.eu.lumiere.net.http.HTTPResponse;

public interface RequestHandler{
	public void onRequestReceived(Socket socket, HTTPRequest request, HTTPResponse response);
}
