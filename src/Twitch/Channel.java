package Twitch;

import Twitch.Message.HOSTTARGET.HostTargetMessage;
import Twitch.Message.NOTICE.*;
import Twitch.Message.ROOMSTATE.RoomStateMessage;
import Twitch.Message.TwitchMessageHandle;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class Channel implements TwitchMessageHandle
{
    private String name;
    private String language;
    private int slowDelay;
    private boolean r9k;
    private boolean subOnly;
    private boolean emoteOnly;
    private boolean hosting;
    private String hostingChannel;
    private boolean suspended;
    
    
    public Channel()
    {
        
    }

    public String getName()
    {
        return name;
    }

    public String getLanguage()
    {
        return language;
    }
    
    public boolean isSlow() {return slowDelay != 0;}

    public int getSlowDelay()
    {
        return slowDelay;
    }

    public boolean isR9k()
    {
        return r9k;
    }

    public boolean isSubOnly()
    {
        return subOnly;
    }

    public boolean isEmoteOnly()
    {
        return emoteOnly;
    }

    public boolean isHosting()
    {
        return hosting;
    }
    
    public String getHostingChannel() {return hostingChannel;}

    public boolean isSuspended()
    {
        return suspended;
    }
    
    public void handle(RoomStateMessage rsm)
    {
        this.name = rsm.getChannel();
        this.language = rsm.getBroadcasterLanguage();
        this.r9k = rsm.isR9kMode();
        this.subOnly = rsm.isSubOnly();
        this.slowDelay = rsm.slow();
    }
    
    public void handle(HostTargetMessage htm)
    {
        this.hosting = true;
        this.hostingChannel = htm.getChannel();
    }
    
    public void handle(HostOffNoticeMessage honm)
    {
        this.hosting =false;
        this.hostingChannel = "";
    }
    
    public void handle(ChannelSuspendedMessage csm)
    {
        this.suspended = true;
    }
    
    public void handle(EmoteOnlyMessage eom)
    {
        this.emoteOnly = eom.isOn();
    }
    
    public void handle(R9kModeMessage rmm)
    {
        this.r9k = rmm.isR9kOn();
    }
    
    public void handle(SlowModeMessage smm)
    {
        this.slowDelay = smm.getSlowDelay();
    }
    
    public void handle(SubOnlyMessage som)
    {
        this.subOnly = som.isSubOnly();
    }
    
    
}
