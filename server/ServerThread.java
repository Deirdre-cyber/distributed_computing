package server;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

class ServerThread implements Runnable {

   private MyStreamSocket myDataSocket;
   private Logger log = Logger.getLogger(ServerThread.class.getName());
   private HashMap<Integer, String> messages;
   private int messageId = 100;
   private final String TOP_BORDER = "\n-+--+--+-+--+-+--+-+--+--+--+--+-\n";
   private final String BOTTOM_BORDER = "-+--+--+-+--+-+--+-+--+--+--+--+-";

   ServerThread(MyStreamSocket myDataSocket) {
      this.myDataSocket = myDataSocket;
      messages = new HashMap<>();
   }

   public void run() {
      boolean done = false;
      String message;

      try {
         // ensure ssl is actually being implemented
         SSLSession sslSession = ((SSLSocket) myDataSocket.getSocket()).getSession();
         log.info("Protocol: " + sslSession.getProtocol());
         log.info("Host: " + sslSession.getPeerHost());

         while (!done) {
            message = myDataSocket.receiveMessage();
            System.out.println("Received: " + message);

            if (message.startsWith("LOGIN")) {
               String credentials = message.substring(6);
               login(credentials);

            } else if (message.startsWith("UPLOAD")) {
               String userMessage = message.substring(7);
               upload(userMessage);

            } else if (message.startsWith("DOWNLOAD ")) {
               String messageId = message.substring(9);
               int id = Integer.parseInt(messageId);
               download(id);

            } else if (message.startsWith("DOWNLOAD_ALL")) {
               downloadAll();

            } else if (message.startsWith("LOGOUT")){
               logout();
               done = true;

            } else if (message.equals("QUIT")) {
               quit();
               done = true;

            } else {
               myDataSocket.sendMessage("Invalid command");
            }
         }
      } catch (Exception ex) {
         log.warning("Exception caught in thread: " + ex);
      }
   }

   private void login(String credentials) {
      try {
         StringBuilder message = new StringBuilder();
         message.append(TOP_BORDER);

         String[] parts = credentials.split(" ");
         if (parts.length != 2 || parts == null) {
            message.append("| 103 Invalid credentials format |\n| Please try again               |\n| Check logs for details         |\n");
            message.append(BOTTOM_BORDER);
            myDataSocket.sendMessage(message.toString());
            log.warning("Invalid credentials format: " + credentials + "\nMust be 'username password'");
            return;
         }

         String username = parts[0];
         String password = parts[1];

         if (authenticate(username, password)) {
            message.append("| 101 Login successful          |\n|  Welcome to [ protocol name ] |\n" + BOTTOM_BORDER);
            myDataSocket.sendMessage(message.toString());
         } else {
            message.append("| 102 Login unsuccessful        |\n| Please try again              |\n| Check logs for details        |\n");
            myDataSocket.sendMessage(message.toString() + BOTTOM_BORDER);
            log.warning("Login unsuccessful\nIncorrect credentials: " + credentials);
         }
      } catch (IOException e) {
         log.severe("Error handling login: " + e.getMessage());
      }
   }

   private boolean authenticate(String username, String password) {
      return username.equals("admin") && password.equals("admin");
   }

   private void upload(String userMessage) {
      StringBuilder message = new StringBuilder();
      message.append(TOP_BORDER);

      if (userMessage != null && !userMessage.isEmpty()) {
         messages.put(messageId, userMessage);
         try {
            message.append("| 201 Upload successful         |\n| Message ID: " + messageId + "               |\n");
            myDataSocket.sendMessage(message.toString() + BOTTOM_BORDER);
            messageId++;
         } catch (IOException e) {
            log.severe("Error sending message: " + e.getMessage());
         }
      } else {
         try {
            message.append("| 202 Upload unsuccessful       |\n| Attempted to upload           |\n| empty message.                |\n" );
            myDataSocket.sendMessage(message.toString() + BOTTOM_BORDER);
         } catch (IOException e) {
            log.severe("Error sending error message: " + e.getMessage());
         }
      }
   }

   private void download(int id) {
      StringBuilder message = new StringBuilder();
      message.append(TOP_BORDER);
      try {
         if (messages.containsKey(id)) {
            message.append("| 301 Download successful       |\n| Message ID: " + id + "               |\n " + messages.get(id) + " \n");
            myDataSocket.sendMessage(message.toString() + BOTTOM_BORDER);
         } else {
            message.append("| 302 Download unsuccessful     |\n| Message ID not found          |\n");
            myDataSocket.sendMessage(message.toString() + BOTTOM_BORDER);
         }
      } catch (IOException e) {
         log.severe("Error sending message: " + e.getMessage());
      }
   }

   private void downloadAll() {
      StringBuilder message = new StringBuilder();
      message.append(TOP_BORDER);
      try {
         if (messages.isEmpty()) {
            message.append("| 402 No messages available      |\n");
            myDataSocket.sendMessage(message.toString() + BOTTOM_BORDER);
         } else {
            StringBuilder allMessages = new StringBuilder();
            for (int key : messages.keySet()) {
               allMessages.append(" " +key + ": " + messages.get(key) + " \n");
            }
            message.append("| 401 Download of all           |\n| messages successful           |\n");
            myDataSocket.sendMessage(message.toString() + "\n" + allMessages.toString() + BOTTOM_BORDER);
         }
      } catch (IOException e) {
         log.severe("Error sending all messages: " + e.getMessage());
      }
   }

   private void logout(){
      StringBuilder message = new StringBuilder();
      message.append(TOP_BORDER);
      try {
         message.append("| 501 Logout successful         |\n| See you again soon            |\n");
         myDataSocket.sendMessage(message.toString() + BOTTOM_BORDER);
      } catch (IOException e) {
         log.severe("Error sending logout message: " + e.getMessage());
      }
   }

   private void quit() {
      StringBuilder message = new StringBuilder();
      message.append(TOP_BORDER);
      try {
         message.append("| 502 Program quit successfully |\n| Goodbye                       |\n"); 
         myDataSocket.sendMessage(message.toString() + BOTTOM_BORDER);
      } catch (IOException e) {
         log.severe("Error sending quit message: " + e.getMessage());
      }
   }
}
