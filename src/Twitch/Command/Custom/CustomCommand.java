package Twitch.Command.Custom;

import Twitch.Command.AbstractCommand;
import Twitch.Command.Custom.Variable.CommandVariable;
import Twitch.Message.PRIVMSG.PrivRequest;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class CustomCommand extends AbstractCommand<PrivRequest> 
{
    private final String echo;
    private final VariableMap variableMap;

    public CustomCommand(String name, int cooldown, int args, String echo, 
            Queue<CommandVariable> cvs, String...userTypes)
    {
        super(name, cooldown, args, userTypes);
        this.echo = echo;
        this.variableMap = new VariableMap(cvs);
    }

    @Override
    public boolean isCustom() {return true;}

    @Override
    public String execute(PrivRequest request)
    {
        String value = echo.replace("[channel]", request.getChannel());
        value = value.replace("[sender]", request.getUser());
        value = variableMap.parseAndInsertVariables(value);
        return String.format(value, (Object[]) request.commandValue().split(" "));
    }
    
    @Override
    public String getInfoRequest()
    {
        StringBuilder sb = new StringBuilder("Command: ");
        sb.append(getName());
        sb.append(" (custom echo command).");
        sb.append(" PRIVMSG only. ");
        sb.append("Available to users: ");
        if(getPermittedUsers().isEmpty())
            sb.append("ALL ");
        getPermittedUsers().forEach((pu) ->
        {
            sb.append(pu);
            sb.append(" ");
        });
        sb.append("Cooldown: ");
        sb.append(getCooldown());
        sb.append(" seconds. ");
        sb.append("Raw Value: ");
        sb.append(echo);
        return sb.toString();
    }

    @Override
    public String cancel()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

class VariableMap
{
    private final Map<String, Queue<CommandVariable>> map;
    
    public VariableMap(Queue<CommandVariable> variables)
    {
        this.map = new HashMap<>();
        variables.forEach((v)-> 
        {
            if(map.containsKey(v.replaceeRegex()))
                map.get(v.replaceeRegex()).add(v);
            else
                map.put(v.replaceeRegex(), new ArrayDeque<CommandVariable>(){{add(v);}});
        });
    }
    
    public String parseAndInsertVariables(String string)
    {
        for (Queue<CommandVariable> queue : map.values())
            for (CommandVariable cv : queue)
                string = string.replaceFirst(cv.replaceeRegex(), cv.execute());
        return string;
    }
}