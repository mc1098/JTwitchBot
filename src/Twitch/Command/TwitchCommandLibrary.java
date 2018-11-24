package Twitch.Command;

import Aggregate.AggregateRoot;
import Twitch.Message.Request.Request;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class TwitchCommandLibrary<T extends Request> 
        implements CommandLibrary<T>
{
    private final Map<String, Command<T>> commands;
    
    public TwitchCommandLibrary()
    {
        this.commands = new HashMap<>();
    }
    
    public TwitchCommandLibrary(Command<T>...commands)
    {
        this();
        for (Command command : commands)
            this.commands.put(command.getName(), command);
    }
    
    @Override
    public boolean hasCommand(String name)
    {
        return commands.containsKey(name);
    }
    
    @Override
    public Command getCommand(String command)
    {
        if(hasCommand(command))
            return commands.get(command);
        return null;
    }

    @Override
    public boolean addCommandIfAbsent(Command<T> command)
    {
        if(!commands.containsKey(command.getName()))
        {
            commands.put(command.getName(), command);
            return true;
        }
        return false;
        
    }
    
    @Override
    public void addCommand(Command<T> command)
    {
        commands.put(command.getName(), command);
    }

    @Override
    public void removeCommand(String command)
    {
        commands.remove(command);
    }

    
    
}
