package Twitch.Message.NOTICE;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class EmoteOnlyMessage extends NoticeMessage
{
    
    private final boolean on;

    public EmoteOnlyMessage(String tags, String channel, String message, 
            String raw)
    {
        super(tags, channel, message, raw);
        this.on = tags.contains("emote_only_on");
    }
    
    public boolean isOn(){return on;}
    
}
