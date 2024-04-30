import java.io.*;
/**
 *  Lloyd Empedrado
 *  CS450-001 Computer Networks
 *  Lab 6
 */

public class Message implements Serializable
{
    private String sender;
    private String content;
    // private Object o;
    private Card card;
    private Hand hand;

    /**
     * Generic String message
     * @param sender
     * @param content
     */
    public Message(String sender, String content)
    {
        this.sender = sender;
        this.content = content;
    }
    
    /**
     *  Message with a Card attached
     *  @param sender the sender of the message
     *  @param content the content of the message
     */
    public Message(String sender, String content, Card card)
    {
        this.sender = sender;
        this.content = content;
        this.card = card;
    }
    
    /**
     * Message with a player's full Hand
     * @param sender
     * @param content
     * @param hand
     */
    public Message(String sender, String content, Hand hand)
    {
        this.sender = sender;
        this.content = content;
        // this.o = card;
        this.hand = hand;
    }

    // public Message(String sender, String content, Object o)
    // {
    //     this.sender = sender;
    //     this.content = content;
    //     this.o = o;
    // }

    /**
     *  @return the sender of this message
     */
    public String getSender()
    {
        return this.sender;
    }

    /**
     *  @return the content of this message
     */
    public String getContent()
    {
        return this.content;
    }

    public Card getCard()
    {
        return this.card;
    }

    public Hand getHand()
    {
        return this.hand;
    }

    /**
     *  Overloaded toString method to format a Message object
     *  @return The sender of the message, followed by the content of their message
     */
    public String toString()
    {
        return this.sender + ": " + this.content;
    }
}
