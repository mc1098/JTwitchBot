package Util.Reference;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class ReferenceWrapper<T> 
{
    private T t;
    
    public ReferenceWrapper()
    {
        this(null);
    }
    
    public ReferenceWrapper(T t)
    {
        this.t = t;
    }
    
    public T get() {return t;}
    
    public void set(T t) {this.t = t;}
}
