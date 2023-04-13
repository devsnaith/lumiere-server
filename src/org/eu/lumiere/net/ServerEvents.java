package org.eu.lumiere.net;

import java.net.Socket;

public interface ServerEvents {
	public void onConnection(Socket socket);
}
