package Twitch.Command.Custom;

import Twitch.Command.Custom.Variable.CommandVariable;
import Twitch.Command.Custom.Variable.CommandVariableFactory;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;


/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class CustomCommandFactory
{
    private final CommandVariableFactory cvFactory;
    
    public CustomCommandFactory()
    {
        this.cvFactory = new CommandVariableFactory();
    }
    
    
    public CustomCommand create(String commandName, int cooldown, 
            String commandValue, String...userTypes)
    {
        int args = countFuturePlaceholders(commandValue);
        Queue<CommandVariable> cvs = new ArrayDeque<>();
        List<String> list = cvFactory.commandVariablePlaceholders();
        
        list.forEach((s) ->
        {
            int count = countPlaceHolders(commandValue, s);
            cvs.addAll(cvFactory.create(s, count));
        });
        
        return new CustomCommand(commandName, cooldown, args, commandValue, cvs, userTypes);
    }
    
    private int countFuturePlaceholders(String string)
    {
        int i = 0;
        int args = 0;
        while((i = string.indexOf("%s", i)) != -1)
        {
            args++;
            i+= 2;
        }
        return args;
    }
    
    private int countPlaceHolders(String string, String ph)
    {
        int i = 0;
        int count = 0;
        while((i = string.indexOf(ph, i)) != -1)
        {
            count++;
            i+= ph.length();
        }
        return count;
    }
}
