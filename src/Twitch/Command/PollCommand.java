package Twitch.Command;

import Twitch.Client.TwitchWriter;
import Twitch.Message.PRIVMSG.PrivRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class PollCommand extends AbstractCommand<PrivRequest>
{

    private final TwitchWriter writer;
    private final ThreadFactory pollThreadFactory;
    private ScheduledExecutorService executorService;
    private ScheduledFuture<?> scheduledPoll;
    private PollCounter key;
    
    private static final String SETUP_REGEX = "([\\s\\S]*[?]) ([\\s\\S]*)";
    private static final String SETUP_REGEX_TIMER = "([\\s\\S]*[?]) ([\\s\\S]*)\\s(\\d+)";
    
    public PollCommand(TwitchWriter writer)
    {
        super("poll", 0, 1, "broadcaster", "moderator");
        this.writer = writer;
        this.pollThreadFactory = ((r)-> {return new Thread(r, "Poll Thread");});
    }

    @Override
    public boolean isCustom()
    {return false;}

    @Override
    public String execute(PrivRequest request)
    {
        if(request.commandValue().isEmpty())
            return "";
        
        if(request.commandValue().equalsIgnoreCase("result"))
            return manualSelectResult(request);
        
        if(request.commandValue().matches(SETUP_REGEX))
        {
            if(scheduledPoll != null && !scheduledPoll.isDone())
                return String.format(".w %s %s poll is still accepting requests, "
                    + "please either wait for the timer to run out [%s seconds] "
                    + "or end it manually by using !poll result", 
                    request.getUser(), key.getName(), 
                    scheduledPoll.getDelay(TimeUnit.SECONDS));
            
            if(request.commandValue().matches(SETUP_REGEX_TIMER))
                return setupPoll(request);
            else
                return setupPoll(request, 1);
            
        } else if(key != null)
        {
            key.poll(request);
        }
        
        return "";
    }
    
    private String manualSelectResult(PrivRequest request)
    {
        if(scheduledPoll == null)
            return String.format(".w %s No poll has been setup! please use "
                    + "!poll [question] [options] "
                    + "[timer (optional)] to setup a raffle!",
                    request.getUser());
        else
        {
            if(scheduledPoll != null)
                if(!scheduledPoll.cancel(true))
                    return "";
            String result = key.getResult();
            key = null;
            executorService.shutdown();
            if(result.isEmpty())
                result = "No correct entries have been made in the time limit! "
                        + "No result after the time limit!";
            writer.sendWhisper(request.getUser(), result);
            return result;
        }
    }
    
    private String setupPoll(PrivRequest request)
    {
        Pattern p = Pattern.compile(SETUP_REGEX_TIMER);
        Matcher m = p.matcher(request.commandValue());
        
        if(!m.matches())
            return "";
        
        String question = m.group(1);
        String[] options = m.group(2).split(" ");
        int duration = Integer.valueOf(m.group(3));
        
        key = new PollCounter(question, options);
        
        setupPollSchedule(request, duration);
        
        StringBuilder sb = new StringBuilder(question);
        sb.append(" vote now by typing !poll [option]. The accepted options are: ");
        for (String o : options)
        {
            sb.append(o);
            sb.append(" ");
        }
        
        sb.append("The result will be announced in ");
        sb.append(duration);
        sb.append(" mins.");
        return sb.toString();
    }
    
    private String setupPoll(PrivRequest request, int duration)
    {
        
        Pattern p = Pattern.compile(SETUP_REGEX);
        Matcher m = p.matcher(request.commandValue());
        
        if(!m.matches())
            return "";
        
        String question = m.group(1);
        String[] options = m.group(2).split(" ");
        
        key = new PollCounter(question, options);
        setupPollSchedule(request, duration);
        
        StringBuilder sb = new StringBuilder(question);
        sb.append(" vote now by typing !poll [option]. The accepted options are: ");
        for (String o : options)
        {
            sb.append(o);
            sb.append(" ");
        }
        
        sb.append("The result will be announced in ");
        sb.append(duration);
        sb.append(" mins.");
        return sb.toString();
    
    }
    
    private void setupPollSchedule(PrivRequest request, int duration)
    {
        executorService = Executors.newScheduledThreadPool(1, pollThreadFactory);
        
        scheduledPoll = executorService.schedule(()->
        {
            if(key != null)
                {
                    String result = key.getResult();
                    key = null;
                    executorService.shutdown();
                    if(result.isEmpty())
                        result = "No correct entries have been made in the time limit! "
                        + "No winner after the time limit!";
                    writer.sendMessage(result);
                    writer.sendWhisper(request.getUser(), result);
                }
        }, duration, TimeUnit.MINUTES);
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
        sb.append("timer based poll. ");
        sb.append("[1]!poll [question] [#1 #2...]  [2]!poll [question] [#1 #2...] 10. ");
        sb.append("[1] will create a new poll with a default timer. ");
        sb.append("[2] will create a new poll with a set 10min timer. ");
        sb.append("chatters are instructed to choose one of the options by using ");
        sb.append("!poll [option]. Polls can be ended prematurely by using ");
        sb.append("!poll result. ");
        
        if(scheduledPoll != null && !scheduledPoll.isDone())
        {
            sb.append(key.getName());
            sb.append(" poll is currently running and will finish in ");
            sb.append(scheduledPoll.getDelay(TimeUnit.SECONDS));
            sb.append(" seconds.");
        }
            
        return sb.toString();
    }

    @Override
    public String cancel()
    {
        if(key != null && scheduledPoll != null && executorService != null)
        {
            String result = String.format("!%s poll has been cancelled! "
                    + "No result will be posted.", key.getName());
            scheduledPoll.cancel(true);
            executorService.shutdown();
            key = null;
            return result;
        }
        
        return "";
    }
    
    
}

class PollCounter
{

    private final String name;
    private final Map<String, Integer> optionMap;
    private final Set<String> voters;
    
    public PollCounter(String name, String...options)
    {
        this.name = name;
        this.optionMap = new HashMap<>();
        this.voters = new HashSet<>();
        for (String option : options)
            optionMap.put(option, 0);
    }
    
    public String getName() {return name;}

    
    public void poll(PrivRequest request)
    {
        if(!voters.contains(request.getUser()))
            if(optionMap.containsKey(request.commandValue()))
            {
                optionMap.put(request.commandValue(), 
                        optionMap.get(request.commandValue())+1);
                voters.add(request.getUser());
            }
    }
    
    public String getResult() 
    {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" results are: ");
        optionMap.forEach((s, i)-> {
            sb.append(s);
            sb.append(" = ");
            sb.append(i); 
            sb.append(" ");
        });
        
        return sb.toString();
    }
    
    
}