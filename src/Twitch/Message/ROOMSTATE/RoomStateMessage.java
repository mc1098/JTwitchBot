package Twitch.Message.ROOMSTATE;

import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchTags;
import java.time.LocalDateTime;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class RoomStateMessage extends TwitchTags implements TwitchMessage
{
    
    private final LocalDateTime recieved;
    private final String channel;
    private final String raw;

    public RoomStateMessage(String tags, String channel, String raw)
    {
        super(tags);
        this.recieved = LocalDateTime.now();
        this.channel = channel;
        this.raw = raw;
    }

    @Override
    public LocalDateTime recieved(){return recieved;}
    
    public String getBroadcasterLanguage() {return getValue("broadcaster-lang");}
    
    public boolean isR9kMode() {return Boolean.valueOf(getValue("r9k"));}
    
    public int slow() 
    {
        String slow = getValue("slow");
        if(slow.isEmpty())
            return 0;
        return Integer.valueOf(slow);
    }
    
    public boolean isSubOnly() {return Boolean.valueOf(getValue("subs-only"));}
    
    public String getChannel() {return channel;}

    @Override
    public String raw() {return raw;}
    
}
