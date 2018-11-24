package Twitch.Message.HOSTTARGET;

import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchMessageFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class HostTargetMessageFactory extends TwitchMessageFactory
{
    
    private final String regex = ":tmi.twitch.tv HOSTTARGET #(\\w*) :(\\w*)";

    @Override
    public String acceptingRegex() {return regex;}

    @Override
    public TwitchMessage create(String raw)
    {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(raw);
        
        if(m.matches())
        {
            String channel = m.group(1);
            String hostTarget = m.group(2);
            
            return new HostTargetMessage(channel, hostTarget, raw);
            
        }
        return null;
    }
    
}
