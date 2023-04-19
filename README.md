# Lumiere Server
**Lumiere Server is a web server designed for people who want to build a basic website in a quick and simple way.**<br>
**_If you want to support me, you can star the project 👀🌃_**
## Build the JAR
To build the JAR file, first clone the repository as shown below:
```bash
sudo apt update
sudo apt install git -y
git clone https://github.com/DevSnaith/lumiere-server.git
```

After the cloning is done, ``cd`` to the ``lumiere-server/`` folder. There you will find the `compile.sh` script; run it to generate the jar file.
>In case you cannot execute `compile.sh`, give it executable permission using `chmod +x ./compile.sh`.

```bash
cd lumiere-server/
chmod +x ./compile.sh
./compile.sh
```

If all goes well, you will find a new file called ``lumiere-server.jar``, Type ``java -jar lumiere-server.jar`` the output should be like the one shown below:

```
Lumiere-server version <version>
lumiere-server at github https://github.com/DevSnaith/lumiere-server
```

Now copy the ``lumiere-server.jar`` and add it to your project.

## Usage/Examples
`Lumiere` class contains the basic things that must be available for the server to work well, it manages the traffic of requests and forwards them to the `RequestHandler` interface. It also adds some properties in the header.

You need to pass a `RequestHandler` to `Lumiere`, The easiest way to do that is to use the `SimpleResponse` class that implements the `RequestHandler` interface.

#### Example using ``SimpleResponse`` class
```java
int server_port = 8080;
String body = "<b>Hello, World!</b>";
boolean contentTypeIsHTML = true; // false = text/plan

SimpleResponse requestHandler = new SimpleResponse(body, contentTypeIsHTML);

Lumiere server = new Lumiere(requestHandler);
server.bootServer(server_port); // start the server
```

If you want to make a dynamic response, use ``RequestHandler``
#### Example using ``RequestHandler`` interface
```java
/* new RequestHandler */
new Lumiere((Socket socket, HttpRequest request, HttpResponse response) -> {
  response.setContentType("text/html");
  response.push("<b>"+request.getProperty("User-Agent")+"</b>");
}).bootServer(8080);
```

You can create multiple pages for your simple server using ``HttpController``

#### Example using ``HttpController`` class
```java
HttpController controller = new HttpController();
controller.addHandler("/", new SimpleResponse("<a href=\"/info\">GO TO INFO</a>", true));
controller.addHandler("/info", new SimpleResponse("Meow", false));
controller.addHandler("/agent", (Socket socket, HttpRequest request, HttpResponse response) -> {
  response.setContentType("text/html");
  response.push("<b>"+request.getProperty("User-Agent")+"</b>");
});

new Lumiere(controller).bootServer(8080);
```

## Logger

``Lumiere-server`` includes a logger class called ``GlobalLogger``.

Use ``GlobalLogger.printf(...)`` to send a message via ``GlobalLogger``.
#### Example

```java
//                              v Msg Level    v The Msg    v obj to return   v formatting the msg
GlobalLogger.getLogger().printf(LogLevel.INFO, "Math: %d",  null,             (5 + 5));
// [INFO 0:00 AM]: Math: 10
```

You can change the message output by changing the outputstream using ``GlobalLogger.os = <OUTOUTSTREAM>;``

You can change the log pattern, the default pattern is ``[!L !D{h:mm a}]: !M\n``.

| char        | usage                                               |
|-------------|-----------------------------------------------------|
| !L          | Prints the msg level.                               |
| !M          | Print the msg.                                      |
| !CNAME      | It is used to find out where the message came from. |
| !D{pattern} | Print the date.                                     |


#### Pattern Example
```java
GlobalLogger.pattern="FROM: !CNAME | YEAR: !D{YYYY} | MSG: !M\n"
// FROM: org.eu.lumiere.net.RequestListener | YEAR: 2023 | MSG: Lumiere using port 8080
```

Disable it using ``GlobalLogger.pattern=""`` or ``GlobalLogger.os = null;``
