package Twitch.Client;

import IrcClient.IrcClientWriter;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface TwitchClientWriter extends IrcClientWriter
{
    public void sendWhisper(String user, String message);
}
