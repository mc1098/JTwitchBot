package Twitch.Message.CLEARCHAT;

import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchMessageFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class ClearChatMessageFactory extends TwitchMessageFactory
{
    
    private final String regex = "([\\s\\S]*):tmi.twitch.tv CLEARCHAT #(\\w*) :(\\w*)[\\s\\S]*";

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
            String user = m.group(3);
            
            return new ClearChatMessage(tags, channel, user, raw);
            
        }
        return null;
       
    }
    
}
