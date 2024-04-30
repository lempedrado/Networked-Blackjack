import java.io.Serializable;

public class Card implements Serializable
{
    private String suit;
    private String rank;
    private int value;

    public Card(String suit, String rank, int value)
    {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    public String getSuit()
    {
        return this.suit;
    }

    public String getRank()
    {
        return this.rank;
    }

    public int getValue()
    {
        return this.value;
    }

    /**
     *  Changes the value of this card, used for Aces
     *
     *  @param i The new value of the card
     */
    protected void setValue(int i)
    {
        this.value = i;
    }

    public String toString()
    {
        return this.rank + this.suit;
    }
}
