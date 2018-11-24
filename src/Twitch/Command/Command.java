package Twitch.Command;

import Twitch.Message.Request.Request;
import java.util.List;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface Command<T extends Request>
{
    public final static String USER_TYPE_DEFAULT = "";
    
    public String getName();
    public boolean hasPermission(String userType);
    public List<String> getPermittedUsers();
    public int getCooldown();
    public int getExpectedArgNo();
    public boolean isCustom();
    public String execute(T request);
    public String cancel();
    
    /**
     * Recommended format for overriding classes is: 
     * 
     * "Command: [command name] ([custom command note]).
     * [Request type] only. Available to users: [Permitted Users] 
     * Cooldown: [cooldown] seconds. [Custom description of purpose of command]".
     * example of below is what is used in the EchoCommand using Stringbuilder.
     * 
     * StringBuilder sb = new StringBuilder("Command: ");
        sb.append(getName());
        sb.append(" (custom echo command).");
        sb.append(" PRIVMSG only. ");
        sb.append("Available to users: ");
        getPermittedUsers().forEach((pu) ->
        {
            sb.append(pu);
            sb.append(" ");
        });
        sb.append("Cooldown: ");
        sb.append(getCooldown());
        sb.append(" seconds. ");
        sb.append("Value: ");
        sb.append(echo);
        return sb.toString();
     * @return String of information about the command to be sent back to the 
     * requester.
     */
    public String getInfoRequest();
    
    
}
