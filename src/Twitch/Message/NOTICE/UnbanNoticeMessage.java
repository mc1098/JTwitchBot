package Twitch.Message.NOTICE;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class UnbanNoticeMessage extends NoticeMessage
{
    private final String unbannedUser;
    
    public UnbanNoticeMessage(String tags, String channel, String message, 
            String unbannedUser, String raw)
    {
        super(tags, channel, message, raw);
        this.unbannedUser = unbannedUser;
    }
    
    public String getUnbannedUser() {return unbannedUser;}
    
}
