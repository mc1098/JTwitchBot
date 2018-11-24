package Twitch.Command;

import Twitch.Client.TwitchWriter;
import Twitch.Command.Custom.CustomCommandFactory;
import Twitch.Message.Request.OperationalRequestMessage;
import Twitch.Message.PRIVMSG.PrivRequest;
import Twitch.Message.TwitchMessageHandle;
import Twitch.Message.WHISPER.WhisperRequest;
import Util.CoolDown.BlockingCooldown;
import Util.CoolDown.Cooldown;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class CommandManager implements TwitchMessageHandle
{
    private final TwitchWriter client;
    private final CommandLibrary<WhisperRequest> whisperCommands;
    private final CommandLibrary<PrivRequest> privCommands;
    
    private final Map<String, Cooldown> commandCooldowns;
    
    public CommandManager(TwitchWriter client, 
            CommandLibrary<WhisperRequest> whisper, 
            CommandLibrary<PrivRequest> priv)
    {
        this.client = client;
        this.whisperCommands = whisper;
        this.privCommands = priv;
        this.commandCooldowns = new HashMap<>();
    }
    
    
    private boolean meetsRequirements(Command cmd, PrivRequest pc)
    {
        if(cmd == null)
            return false;
        
        if(commandCooldowns.containsKey(pc.commandName()))
            if(!commandCooldowns.get(pc.commandName()).isReady())
                return false;
        
        if(!Arrays.asList(pc.getBadges()).stream()
                .anyMatch((String b)-> {return cmd.hasPermission(b);}))
            return false;
        
        return (pc.commandValue().split(" ").length >= cmd.getExpectedArgNo());
    }
    
    public void handle(PrivRequest pr)
    {
        if(!privCommands.hasCommand(pr.commandName()))
            if(whisperCommands.hasCommand(pr.commandName()))
                client.timeoutUser(pr.getUser(), 10, "Don't use whisper commands in public chat please!");
        
        Command<PrivRequest> cmd = privCommands.getCommand(pr.commandName());
        
        if(!meetsRequirements(cmd, pr))
            return;
        
        String result = cmd.execute(pr);
        
        if(!result.isEmpty())
        {
            client.sendMessage(result);
            if(!commandCooldowns.containsKey(pr.commandName()))
                commandCooldowns.put(pr.commandName(), 
                        new BlockingCooldown(cmd.getCooldown(), 0, 
                                TimeUnit.SECONDS));
        }
        
    }
    
    private boolean meetsRequirements(Command cmd, WhisperRequest wc)
    {
        if(commandCooldowns.containsKey(wc.commandName()))
            if(!commandCooldowns.get(wc.commandName()).isReady())
                return false;
        
        if(!cmd.hasPermission(wc.getUserType()))
            return false;
        
        List<String> split = Arrays.asList(wc.commandValue().split(" "));
        split.removeIf((s)->{return s.isEmpty();});
        return (split.size() >= cmd.getExpectedArgNo());
    }
    
    public void handle(WhisperRequest wr)
    {
        if(!whisperCommands.hasCommand(wr.commandName()))
            return;
        
        Command cmd = whisperCommands.getCommand(wr.commandName());
        
        if(!meetsRequirements(cmd, wr))
            return;
        
        String result = cmd.execute(wr);
                
        if(!result.isEmpty())
            client.sendMessage(result);
    }
    
    public void handle(OperationalRequestMessage orm)
    {
        switch(orm.getOperation())
        {
            case INFO: 
                infoCommand(orm.commandName(), orm);
                break;
            case ADD:
                addCommand(orm.commandName(), orm);
                break;
            case EDIT:
                editCommand(orm.commandName(), orm);
                break;
            case CANCEL: 
                cancelCommand(orm.commandName());
                break;
            case DELETE:
                deleteCommand(orm.commandName());
                break;
            default:
                throw new AssertionError(orm.getOperation().name());
            
        }
    }
    
    private void infoCommand(String name, OperationalRequestMessage orm)
    {
        Command cmd = null;
        
        if(privCommands.hasCommand(name))
            cmd = privCommands.getCommand(name);
        if(whisperCommands.hasCommand(name))
            cmd = whisperCommands.getCommand(name);
        
        if(cmd != null)
            client.sendWhisper(orm.getUser(), cmd.getInfoRequest());
    }
    
    private void addCommand(String name, OperationalRequestMessage orm)
    {
        if(orm.commandValue().isEmpty())
            return;
        
        CustomCommandFactory ccf = new CustomCommandFactory();
        Command cmd = ccf.create(name, orm.getCooldown(), orm.commandValue(), 
                orm.getPermittedUsers());
        
        if(privCommands.addCommandIfAbsent(cmd))
            client.sendMessage(String.format("%s command has been added!", name));
        else
            client.sendMessage(String.format("Unable to add command", name));
    }
    
    private void editCommand(String name, OperationalRequestMessage orm)
    {
        if(privCommands.hasCommand(name))
            if(!privCommands.getCommand(name).isCustom())
            {
                client.sendMessage(String.format("%s command is not a custom "
                        + "command and cannot be updated from chat", name));
                return;
            }
        
        CustomCommandFactory ccf = new CustomCommandFactory();
        Command cmd = ccf.create(name, orm.getCooldown(), orm.commandValue(),
                orm.getPermittedUsers());
        
        privCommands.addCommand(cmd);
        client.sendMessage(String.format("%s command has been updated", 
                name));
    }
    
    private void cancelCommand(String name)
    {
        String result = "";
        
        if(privCommands.hasCommand(name))
            result = privCommands.getCommand(name).cancel();
        else if(whisperCommands.hasCommand(name))
            result = whisperCommands.getCommand(name).cancel();
        
        if(!result.isEmpty())
            client.sendMessage(result);
    }
    
    private void deleteCommand(String name)
    {
        if(privCommands.hasCommand(name))
        {
            if(!privCommands.getCommand(name).isCustom())
            {
                client.sendMessage(String.format("%s is not a custom command "
                        + "and cannot be deleted from chat", name));
                return;
            }
            privCommands.removeCommand(name);
            client.sendMessage(String.format("%s command has been deleted!", 
                    name));
        }
        else
            client.sendMessage(String.format("Error deleting command %s: "
                    + "No such command exists!", name));
    }
    
    
}
