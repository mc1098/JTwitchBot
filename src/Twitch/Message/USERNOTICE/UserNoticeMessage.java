package Twitch.Message.USERNOTICE;

import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchTags;
import java.time.LocalDateTime;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class UserNoticeMessage extends TwitchTags implements TwitchMessage
{
    
    private final LocalDateTime recieved;
    private final String channel;
    private final String message;
    private final String raw;

    public UserNoticeMessage(String tags, String channel, String message, 
            String raw)
    {
        super(tags);
        this.recieved = LocalDateTime.now();
        this.channel = channel;
        this.message = message;
        this.raw = raw;
    }

    @Override
    public LocalDateTime recieved(){return recieved;}
    
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
    
    public String getUser() {return getValue("login");}
    
    public boolean isMod() {return Boolean.valueOf(getValue("mod"));}
    
    public boolean isRaid() {return getValue("msg-id").equalsIgnoreCase("raid");}
    
    public String getRaiderDisplayName() 
    {
        if(isRaid())
            return getValue("msg-param-displayName");
        return "";
    }
    
    public String getRaider()
    {
        if(isRaid())
            return getValue("msg-param-login");
        return "";
    }
    
    public int getRaiderCount()
    {
        if(isRaid())
            return Integer.valueOf(getValue("msg-param-viewerCount"));
        return 0;
    }
    
    public boolean isSub() {return getValue("msg-id").equalsIgnoreCase("sub");}
    
    public boolean isResub() {return getValue("msg-id").equalsIgnoreCase("resub");}
    
    public int getSubStreak() 
    {
        if(!(isSub() || isResub() || isSubGift()))
            return -1;
        return Integer.valueOf(getValue("msg-param-months"));
    }
    
    public String getSubType() 
    {
        if(!(isSub() || isResub() || isSubGift()))
            return "";
        return getValue("msg-param-sub-plan");
    }
    
    public String getSubTypeName() 
    {
        if(!(isSub() || isResub() || isSubGift()))
            return "";
        return getValue("msg-param-sub-plan-name");
    }
    
    public boolean isSubGift() {return getValue("msg-id").equalsIgnoreCase("subgift");}
    
    public String getGiftRecieverDisplayName()
    {
        if(!isSubGift())
            return "";
        return getValue("msg-param-recipient-display-name");
    }
    
    public String getGiftRecieverId()
    {
        if(!isSubGift())
            return "";
        return getValue("msg-param-recipient-id");
    }
    
    public String getGiftReciever()
    {
        if(!isSubGift())
            return "";
        return getValue("msg-param-recipient-user-name");
    }
    
    public boolean isRitual() {return getValue("msg-id").equalsIgnoreCase("ritual");}
    
    public String getRitualUser() 
    {
        if(!isRitual())
            return "";
        return getValue("msg-param-ritual-name");
    }
    
    public String getRoomId() {return getValue("room-id");}
    
    public boolean isUserSub() {return Boolean.valueOf(getValue("subscriber"));}
    
    public boolean isTurbo() {return Boolean.valueOf(getValue("turbo"));}
    
    public String getSystemMessage() {return getValue("system-msg");}
    
    public String getUserId() {return getValue("user-id");}
    
    public String getUserType() {return getValue("user-type");}
    
    public String getChannel() {return channel;}
    
    public String getMessage() {return message;}
    
    @Override
    public String raw(){return raw;}
    
}
