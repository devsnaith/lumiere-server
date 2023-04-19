package org.eu.lumiere.error;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.eu.lumiere.loggers.GlobalLogger;
import org.eu.lumiere.loggers.GlobalLogger.LogLevel;
import org.eu.lumiere.net.http.HTTPResponse;

public class ErrorHandler extends HTTPException {
	
	public boolean debug = true;
	private static GlobalLogger l = GlobalLogger.getLogger();
	private final HashMap<Integer, String> errors = new HashMap<>();
	private static ErrorHandler errorHandler = new ErrorHandler();
	private int defaultErrorCode = 500;
	
	public void setError(int errorCode, String statusLine) {
		errors.put(errorCode, (String) (debug ? l.printf(LogLevel.INFO, "[%d:%s] added to ErrorHandler", statusLine, errorCode, statusLine) : statusLine));
	}
	
	public void initialize() {
		setError(500, "Internal Server Error");
		setError(501, "Not Implemented");
		setError(503, "Service Unavailable");
		setError(400, "Bad Request");		
		setError(401, "Unauthorized");
		setError(403, "Forbidden");
		setError(404, "Not Found");
		setError(411, "Length Required");
		setError(414, "URI Too Long");
		setError(423, "Locked");
		setError(429, "Too Many Requests");
	}
	
	@Override
	public void onException(HTTPResponse res, int httpCode, String msg) {
		String[] sLine = getStatusLine(httpCode).split(" ", 3);
		StringBuilder htmlBuilder = new StringBuilder("<!DOCTYPE html><html lang=\"en\">\n<head><meta charset=\"UTF-8\">\n");
		htmlBuilder.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">").append("\n");
		htmlBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">").append("\n");		
		htmlBuilder.append("<title>").append(String.format("%s %s", sLine[1], sLine[2])).append("</title>").append("\n<head>");
		htmlBuilder.append("<body style=\"color: white; background-color: #000000;\">").append("\n");
		htmlBuilder.append("<b><code>").append(String.format("%s %s", sLine[1], sLine[2])).append("</code></b><br><code>").append(msg);				
		res.setStatus(String.format("%s %s %s", sLine[0], sLine[1], sLine[2]));
		res.setContentType("text/html");		
		res.push(htmlBuilder.append("</code></body></html>").toString());
	}

	public void generateHTML(OutputStream os, int httpCode, String msg) {
		HTTPResponse r = new HTTPResponse(os, null, null);
		onException(r, httpCode, msg);
	}

	public byte[] generateHTMLAsBytes(int httpCode, String msg) {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		generateHTML(byteArray, httpCode, msg);		
		return byteArray.toByteArray();
	}

	public String generateHTMLAsString(int httpCode, String msg) {
		return new String(generateHTMLAsBytes(httpCode, msg));
	}
	
	public synchronized String getStatusLine(int httpCode) {
		if(!errors.containsKey(httpCode))
			return errors.containsKey(defaultErrorCode) ? getStatusLine(defaultErrorCode) : "HTTP/1.0 200 OK";
		return String.format("HTTP/1.0 %d %s", httpCode, errors.get(httpCode));
	}
	public static ErrorHandler getHandler() {
		return errorHandler;
	}

}
