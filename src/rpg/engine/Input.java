package rpg.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Input implements CONST.READ{

    HashMap<String,String[]> storyFile  = new HashMap<>();

    // Run stuff on initialisation
    public void load( String filename )
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
                    String scLine;
                    int i = 0;
                    
                    while ( file.ready() && !(scLine = file.readLine() ).startsWith(CODE_DELIM) )
                    {
                        story[i] = scLine;
                        i++;
                        file.mark(1024);
                    }
                    file.reset();
                    for(String code:line.split("\\"+OR_CODE_DELIM))
                    {
                        // use putIfAbsent so the earliest copy of any code will be the one used
                        script.putIfAbsent(code,story);
                    }
                }
                line = file.readLine();
            }
        }
        catch(Exception ex)
        {
            System.exit(-3);
        }
        return script;
    }
    /**String[] readVars(BufferedReader)*/
    String[] readVars(BufferedReader file)
    {
        return new String[1];
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

}