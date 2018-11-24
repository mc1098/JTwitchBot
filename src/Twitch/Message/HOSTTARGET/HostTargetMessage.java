package Twitch.Message.HOSTTARGET;

import Twitch.Message.TwitchMessage;
import java.time.LocalDateTime;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class HostTargetMessage implements TwitchMessage
{
    private final LocalDateTime recieved;
    private final String channel;
    private final String hostTarget;
    private final String raw;
    
    public HostTargetMessage(String channel, String hostTarget, String raw)
    {
        this.recieved = LocalDateTime.now();
        this.channel = channel;
        this.hostTarget = hostTarget;
        this.raw = raw;
    }

    @Override
    public LocalDateTime recieved() {return recieved;}

    @Override
    public String getValue(String tag) {return "";}
    
    public String getChannel() {return channel;}
    
    public String getHostTarget() {return hostTarget;}

    @Override
    public String raw() {return raw;}
    
}
