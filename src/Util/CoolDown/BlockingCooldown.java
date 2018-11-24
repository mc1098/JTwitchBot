package Util.CoolDown;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class BlockingCooldown implements Cooldown
{
    private LocalTime last;
    private final long cooldown;
    private long accrued;
    private final long accruedLimit;
    
    public BlockingCooldown(long cooldown, long accruedLimit, TimeUnit tu)
    {
        this.last = LocalTime.now();
        this.cooldown = TimeUnit.MILLISECONDS.convert(cooldown, tu);
        this.accrued = 0;
        this.accruedLimit = TimeUnit.MILLISECONDS.convert(accruedLimit, tu);
    }
    
    public boolean BlockAndWait()
    {
        long value =  last.until(LocalTime.now(), ChronoUnit.MILLIS); 
        long toWait = (cooldown - value) - accrued;
        
        if(toWait < 0)
        {
            accrued = toWait *-1;
            accrued = accrued < accruedLimit ? accrued : accruedLimit;
            last = LocalTime.now();
            return true;
        }
        else
        {
            try
            {
                TimeUnit.MILLISECONDS.sleep(toWait);
                accrued = 0;
                last = LocalTime.now();
                return true;
            } catch(InterruptedException ex)
            {
                return false;
            }
        }
    }

    @Override
    public boolean isReady()
    {
        long value =  last.until(LocalTime.now(), ChronoUnit.MILLIS); 
        long toWait = (cooldown - value) - accrued;
        
        if(toWait < 0)
        {
            accrued = toWait *-1;
            last = LocalTime.now();
            return true;
        }
        else
            return false;
    }

    @Override
    public long peek()
    {
        long value =  last.until(LocalTime.now(), ChronoUnit.MILLIS); 
        return (cooldown - value) - accrued;
    }

    @Override
    public long getAccruedLimit(){return accruedLimit;}
            
    
}
