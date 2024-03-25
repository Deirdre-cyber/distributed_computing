import java.io.*;
import java.net.*;
import java.util.logging.Logger;

/**
 * This module contains the application logic of an echo server
 * which uses a stream-mode socket for interprocess communication.
 * Unlike EchoServer2, this server services clients concurrently.
 * A command-line argument is required to specify the server port.
 * 
 * @author M. L. Liu
 */

// Handles logic of client session
// This is the main class responsible for setting up the server socket and
// accepting incoming connections
// It spawns new threads (instances of EchoServerThread) to handle each client
// session concurrently.

public class EchoServer3 {
   public static void main(String[] args) {
      Logger log = Logger.getLogger("EchoServer3");	
      int serverPort = 7;

      if (args.length == 1)
         serverPort = Integer.parseInt(args[0]);
      try {
         try (ServerSocket myConnectionSocket = new ServerSocket(serverPort)) {
            System.out.println("Server ready.");
            while (true) {
               System.out.println("Waiting for a connection.");
               MyStreamSocket myDataSocket = new MyStreamSocket(myConnectionSocket.accept());
               System.out.println("Connection accepted");

               Thread theThread = new Thread(new EchoServerThread(myDataSocket));
               theThread.start();
            }
         }
         
      }
      catch (SocketException sx) {
         log.severe("Connection lost with client: " + sx);
      }
      catch (Exception ex) {
         log.severe("Exception occurred while running server: " + ex);
         ex.printStackTrace();
      }
      
   }
}
