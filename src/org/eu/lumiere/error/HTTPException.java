package org.eu.lumiere.error;

import org.eu.lumiere.net.http.HTTPResponse;

public abstract class HTTPException {
	public abstract void onException(HTTPResponse os, int httpCode, String msg);
}
