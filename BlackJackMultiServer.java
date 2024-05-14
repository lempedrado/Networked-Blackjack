import java.net.*;
import java.io.*;

public class BlackJackMultiServer {
    public static void main(String[] args) throws IOException {

    //requires port number to create socket
    if (args.length != 1) {
        System.err.println("Usage: java BlackJackMultiServer <port number>");
        System.exit(1);
    }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;
        Deck deck = new Deck();
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (listening) {
                //all players will share the same deck of cards to play with
                new BlackJackMultiServerThread(serverSocket.accept(), deck).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
