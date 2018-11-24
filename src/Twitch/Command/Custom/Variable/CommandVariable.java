package Twitch.Command.Custom.Variable;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface CommandVariable
{
    public String replaceeRegex();
    public String execute();
    public void refresh();
}
