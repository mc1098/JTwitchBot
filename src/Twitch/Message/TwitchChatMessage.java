package Twitch.Message;


/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface TwitchChatMessage extends TwitchMessage
{
    public String[] getBadges(); 
    public String getColor();
    public String getDisplayName();
    public int[] getEmotes();
    public String getMessageId();
    public boolean isTurbo();
    public String getUserId();
    public String getUserType();
    public String getUser();
    public String getMessage();
}

