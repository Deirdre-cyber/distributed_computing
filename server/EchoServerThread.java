package server;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

class EchoServerThread implements Runnable {

   private MyStreamSocket myDataSocket;
   private Logger log = Logger.getLogger(EchoServerThread.class.getName());
   private HashMap<Integer, String> messages;
   private int messageId = 100;

   EchoServerThread(MyStreamSocket myDataSocket) {
      this.myDataSocket = myDataSocket;
      messages = new HashMap<>();
   }

   public void run() {
      boolean done = false;
      String message;

      try {
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

            } else if (message.equals("QUIT")) {
               quit();
               done = true;
            } else {
               myDataSocket.sendMessage("Invalid command");
            }
         }
      } catch (Exception ex) {
         log.severe("Exception caught in thread: " + ex);
      }
   }

   private void quit() {
      try {
         myDataSocket.sendMessage("104 Quit successful\nGoodbye");
      } catch (IOException e) {
         log.severe("Error sending quit message: " + e.getMessage());
      }
   }

   private void downloadAll() {
      try {
         if (messages.isEmpty()) {
            myDataSocket.sendMessage("402 No messages available");
         } else {
            StringBuilder allMessages = new StringBuilder();
            for (int key : messages.keySet()) {
               allMessages.append(key + ": " + messages.get(key) + " \n");
            }
            myDataSocket.sendMessage("401 Download of all messages successful\n" + allMessages.toString());
         }
      } catch (IOException e) {
         log.severe("Error sending all messages: " + e.getMessage());
      }
   }

   private void download(int id) {
      try {
         if (messages.containsKey(id)) {
            myDataSocket.sendMessage("301 Download of message " + id + " successful\nMessage: " + messages.get(id));
         } else {
            myDataSocket.sendMessage("302 Download unsuccessful\nMessage ID not found");
         }
      } catch (IOException e) {
         log.severe("Error sending message: " + e.getMessage());
      }
   }

   private void upload(String userMessage) { 
      if (userMessage != null && !userMessage.isEmpty()){
         messages.put(messageId, userMessage);
         try {
            myDataSocket.sendMessage("201 Upload successful\nMessage ID: " + messageId);
            messageId++;
         } catch (IOException e) {
            log.severe("Error sending message: " + e.getMessage());
         }
      } else {
         try {
            myDataSocket.sendMessage("202 Upload unsuccessful\nAttempted to upload a null message.");
         } catch (IOException e) {
            log.severe("Error sending error message: " + e.getMessage());
         }
      }
   }

   private void login(String credentials) {
      try {
         String[] parts = credentials.split(" ");
         if (parts.length != 2 || parts == null) {
            myDataSocket.sendMessage("103 Invalid credentials format\nMust be 'username password'");
            log.warning("Invalid credentials format: " + credentials);
            return;
         }

         String username = parts[0];
         String password = parts[1];

         if (authenticate(username, password)) {
            myDataSocket.sendMessage("101 Login successful\nWelcome to [ protocol name ]");
         } else {
            myDataSocket.sendMessage("102 Login unsuccessful\nPlease try again\nCheck logs for details");
            log.warning("Login unsuccessful\nIncorrect credentials: " + credentials);
         }
      } catch (IOException e) {
         log.severe("Error handling login: " + e.getMessage());
      }
   }

   private boolean authenticate(String username, String password) {
      return username.equals("admin") && password.equals("admin");
   }
}
