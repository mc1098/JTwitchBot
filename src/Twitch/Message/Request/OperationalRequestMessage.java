package Twitch.Message.Request;

import Twitch.Message.AbstractTwitchChatMessage;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class OperationalRequestMessage 
        extends AbstractTwitchChatMessage 
        implements Request
{
    private final Operation operation;
    private final String[] permittedUsers;
    private final int cooldown;
    private final String commandName;
    private final String commandValue;

    public OperationalRequestMessage(String tags, String user, Operation o, 
            String[] permittedUsers, int cooldown,
            String commandName, String commandValue, String raw)
    {
        super(tags, user, String.format("%s %s", commandName, commandValue), 
                raw);
        this.operation = o;
        this.permittedUsers = permittedUsers;
        this.cooldown = cooldown;
        this.commandName = commandName;
        this.commandValue = commandValue;
    }
    
    public Operation getOperation() {return operation;}
    
    public String[] getPermittedUsers() {return permittedUsers;}
    
    public int getCooldown() {return cooldown;}
    
//    @Override
//    public String getUserType() {return getValue("ut");}
    
//    public Integer getCooldown() 
//    {
//        String value = getValue("cd");
//        if(!value.isEmpty())
//            return Integer.valueOf(value);
//        return 0;
//    }

    @Override
    public String commandName() {return commandName;}

    @Override
    public String commandValue() {return commandValue;}
    
    public enum Operation
    {
        INFO("info"),
        ADD("add"), 
        EDIT("edit"),
        CANCEL("cancel"),
        DELETE("delete");
        
        private final String name;
        
        private Operation(final String name)
        {
            this.name = name;
        }
        
        public static Operation valueOfIgnoreCase(String name)
        {
            for (Operation value : values())
                if(value.name.equalsIgnoreCase(name))
                    return value;
            return null;
        }
    }
    
}
