package Twitch.Command.Custom.Variable;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class Decrementer implements CommandVariable
{
    private int count;
    
    public Decrementer()
    {
        this.count = 0;
    }

    @Override
    public String replaceeRegex() {return "\\-\\-";}

    @Override
    public String execute()
    {
        count-= 1;
        return String.valueOf(count);
    }
    
    @Override
    public void refresh()
    {
        count = 0;
    }
    
    
}
