package Twitch.Message.USERSTATE;

import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchTags;
import java.time.LocalDateTime;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class UserStateMessage extends TwitchTags implements TwitchMessage
{

    private final LocalDateTime recieved;
    private final String channel;
    private final String raw;
    
    public UserStateMessage(String tags, String channel, String raw)
    {
        super(tags);
        this.recieved = LocalDateTime.now();
        this.channel = channel;
        this.raw = raw;
    }

    @Override
    public LocalDateTime recieved() {return recieved;}
    
    public String getColor() {return getValue("color");}
    
    public String getDisplayName() {return getValue("display-name");}
    
    public int [] getEmoteSets() 
    {
        String emoteSet = getValue("emote-sets");
        if(emoteSet.contains(","))
        {
            String[] emoteSets = emoteSet.split(",");
            int[] i_emoteSets = new int[emoteSets.length];
            for (int i = 0; i < emoteSets.length; i++)
                i_emoteSets[i] = Integer.valueOf(emoteSets[i]);
            return i_emoteSets;
        }
        return new int[] {Integer.valueOf(emoteSet)};
    }
    
    public boolean isMod() {return Boolean.valueOf(getValue("mod"));}
    
    public boolean isUserSub() {return Boolean.valueOf(getValue("subscriber"));}
    
    public boolean isTurbo() {return Boolean.valueOf(getValue("turbo"));}
    
    public String getUserType() {return getValue("user-type");}
    
    public String getChannel() {return channel;}

    @Override
    public String raw() {return raw;}
    
}
