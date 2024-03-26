package client;

import java.io.*;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class EchoClient2 {

   private static Logger log = Logger.getLogger(EchoClient2.class.getName());
   private static final String DEFAULT_HOST = "localhost";
   private static final String DEFAULT_PORT = "7";

   public static void main(String[] args) {

      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);
      SSLSocket sslSocket = null;

      try {
         System.out.println("Welcome!");

         System.setProperty("javax.net.ssl.trustStore", "client/truststore.jks");
         System.setProperty("javax.net.ssl.trustStorePassword", "password");

         SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
         sslSocket = (SSLSocket) sslSocketFactory.createSocket(DEFAULT_HOST, Integer.parseInt(DEFAULT_PORT));

         EchoClientHelper2 helper = new EchoClientHelper2(sslSocket);

         boolean done = false;
         String message;

         boolean loggedIn = false;

         while (!loggedIn) {
            System.out.println("Please enter your username and password:");
            String credentials = br.readLine();

            String loginResult = helper.login(credentials);
            System.out.println(loginResult);

            if (loginResult.startsWith("101")) {
               loggedIn = true;
            }
         }

         while (!done) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1. Upload a message: ");
            System.out.println("2. Read a message: ");
            System.out.println("3. Read all messages: ");
            System.out.println("4. Logout");
            System.out.println("5. Quit");

            message = br.readLine();

            switch (message) {
               case "1":
                  System.out.println("Enter your message: ");
                  String userMessage = br.readLine();
                  System.out.println(helper.sendMessage(userMessage));
                  break;
               case "2":
                  System.out.println("Enter the message id: ");
                  String messageId = br.readLine();
                  System.out.println(helper.readMessage(messageId));
                  break;
               case "3":
                  System.out.println(helper.readAllMessages());
                  break;
               case "4":
                  loggedIn = false;
                  done = true;
                  
                  break;
               case "5":
                  done = true;
                  System.out.println(helper.quit());
                  break;
               default:
                  log.warning("Invalid option. Please try again.");
                  break;
            }
         }
      } catch (Exception ex) {
         log.severe("No connection to server. Please try again later: " + ex.getMessage());
      } finally {
         try {
            if (sslSocket != null && !sslSocket.isClosed()) {
               sslSocket.close();
            }
         } catch (IOException e) {
            log.severe("Error closing socket: " + e.getMessage());
         }
      }
   }
}