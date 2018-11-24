package Twitch.Client;

import IrcClient.StringReader;
import Twitch.Message.TwitchChatLog;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class TwitchReader extends Thread implements TwitchClientReader
{
    private final TwitchClient client;
    private final StringReader reader;
    private final TwitchChatLog chatLog;
    
    private final AtomicBoolean isActive;
    
    public TwitchReader(TwitchClient client, TwitchChatLog chatLog)
    {
        this.client = client;
        this.reader = client.getReader();
        this.chatLog = chatLog;
        this.isActive = new AtomicBoolean();
        this.setName("Twitch Reader");
        this.setDaemon(true);
    }
    
    @Override
    public void run()
    {
        isActive.set(true);
        while(isActive.get())
        {
            if(!client.isConnected())
                if(client.isReconnecting())
                    continue;
            else
                {
                    client.reconnect();
                    continue;
                }
            try
            {
                String message = reader.readString();
                if(message != null && !message.isEmpty())
                {
                    if(message.contains("PING :tmi.twitch.tv"))
                        client.getWriter().write("PONG :tmi.twitch.tv\n");
                    
                    chatLog.recieve(message);
                }
                else
                    sleep(100);
            } catch(IOException | InterruptedException ex)
            {
                if(client.isConnected())
                {
                    System.err.println(ex.toString());
                    continue;
                }
                    
                if(client.isReconnecting() || client.reconnect())
                    continue;
                getDefaultUncaughtExceptionHandler().uncaughtException(this, ex);
            }
        }
    }

    @Override
    public TwitchChatLog getChatLog() {return chatLog;}
    
}
