package Twitch.Message.WHISPER;

import Twitch.Message.Request.Request;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class WhisperRequest extends WhisperMessage implements Request
{
    
    private final String commandName;
    private final String commandValue;

    public WhisperRequest(String tags, String user, String commandName, 
            String commandValue, String raw)
    {
        super(tags, user, String.format("%s %s", commandName, 
                commandValue), raw);
        this.commandName = commandName;
        this.commandValue = commandValue;
    }

    @Override
    public String commandName() {return commandName;}

    @Override
    public String commandValue() {return commandValue;}
    
}
