package Twitch.Message.PRIVMSG;

import Twitch.Message.TwitchChatMessage;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface Priv extends TwitchChatMessage
{
    public boolean hasBits();
    public int getBits();
    public boolean isMod();
    public String getRoomId();
    public boolean isUserSub();
    public String getChannel();
}
