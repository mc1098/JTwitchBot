package Twitch.Command.Custom.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MaraKorvus <MaraKorvus@gmail.com>
 */
public class CommandVariableFactory
{
    
   public List<String> commandVariablePlaceholders()
   {
       return new ArrayList<String>() 
       {{
           add("++");
           add("--");
       }};
   }
   
   public CommandVariable create(String cvph)
   {
       if(cvph.equals("++"))
           return new Incrementer();
       if(cvph.equals("--"))
           return new Decrementer();
       return new NullCommandVariable();
   }
   
   public List<CommandVariable> create(String cvph, int i)
   {
       List<CommandVariable> list = new ArrayList<>();
       for (int j = 0; j < i; j++)
           list.add(create(cvph));
       return list;
   }
    
}

class NullCommandVariable implements CommandVariable
{

    @Override
    public String replaceeRegex() {return "";}

    @Override
    public String execute() {return "";}

    @Override
    public void refresh()
    {}
    
}