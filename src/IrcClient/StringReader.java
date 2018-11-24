package IrcClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class StringReader
{
    private InputStream is;
    
    public StringReader(InputStream is)
    {
        this.is = is;
    }
    
    public String readString() throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        if((length = is.read(buffer)) != -1)
            result.write(buffer, 0, length);
        return result.toString("UTF-8");
    }
    
    public void updateStream(InputStream is)
    {
        this.is = is;
    }
    
}
