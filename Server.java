import java.net.*;

public class Server {
    // initialize socket and server
    private Socket socket = null;
    private ServerSocket server = null;

    // constructor for constructing multithreaded server
    public Server(int portNumber) {
        try {
            server = new ServerSocket(portNumber); // start server

            // display server ip address and port that is being started
            InetAddress addr = InetAddress.getLocalHost();
            String ipAddr[] = addr.toString().split("/");
            System.out.println("Server " + ipAddr[1].replace("/", "") + ":" + portNumber + " started");

            while (true) {
                try {
                    synchronized (server) {
                        socket = server.accept(); // wait for connection and connect once found
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                new ThreadedServer(socket).start(); // start the multithreaded server
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) {
        Server server = new Server(5000); // TCP port
    }
}