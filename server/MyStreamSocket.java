package server;
import java.net.*;
import java.io.*;

public class MyStreamSocket extends Socket {
   private Socket  socket;
   private BufferedReader input;
   private PrintWriter output;

   public MyStreamSocket(InetAddress acceptorHost, int acceptorPort ) throws SocketException, IOException{
      socket = new Socket(acceptorHost, acceptorPort );
      setStreams( );
   }
   // constructor needed to achieve ssl
   public MyStreamSocket(Socket socket) throws IOException {
      this.socket = socket;
      setStreams( );
   }

   private void setStreams( ) throws IOException{
      InputStream inStream = socket.getInputStream();
      input = new BufferedReader(new InputStreamReader(inStream));
      OutputStream outStream = socket.getOutputStream();
      output = new PrintWriter(new OutputStreamWriter(outStream));
   }

   // ensure ssl is actually being implemented
   public Socket getSocket( ) {
      return socket;
   }

   public void sendMessage(String message) throws IOException {	
      output.print(message + "\n");   
      output.flush();               
   }

   public String receiveMessage( ) throws IOException {	
      StringBuilder message = new StringBuilder();
      String line;
      
      // updated this to ensure correct parsing of messages with newlines
      while ((line = input.readLine( )) != null) {
         message.append(line);
         if (!input.ready()) {
            break;
         }
         message.append("\n");
      }
      return message.toString();
   }

   public void close( ) throws IOException {	
      socket.close( );
   }
}
