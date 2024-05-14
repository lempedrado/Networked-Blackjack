import java.io.*;
import java.net.*;

public class Player {
        public static void main(String args[]) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage: java Player <host name> <player name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        String playerName = args[1];
        int portNumber = Integer.parseInt(args[2]);

        try (
                // Initiate a connection request to server's Socket
                Socket IMSocket = new Socket(hostName, portNumber);
                ObjectOutputStream out = new ObjectOutputStream(IMSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(IMSocket.getInputStream());) {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            // stores the Messages from the Server and the User
            Message fromServer, fromUser;
            // The String content of a Message
            String content;
            Hand hand = new Hand();
            Card c;

            // Receive Messages from the Server
            while ((fromServer = (Message) in.readObject()) != null) {
                // get the Server's message
                content = fromServer.getContent();
                if (content != null) {
                    // if received a Card
                    if ((c = fromServer.getCard()) != null) {
                        hand.add(c);
                        //print the Dealer's Hand
                        System.out.println(fromServer);
                        //print the Player's Hand
                        System.out.printf("%s: %s\n\n", playerName, hand);
                        //determine if the player has 21 or more
                        if(hand.validate() == 0)
                            //send the Player's Hand
                            out.writeObject(new Message(playerName, "Blackjack", hand));
                        else if(hand.validate() == 1)
                            out.writeObject(new Message(playerName, "Bust", hand));
                        else if(hand.validate() == -1 && hand.getLength() > 2)
                            out.writeObject(new Message(playerName, "Continue", hand));
                        else if(hand.getLength() == 2)
                            out.writeObject(new Message(playerName, "start", hand));
                    }
                    else
                    {
                        //print the Dealer's Message
                        System.out.println(fromServer);
                        //server prompts for input
                        if(fromServer.getSender().equals("Server") && !(content.equals("Connection established.")))
                        {
                            if(content.equals("Enter 'y' or 'n' if you want to play again"))
                                hand = new Hand();
                            else if(content.equals("Thank you for playing"))
                                break;
                            //prompt the Player for an action
                            content = stdIn.readLine();
                            fromUser = new Message(playerName, content, hand);

                            // print the Client's message
                            System.out.println(fromUser + "\n\n");
                            // Send response to Server
                            out.writeObject(fromUser);
                        }
                    }
                }

            }
            IMSocket.close();
            // catches error if the Client's connection parameters are not consistent with
            // the Server
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
            // catches error if unable to connect to host
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.err.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
