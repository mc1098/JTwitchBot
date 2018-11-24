package Twitch.Message.NOTICE;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class HostOffNoticeMessage extends NoticeMessage
{
    
    public HostOffNoticeMessage(String msgId, String channel, String message, 
            String raw)
    {
        super(msgId, channel, message, raw);
    }
    
}
