package Twitch.Message;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class TwitchTags
{
    private final String tags;
    
    public TwitchTags(String tags)
    {
        this.tags = tags;
    }
    
    public String getValue(String tag)
    {
        if(!tags.contains(tag))
            return "";
        int i = tags.indexOf(String.format("%s=", tag));
        int ii = tags.indexOf(";", i);
        if(ii == -1)
        {
            int j = tags.indexOf(" ", i);
            if(j != -1 && j < tags.length()-1)
                ii = tags.indexOf(" ", i);
            else
                ii = tags.length();
        }
        
        return tags.substring(i + tag.length()+1, ii);
    }
}
