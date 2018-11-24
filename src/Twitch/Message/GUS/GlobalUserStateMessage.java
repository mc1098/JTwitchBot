package Twitch.Message.GUS;

import Twitch.Message.TwitchMessage;
import Twitch.Message.TwitchTags;
import java.time.LocalDateTime;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class GlobalUserStateMessage extends TwitchTags implements TwitchMessage
{
    
    private final LocalDateTime recieved;
    private final String raw;

    public GlobalUserStateMessage(String tags, String raw)
    {
        super(tags);
        this.recieved = LocalDateTime.now();
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
    
    public boolean isTurbo() {return Boolean.valueOf(getValue("turbo"));}
    
    public String getUserId() {return getValue("user-id");}
    
    public String getUserType() {return getValue("user-type");}

    @Override
    public String raw() {return raw;}
    
}
