package rpg.engine;

import java.util.HashMap;


public class Story {
    static HashMap<String,Object> variables = new HashMap<>();
    static HashMap<String,Action> actions = new HashMap<>();
    //Note: the following code is an attempted implementation of the read function
    static HashMap<String,Class> typeMap = new HashMap<>();
    static
    {
        typeMap.put("java.lang.String",String.class);
        typeMap.put("java.lang.Integer",Integer.class);
        typeMap.put("java.lang.Boolean",Boolean.class);

    }
    /** This function will be used to obtain player vars when saving.*/
    static String[] getSaveString(HashMap<String,Object> vars) {
        String[] result = new String[vars.size()];
        int k = 0;
        for (String str : vars.keySet()) {
            result[k] = CONST.SAVE.VAR_STR_DELIM + str + CONST.SAVE.VAR_TYPE_DELIM + vars.get(str).getClass().getTypeName() + CONST.SAVE.VAR_VAL_DELIM + vars.get(str).toString();
            k++;
        }
        return result;
    }
    /** Save read function */
    static HashMap<String,Object> readSaveString(String[] vars){
        HashMap<String,Object> result = new HashMap<>();
        String[] temp;
        String name,type;
        Object value;
        for(String line:vars)
        {
            temp = line.split(CONST.SAVE.VAR_TYPE_DELIM,1);
            name = temp[0];
            temp = temp[1].split(CONST.SAVE.VAR_VAL_DELIM,1);
            type = temp[0];
            value = temp[1];
            //This code is designed to convert a string into its true type
            /*if(typeMap.containsKey(type))
                value = typeMap.get(type).valueOf(value);
            else
                continue;*/
            result.put(name,value);
        }
        return result;
    }
    /**Add an object to the variable table if it isnt there already*/
    public static void addVar(String name, Object value)
    {
        variables.putIfAbsent(name, value);
    }
    /**Check if the variable table has a variable*/
    public static Boolean hasVar(String name)
    {
        return variables.containsKey(name);
    }
    /**Check if the variable table has a variable and its type is string*/
    public static Boolean hasString(String name)
    {
        return (variables.containsKey(name) && (variables.get(name) instanceof String));
    }
    /**Change a variables value*/
    public static void setVar(String name, Object value)
    {
        variables.put(name, value);
    }
    /**Get a variable from the variable table*/
    public static Object getVar(String name)
    {
        if(!variables.containsKey(name))return "<Null pointer key(key does not exist)>";
        return variables.get(name);
    }
    /**get a variable thats a string from the variable table*/
    public static String getString(String name)
    {
        if(!(variables.get(name) instanceof String))return "<Incorrect variable type(Requested String)>";
            return (String) variables.get(name);

    }
    /**clears the variable table<p>There should never be a reason to clear the action table and that is why no clearActions function exists*/
    public static void clearVars()
    {
        variables.clear();
    }
    
    
    
    /**Check if the action table has a function*/
    public static Boolean hasAction(String name)
    {
        return actions.containsKey(name);
    }
    /**Add an action to the action table if it isnt there already.*/
    public static void addAction(String name, Action value)
    {
        actions.putIfAbsent(name, value);
    }
    /**Set and override an action variable*/
    public static void setAction(String name, Action value)
    {
        actions.put(name, value);
    }
    /**call and get the result of an action<p>returns null if action does not exist</p>*/
    public static String callAction(String name, String[] args)
    {
        if(!actions.containsKey(name))
            return null;
        return actions.get(name).run(args);
    }
    /**get and action if you for some reason don't want callAction*/
    public static Action getAction(String name)
    {
        return actions.get(name);
    }
    /**This function should never be used, I don't know why it is ever here<br>(don't remember writing it)*/
    public static void clearActions()
    {
        actions.clear();
    }
    
    
    /**Nuke everything!*/
    public static void clear()
    {
        clearActions();
        clearVars();
    }
    
}
