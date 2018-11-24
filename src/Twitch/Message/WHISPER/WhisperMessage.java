package Twitch.Message.WHISPER;

import Twitch.Message.AbstractTwitchChatMessage;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
class WhisperMessage extends AbstractTwitchChatMessage implements Whisper
{
    
    
    public WhisperMessage(String tags, String user, String message, String raw)
    {
        super(tags, user, message, raw);
    }
    
    @Override
    public String getThreadId() {return getValue("thread-id");}
    
}
