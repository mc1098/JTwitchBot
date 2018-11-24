package Twitch.Message.NOTICE;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class SubOnlyMessage extends NoticeMessage
{

    private final boolean subOnly;
    
    public SubOnlyMessage(String tags, String channel, String message, 
            String raw)
    {
        super(tags, channel, message, raw);
        this.subOnly = tags.contains("subs_on");
    }
    
    public boolean isSubOnly() {return subOnly;}
    
}
