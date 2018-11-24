package Twitch.Command.Custom.Variable;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class Incrementer implements CommandVariable
{
    private int count;
    
    public Incrementer()
    {
        this.count = 0;
    }

    @Override
    public String execute() 
    {
        count+= 1;
        return String.valueOf(count);
    }

    @Override
    public String replaceeRegex() {return "\\+\\+";}

    @Override
    public void refresh()
    {
        count = 0;
    }
    
}
