import java.net.*;
import java.util.logging.Logger;

public class EchoServer3 {
   public static void main(String[] args) {
      Logger log = Logger.getLogger(EchoServer3.class.getName());	
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
         log.severe("Connection lost with client: " + sx.getMessage());
      }
      catch (Exception ex) {
         log.severe("Exception occurred while running server: " + ex.getMessage());
      }
   }
}
