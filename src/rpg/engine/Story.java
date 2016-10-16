package rpg.engine;

import java.util.Arrays;
import java.util.HashMap;


public class Story {
    private HashMap<String,Object> variables = new HashMap<>();
    private HashMap<String,Action> actions = new HashMap<>();
    /** This function will be used to obtain player vars when saving.*/
     String[] getSaveString(HashMap<String,Object> vars) {
        String[] result = new String[vars.size()];
        int k = 0;
        for (String str : vars.keySet()) {
            if(vars.get(str).getClass().isArray())
                result[k] = CONST.SAVE.VAR_STR_DELIM + str + CONST.SAVE.VAR_TYPE_DELIM + vars.get(str).getClass().getTypeName() + CONST.SAVE.VAR_VAL_DELIM + Arrays.toString((Object[]) vars.get(str));
            else
                result[k] = CONST.SAVE.VAR_STR_DELIM + str + CONST.SAVE.VAR_TYPE_DELIM + vars.get(str).getClass().getTypeName() + CONST.SAVE.VAR_VAL_DELIM + vars.get(str).toString();
            k++;
        }
        return result;
    }
    /** Save read function */
     HashMap<String,Object> readSaveString(String[] vars){
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
            if(type.equals("java.lang.Integer"))
                value = Integer.valueOf((String) value);
            else if(type.equals("java.lang.Boolean"))
                value = Boolean.valueOf((String) value);
            else if(!(type.equals("java.lang.String")))
                value += "<" + type + ">";
            if(type.endsWith("[]"))
            {
                type = type.substring(0,type.length()-2);
                value = StringToArray(type,(String) value);
            }

            result.put(name,value);
        }
        return result;
    }
    /**Add an object to the variable table if it isnt there already*/
    public void addVar(String name, Object value)
    {
        variables.putIfAbsent(name, value);
    }
    /**Check if the variable table has a variable*/
    public Boolean hasVar(String name)
    {
        return variables.containsKey(name);
    }
    /**Check if the variable table has a variable and its type is string*/
    public Boolean hasString(String name)
    {
        return (variables.containsKey(name) && (variables.get(name) instanceof String));
    }
    /**Change a variables value*/
    public void setVar(String name, Object value)
    {
        variables.put(name, value);
    }
    /**Get a variable from the variable table*/
    public Object getVar(String name)
    {
        if(!variables.containsKey(name))return "<Null pointer key(key does not exist)>";
        return variables.get(name);
    }
    /**get a variable thats a string from the variable table*/
    public String getString(String name)
    {
        if(!(variables.get(name) instanceof String))return "<Incorrect variable type(Requested String)>";
            return (String) variables.get(name);

    }
    /**clears the variable table<p>There should never be a reason to clear the action table and that is why no clearActions function exists*/
    public void clearVars()
    {
        variables.clear();
    }
    
    
    
    /**Check if the action table has a function*/
    public Boolean hasAction(String name)
    {
        return actions.containsKey(name);
    }
    /**Add an action to the action table if it isnt there already.*/
    public void addAction(String name, Action value)
    {
        actions.putIfAbsent(name, value);
    }
    /**Set and override an action variable*/
    public void setAction(String name, Action value)
    {
        actions.put(name, value);
    }
    /**call and get the result of an action<p>returns null if action does not exist</p>*/
     public String callAction(String name, String[] args)
    {
        if(!actions.containsKey(name))
            return null;
        return actions.get(name).run(args);
    }
    /**get and action if you for some reason don't want callAction*/
     public Action getAction(String name)
    {
        return actions.get(name);
    }
    /**This function should never be used, I don't know why it is ever here<br>(don't remember writing it)*/
     public void clearActions()
    {
        actions.clear();
    }
    
    
    /**Nuke everything!*/
     public void clear()
    {
        clearActions();
        clearVars();
    }
    static Object[] StringToArray(String type, String str) {
        Object[] result;
        str = str.substring(1,str.length()-1);
        String[] temp = str.split(",");
        result = new Object[temp.length];
        int x = 0;
        if(type == "java.lang.Integer")
            for(String s:temp) {
                result[x] = Integer.valueOf(s);
                ++x;
            }
        else if(type == "java.lang.Boolean")

            for(String s:temp) {
                if(s == "null") s = null;
                result[x] = Boolean.valueOf(s);
                ++x;
            }
        else if(!(type == "java.lang.String"))
            for(String s:temp) {
                result[x] = String.valueOf(s);
                ++x;
            }
        else result = temp;

        return result;
    }
    
}
