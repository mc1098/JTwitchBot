package Twitch.Message;

import java.time.LocalDateTime;


/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface TwitchMessage 
{
    public abstract LocalDateTime recieved();
    public abstract String getValue(String tag);
    public abstract String raw();
}
