package Twitch.Message.NOTICE;

import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchMessageFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class NoticeMessageFactory extends TwitchMessageFactory
{
    
    private final String regex = "([\\s\\S]*):tmi.twitch.tv NOTICE #(\\w*) :([\\s\\S]*)";

    @Override
    public String acceptingRegex() {return regex;}

    @Override
    public TwitchMessage create(String raw)
    {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(raw);
        
        if(m.matches())
        {
            String tags = m.group(1);
            String channel = m.group(2);
            String message = m.group(3);
            
            return create(tags, channel, message, raw);
            
        }
        return null;
    }
    
    
    private NoticeMessage create(String tags, String channel, String message, 
            String raw)
    {
        
        if(tags.contains("host_off"))
            return new HostOffNoticeMessage(tags, channel, message, raw);
        if(tags.contains("ban_success"))
        {
            int ii = message.indexOf(" ");
            String bannedUser = message.substring(0, ii);
            return new BanNoticeMessage(tags, channel, message, bannedUser, raw);
        }
        if(tags.contains("unban"))
        {
            int ii = message.indexOf(" ");
            String bannedUser = message.substring(0, ii);
            return new UnbanNoticeMessage(tags, channel, message, bannedUser, 
                    raw);
        }
        if(tags.contains("emote_only"))
            return new EmoteOnlyMessage(tags, channel, message, raw);
        if(tags.contains("msg_channel_suspended"))
            return new ChannelSuspendedMessage(tags, channel, message, raw);
        if(tags.contains("r9k"))
            return new R9kModeMessage(tags, channel, message, raw);
        if(tags.contains("slow"))
            return SlowModeMessage.of(tags, channel, message, raw);
        if(tags.contains("subs"))
            return new SubOnlyMessage(tags, channel, message, raw);
        if(tags.contains("unrecognized_cmd"))
            return new UnrecognizedCommand(tags, channel, message, raw);
        
        return null;
    }
    
}
