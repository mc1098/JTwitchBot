package Twitch.Command;

import Twitch.Message.PRIVMSG.PrivRequest;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class TimeCommand extends AbstractCommand<PrivRequest>
{
    
    public TimeCommand()
    {
        super("time");
    }

    @Override
    public boolean isCustom()
    {return false;}

    @Override
    public String execute(PrivRequest request)
    {
        return String.format("The time is now %s for %s.", 
                LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")), 
                request.getChannel());
    }

    @Override
    public String cancel()
    {
        return "";
    }

    @Override
    public String getInfoRequest()
    {
        StringBuilder sb = new StringBuilder("Command: ");
        sb.append(getName());
        sb.append(" Not custom and cannot be edited in chat.");
        sb.append(" PRIVMSG only. ");
        sb.append("Available to users: ");
        if(getPermittedUsers().isEmpty())
            sb.append("ALL ");
        else
            getPermittedUsers().forEach((pu) ->
            {
                sb.append(pu);
                sb.append(" ");
            });
        sb.append("Cooldown: ");
        sb.append(getCooldown());
        sb.append(" seconds. ");
        sb.append("Command for broadcasters Local time");
        return sb.toString();
    }
    
}
