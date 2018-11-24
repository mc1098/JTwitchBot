package Twitch.Message.NOTICE;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class BanNoticeMessage extends NoticeMessage
{
    
    private final String bannedUser;
    
    public BanNoticeMessage(String msgId, String channel, String message,
            String bannedUser, String raw)
    {
        super(msgId, channel, message, raw);
        this.bannedUser = bannedUser;
    }
    
    public String getBannedUser() {return bannedUser;}
    
}
