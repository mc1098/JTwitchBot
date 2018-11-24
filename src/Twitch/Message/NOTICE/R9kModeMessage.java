package Twitch.Message.NOTICE;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class R9kModeMessage extends NoticeMessage
{
    private final boolean r9kOn;
    public R9kModeMessage(String tags, String channel, String message, 
            String raw)
    {
        super(tags, channel, message, raw);
        this.r9kOn = tags.contains("r9k_on");
    }
    
    public boolean isR9kOn() {return r9kOn;}
    
}
