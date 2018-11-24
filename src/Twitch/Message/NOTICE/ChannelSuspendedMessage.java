package Twitch.Message.NOTICE;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class ChannelSuspendedMessage extends NoticeMessage
{
    public ChannelSuspendedMessage(String tags, String channel, String message, 
            String raw)
    {
        super(tags, channel, message, raw);
    }
    
}
