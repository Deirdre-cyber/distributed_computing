import java.io.*;
import java.util.logging.Logger;

/**
 * This module contains the presentaton logic of an Echo Client.
 * @author M. L. Liu
 */
public class EchoClient2 {

   private static Logger log = Logger.getLogger("EchoClient2");

   static final String endMessage = "Logout";
   static final String userName = "admin";
   static final String password = "admin";
   
   public static void main(String[] args) {
      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);
      try {
         System.out.println("Welcome to the Echo client.\n" +
            "What is the name of the server host?");
         String hostName = br.readLine();
         if (hostName.length() == 0) // if user did not enter a name
            hostName = "localhost";  //   use the default host name
         System.out.println("What is the port number of the server host?");
         String portNum = br.readLine();
         if (portNum.length() == 0)
            portNum = "7";          // default port number
         EchoClientHelper2 helper = 
            new EchoClientHelper2(hostName, portNum);
         boolean done = false;
         String message, echo;

         while (!done) {
            System.out.println("Welcome to the Echo Client. Please enter your username and password:");
            message = br.readLine( );
            if ((message.trim()).equals (endMessage)){
               done = true;
               helper.done( );
            }
            else {
               echo = helper.getEcho( message);
               System.out.println(echo);
            }
          }

      } // end try  
      catch (Exception ex) {
         System.out.println("No connection to server. Please try again later.");
         log.severe("Error: " + ex.getMessage());

         //ex.printStackTrace( );
      } //end catch
   } //end main
} // end class
