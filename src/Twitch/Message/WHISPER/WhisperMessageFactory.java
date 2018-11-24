package Twitch.Message.WHISPER;

//import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchChatMessage;
import Twitch.Message.TwitchMessageFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class WhisperMessageFactory extends TwitchMessageFactory
{
    
    private final String whisperRegex = "([\\s\\S]*):(\\w*)!\\w*@\\w*.tmi.twitch.tv WHISPER (\\w*) :([\\s\\S]*)";
    private final String commandRegex = "!(\\w*) ?([\\s\\S]*)";

    @Override
    public String acceptingRegex() {return whisperRegex;}

    @Override
    public TwitchChatMessage create(String raw)
    {
        Pattern p = Pattern.compile(whisperRegex);
        Matcher m = p.matcher(raw);
        
        if(m.matches())
        {
            String tags = m.group(1);
            String user = m.group(2);
            String target = m.group(3);
            String message = m.group(4);
            if(message.startsWith("!"))
                return createRequest(tags, user, message, raw);
            else
                return new WhisperMessage(tags, user, message, raw);
                
        }
        return null;
    }
    
    private TwitchChatMessage createRequest(String tags, String user,
            String message, String raw)
    {
        Pattern p = Pattern.compile(commandRegex);
        Matcher m = p.matcher(message);
        if(m.matches())
        {
            String commandName = m.group(1);
            String commandValue = m.group(2);
            
            return new WhisperRequest(tags, user, commandName, 
                    commandValue, raw);
        }
        else
            return new WhisperMessage(tags, user, message, raw);
        
        
    }
    
}
