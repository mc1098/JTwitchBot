package Util.CoolDown;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public interface Cooldown
{
    /**
     * @return Gets limit of accruement after cooldown has been ready.
     */
    public long getAccruedLimit();
    
    /**
     * Checks if cooldown is over and returns boolean. 
     * If this method returns true it acts as an indication that the cooldown 
     * should be reset. If the intention is just to see the state of this Cooldown
     * object see {@link Cooldown#peek()}
     * 
     * @return true if the cooldown period has been met, false if it hasn't
     */
    public boolean isReady();
    
    /**
     * Calculates the current wait time (ms) till {@link Util.CoolDown.Cooldown#isReady()}
     * would return true. This method does not reset the cooldown when called.
     * This can return a minus amount when the cooldown has been ready.
     * @return 
     */
    public long peek();
}
