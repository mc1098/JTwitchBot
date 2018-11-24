package Twitch.Command;

import Twitch.Client.TwitchWriter;
import Twitch.Message.PRIVMSG.PrivRequest;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class RaffleCommand extends AbstractCommand<PrivRequest>
{
    private final TwitchWriter writer;
    private final CommandLibrary<PrivRequest> commandLibrary;
    private final ThreadFactory raffleThreadFactory;
    private ScheduledExecutorService executorService;
    private ScheduledFuture<?> scheduledRaffle;
    private RaffleKeyCommand key;
    

    public RaffleCommand(TwitchWriter writer, CommandLibrary<PrivRequest> cl)
    {
        super("raffle", 0, 1, "broadcaster");
        this.writer = writer;
        this.commandLibrary = cl;
        this.raffleThreadFactory = ((r)-> {return new Thread(r, "Raffle Thread");});
        
    }

    @Override
    public boolean isCustom()
    {return false;}

    @Override
    public String execute(PrivRequest request)
    {
        if(request.commandValue().isEmpty())
            return "";
        
        if(request.commandValue().equalsIgnoreCase("winner"))
            return manualSelectWinner(request);
                
        if(scheduledRaffle != null && !scheduledRaffle.isDone())
            return String.format(".w %s %s raffle is still accepting requests, "
                    + "please either wait for the timer to run out [%s seconds] "
                    + "or end it manually by using !raffle winner", 
                    request.getUser(), key.getName(), 
                    scheduledRaffle.getDelay(TimeUnit.SECONDS));
        
        String[] args = request.commandValue().split(" ");
        
        if(args.length != 2)
            return setupRaffle(request, request.commandValue(), 1);
        else
        {
            String keyword = args[0];
            String wait = args[1];
            if(wait.matches("\\d+"))
                return setupRaffle(request, keyword, Integer.valueOf(wait));
            else
                return "";
        }
    }

    private String manualSelectWinner(PrivRequest request)
    {
        if(scheduledRaffle == null)
            return String.format(".w %s No raffle has been setup! please use "
                    + "!raffle [keyword] to setup a raffle!",
                    request.getUser());
        else
        {
            if(scheduledRaffle != null)
                if(!scheduledRaffle.cancel(true))
                    return "";
            String result = key.selectWinner();
            commandLibrary.removeCommand(key.getName());
            key = null;
            executorService.shutdown();
            if(result.isEmpty())
                result = "No correct entries have been made in the time limit! "
                        + "No winner after the time limit!";
            writer.sendWhisper(request.getUser(), result);
            return result;
        }
    }
    
    private String setupRaffle(PrivRequest request, String keyword, int mins)
    {
        key = new RaffleKeyCommand(keyword);
        if(!commandLibrary.addCommandIfAbsent(key))
            return String.format(".w %s The keyword cannot be 'raffle' "
                    + "please choose another word!", request.getUser());
        
        
        executorService = Executors.newScheduledThreadPool(1, raffleThreadFactory);
        
        scheduledRaffle = executorService.schedule(()->
        {
            if(key != null)
                {
                    String result = key.selectWinner();
                    commandLibrary.removeCommand(key.getName());
                    key = null;
                    executorService.shutdown();
                    if(result.isEmpty())
                        result = "No correct entries have been made in the time limit! "
                        + "No winner after the time limit!";
                    writer.sendMessage(result);
                    writer.sendWhisper(request.getUser(), result);
                }
        }, mins, TimeUnit.MINUTES);
        
        return String.format("A new raffle has been started! To enter type !%s. "
                + "The winner will be choosen in %d mins", keyword, mins);
        
    }

    @Override
    public String cancel()
    {
        if(key != null && scheduledRaffle != null && executorService != null)
        {
            String result = String.format("!%s raffle has been cancelled! "
                    + "No winner will be choosen.", key.getName());
            commandLibrary.removeCommand(key.getName());
            scheduledRaffle.cancel(true);
            executorService.shutdown();
            key = null;
            return result;
        }
        
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
        getPermittedUsers().forEach((pu) ->
        {
            sb.append(pu);
            sb.append(" ");
        });
        sb.append("Cooldown: ");
        sb.append(getCooldown());
        sb.append(" seconds. ");
        sb.append("timer based raffle. ");
        sb.append("[1]!raffle keyword [2]!raffle keyword 10. ");
        sb.append("[1] will create a new raffle with a default timer. ");
        sb.append("[2] will create a new raffle with a set 10min timer. ");
        sb.append("both use 'keyword' as the keyword and chatters are ");
        sb.append(" instructed to use !keyword to enter the raffle. ");
        sb.append("raffles can be ended prematurely by using !raffle winner. ");
        
        if(scheduledRaffle != null && !scheduledRaffle.isDone())
        {
            sb.append(key.getName());
            sb.append(" raffle is currently running and will finish in ");
            sb.append(scheduledRaffle.getDelay(TimeUnit.SECONDS));
            sb.append(" seconds.");
        }
            
        return sb.toString();
    }
    
}


class RaffleKeyCommand extends AbstractCommand<PrivRequest>
{
    
    private final Set<String> rafflers;

    public RaffleKeyCommand(String name)
    {
        super(name);
        this.rafflers = new HashSet<>();
    }
    
    public String selectWinner()
    {
        int size = rafflers.size();
        if(size == 0)
            return "";
        Random rnd = new Random();
        int i = rnd.nextInt(size);
        
        String winner = (String) rafflers.toArray()[i];
        if(!winner.isEmpty())
            return String.format("The winner is %s!", winner);
        else 
            return "";
    }

    @Override
    public boolean isCustom()
    {return false;}

    @Override
    public String execute(PrivRequest request)
    {
        if(!request.getUser().isEmpty())
            rafflers.add(request.getUser());
        return "";
    }

    @Override
    public String cancel() {return "";}

    @Override
    public String getInfoRequest()
    {
        return String.format("Command: %s. This is a temporary command created "
                + "from the !raffle command", getName());
    }
    
    
    
}
