package server;

import java.io.FileInputStream;
import java.net.*;
import java.security.KeyStore;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class EchoServer3 {
   public static void main(String[] args) {
      Logger log = Logger.getLogger(EchoServer3.class.getName());
      int serverPort = 443;

      // Set the system properties for // Load keystore from PEM files
      System.setProperty("javax.net.ssl.keyStore", "server/certificate.pem");
      System.setProperty("javax.net.ssl.keyStoreType", "PEM");
      System.setProperty("javax.net.ssl.keyStorePassword", "admin123");

      if (args.length == 1)
         serverPort = Integer.parseInt(args[0]);
      try {
         // init ssl context
         SSLContext sslContext = SSLContext.getInstance("SSL");
         sslContext.init(null, null, null);

         // create ssl server socket factory
         SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

         try (SSLServerSocket myConnectionSocket = (SSLServerSocket) sslServerSocketFactory
               .createServerSocket(serverPort)) {

            System.out.println("Server ready.");
            while (true) {
               System.out.println("Waiting for a connection.");
               MyStreamSocket myDataSocket = new MyStreamSocket(myConnectionSocket.accept());
               System.out.println("Connection accepted");

               Thread theThread = new Thread(new EchoServerThread(myDataSocket));
               theThread.start();
            }
         }
      } catch (SocketException sx) {
         log.severe("Connection lost with client: " + sx.getMessage());
      } catch (Exception ex) {
         log.severe("Exception occurred while running server: " + ex.getMessage());
      }
   }
}
