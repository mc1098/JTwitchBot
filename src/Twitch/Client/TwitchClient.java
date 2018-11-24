package Twitch.Client;

import IrcClient.IrcClient;
import java.io.IOException;


/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface TwitchClient extends IrcClient
{
    public String getUser();
    public String getChannel();
    public void connectToTwitch()throws IOException;
    public void joinChannel() throws IOException;
    public void joinChannel(String channel) throws IOException;
    
}
