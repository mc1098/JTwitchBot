package Twitch.Client;

import IrcClient.StringWriter;
import Util.CoolDown.BlockingCooldown;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class TwitchWriter extends Thread implements TwitchClientWriter
{
    private final TwitchClient client;
    private final StringWriter writer;
    private final BlockingCooldown cooldown;
    
    private final AtomicBoolean isActive;
    private final BlockingQueue<String> messageQueue;
    
    private final MessageSplitAndSender splitAndSender;
    
    public TwitchWriter(TwitchClient client, BlockingCooldown writecd)
    {
        this.client = client;
        this.writer = client.getWriter();
        this.cooldown = writecd;
        
        this.isActive = new AtomicBoolean();
        this.messageQueue = new ArrayBlockingQueue<>(100);
        
        this.setName("Twitch Writer");
        
        this.splitAndSender = new MessageSplitAndSender(31, 500, this);
    }

    @Override
    public void sendWhisper(String user, String message)
    {
        splitAndSender.splitAndSend(String.format(".w %s", user), message);
//        message = String.format(".w %s %s", user, message);
//        sendMessage(message);
    }

    @Override
    public void sendMessage(String message)
    {
        messageQueue.add(message);
    }
    
    public void sendInstantMessage(String message) throws IOException
    {
        String user = client.getUser();
        message = String.format(":%s!%s@%s.tmi.twitch.tv PRIVMSG #%s :%s\n", user, 
                user, user, client.getChannel(), message);
        writer.write(message);
    }

    @Override
    public void run()
    {
        isActive.set(true);
        
        while(isActive.get())
        {
            try
            {
                String message = messageQueue.take();
                if(cooldown.BlockAndWait())
                    sendInstantMessage(message);
                
            }catch(InterruptedException ex)
            {
                getDefaultUncaughtExceptionHandler().uncaughtException(this, ex);
            }
            catch(IOException e)
            {
                if(!client.isConnected())
                    if(client.reconnect())
                        continue;
                
                getDefaultUncaughtExceptionHandler().uncaughtException(this, e);
                throw new RuntimeException("Unable to reconnect client");
            }
            
        }
    }
    
    public void timeoutUser(String user, int seconds, String reason)
    {
        sendMessage(String.format("/timeout %s %d %s", user, seconds, reason));
    }
    
}


class MessageSplitAndSender
{
    
    private final int prefixSize;
    private final int limit;
    private final TwitchWriter writer;
    
    public MessageSplitAndSender(int basePrefix, int limit, TwitchWriter writer)
    {
        this.prefixSize = basePrefix;
        this.limit = limit;
        this.writer = writer;
    }
    
    public void splitAndSend(String prefix, String message)
    {
        int totalSize = prefix.length()+ message.length() + prefixSize; 
        int divider;
        if(totalSize > limit)
        {
            divider = (int)totalSize/limit;
            divider = divider == 1 ? divider+1 : divider;
            List<String> messages = splitMessageAndPrefix(prefix, message, divider);
            send(messages);
            return;
        }
        
        send(String.format("%s %s", prefix, message));
    }
    
    private List<String> splitMessageAndPrefix(String prefix, String message, 
            int division)
    {
        List<String> messages = new ArrayList<>();
        int i = 0;
        int ii;
        
        int denom = message.length()/division;
        
        for (int j = 1; j <= division; j++)
        {
            ii = message.lastIndexOf(".", denom*j);
            ii = ii == -1 ? message.lastIndexOf(" ", denom*j) : ii;
            ii = ii > message.length() ? message.length() : ii;
            
            messages.add(String.format("%s [%d/%d] %s", prefix, j, division, 
                    message.substring(i, ii)));
            
            i = ii+1;
        }
        
        return messages;
        
    }
    
    
    private void send(String message)
    {
        writer.sendMessage(message);
    }
    
    private void send(List<String> messages)
    {
        messages.forEach((m)->{send(m);});
    }
}