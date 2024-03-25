import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This module is to be used with a concurrent Echo server.
 * Its run method carries out the logic of a client session.
 * 
 * @author M. L. Liu
 */

// This class represents a single client session
// It handles the logic of receiving messages from the client, possibly
// processing them, and sending back responses.

class EchoServerThread implements Runnable {

   MyStreamSocket myDataSocket;
   private Logger log = Logger.getLogger("EchoServerThread");
   private HashMap<Integer, String> messages;
   private int messageId = 100;

   EchoServerThread(MyStreamSocket myDataSocket) {
      this.myDataSocket = myDataSocket;
      messages = new HashMap<Integer, String>();
   }

   public void run() {
      boolean done = false;
      String message;

      try {
         while (!done) {
            message = myDataSocket.receiveMessage();
            System.out.println("Message received: " + message);  //DEBUG

            if (message.startsWith("LOGIN")) {
               String credentials = message.substring(6);
               login(credentials);

            } else if (message.startsWith("UPLOAD")) {
               String userMessage = message.substring(7);
               upload(userMessage);

            } else if (message.startsWith("DOWNLOAD")) {
               String messageId = message.substring(9);
               int id = Integer.parseInt(messageId);
               download(id);

            } else if (message.startsWith("DOWNLOAD_ALL")) {
               downloadAll(messageId);
            }
            else if (message.equals("Logout")) {
               myDataSocket.sendMessage("Logout successful");
               done = true;
            }
            else {
               myDataSocket.sendMessage("Invalid command");
            }
         }
      } catch (Exception ex) {
         System.out.println("Exception caught in thread: " + ex);
      }

   }

   private void downloadAll(int messageId) {
      if (messages.isEmpty()) {
         try {
            myDataSocket.sendMessage("402 No messages available");
         } catch (IOException e) {
            log.severe("403 Download unsuccessful\nPlease try again\nCheck logs for details");
            log.info(e.getMessage());
         }
      } else {
         try {
            StringBuilder allMessages = new StringBuilder();

            for (int i = 0; i < messageId; i++) {
               if (messages.containsKey(i)) {
                  allMessages.append("Message ID: " + i + " Message: " + messages.get(i) + "\n");
               }
            }
            myDataSocket.sendMessage("401 Download of all messages successful\n" + allMessages.toString());
         } catch (IOException e) {
            log.severe("403 Download unsuccessful\nPlease try again\nCheck logs for details");
            log.info(e.getMessage());
         }
      }
   }

   private void download(int id) {
      if (messages.containsKey(id)) {
         try {
            myDataSocket.sendMessage("301 Download of message " + id + " successful\nMessage: " + messages.get(id));
         } catch (IOException e) {
            log.severe("303 Download of message " + id + " unsuccessful\nPlease try again\nCheck logs for details");
            log.info(e.getMessage());
         }
      } else {
         try {
            myDataSocket.sendMessage("302 Download unsuccessful\nMessage ID not found");
         } catch (IOException e) {
            log.severe("303 Download unsuccessful\nPlease try again\nCheck logs for details");
            log.info(e.getMessage());
         }
      }
   }

   private void upload(String userMessage) {
      messages.put(messageId, userMessage);
      messageId++;
      try {
         myDataSocket.sendMessage("201 Upload successful. Message ID: " + messageId);
      } catch (IOException e) {
         log.severe("202 Upload unsuccessful. Please try again. Check logs for details");
         log.info(e.getMessage());
      }	
   }

   private void login(String credentials) {
      try {
         String[] parts = credentials.split(" ");

         if (parts.length != 2 || parts == null) {
            myDataSocket.sendMessage("103 Invalid credentials format. Must be 'username password'");
            log.warning("Invalid credentials format: " + credentials);
            return;
         }

         String username = parts[0];
         String password = parts[1];

         if (authenticate(username, password)) {
            myDataSocket.sendMessage("101 Login successful. Welcome to [ protocol name ]");
         } else {
            myDataSocket.sendMessage("102 Login unsuccessful. Please try again. Check logs for details");
            log.warning("Login unsuccessful. Incorrect credentials: " + credentials);
         }
      } catch (IOException e) {
         log.severe("Error handling login: " + e.getMessage());
      }
   }

   // added for 'authentication' of user
   private boolean authenticate(String username, String password) {
      return username.equals("admin") && password.equals("admin");
   }

} // end class
