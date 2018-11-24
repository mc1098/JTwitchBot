package Twitch.Command;

import Twitch.Message.Request.Request;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface CommandLibrary<T extends Request>
{
    
    public boolean hasCommand(String command);
    public Command<T> getCommand(String command);
    
    public boolean addCommandIfAbsent(Command<T> command);
    public void addCommand(Command<T> command);
    public void removeCommand(String command);
    
}
