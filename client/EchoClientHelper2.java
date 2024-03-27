package client;
import java.net.*;

import javax.net.ssl.SSLSocket;

import java.io.*;

public class EchoClientHelper2 {

   private MyStreamSocket mySocket;
   private InetAddress serverHost;
   private int serverPort;

   // constructor needed to achieve ssl
   EchoClientHelper2(SSLSocket sslSocket) throws IOException {
      try {
         this.mySocket = new MyStreamSocket(sslSocket);
         System.out.println("Connection request made");
      } catch (UnknownHostException e) {
         throw new IOException ("Unknown host: " + e.getMessage());
      } catch (SocketException e) {
         throw new IOException ("Unable to create socket." + e.getMessage());
      }
   }

   EchoClientHelper2(String hostName, String portNum) throws IOException {
      try {
         this.serverHost = InetAddress.getByName(hostName);
         this.serverPort = Integer.parseInt(portNum);
         this.mySocket = new MyStreamSocket(this.serverHost, this.serverPort);
         System.out.println("Connection request made");
      } catch (UnknownHostException e) {
         throw new IOException ("Unknown host: " + hostName + "\n" +e.getMessage());
      } catch (SocketException e) {
         throw new IOException ("Unable to create socket." + "\n" + e.getMessage());
      }
   }

   public String login(String credentials) throws IOException {
      String login = "LOGIN " + credentials;

      try{
         mySocket.sendMessage(login);
      return mySocket.receiveMessage();
      } catch (IOException e) {
         return "Error loggin in: " + e.getMessage();
      }
   }

   public String sendMessage(String userMessage) throws IOException{
      String message = "UPLOAD " + userMessage;
      
      try {
         mySocket.sendMessage(message);
         return mySocket.receiveMessage();
      } catch (IOException e) {
         throw new IOException("Error sending message: " + e.getMessage());
      }
   }

   public String readMessage(String messageId) throws IOException {
      String message = "DOWNLOAD " + messageId;
      try {
         mySocket.sendMessage(message);
         return mySocket.receiveMessage();
      } catch (IOException e) {
         throw new IOException("Error reading message: " + e.getMessage());
      }
   }

   public String readAllMessages() throws IOException {
      String message = "DOWNLOAD_ALL";
      try{
         mySocket.sendMessage(message);
         return mySocket.receiveMessage();
      } catch (IOException e) {
         throw new IOException("Error reading all messages: " + e.getMessage());
      }
   }

   public String logout() throws IOException {
      String message = "LOGOUT";
      try{
         mySocket.sendMessage(message);
         return mySocket.receiveMessage();
      } catch (IOException e) {
         throw new IOException("Error logging out: " + e.getMessage());
      }
   }

   public String quit() throws IOException {
      String message = "QUIT";
      try{
         mySocket.sendMessage(message);
         String response = mySocket.receiveMessage();
         mySocket.close();
         return response;
      } catch (IOException e) {
         throw new IOException("Error quitting program: " + e.getMessage());
      }
   }
}
