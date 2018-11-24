package Twitch.Message;

import java.time.LocalDateTime;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class RawTwitchMessage implements TwitchMessage
{
    private final LocalDateTime recieved;
    private final String raw;
    
    public RawTwitchMessage(String raw)
    {
        this.recieved = LocalDateTime.now();
        this.raw = raw;
    }

    @Override
    public LocalDateTime recieved() {return recieved;}

    @Override
    public String getValue(String tag) {return "";}

    @Override
    public String raw() {return raw;}
    
}
