package Twitch.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class TwitchMessageParser 
{
    private final List<TwitchMessageFactory> messageFactorys;
    
    public TwitchMessageParser(List<TwitchMessageFactory> factorys)
    {
        this.messageFactorys = factorys;
    }
    
    public TwitchMessageParser(TwitchMessageFactory...factorys)
    {
        this(Arrays.asList(factorys));
    }
    
    public TwitchMessage parse(String string)
    {
        string = string.trim();
        for (TwitchMessageFactory tmf : messageFactorys)
            if(string.matches(tmf.acceptingRegex()))
                return tmf.create(string);
        return new RawTwitchMessage(string);
    }
    
    public List<TwitchMessage> parseBulkMessage(String string)
    {
        string = string.trim();
        List<TwitchMessage> messages = new ArrayList<>();
        
        for (TwitchMessageFactory tmf : messageFactorys)
        {
            Pattern p = Pattern.compile(tmf.acceptingRegex());
            Matcher m = p.matcher(string);
            if(m.find())
            {
                int i = m.start();
                int ii = m.end();
                String match = string.substring(i, ii);
                TwitchMessage tm = tmf.create(match);
                if(tm != null)
                    messages.add(tm);
                else
                    messages.add(new RawTwitchMessage(match));
                string = string.replace(match, "");
            }
        }
        
        if(!string.isEmpty())
            messages.add(new RawTwitchMessage(string));
        
        return messages;
        
    }
    
    public void add(TwitchMessageFactory tmf)
    {
        messageFactorys.add(tmf);
    }
    
    public void add(TwitchMessageFactory...tmfs)
    {
        messageFactorys.addAll(Arrays.asList(tmfs));
    }
    
    public void remove(TwitchMessageFactory tmf)
    {
        messageFactorys.remove(tmf);
    }
    
}
