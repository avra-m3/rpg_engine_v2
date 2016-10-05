package rpg.engine;
import java.util.HashMap;



public abstract class Player
{
    //stuff that we need for the Player
    public String status;
    public Input lookup;
    public Story story;
    // List of random names to draw from

    HashMap<Integer,String[]> responseTable = new HashMap<>();
    HashMap<Integer,String[]> narrationTable = new HashMap<>();
    /**Player class constructor*/
    public Player() {
        this.lookup = new Input();
        this.story = new Story();
        this.status = "";
        this.init();
        
    }
    /***/
    public abstract void init();
    public void putNextStatus()
    {
        String[] myStatus = this.lookup.getScript(this.status);
        //System.out.print(Arrays.toString(myStatus));
        this.putStatus(myStatus);
    }
    // interpret and show Input
    public void putStatus(String[] script)
    {
        // wipe the current mapping
        this.narrationTable.clear();
        this.responseTable.clear();
        //
        for(String line : script)
        {
            //if an index is null, it means we have reached the end of the array
            if(line == null)
                break;
            line = line.trim();
            // if the line is empty we don't care about it
            if(line.isEmpty())
                continue;
            if(line.startsWith(CONST.READ.QUERY_DELIM))
            {
                this.addQuery(line.replace(CONST.READ.QUERY_DELIM,""));
            }
            else
            {
                this.addResponse(line);
            }
        }
            
    }
    public void addQuery(String query)
    {
        // split the query into actor and string, remembering to ignore escaped split delims
        String[] line = query.split("(?<!"+ CONST.READ.IGNORE_DELIM +")"+ CONST.READ.SPLIT_DELIM_1);
        // make sure it is the correct length
        if(line.length != 2)
            return;
        /*
        Make the line grammatically correct if it isnt already
        (Upper case first letter and strip trailing/begining spaces)
        */
        line[1] = line[1].trim().replaceAll("^[\\s\\S]", ("" +line[1].trim().charAt(0)).toUpperCase());
        this.narrationTable.put(this.narrationTable.size(), line);
    }
    public void addResponse(String response)
    {
        // we want to ignore decision delims preceeded by escape chars
        String[] line = response.split("(?<!"+ CONST.READ.IGNORE_DELIM +")"+ CONST.READ.SPLIT_DELIM_2);
        // we should only have 2 results if we get more play it safe and ignore this
        if(line.length != 2)
        {
            // Amendment: if we have a 1 element result while the table is empty assume this is a dead-end option.
            if(line.length == 1 && responseTable.isEmpty())
                this.responseTable.put(0, line);
            return;
        }

        this.responseTable.put(this.responseTable.size(), line);
    }
    public void narrateNext()
    {
        this.narrate(this.narrationTable);
    }
    public void narrate(HashMap<Integer,String[]> narrationTable)
    {
        for(int ind = 0;ind<narrationTable.size();ind++)
        {
           // System.out.println(narrationTable.get(ind+1));
            String actor = narrationTable.get(ind)[0];
            String line = narrationTable.get(ind)[1];
            
            actor = insertVars(actor);
            line = insertVars(line);
            
            this.talk(actor, line);
            Input.request();
        }
    }
    public void respondNext()
    {
        this.respond(this.responseTable);
    }
    public void respond(HashMap<Integer,String[]> responseTable)
    {
        // if there is no dialog attached to the only option available, dont bother asking for a decision

        if(this.responseTable.size() == 1 && this.responseTable.get(0).length == 1)
        {
            this.interpStringCode(this.responseTable.get(0)[0]);
            return;
        }
        for(int ind = 0;ind<responseTable.size();ind++)
        {
            String line = responseTable.get(ind)[1];
            
            line = insertVars(line);
            
            this.ask(ind+1, line);
        }
        System.out.println();
    }
    public void choose()
    {
        if(this.responseTable.isEmpty())
        {

            System.out.println("Empty choice table, exiting to avoid infinite loop");
            System.exit(-404);
            //just in case
            return;
        }

        Integer result = Input.requestChoice();
        while(!validateChoice(result-1))
        {
            System.out.println("That isn't a choice");
            result = Input.requestChoice();
        }
        this.interpStringCode(this.responseTable.get(result-1)[0]);   
        
    }
    public void talk(String actor, String line)
    {
        if(actor.isEmpty())
        {
            System.out.print(line);
            return;
        }
        System.out.printf("[%s]%-1s", actor,line);
    }
    public void ask(Integer index, String option )
    {
        System.out.printf("%d:%-2s\n", index, option );
    }
    
    public boolean validateChoice(int index)
    {
        /*if( this.responseTable.containsKey(index))
            return true;
        return false;*/
        return this.responseTable.containsKey(index);
    }
    public void interpStringCode(String code)
    {
        
        if(this.isActionCall(code))
        {
         //   System.out.println("made call using" + code);
            code = this.makeActionCall(code);
        }
        this.status += code;
    }
    public String insertVars(String line)
    {
        String[] varList = line.split("(?<!"+ CONST.READ.IGNORE_DELIM + ")" + CONST.READ.VAR_DELIM_1 +"|(?<!" + CONST.READ.IGNORE_DELIM + ")"+ CONST.READ.VAR_DELIM_2);
        String v;
        for(int ind = 0; ind < varList.length; ind++)
        {
            if(ind%2 == 0)
                continue;
            
            v = varList[ind];
            //System.out.println(this.getVar(v));
            line = line.replaceAll("<" + v + ">", this.getVar(v));
        }
        return line;
    }
    public String getVar(String var)
    {
        if(this.story.hasString(var))
            return this.story.getString(var);
        return "<Null Variable Pointer("+ var +") >";
    }
    public boolean isActionCall(String code)
    {  // System.out.println(Arrays.toString(this.actionTable.keySet().toArray()));
       // System.out.printf("Call: %s\nPassed check 1: %s\nPassed check 2: %s\nCheck 2 key: %s\n", code,code.startsWith("" + Format.FUNCTION_DELIM), this.actionTable.containsKey(code.replace(""+Format.FUNCTION_DELIM,"").trim()),code.replace(""+Format.FUNCTION_DELIM,"") );
        if(code.startsWith("" + CONST.READ.FUNCTION_DELIM))
            if(this.story.hasAction(code.replace(CONST.READ.FUNCTION_DELIM,"")))
                return true;
        return false;
    }
    public String makeActionCall(String code)
    {
        String result;
        String call = code.replace(CONST.READ.FUNCTION_DELIM,"").split(CONST.READ.ARUMENT_DELIM,1)[0];
        //System.out.println(call);
        result = this.story.callAction(call,getArguments(code));
        return result;
    }
    public String[] getArguments(String call)
    {
        String[] result;
        result = call.split("%[^"+ CONST.READ.ARUMENT_DELIM + "]*"+ CONST.READ.ARUMENT_DELIM + "|"+ CONST.READ.ARUMENT_DELIM);
        return result;
    }
    
}
