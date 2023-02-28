import java.net.*;
import java.io.*;

public class Client {
    // initialize socket, inputs, and output
    private Socket socket = null;
    private BufferedReader input = null; // datainputstream readline deprecated
    private DataOutputStream output = null;
    private BufferedReader server_input = null; // what the server sends the client

    // constructor for connecting to server and passing requests
    public Client(String serverAddress, int portNumber) {
        try {
            socket = new Socket(serverAddress, portNumber); // connect to server
            System.out.println("Connected (type 'quit' to close the connection)");

            // get input from user
            input = new BufferedReader(new InputStreamReader(System.in));

            // for when you send requests to server
            output = new DataOutputStream(socket.getOutputStream());

            // for when you ned to get input from server
            server_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (Exception e) {
            System.out.println(e);
        }

        // text to store read values
        String data = "";
        String result = "";

        try {
            result = server_input.readLine(); // get input from server
            System.out.println(result);

        } catch (Exception e) {
            System.out.println(e);
        }

        // client can keep sending requests until they say quit
        while (!data.equalsIgnoreCase("Quit")) {
            // read from keyboard
            try {
                data = input.readLine();
                output.writeUTF(data);
                output.flush();
            } catch (Exception e) {
                System.out.println(e);
            }
            // read from server
            try {
                if (!data.equalsIgnoreCase("Quit")) {
                    result = server_input.readLine();
                    System.out.println(result);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        // make sure to quit without crashing
        try {
            input.close();
            output.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) {
        Client client = new Client("127.0.0.1", 5000); // TCP on local host
    }
}