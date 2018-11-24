package IrcClient;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface IrcClientWriter extends Runnable
{
    public void sendMessage(String message);
}
