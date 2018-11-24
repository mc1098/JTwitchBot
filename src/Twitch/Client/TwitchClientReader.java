package Twitch.Client;

import IrcClient.IrcClientReader;
import Twitch.Message.TwitchChatLog;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface TwitchClientReader extends IrcClientReader
{
    public TwitchChatLog getChatLog();
}
