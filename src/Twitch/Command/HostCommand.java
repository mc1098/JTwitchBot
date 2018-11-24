package Twitch.Command;

import Twitch.Message.PRIVMSG.PrivRequest;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class HostCommand extends AbstractCommand<PrivRequest>
{

    public HostCommand()
    {
        super("host");
    }

    @Override
    public String cancel() {return "";}

    @Override
    public String getInfoRequest()
    {
        StringBuilder sb = new StringBuilder("Command: ");
        sb.append(getName());
        sb.append(" Not custom and cannot be edited in chat.");
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
        sb.append("Command to allow for easy hosting and unhosting. This command ");
        sb.append("can be used by moderators of the channel. This is only ");
        sb.append("available if this bot is given editor status to the channel.");
        return sb.toString();
    }

    @Override
    public boolean isCustom() {return false;}

    @Override
    public String execute(PrivRequest request)
    {
        if(request.commandValue().isEmpty())
            return "/unhost";
        else
            return String.format("/host %s", request.commandValue());
    }
    
}
