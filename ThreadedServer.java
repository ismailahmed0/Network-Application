import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.*;
import java.lang.Math;

public class ThreadedServer extends Thread {
    // initialize socket, client address, times, and dateTime format
    protected Socket socket;
    protected String clientAddress; // name of the client
    protected long startTime;
    protected long elapsedTime; // how long client connected to server
    protected LocalDateTime dateTime; // when client connected to server
    protected DateTimeFormatter dateFormat; // date/time formatter

    // constructor that creates the threadedserver with appropriate functionalities
    public ThreadedServer(Socket socket) {
        // get and log starting times
        startTime = System.nanoTime();
        dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        dateTime = LocalDateTime.now();

        this.socket = socket;

        // display client ip address and port that is being connected along with the datetime
        InetSocketAddress addr = (InetSocketAddress) socket.getRemoteSocketAddress();
        clientAddress = addr.toString().replace("/", "");
        System.out.println("Client " + clientAddress + " connected on " + dateTime.format(dateFormat));
    }

    public void run() {
        try {
            // initialize input and output
            DataInputStream input = null;
            PrintStream output = null;

            // gets client request
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            output = new PrintStream(socket.getOutputStream());

            String data = ""; // store data read from client

            output.println("Format for basic math calculations: Number Operator Number. Ex: \"5 + 5\"");
            output.flush();

            // gets client requests until they say quit and logs all activities
            while (true) {
                try {
                    data = input.readUTF(); // gets client request

                    // evaluate client request
                    if (data.equalsIgnoreCase("Quit")) break;
                    String result = this.eval(data);

                    // send result to client
                    output.println(result);
                    output.flush();

                    System.out.println("Result for " + clientAddress + " request of \"" + data + "\" sent: " + result); // log the result
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            // end the connection between the client and server
            socket.close();
            input.close();
            output.close();

            System.out.println("Closed connection with client " + clientAddress + " on " + LocalDateTime.now().format(dateFormat)); // log

            // log how long client was connected to server in seconds
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Session length: " + (elapsedTime / 1_000_000_000) + " seconds");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // evaluate the non-quitting request from the client
    public String eval(String args) {
        String[] splited_args = args.split(" ");

        // invalid operation
        if (splited_args.length != 3) {
            return ("Operation not recognized. Format: Number Operator Number. Ex: \"5 + 5\"");
        }

        try {
            // split into number, operator, and number
            Double A = Double.parseDouble(splited_args[0]);
            String sym = splited_args[1];
            Double B = Double.parseDouble(splited_args[2]);

            // calculate basic math request and send result
            if (sym.equals("+")) {
                return A + B + "";
            } else if (sym.equals("-")) {
                return A - B + "";
            } else if (sym.equals("*")) {
                return A * B + "";
            } else if (sym.equals("/")) {
                return A / B + "";
            } else if (sym.equals("%")) {
                return A % B + "";
            } else if (sym.equals("^")) {
                return Math.pow(A, B) + "";
            }
        } catch (NumberFormatException e) {
            return ("Operation not recognized. Format: Number Operator Number. Ex: \"5 + 5\"");
        }

        return ("Operation not recognized. Format: Number Operator Number. Ex: \"5 + 5\""); // invalid operation
    }
}