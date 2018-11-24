package IrcClient;

import Util.CoolDown.BlockingCooldown;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class BlockingCoolDownTest
{
    
    public BlockingCoolDownTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of BlockAndWait method, of class BlockingCooldown.
     */
    @Test
    public void testBlockAndWait()
    {
        System.out.println("BlockAndWait");
        boolean expResult = true;
        LocalTime before = LocalTime.now();
        BlockingCooldown instance = new BlockingCooldown(500, 0, TimeUnit.MILLISECONDS);
        System.out.println(String.format("Before: %s", before.toString()));
        boolean result = instance.BlockAndWait();
        LocalTime after = LocalTime.now();
        System.out.println(String.format("After: %s \nwait: %d ms", 
                after.toString(), before.until(after, ChronoUnit.MILLIS)));
        assertEquals(expResult, result);
    }
    
}
