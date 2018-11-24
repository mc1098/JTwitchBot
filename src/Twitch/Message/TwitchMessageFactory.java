package Twitch.Message;


/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public abstract class TwitchMessageFactory
{
    public abstract String acceptingRegex();
    public abstract TwitchMessage create(String raw);
    
}
