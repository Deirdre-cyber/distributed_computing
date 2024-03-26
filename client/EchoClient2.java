package client;

import java.io.*;
import java.net.ConnectException;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class EchoClient2 {

   private static Logger log = Logger.getLogger(EchoClient2.class.getName());

   public static void main(String[] args) {

      // Load truststore from PEM file
      System.setProperty("javax.net.ssl.trustStore", "client/certificate.pem");
      System.setProperty("javax.net.ssl.trustStoreType", "PEM");
      System.setProperty("javax.net.ssl.trustStorePassword", "admin123");

      try {
         SSLContext sslContext = SSLContext.getInstance("SSL");
         sslContext.init(null, null, null);

         SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

         try (SSLSocket socket = (SSLSocket) socketFactory.createSocket("127.0.0.1", 443)) {

            System.out.println("Connection established");

            socket.startHandshake();
            communicateWithServer(socket);

         } catch (SSLHandshakeException e) {
            log.severe("Handshake unsuccessful: " + e.getMessage());
            throw new RuntimeException(e);
         } catch (ConnectException e) {
            log.severe("Connection refused: " + e.getMessage());
         }
      } catch (Exception e) {
         log.severe("Error creating socket: " + e.getMessage());
      }
   }

   private static void communicateWithServer(SSLSocket socket) {
      try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            PrintWriter os = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            BufferedReader b = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"))) {

         System.out.println("Welcome");

         EchoClientHelper2 helper = new EchoClientHelper2("127.0.0.1", "443");

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
                  done = true;
                  System.out.println(helper.logout());
                  break;
               default:
                  log.warning("Invalid option. Please try again.");
                  break;
            }
         }
      } catch (Exception ex) {
         log.severe("No connection to server. Please try again later: " + ex.getMessage());
      } finally {
         log.info("Connection closed.");
      }
   }
}
