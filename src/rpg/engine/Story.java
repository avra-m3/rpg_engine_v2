package rpg.engine;

import java.util.HashMap;



public class Story {
    static HashMap<String,Object> variables = new HashMap<>();
    static HashMap<String,Action> actions = new HashMap<>();
    
    
    public static void addVar(String name, Object value)
    {
        variables.putIfAbsent(name, value);
    }
    public static Boolean hasVar(String name)
    {
        return variables.containsKey(name);
    }
    public static Boolean hasString(String name)
    {
        return variables.containsKey(name) && (variables.get(name) instanceof String);
    }
    public static void setVar(String name, Object value)
    {
        variables.put(name, value);
    }
    
    public static Object getVar(String name)
    {
        return variables.get(name);
    }
    public static String getString(String name)
    {
        return (String) variables.get(name);
    }
    public static void clearVars()
    {
        variables.clear();
    }
    
    
    
    
    public static Boolean hasAction(String name)
    {
        return actions.containsKey(name);
    }
    public static void addAction(String name, Action value)
    {
        actions.put(name, value);
    }
    public static String callAction(String name, String[] args)
    {
        if(!actions.containsKey(name))
            return null;
        return actions.get(name).run(args);
    }
    public static Action getAction(String name)
    {
        return actions.get(name);
    }
    public static void clearActions()
    {
        actions.clear();
    }
    
    
    
    public static void clear()
    {
        clearActions();
        clearVars();
    }
    
}
