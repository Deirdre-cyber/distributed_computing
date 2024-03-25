import java.awt.BorderLayout;
import java.io.*;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This module contains the presentaton logic of an Echo Client.
 * 
 * @author M. L. Liu
 */
public class EchoClient2 extends JFrame {

   private static Logger log = Logger.getLogger(EchoClient2.class.getName());

   public static void main(String[] args) {
      JTextArea textArea;

      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);

      // gui code
      EchoClient2 client = new EchoClient2();
      client.setTitle("Protocol Client");
      client.setSize(600, 400);
      client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // text area
      textArea = new JTextArea();
      textArea.setBounds(10, 10, 560, 340);
      client.add(textArea);

      JPanel inputPanel = new JPanel();
      inputPanel.setLayout(new BorderLayout());

      JTextField inputField = new JTextField();
      JButton sendButton = new JButton("Send");
      inputPanel.add(inputField, BorderLayout.CENTER);
      inputPanel.add(sendButton, BorderLayout.EAST);
      client.add(inputPanel, BorderLayout.SOUTH);

      client.setVisible(true);

      try {
         textArea.append("Welcome! \nWhat is the name of the server host?\n");
         String hostName = br.readLine();
         if (hostName.length() == 0) // if user did not enter a name
            hostName = "localhost"; // use the default host name
         System.out.println("What is the port number of the server host?");
         String portNum = br.readLine();
         if (portNum.length() == 0)
            portNum = "7"; // default port number
         EchoClientHelper2 helper = new EchoClientHelper2(hostName, portNum);
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

      } // end try
      catch (Exception ex) {
         log.severe("No connection to server. Please try again later: " + ex.getMessage());
      } // end catch
   } // end main
} // end class
