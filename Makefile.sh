all: ThreadedServer.class Server.class Client.class

# building the classes
ThreadedServer.class: ThreadedServer.java
	javac -d . -classpath . ThreadedServer.java
Server.class: Server.java
	javac -d . -classpath . Server.java
Client.class: Client.java
	javac -d . -classpath . Client.java

# clean command to remove old class files
clean:
	rm -f *.class