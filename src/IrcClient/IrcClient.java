package IrcClient;



/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface IrcClient extends Reconnectable, AutoCloseable
{
    public boolean isConnected();
    public boolean isReconnecting();
    public StringReader getReader();
    public StringWriter getWriter();
}
