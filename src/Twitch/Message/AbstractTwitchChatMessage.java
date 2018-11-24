package Twitch.Message;

import java.time.LocalDateTime;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public abstract class AbstractTwitchChatMessage 
        extends TwitchTags 
        implements TwitchMessage
{
    
    protected final LocalDateTime recieved;
    protected final String user;
    protected final String message;
    protected final String raw;
    
    public AbstractTwitchChatMessage(String tags, String user, String message, 
            String raw)
    {
        super(tags);
        this.recieved = LocalDateTime.now();
        this.user = user;
        this.message = message;
        this.raw = raw;
    }
    
    public String[] getBadges() 
    {
        String badgeGroup = getValue("badges");
        if(badgeGroup.contains(","))
            return badgeGroup.split(",");
        return new String[] {badgeGroup};
    }
    
    public String getColor() {return getValue("color");}
    
    public String getDisplayName() {return getValue("display-name");}
    
    public int[] getEmotes()
    {
        String emoteGroups = getValue("emotes");
        if(emoteGroups.isEmpty())
            return new int[0];
        
        String[] emoteGroup = emoteGroups.split("/");
        int[] emotes = new int[emoteGroup.length];
        
        for (int i = 0; i < emoteGroup.length; i++)
        {
            String eg = emoteGroup[i];
            int j = emoteGroups.indexOf(eg);
            int jj = eg.indexOf(":");
            emotes[i] = Integer.valueOf(emoteGroups.substring(j, j + jj));
        }
        
        return emotes;
    }
    
    public String getMessageId() {return getValue("id");}
    
    public boolean isTurbo() {return Boolean.valueOf(getValue("turbo"));}
    
    public String getUserId() {return getValue("user-id");}
    
    public String getUserType() {return getValue("user-type");}
    
    public String getUser() {return user;}
    
    public String getMessage() {return message;}

    @Override
    public LocalDateTime recieved() {return recieved;}

    @Override
    public String raw() {return raw;}
    
}
