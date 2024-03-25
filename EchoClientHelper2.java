import java.net.*;

import javax.net.ssl.SSLSocket;

import java.io.*;

/**
 * This class is a module which provides the application logic
 * for an Echo client using stream-mode socket.
 * 
 * @author M. L. Liu
 */

public class EchoClientHelper2 {

   private MyStreamSocket mySocket;
   private InetAddress serverHost;
   private int serverPort;

   EchoClientHelper2(String hostName,
         String portNum) throws SocketException,
         UnknownHostException, IOException {

      this.serverHost = InetAddress.getByName(hostName);
      this.serverPort = Integer.parseInt(portNum);
      // Instantiates a stream-mode socket and wait for a connection.
      this.mySocket = new MyStreamSocket(this.serverHost,
            this.serverPort);
      /**/ System.out.println("Connection request made");
   } // end constructor

   public String login(String credentials) throws SocketException, IOException {
      String login = "LOGIN " + credentials;
      mySocket.sendMessage(login);
      return mySocket.receiveMessage();
   }

   public String sendMessage(String userMessage) throws SocketException, IOException {

      String message = "UPLOAD " + userMessage;
      mySocket.sendMessage(message);
      return mySocket.receiveMessage();
   }

   public String readMessage(String messageId) throws SocketException, IOException {

      String message = "DOWNLOAD " + messageId;
      mySocket.sendMessage(message);
      return mySocket.receiveMessage();
   }

   public String readAllMessages() throws SocketException, IOException {
      String message = "DOWNLOAD_ALL";
      mySocket.sendMessage(message);
      return mySocket.receiveMessage();
   }

   public String logout() throws IOException {
      String message = "LOGOUT";

      mySocket.sendMessage(message);
      String response = mySocket.receiveMessage();
      mySocket.close();
      return response;
      
   }
} // end class
