import java.net.*;
import java.io.*;

public class BlackJackMultiServerThread extends Thread {
    private Socket socket = null;
    // private Table table = null;
    private Hand dealer;
    private Deck deck;

    public BlackJackMultiServerThread(Socket socket) {
        super("BlackJackMultiServerThread");
        this.socket = socket;
        this.deck = new Deck();
        this.dealer = new Hand();
        // this.table = table;
        // this.hand = new ArrayList<Card>();
    }
    
    public void run() {

        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            // BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            Message inputMsg, outputMsg;
            String content;
            // BlackJackProtocol bjp = new BlackJackProtocol();
            
            //Send a message to the Player verifying the connection and sending the shared resource
            out.writeObject(new Message("Server", "Connection established."));

            //initialize the game by dealing the Player and Server two cards each
            for(int i = 1; i <= 2; i++)
            {
                //deal the server a Card
                dealer.add(deck.deal());
                //deal the player a Card
                out.writeObject(new Message("Dealer", dealer.dealer(), deck.deal()));
            }

            //Dealer must check for Blackjack at the beginning of the game
            if(dealer.validate() == 0)
            {
                out.writeObject(new Message("Dealer", dealer + "\nDealer has Blackjack!. Thank you for playing."));
                socket.close();
                return;
            }

            //Prompt the Player for an action
            out.writeObject(new Message("Dealer", "Please enter 'Hit' or 'Stand'"));
            //recieve message from Player
            while ((inputMsg = (Message)in.readObject()) != null) {
                //get the content of the Message
                content = inputMsg.getContent();
                
                if(content != null)
                {
                    switch (content.trim().toLowerCase()) {
                        //if the player wants to draw another card
                        case "hit":
                            outputMsg = new Message("Server", "Player hit", deck.deal());
                            out.writeObject(outputMsg);
                            System.out.println(outputMsg);
                            break;
                        //if the player wants to stop drawing cards
                        case "stand":
                            //send Message of current action
                            outputMsg = new Message("Server", "Player stands");
                            out.writeObject(outputMsg);
                            // System.out.println(outputMsg);

                            //Dealer draws cards until Hand value of at least 17
                            while(dealer.getValue() < 17)
                            {
                                Card c = deck.deal();
                                dealer.add(c);
                                outputMsg = new Message("Dealer", "", dealer);
                                out.writeObject(outputMsg);
                                // System.out.println(outputMsg);
                            }
                            outputMsg = new Message("Dealer", "", dealer);
                            out.writeObject(outputMsg);
                            if(dealer.validate() == 0)
                                // outputMsg = new Message("Dealer", "Both players tied.", dealer);
                                outputMsg = new Message("Dealer", "Both players tied.");
                            else if(dealer.validate() == 1)
                                // outputMsg = new Message("Dealer", "Dealer bust! You win", dealer);
                                outputMsg = new Message("Dealer", "Dealer bust! You win");
                            else
                                // outputMsg = new Message("Dealer", "You win!", dealer);
                                outputMsg = new Message("Dealer", "You win!");
                            out.writeObject(outputMsg);
                            socket.close();
                            return;
                            // break;
                        case "bust":
                            //Send the Dealer's Hand
                            outputMsg = new Message("Dealer", dealer.toString());
                            out.writeObject(outputMsg);
                            outputMsg = new Message("Dealer", "Player busts. Dealer wins. Thank you for playing", dealer);
                            out.writeObject(outputMsg);
                            // System.out.println(outputMsg);
                            socket.close();
                            return;
                        case "blackjack":
                            while(dealer.getValue() < 17)
                            {
                                Card c = deck.deal();
                                dealer.add(c);
                                outputMsg = new Message("Dealer", "", dealer);
                                out.writeObject(outputMsg);
                                // System.out.println(outputMsg);
                            }
                            outputMsg = new Message("Dealer", "", dealer);
                            out.writeObject(outputMsg);
                            if(dealer.validate() == 0)
                                // outputMsg = new Message("Dealer", "Both players tied.", dealer);
                                outputMsg = new Message("Dealer", "Both players tied.");
                            else if(dealer.validate() == 1)
                                // outputMsg = new Message("Dealer", "Dealer bust! You win", dealer);
                                outputMsg = new Message("Dealer", "Dealer bust! You win");
                            else
                                // outputMsg = new Message("Dealer", "You win!", dealer);
                                outputMsg = new Message("Dealer", "You win!");
                            out.writeObject(outputMsg);
                            socket.close();
                            return;
                        //if the player entered a message other than the above
                        default:
                            outputMsg = new Message("Server", "Invalid input. Please enter 'Hit' or 'Stand'");
                            out.writeObject(outputMsg);
                            System.out.println(outputMsg);
                            break;
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
