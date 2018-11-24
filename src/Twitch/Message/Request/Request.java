package Twitch.Message.Request;

import Twitch.Message.TwitchChatMessage;
//import Twitch.Message.TwitchMessage;


/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface Request extends TwitchChatMessage
{
    public String commandName();
    public String commandValue();
}
