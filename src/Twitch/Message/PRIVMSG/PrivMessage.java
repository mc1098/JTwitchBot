package Twitch.Message.PRIVMSG;

import Twitch.Message.AbstractTwitchChatMessage;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
class PrivMessage extends AbstractTwitchChatMessage implements Priv
{
    private final String channel;
    
    public PrivMessage(String tags, String user, String channel, String message, 
            String raw)
    {
        super(tags, user, message, raw);
        this.channel = channel;
    }
    
    @Override
    public boolean hasBits() {return !getValue("bits").isEmpty();}
    
    @Override
    public int getBits()
    {
        String bits = getValue("bits");
        if(bits.isEmpty())
            return 0;
        return Integer.valueOf(bits);
    }
    
    @Override
    public boolean isMod() {return Boolean.valueOf(getValue("mod"));}
    
    @Override
    public String getRoomId() {return getValue("room-id");}
    
    @Override
    public boolean isUserSub() {return Boolean.valueOf(getValue("subscriber"));}
    
    @Override
    public String getChannel() {return channel;}
    
}
