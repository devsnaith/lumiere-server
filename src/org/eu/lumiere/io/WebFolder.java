package org.eu.lumiere.io;

import java.io.File;
import java.net.Socket;

import org.eu.lumiere.net.RequestHandler;
import org.eu.lumiere.net.http.HTTPController;
import org.eu.lumiere.net.http.HTTPRequest;
import org.eu.lumiere.net.http.HTTPResponse;

public class WebFolder implements RequestHandler {

	@SuppressWarnings("unused")
	private HTTPController controller;
	
	public WebFolder(File folder) {
		if(folder == null)
			throw new NullPointerException("WebFolder folder path cannot == null");
		else if(!folder.isDirectory())
			throw new IllegalAccessError("WebFolder need a directory");
		controller = new HTTPController();
	}
	
	@Override
	public void onRequestReceived(Socket socket, HTTPRequest request, HTTPResponse response) {
		
	}
	
}
