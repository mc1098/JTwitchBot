package Twitch.Message.NOTICE;

import Twitch.Message.TwitchMessage;
import java.time.LocalDateTime;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public abstract class NoticeMessage  implements TwitchMessage
{

    private final LocalDateTime recieved;
    private final String msgId; 
    private final String channel;
    private final String message;
    private final String raw;
            
    public NoticeMessage(String msgId, String channel, String message, 
            String raw)
    {
        this.recieved = LocalDateTime.now();
        this.msgId = msgId;
        this.channel = channel;
        this.message = message;
        this.raw = raw;
    }

    @Override
    public LocalDateTime recieved() {return recieved;}
    
    @Override
    public String getValue(String tag) {return "";}
    
    public String getMsgId() {return msgId;}
    
    public String getChannel() {return channel;}
    
    public String getMessage() {return message;}

    @Override
    public String raw() {return raw;}
    
}
