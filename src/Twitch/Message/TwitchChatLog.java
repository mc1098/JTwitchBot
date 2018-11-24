package Twitch.Message;


/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface TwitchChatLog extends Runnable
{
    public TwitchMessageParser getParser();
    public void setParser(TwitchMessageParser tmp);
    
    public void recieve(String message);
    
    public void add(TwitchMessageHandle handle);
    public void remove(TwitchMessageHandle handle);
}

