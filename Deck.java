import java.io.Serializable;
import java.util.ArrayList;

public class Deck implements Serializable
{
    /** The deck of Cards */
    private ArrayList<Card> deck;

    /** Number of Cards left to be dealt */
    int size;

    /** String array of card ranks */
    String ranks[] = {"A", "2", "3", "4", "5", "6", "7", "8", "9","10", "J", "Q", "K"};

    /** String array of card suits */
    String suits[] = {"♥", "♦", "♠", "♣"};

    /** Default constructor */
    public Deck()
    {
        this.deck = new ArrayList<Card>();
        //Blackjack is often played with 3 combined decks
        build(3);
    }

    /**
     *  Overloaded constructor to build a Deck with a specified number of decks
     *
     *  @param num The number of decks to combine
     */
    public Deck(int num)
    {
        this();
        build(num);
    }

    /**
     *  Builds the Deck with a certain number of combined decks
     *
     *  @param num The number of decks to combine
     */
    private void build(int num)
    {
        int val;
        //iterate through the card suits
        for(int s = 0; s < 4; s++)
        {
            //iterate through the card ranks
            for(int r = 0; r < 13; r++)
            {
                //ACE card
                if(r == 0)
                    val = 11;
                //Face cards (JACK, QUEEN, KING)
                else if(r > 9 && r < 13)
                    val = 10;
                //Number card values
                else
                    val = r + 1;

                //add num copies of this Card
                for(int i = 0; i < num; i++)
                    //Create the Card object and add it to the deck
                    deck.add(new Card(suits[s], ranks[r], val));
            }
        }

        this.size = deck.size();

        //Shuffle the deck
        shuffle();
    }

    /** Shuffles the deck of Cards */
    private void shuffle()
    {
        for(int j = size - 1; j > 0; j--)
        {
            int pos = (int)(Math.random() * (j + 1));
            Card temp = deck.set(j, deck.get(pos));
            deck.set(pos, temp);
        }

    }

    /** Deals a single Card from the deck */
    public Card deal()
    {
        //if the deck is almost empty, reshuffle a new deck and add it to the current deck
        if(deck.size() < 15)
            build(3);

        //gets the Card at the top of the deck
        Card c = deck.remove(size - 1);
        size = deck.size();

        //returns dealt Card
        return c;
    }
}
