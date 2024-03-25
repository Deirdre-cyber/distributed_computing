import java.io.*;
import java.util.logging.Logger;

/**
 * This module contains the presentaton logic of an Echo Client.
 * @author M. L. Liu
 */
public class EchoClient2 {

   private static Logger log = Logger.getLogger("EchoClient2");

   public static void main(String[] args) {
      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);
      try {
         System.out.println("Welcome!\n" +
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
            System.out.println("Please choose an option:");
            System.out.println("1. Upload a message: ");
            System.out.println("2. Read a message: ");
            System.out.println("3. Read all messages: ");
            System.out.println("4. Logout");

            message = br.readLine( );
            
            switch (message) {
               case "1":
                  System.out.println("Enter your message: ");
                  String userMessage = br.readLine( );
                  System.out.println(helper.sendMessage(userMessage));
                  break;
               case "2":
                  System.out.println("Enter the message id: ");
                  String messageId = br.readLine( );
                  System.out.println("id entered" + messageId);  //DEBUG
                  System.out.println(helper.readMessage(messageId));
                  break;
               case "3":
                  System.out.println(helper.readAllMessages());
                  break;
               case "4":
                  done = true;
                  helper.logout();
                  break;
               default:
                  System.out.println("Invalid option. Please try again.");
                  break;
            }
          }

      } // end try  
      catch (Exception ex) {
         log.severe("No connection to server. Please try again later: " + ex.getMessage());
      } //end catch
   } //end main
} // end class
