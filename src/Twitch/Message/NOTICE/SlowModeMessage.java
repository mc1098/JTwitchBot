package Twitch.Message.NOTICE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class SlowModeMessage extends NoticeMessage
{
    
    private final boolean slowMode;
    private final int slowDelay;
    
    private SlowModeMessage(String tags, String channel, String message, 
            int slowDelay, String raw)
    {
        super(tags, channel, message, raw);
        this.slowMode = slowDelay != 0;
        this.slowDelay = slowDelay;
    }

    public static SlowModeMessage of(String tags, String channel, String message, 
            String raw)
    {
        Pattern p = Pattern.compile("[\\s\\S]*?(\\d+)[\\s\\S]*");
        Matcher m = p.matcher(message);
        
        if(m.matches())
        {
            int slowDelay = Integer.valueOf(m.group(1));
            return new SlowModeMessage(tags, channel, message, slowDelay, raw);
        }
        return new SlowModeMessage(tags, channel, message, 0, raw);
    }
    
    public boolean isSlowMode() {return slowMode;}
    
    public int getSlowDelay() {return slowDelay;}
    
}
