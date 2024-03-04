import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {
    static final String endMessage = ".";

    public static void main(String[] args) {
        try {
            // Prompt user for server host and port
            String hostName = "localhost"; // Default host
            int portNumber = 5000; // Default port

            // Connect to the server
            Socket socket = new Socket(hostName, portNumber);

            // Set up input and output streams
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String inputLine;
            while (true) {
                System.out.println("Enter a line to receive an echo from the server, or a single period (.) to quit.");
                inputLine = userInput.readLine();
                
                // Check for termination condition
                if (inputLine.equals(endMessage)) {
                    break;
                }
                
                // Send user input to the server
                out.println(inputLine);
            }

            // Close the socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
