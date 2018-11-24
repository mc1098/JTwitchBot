package Twitch.Message.CLEARCHAT;

import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchTags;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class ClearChatMessage extends TwitchTags implements TwitchMessage
{

    private final LocalDateTime recieved;
    private final String channel;
    private final String user;
    private final String raw;
    
    public ClearChatMessage(String tags, String channel, String user, String raw)
    {
        super(tags);
        this.recieved = LocalDateTime.now();
        this.channel = channel;
        this.user = user;
        this.raw = raw;
    }

    @Override
    public LocalDateTime recieved() {return recieved;}
    
    public Duration getBanDuration()
    {
        String ban = getValue("ban-duration");
        if(ban.isEmpty())
            return null;
        else
            return Duration.ofSeconds(Long.valueOf(ban));
    }
    
    public String getBanReason()
    {
        String ban = getValue("ban-reason").replace("\\s", " ");
        return ban;
    }
    
    public String getChannel() {return channel;}
    
    public String getUser() {return user;}

    @Override
    public String raw() {return raw;}

}
