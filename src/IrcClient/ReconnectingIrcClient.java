package IrcClient;

import Util.CoolDown.BlockingCooldown;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class ReconnectingIrcClient implements IrcClient
{
    protected final SocketAddress socketAddress;
    protected Socket socket;
    private final StringReader reader;
    private final StringWriter writer;
    private final AtomicBoolean isReconnecting;
    
    public ReconnectingIrcClient(Socket socket, InputStream is, OutputStream os)
    {
        this.socketAddress = socket.getRemoteSocketAddress();
        this.socket = socket;
        this.reader = new StringReader(is);
        this.writer = new StringWriter(os);
        this.isReconnecting = new AtomicBoolean();
    }
    
    @Override
    public StringReader getReader() {return reader;}
    
    @Override
    public StringWriter getWriter() {return writer;}

    @Override
    public void close() throws IOException
    {
        socket.close();
    }
    
    @Override
    public boolean isReconnecting() {return isReconnecting.get();}
    
    @Override
    public boolean isConnected()
    {
        return socket.isConnected();
    }
    
    @Override
    public boolean reconnect()
    {
        isReconnecting.set(true);
        BlockingCooldown reconnectCoolDown = new BlockingCooldown(30, 0, TimeUnit.SECONDS);
        long wait = 0;
        
        while(wait < 300)
        {
            try
            {
                socket.close();
                if(socket.isClosed())
                {
                    socket = new Socket();
                    socket.connect(socketAddress);
                    if(socket.isConnected())
                    {
                        reader.updateStream(socket.getInputStream());
                        writer.updateStream(socket.getOutputStream());
                        isReconnecting.set(false);
                        return true;
                    }
                }
            } catch (IOException e)
            {
                //logging
                System.err.println(e.toString());
            }
            
            if(reconnectCoolDown.BlockAndWait())
                wait += 30;
            else
                throw new RuntimeException("Critical failure when reconnecting, "
                        + "unable to reconnect");
            
        }
        //reconnecting has failed after reaching 5 mins
        isReconnecting.set(false);
        return false;
    }
}
