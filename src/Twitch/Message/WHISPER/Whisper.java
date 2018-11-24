package Twitch.Message.WHISPER;

import Twitch.Message.TwitchChatMessage;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface Whisper extends TwitchChatMessage
{
    public String getThreadId();
}
