package Twitch.Message.PRIVMSG;

import Twitch.Message.Request.OperationalRequestMessage;
import Twitch.Message.TwitchChatMessage;
//import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchMessageFactory;
import Twitch.Message.TwitchTags;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class PrivMessageFactory extends TwitchMessageFactory
{
    
    private final String channelRegex = "([\\s\\S]*)?.?:(\\w*)!\\w*@\\w*.tmi.twitch.tv PRIVMSG #(\\w*) :([\\s\\S]*)";
    private final String opCommandRegex = "!op (\\w*)([\\s\\S]*)? !(\\w*)?([\\s\\S]*)";
    private final String commandRegex = "!(\\w*) ?([\\s\\S]*)";
    

    @Override
    public String acceptingRegex() {return channelRegex;}

    @Override
    public TwitchChatMessage create(String raw)
    {
        Pattern p = Pattern.compile(channelRegex);
        Matcher m = p.matcher(raw);
        
        if(m.matches())
        {
            String tags = m.group(1);
            String user = m.group(2);
            String channel = m.group(3);
            String message = m.group(4);
            if(message.startsWith("!"))
                if(message.startsWith("!op"))
                    return createOpRequest(tags, user, channel, message, raw);
                else
                    return createRequest(tags, user, channel, message, raw);
            else
                return new PrivMessage(tags, user, channel, message, raw);
                
        }
        return null;
    }
    
    private PrivMessage createRequest(String tags, String user, String channel, 
            String message, String raw)
    {
        Pattern p = Pattern.compile(commandRegex);
        Matcher m = p.matcher(message);
        if(m.matches())
        {
            String commandName = m.group(1);
            String commandValue = m.group(2);
            
            return new PrivRequest(tags, user, channel, commandName, 
                    commandValue, raw);
        }
        return new PrivMessage(tags, user, channel, message, raw);
    }
    
    private TwitchChatMessage createOpRequest(String tags, String user, 
            String channel, String message, String raw)
    {
        if(!(tags.contains("broadcaster") || tags.contains("moderator")))
            return new PrivMessage(tags, user, channel, message, raw);
        
        Pattern p = Pattern.compile(opCommandRegex);
        Matcher m = p.matcher(message);
        if(m.matches())
        {
            String opName = m.group(1);
            String opTags = m.group(2);
            String commandName = m.group(3);
            String commandValue = m.group(4);
            
            OperationalRequestMessage.Operation op = OperationalRequestMessage
                    .Operation.valueOfIgnoreCase(opName);
            
            TwitchTags tt = new TwitchTags(opTags);
            String ut = tt.getValue("ut");
            String[] userTypes;
            if(ut.isEmpty())
                userTypes = new String[0];
            else
                userTypes = ut.contains("|") ? ut.split("|") : new String[]{ut};
            
            String cd = tt.getValue("cd");
            int cooldown = 0;
            if(!cd.isEmpty() && cd.matches("\\d+"))
                cooldown = Integer.valueOf(cd);
            
            return new OperationalRequestMessage(tags, user, op, 
                    userTypes, cooldown, commandName, commandValue, raw);
        }
        return new PrivMessage(tags, user, channel, message, raw);
    }
    
}
