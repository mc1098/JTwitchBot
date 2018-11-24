package Twitch.Command;

import Twitch.Message.Request.Request;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public abstract class AbstractCommand<T extends Request> implements Command<T>
{
    
    private final String name;
    private final List<String> permittedUsers;
    private final int cooldown;
    private final int expectedArgs;
    
    public AbstractCommand(String name)
    {
        this(name, 0, 0);
    }
    
    public AbstractCommand(String name, int cooldown, int expectedArgs, 
            String...users)
    {
        this.name = name;
        this.cooldown = cooldown;
        this.expectedArgs = expectedArgs;
        this.permittedUsers = Arrays.asList(users);
    }
    

    @Override
    public String getName() {return name;}
    
    @Override
    public boolean hasPermission(String userType)
    {
        if(permittedUsers.isEmpty())
            return true;
        return permittedUsers.stream().anyMatch((ut)-> 
        {
            return userType.contains(ut);
        });
    }
    
    @Override
    public List<String> getPermittedUsers() {return permittedUsers;}

    @Override
    public int getCooldown() {return cooldown;}

    @Override
    public int getExpectedArgNo(){return expectedArgs;}
    
    @Override
    public abstract String cancel();
    
    @Override
    public abstract String getInfoRequest();
    
}
