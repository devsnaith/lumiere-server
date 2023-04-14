package org.eu.lumiere.loggers;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatException;

public class GlobalLogger {
	
	public static OutputStream os = System.out;
	public static String pattern = "[!L !D{h:mm a}]: !M\n";
	private static final GlobalLogger logger = new GlobalLogger();
	private GlobalLogger() {}
	
	public static enum LogLevel {
		INFO, WARNING, ERROR;
	}
	
	public Object printf(LogLevel l, String m, Object r, Object... args) {
		if(os == null || (m == null || m.length() <=0))
			return r;
		int index = -1;
		String output = new String(pattern);
		output = output.replaceAll("!CNAME", Thread.currentThread().getStackTrace()[2].getClassName());
		
		try {
			output = output.replaceAll("!M", args == null || args.length <= 0 ? m : String.format(m, args));
		}catch(IllegalFormatException ex) {			
			printf(LogLevel.ERROR, ex.getMessage(), null);
			output = output.replaceAll("!M", m);
		}
		
		output = output.replaceAll("!L", (l == null ? LogLevel.INFO : l).toString());
		while((index = output.indexOf("!D{")) != -1) {
			String dPattern = output.substring(index);
			dPattern = dPattern.substring(3, dPattern.indexOf("}"));			
			SimpleDateFormat format = new SimpleDateFormat(dPattern);
			output = output.replace(String.format("!D{%s}", dPattern), format.format(new Date()));
		}
		try {
			os.write(output.getBytes());
		} catch (IOException e) {
			System.err.println(e);
		}

		return r;
	}
	
	public static GlobalLogger getLogger() {
		return GlobalLogger.logger;
	}
	
}
