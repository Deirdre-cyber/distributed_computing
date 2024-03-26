import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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

      textArea = new JTextArea();
      textArea.setEditable(false);
      client.add(textArea);

      JPanel inputPanel = new JPanel();
      inputPanel.setLayout(new BorderLayout());

      JTextField inputField = new JTextField();
      JButton sendButton = new JButton("OK");
      inputPanel.add(inputField, BorderLayout.CENTER);
      inputPanel.add(sendButton, BorderLayout.EAST);
      client.add(inputPanel, BorderLayout.SOUTH);

      sendButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             String message = inputField.getText();
             if (!message.isEmpty()) {
                 textArea.append("You: " + message + "\n");
                 // Handle sending the message to the server here
                 // EchoClientHelper2 helper = new EchoClientHelper2(hostName, portNum);
                 // String response = helper.sendMessage(message);
                 // textArea.append("Server: " + response + "\n");
                 inputField.setText(""); // Clear the input field after sending message
             }
         }
     });

     client.setVisible(true);

      try {
         textArea.append("Welcome! \nWhat is the name of the server host?\n");
         //System.out.println("Welcome! \nWhat is the name of the server host?\n");
         String hostName = br.readLine();
         if (hostName.length() == 0)
            hostName = "localhost";
         textArea.append("What is the port number of the server host?\n");
         //System.out.println("What is the port number of the server host?");
         String portNum = br.readLine();
         if (portNum.length() == 0)
            portNum = "7";
         EchoClientHelper2 helper = new EchoClientHelper2(hostName, portNum);
         boolean done = false;
         String message;

         boolean loggedIn = false;

         while (!loggedIn) {
            textArea.append("Please enter your username and password:\n");
            //System.out.println("Please enter your username and password:");
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
      }
      catch (Exception ex) {
         log.severe("No connection to server. Please try again later: " + ex.getMessage());
      }
   }
}
