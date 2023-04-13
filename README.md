# Lumiere Server 💻👀
Simple and lightweight web server for creating basic pages using Java ⭐

```java
import org.eu.lumiere.Lumiere;
import org.eu.lumiere.utils.BasicResponse;

public class MyServer {
	
	public static void main(String[] args) {
		Lumiere server = new Lumiere(new BasicResponse("Hello, World", false));
		server.bootServer(8080);
	}
	
}
```
