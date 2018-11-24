package Twitch.Client;

import IrcClient.ReconnectingIrcClient;
import IrcClient.StringWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class ReconnectingTwitchClient extends ReconnectingIrcClient 
        implements TwitchClient
{
    private final String user;
    private final String OAuth;
    private String channel;
    private final AtomicBoolean IsTwitchSetupComplete;

    public ReconnectingTwitchClient(String user, String OAuth, String channel, 
            Socket socket, InputStream is, OutputStream os)
    {
        super(socket, is, os);
        this.user = user;
        this.OAuth = OAuth;
        this.channel = channel;
        this.IsTwitchSetupComplete = new AtomicBoolean();
    }
    
    @Override
    public void connectToTwitch() throws IOException
    {
        StringWriter ssw = getWriter();
        ssw.write("CAP REQ :twitch.tv/membership\n");
        ssw.write("CAP REQ :twitch.tv/tags\n");
        ssw.write("CAP REQ :twitch.tv/commands\n");
        ssw.write(String.format("PASS %s\n", OAuth));
        ssw.write(String.format("NICK %s\n", user));
        joinChannel();
        IsTwitchSetupComplete.set(true);
    }

    @Override
    public String getUser() {return user;}

    @Override
    public String getChannel() {return channel;}

    @Override
    public void joinChannel() throws IOException
    {
        StringWriter ssw = getWriter();
        ssw.write(String.format(":%s!%s@%s.tmi.twitch.tv JOIN #%s\n", user, user,
                user, channel));
    }

    @Override
    public void joinChannel(String channel) throws IOException
    {
        if(channel != null && !channel.isEmpty())
        {
            this.channel = channel;
            joinChannel();
        }
            
    }
    
    @Override
    public boolean isConnected()
    {
        if(super.isConnected())
            return IsTwitchSetupComplete.get();
        return false;
    }
    
    @Override
    public boolean reconnect()
    {
        IsTwitchSetupComplete.set(false);
        if(super.reconnect())
        {
            try
            {
                connectToTwitch();
                return true;
            } catch(IOException ex)
            {
                throw new RuntimeException(ex);
            }
        }
        return false;
    }
    
}
