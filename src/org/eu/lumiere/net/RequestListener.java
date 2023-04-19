package org.eu.lumiere.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.eu.lumiere.loggers.GlobalLogger;
import org.eu.lumiere.loggers.GlobalLogger.LogLevel;

public class RequestListener implements Runnable {
	
	private GlobalLogger l = GlobalLogger.getLogger();
	private String threadName = "Lumiere server thread";
	private boolean isRunning = false;
	private Thread thread;
	
	private int port = 8080;
	private ServerSocket server;
	private ServerEvents e;
		
	public RequestListener(String name, ServerEvents e, int port) {
		this.threadName = name == null ? this.threadName : name;
		this.port = port;
		this.e = e;
	}
	
	public synchronized void start() {
		if(isRunning || thread != null) {
			l.printf(LogLevel.WARNING, threadName + " is already running", null);
			return;
		}
		
		try {
			server = new ServerSocket(port);
			thread = new Thread(this, threadName);
			isRunning = true;
			thread.start();
		} catch (Exception ex) {
			l.printf(LogLevel.ERROR, ex instanceof IllegalArgumentException ? 
					"Server port must be between 0 and 65535" : ex.getMessage(), null);
		}
		
	}
	
	public synchronized void stop() {
		if(!isRunning || thread == null) {
			l.printf(LogLevel.WARNING, threadName + " is already running", null);
			return;
		}
		
		thread.interrupt();
		isRunning = false;
		thread = null;
		
		if(server != null) {
			try {
				server.close();
			} catch (IOException e) {
				l.printf(LogLevel.ERROR, e.getMessage(), null);
			}
		}
	}
	
	@Override
	public void run() {
		l.printf(LogLevel.INFO, threadName + " using port " + getPort(), null);
		while(isRunning && !thread.isInterrupted()) {
			if(server != null) {
				try {
					Socket s = server.accept();
					if(e != null)
						e.onConnection(s);
					} catch (IOException ex) {
						l.printf(LogLevel.ERROR, ex.getMessage(), null);
					}			
				continue;
			}
			stop();
		}
		l.printf(LogLevel.INFO, threadName + " has been stopped", null);
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public int getPort() {
		return this.port;
	}
	
}
