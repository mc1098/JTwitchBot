package Twitch.Message.NOTICE;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class UnrecognizedCommand extends NoticeMessage
{

    public UnrecognizedCommand(String tags, String channel, String message, 
            String raw)
    {
        super(tags, channel, message, raw);
    }
    
}
