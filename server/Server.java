package server;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class Server {

   private static Logger log = Logger.getLogger(Server.class.getName());
   final static int SERVER_PORT = 7;

   public static void main(String[] args) {

      try {
         char[] ksPass = "password".toCharArray();
         KeyStore ks = KeyStore.getInstance("JKS");
         FileInputStream ksFile = new FileInputStream("server/self_signed.jks");
         ks.load(ksFile, ksPass);

         KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
         kmf.init(ks, ksPass);

         SSLContext sslContext = SSLContext.getInstance("TLS");
         sslContext.init(kmf.getKeyManagers(), null, null);

         SSLServerSocketFactory sslServerSocketfactory = sslContext.getServerSocketFactory();
         SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(SERVER_PORT);

         System.out.println("Server ready.");

         while (true) {
            
            MyStreamSocket myDataSocket = new MyStreamSocket(sslServerSocket.accept());
            System.out.println("Waiting for a connection.");
            Thread theThread = new Thread(new ServerThread(myDataSocket));
            System.out.println("Connection accepted.");
            theThread.start();
         }
      } catch (Exception e) {
         log.severe("Exception occurred while running server: " + e.getMessage());
      }
   }
}
