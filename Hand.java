import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable
{
    private ArrayList<Card> hand;
    private int length;
    private int total;
    
    /**
     * Hand constructor
     */
    public Hand()
    {
        this.hand = new ArrayList<Card>();
        this.length = 0;
        this.total = 0;
    }

    /**
     * Adds a Card to this Hand
     * @param c The Card that was drawn
     */
    public void add(Card c)
    {
        length++;
        //if the card is an Ace, determine if it will be 11 or 1
        if(c.getRank().equals("A"))
        {
            //if a value of 11 will result in a total over 21, set the value to 1
            if(total + 11 > 21)
                c.setValue(1);
        }
        total = total + c.getValue();
        hand.add(c);
    }

    /**
     * Returns the total value of this Hand
     */
    public int getValue()
    {
        return total;
    }

    public int getLength()
    {
        return length;
    }

    public void clear()
    {
        hand.clear();
        length = 0;
        total = 0;
    }

    /**
     * Determines if this is a valid Hand to continue playing
     * @return -1 for <21, 0 for =21, 1 for >21
     */
    public int validate()
    {
        if(total > 21)
            return 1;
        if(total == 21)
            return 0;
        return -1;
    }

    /**
     * Formats the dealer's hand at the beginning of the game to hide the second card
     * @return the formatted dealer's Hand
     */
    public String dealer()
    {
        String ret = "";
        //reveal the first Card in the Hand
        ret += hand.get(0);
        //hide the second Card in the Hand
        if(length == 2)
            ret += " []";
        return ret;
    }

    /**
     * Formats the printing of the Cards in a Player's Hand
     * @return the formatted Hand
     */
    public String toString()
    {
        String ret = "";
        for(int i = 0; i < length; i++)
            ret += hand.get(i) + " ";
        return ret.trim();
    }
}
