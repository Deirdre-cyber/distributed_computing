package server;

import java.io.FileInputStream;
import java.net.*;
import java.security.KeyStore;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class EchoServer3 {
   public static void main(String[] args) {
      Logger log = Logger.getLogger(EchoServer3.class.getName());
      int serverPort = 443;

      // Set the system properties for // Load keystore from PEM files
      System.setProperty("javax.net.ssl.keyStore", "server/mykeystore.jks");
      System.setProperty("javax.net.ssl.keyStoreType", "JKS");
      System.setProperty("javax.net.ssl.keyStorePassword", "admin123");

      if (args.length == 1)
         serverPort = Integer.parseInt(args[0]);
      try {

         // load keystpre
         String ksName = "server/mykeystore.jks";
         char ksPass[] = "admin123".toCharArray();
         KeyStore ks = KeyStore.getInstance("JKS");
         ks.load(new FileInputStream(ksName), ksPass);

         // init key manager factory
         KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
         kmf.init(ks, ksPass);

         // init ssl context
         SSLContext sslContext = SSLContext.getInstance("TLS");
         sslContext.init(kmf.getKeyManagers(), null, null);

         // create ssl server socket factory
         SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
         SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(serverPort);

         System.out.println("Server ready.");

         while (true) {
            System.out.println("Waiting for a connection.");
            SSLSocket myConnectionSocket = (SSLSocket) sslServerSocket.accept();
            MyStreamSocket myDataSocket = new MyStreamSocket(myConnectionSocket);


            System.out.println("Connection accepted");

            Thread theThread = new Thread(new EchoServerThread(myDataSocket));
            theThread.start();
         }
      } catch (Exception e) {
         log.severe("Exception occurred while running server: " + e.getMessage());
      }
   }
}
