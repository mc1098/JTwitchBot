package Twitch.Message;

import Aggregate.Handles.HandlerBroker;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class TwitchChatLogQueue implements TwitchChatLog
{
    
    private TwitchMessageParser parser;
    private final List<TwitchMessageHandle> handles;
    private final BlockingQueue<TwitchMessage> messageQueue;
    private final TwitchMessageBroker broker;
    
    public TwitchChatLogQueue(TwitchMessageParser parser)
    {
        this.parser = parser;
        this.handles = new ArrayList<>();
        this.messageQueue = new ArrayBlockingQueue<>(100);
        this.broker = new TwitchMessageBroker();
    }

    @Override
    public TwitchMessageParser getParser(){return parser;}

    @Override
    public void setParser(TwitchMessageParser tmp) {parser = tmp;}

    @Override
    public void recieve(String message)
    {
        List<TwitchMessage> messages = parser.parseBulkMessage(message);
        messages.forEach((m)-> messageQueue.offer(m));
    }
    
    @Override
    public void add(TwitchMessageHandle handle)
    {
        handles.add(handle);
    }

    @Override
    public void remove(TwitchMessageHandle handle)
    {
        handles.remove(handle);
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                TwitchMessage tm = messageQueue.take();
                handles.forEach((h)-> { broker.brokerNoThrow(h, tm);});
            } catch(InterruptedException ex)
            {
                throw new RuntimeException(ex);
            }
            
        }
    }

    
}

class TwitchMessageBroker 
        extends HandlerBroker<TwitchMessageHandle, TwitchMessage>
{
    
}