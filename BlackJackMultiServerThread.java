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
        ) {
            Message inputMsg, outputMsg;
            String content;
            boolean playing = true;
            boolean gameFinished;
            Hand player;

            // Send a message to the Player verifying the connection
            out.writeObject(new Message("Server", "Connection established."));

            while (playing) {
                gameFinished = false;
                // player = -1;
                // reset the dealer's hand for a new game
                dealer = new Hand();
                // initialize the game by dealing the Player and Server two cards each
                for (int i = 1; i <= 2; i++) {
                    // deal the server a Card
                    dealer.add(deck.deal());
                    // deal the player a Card
                    out.writeObject(new Message("Dealer", dealer.dealer(), deck.deal()));
                }

                // Dealer must check for Blackjack at the beginning of the game
                //TODO add replayability if dealer has blackjack
                if (dealer.validate() == 0) {
                    out.writeObject(new Message("Dealer", dealer + "\nDealer has Blackjack!. Thank you for playing."));
                    socket.close();
                    return;
                }

                // recieve message from Player
                while ((inputMsg = (Message) in.readObject()) != null) {
                    // get the content of the Message
                    content = inputMsg.getContent();
                    if (content != null) {
                        player = inputMsg.getHand();

                        //determine what to do based on Player's action
                        switch (content.trim().toLowerCase()) {
                            // if the player wants to draw another card
                            case "hit":
                                outputMsg = new Message("Dealer", "Player hit", deck.deal());
                                out.writeObject(outputMsg);
                                break;
                            // if the player wants to stop drawing cards
                            case "stand":
                                outputMsg = new Message("Dealer", "Player stands");
                                out.writeObject(outputMsg);

                                // Dealer draws cards until Hand value of at least 17
                                while (dealer.getValue() < 17) {
                                    Card c = deck.deal();
                                    dealer.add(c);
                                }
                                // send the Dealer's final Hand
                                outputMsg = new Message("Dealer", "", dealer);
                                out.writeObject(outputMsg);

                                gameFinished = true;
                                break;
                            //if the Player has over 21
                            case "bust":
                                // Send the Dealer's Hand
                                outputMsg = new Message("Dealer", "", dealer);
                                out.writeObject(outputMsg);

                                gameFinished = true;
                                break;
                            //if the Player has =21
                            case "blackjack":
                                while (dealer.getValue() < 17) {
                                    Card c = deck.deal();
                                    dealer.add(c);
                                }
                                //send the Dealer's final Hand
                                outputMsg = new Message("Dealer", "", dealer);
                                out.writeObject(outputMsg);

                                gameFinished = true;
                                break;
                            //used to indicate that initial deal has finished and game can begin
                            case "continue":
                                out.writeObject(new Message("Server", "Please enter 'Hit' or 'Stand'"));
                                break;
                            // if the player entered a message other than the above
                            default:
                                outputMsg = new Message("Server", "Please enter 'Hit' or 'Stand'");
                                out.writeObject(outputMsg);
                                break;
                        }

                        // if the game is finished, determine the results and prompt to play again
                        if (gameFinished) {
                            //if player busts, dealer wins
                            if(player.validate() == 1)
                                outputMsg = new Message("Dealer", "Player busts. Dealer wins.");
                            //if player stands and dealer busts, player wins
                            else if(dealer.validate() == 1)
                                outputMsg = new Message("Dealer", "Dealer bust! You win");
                            //both player and dealer have the same hand value
                            else if (dealer.validate() == player.validate() && dealer.getValue() == player.getValue())
                                outputMsg = new Message("Dealer", "Both players tied.");
                            //dealer has 21 and player has less than 21
                            else if(player.validate() == -1 && dealer.validate() == 0)
                                outputMsg = new Message("Dealer", "Dealer win!");
                            //player has 21 and dealer has less than 21
                            else if(player.validate() == 0 && dealer.validate() == -1)
                                outputMsg = new Message("Dealer", "You win!");
                            //if both player and dealer have below 21, determine who has the higher value hand
                            else
                            {
                                if(dealer.getValue() < player.getValue())
                                    outputMsg = new Message("Dealer", "You win!");
                                else
                                    outputMsg = new Message("Dealer", "Dealer win!");
                            }
                            out.writeObject(outputMsg);

                            // prompt the Player to play again
                            outputMsg = new Message("Server", "Enter 'y' or 'n' if you want to play again");
                            out.writeObject(outputMsg);

                            //loop until valid input
                            while ((inputMsg = (Message) in.readObject()) != null) {
                                content = inputMsg.getContent();
                                if (content.equalsIgnoreCase("y"))
                                    break;
                                else if (content.equalsIgnoreCase("n")) {
                                    playing = false;
                                    break;
                                } else {
                                    outputMsg = new Message("Server", "Enter 'y' or 'n' if you want to play again");
                                    out.writeObject(outputMsg);
                                }
                            }
                            //break outer loop to finish game
                            break;
                        }
                    }
                }
            }
            outputMsg = new Message("Server", "Thank you for playing");
            out.writeObject(outputMsg);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
