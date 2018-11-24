package IrcClient;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class StringWriter
{
    private OutputStream os;
    
    public StringWriter(OutputStream os)
    {
        this.os = os;
    }
    
    public void write(String string) throws IOException
    {
        os.write(string.getBytes("UTF-8"));
        os.flush();
    }
    
    public void updateStream(OutputStream os)
    {
        this.os = os;
    }
    
}
