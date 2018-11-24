package Twitch.Message.USERSTATE;

import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchMessageFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class UserStateMessageFactory extends TwitchMessageFactory
{
    
    private final String regex = "@(.*?) :tmi.twitch.tv USERSTATE #(\\w*)";

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
            
            return new UserStateMessage(tags, channel, raw);
            
        }
        return null;
    }
    
}
