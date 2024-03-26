import java.net.*;

import javax.net.ssl.SSLSocket;

import java.io.*;

public class EchoClientHelper2 {

   private MyStreamSocket mySocket;
   private InetAddress serverHost;
   private int serverPort;

   EchoClientHelper2(String hostName,
         String portNum) throws SocketException,
         UnknownHostException, IOException {

      this.serverHost = InetAddress.getByName(hostName);
      this.serverPort = Integer.parseInt(portNum);
      this.mySocket = new MyStreamSocket(this.serverHost,
            this.serverPort);
      System.out.println("Connection request made");
   }

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
}
