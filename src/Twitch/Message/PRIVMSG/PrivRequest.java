package Twitch.Message.PRIVMSG;

import Twitch.Message.Request.Request;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class PrivRequest extends PrivMessage implements Request
{
    
    private final String commandName;
    private final String commandValue;
    
    public PrivRequest(String tags, String user, String channel, 
            String commandName, String commandValue, String raw)
    {
        super(tags, user, channel, String.format("%s %s", commandName, 
                commandValue), raw);
        this.commandName = commandName;
        this.commandValue = commandValue;
    }

    @Override
    public String commandName() {return commandName;}

    @Override
    public String commandValue() {return commandValue;}
    
}
