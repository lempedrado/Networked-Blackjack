import java.io.*;

/**
 *  Lloyd Empedrado
 *  CS450-001 Computer Networks
 *  HW 3
 */

/**
 *  Table - the shared resource to pass Messages and Cards
 *  between the Server and Clients
 */
public class Table implements Serializable
{
    private Message msg;
    private boolean empty = true;
    private Deck deck;
    private int receiver;

    //takes a message out of the shared resource
    public synchronized Message take(int requester)
    {
        while(empty)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e) {}
        }

        //only return the message if the requester is the intended receiver
        if(requester == receiver)
        {
            empty = true;

            notifyAll();
            return msg;
        }
        return null;
    }

    //puts a message into the shared resource
    public synchronized void put(Message msg, int receiver)
    {
        while(!empty)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e) {}
        }
        //toggle status
        empty = false;

        this.msg = msg;
        this.receiver = receiver;
        notifyAll();
    }

    public int getReceiver()
    {
        return this.receiver;
    }

    public void initialize()
    {
        this.deck = new Deck(3);
        //deal two cards to the Server
        for(int i = 0; i < 2; i++)
        {
            put(new Message("Dealer", "", deck.deal()), 0);
        }
        //deal two cards to the Player
        for(int i = 0; i < 2; i++)
        {
            put(new Message("Dealer", "", deck.deal()), 1);
        }
    }
}
