import java.net.*;
import java.io.*;

/**
 * A wrapper class of Socket which contains 
 * methods for sending and receiving messages
 * @author M. L. Liu
 */

 // This class deals with the communication between clients and the server
 // It handles sending and receiving messages over sockets

public class MyStreamSocket extends Socket {
   private Socket  socket;
   private BufferedReader input;
   private PrintWriter output;

   MyStreamSocket(InetAddress acceptorHost,
                  int acceptorPort ) throws SocketException,
                                   IOException{
      socket = new Socket(acceptorHost, acceptorPort );
      setStreams( );

   }

   MyStreamSocket(Socket socket)  throws IOException {
      this.socket = socket;
      setStreams( );
   }

   private void setStreams( ) throws IOException{
      // get an input stream for reading from the data socket
      InputStream inStream = socket.getInputStream();
      input = 
         new BufferedReader(new InputStreamReader(inStream));
      OutputStream outStream = socket.getOutputStream();
      // create a PrinterWriter object for character-mode output
      output = 
         new PrintWriter(new OutputStreamWriter(outStream));
   }

   public void sendMessage(String message)
   		          throws IOException {	
      output.print(message + "\n");   
      output.flush();               
   }

   public String receiveMessage( )
		throws IOException {	
      StringBuilder message = new StringBuilder();
      String line;
      
      while ((line = input.readLine( )) != null) {
         message.append(line);
         if (!input.ready()) {
            break;
         }
         message.append("\n");
      }
      return message.toString();
   }

   public void close( )
		throws IOException {	
      socket.close( );
   }
}
