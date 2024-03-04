import java.io.IOException;
import java.net.ServerSocket;

public class EchoServer2 {
    static final String endMessage = ".";

    public static void main(String[] args) {
        int serverPort = 5000; // Default port

        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server is listening...");

            while (true) {
                MyStreamSocket myDataSocket = new MyStreamSocket(serverSocket.accept());
                System.out.println("Connection accepted");

                boolean done = false;
                while (!done) {
                    String message = myDataSocket.receiveMessage();
                    System.out.println("Message received: " + message);

                    if (message.trim().equals(endMessage)) {
                        System.out.println("Session over.");
                        myDataSocket.close();
                        done = true;
                    } else {
                        myDataSocket.sendMessage(message);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
