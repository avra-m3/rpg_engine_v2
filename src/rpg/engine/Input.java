package rpg.engine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.util.HashMap;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author A
 */


public class Input implements Format{
    
    
    private static BufferedReader console   = new BufferedReader(new InputStreamReader(System.in));
    HashMap<String,String[]> storyFile  = new HashMap<>();
    
    
    public static void main(String[] args)
    {
        System.out.println("--------------------------------------");
        System.out.println("Running input.java in debug mode");
        System.out.println("-");
        System.out.printf("Testing input.read(String name)\n Result: %s\n", Input.read("Story1.script"));
    }
   
    // Run stuff on initialisation
    void load( String filename )
    {
        this.storyFile = Input.read(filename);
    }

    static HashMap<String, String[]> read(String name)
    {
        HashMap<String, String[]> script = new HashMap<>();
        try
        {
            BufferedReader file = new BufferedReader(new FileReader(name));
            String line = file.readLine();
            String[] story;
            while ( file.ready() )
            {
                line = line.trim();
                
                if(line.startsWith(CODE_DELIM))
                {
                    line = line.replace(CODE_DELIM, "");
                    story = new String[32];
                    String scLine = "";
                    int i = 0;
                    
                    while ( file.ready() && !(scLine = file.readLine() ).startsWith(CODE_DELIM) )
                    {
                        story[i] = scLine;
                        i++;
                    }
                    
                    for(String code:line.split("\\"+OR_CODE_DELIM))
                    {
                        // use putIfAbsent so the earliest copy of any code will be the one used
                        script.putIfAbsent(code,story);
                    }
                    line = scLine;
                }
                else
                    line = file.readLine();
            }
        }
        catch(Exception ex)
        {
            
        }
        return script;
    }
    public String[] getScript(String code)
    {
        if(!this.storyFile.containsKey(code))
        {
            //System.out.println("[ERROR]: Code not found: ("+ code + ")");
            return this.storyFile.get("*");
        }
        return this.storyFile.get(code);
    }
    public static String requestString()
    {
        try
        {
            String result = console.readLine();
            return result;
        }
        catch(IOException ex)
        {
            System.exit(-43);
        }
        return new String();
    }
    public static void request()
    {
        try
        {
            console.readLine();
        }
        catch(Exception ex)
        {
            
        }
        
    }
    public static String requestString(String regex)
    {
        try
        {
            
            String result = console.readLine();
            while(1==1)
            {
                if(result.matches(regex))
                    return result;
                System.out.println("Sorry that is a bad answer, please try again.");
                result = console.readLine();
            }
        }
        catch(IOException ex)
        {
            System.exit(-43);
        }
        return new String();
    }
    static Integer requestChoice()
    {
        System.out.println("Please choose an option:");
        try
        {
            while(true)
            {
                String result = console.readLine();
                if(result.trim().equals("") )
                {
                }
                else if(result.matches("\\d{" + result.length() + "}"))
                    return Integer.parseInt(result);
                System.out.println("You must enter a number");
            }
        }
        catch(IOException ex)
        {
            System.out.println(ex);
            System.exit(-300);
        }
        return -1;
    }
    public static Integer requestChoice(int lowerLimit, int upperLimit)
    {
        System.out.println("Please choose an option:");
        try
        {
            while(true)
            {
                String result = console.readLine();
                if(result.trim().equals("") )
                {
                }
                else if(result.matches("["+ lowerLimit + "-" + upperLimit + "]"))
                    return Integer.parseInt(result);
                System.out.println("You must enter a number between " + lowerLimit + " and " + upperLimit);
            }
        }
        catch(IOException ex)
        {
            System.out.println(ex);
            System.exit(-300);
        }
        return -1;
    }
            
}