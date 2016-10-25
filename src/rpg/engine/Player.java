package rpg.engine;
import java.io.IOException;
import java.util.HashMap;

/**
 * <h1>Player class</h1>
 * <h5>The player class is the main class of this package, sub stories should extend this abstract and define functions
 * used in the story as required.</h5>
 */


public abstract class Player
{
    /**
     * Internal Variables
     */
    protected String status;
    private Input lookup;
    protected Story story;
    /**
     * Map definitions
     */
    private HashMap<Integer,String[]> responseTable = new HashMap<>();
    private HashMap<Integer,String[]> narrationTable = new HashMap<>();
    /**
     * Player class constructor
     */
    public Player() {
        this.lookup = new Input();
        this.story = new Story();
        this.status = "";
        this.init();
    }

    /**<h1>Init required to ensure child code is neat</h1>
     * <br>All items in the story table should be added here
     */
    public abstract void init();

    /**
     * Handles exiting/saving
     * Depreciated for final build as unable to work it into the engine properly.
     */
    void onExit()
    {
        this.talk("","Would you like to save? (y/n)");
        String ans = Console.requestString("yes|no|y|n");
        if (ans == null) return;
        if(ans.equals("y") || ans.equals("yes"))
        {
            this.talk("","What would you like to call this save?\n");
            ans = Console.requestString();
            this.talk("","Which slot would you like to save in?\n");
            for(int x=1;x<10; ++x)
                this.ask(x,Loop.save.getSaveList(x-1));
            Integer slot = Console.requestInteger(1,10);
            try {
                Loop.save.saveState(Output.SAVE_PATH, ans, this, slot-1);
            }
            catch (IOException e)
            {
                this.talk("Developer","For an unknown reason we were unable to save the game.");
            }
        }
    }

    /**
     * void setSaveState
     * Update player and story vars from a save array.
     * @param save the save array.
     */
    void setSaveState(String[] save)
    {
        this.story.clear();
        for(int x=0; x<save.length;++x)
        {
            String line = save[x];
            if(line.startsWith("@status="))
            {
                this.status = line.substring("@status".length());
                save[x] = null;
                this.story.readSave(save);
            }
        }
    }

    /**
     * static void setTitle(Scope:protected)
     * Change the story title
     * @param name The new title.
     */
    protected static void setTitle(String name){
        Story.Title=name;
    }

    /**
     * final void setScript(Scope:Protected).
     * sets the script file the game should run from.
     * @param filePath path to sc
     */
    final protected void setScript(String filePath){
        this.lookup.load(filePath);
    }

    /**
     * <b>Void:</b> putNextStatus()
     * This functions gets the next status from the input buffer.
     */
    void putNextStatus()
    {
        String[] myStatus = this.lookup.getScript(this.status);
        this.putStatus(myStatus);
    }

    /**
     *  <b>Void:</b> putStatus
     *  <p>This functions gets the .script information associated with the strStatus and reads it into the narration and response buffers.</p>
     *  @param strStatus String code of the script to get from the input buffer
     */
    private void putStatus(String[] strStatus)
    {
        // wipe the current mapping
        this.narrationTable.clear();
        this.responseTable.clear();
        //
        for(String line : strStatus)
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

    /**
     * <b>Void:</b> addQuery(String query)
     * <p>This function reads a query line into the response table</p>
     * @param query A "query" string (any line that starts with CONST.READ.QUERY_DELIM from the script file.
     */
    private void addQuery(String query)
    {
        String[] line = query.split("(?<!"+ CONST.READ.IGNORE_DELIM +")"+ CONST.READ.SPLIT_DELIM_1);
        if(line.length != 2)
            return;
        /*
        Make the line grammatically correct if it isnt already
        (Upper case first letter and strip trailing/begining spaces)
        */
        line[1] = line[1].trim().replaceAll("^[\\s\\S]", ("" +line[1].trim().charAt(0)).toUpperCase());
        this.narrationTable.put(this.narrationTable.size(), line);
    }

    /**
     * <b>Void:</b> addResponse
     * <p></p>
     * @param response the response string to add.
     */
     private void addResponse(String response)
    {
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
    void narrateNext()
    {
        this.narrate(this.narrationTable);
    }

    /**
     * void narrate(Scope:private)
     * read lines from the narration table.
     * @param narrationTable A narration table.
     */
    private void narrate(HashMap<Integer, String[]> narrationTable)
    {
        for(int ind = 0;ind<narrationTable.size();ind++)
        {
           // System.out.println(narrationTable.get(ind+1));
            String actor = narrationTable.get(ind)[0];
            String line = narrationTable.get(ind)[1];
            
            actor = insertVars(actor);
            line = insertVars(line);
            
            this.talk(actor, line);
            Console.request();
        }
    }

    /**
     * void respondNext
     * read the next options table.
     */
    void respondNext()
    {
        this.respond(this.responseTable);
    }

    /**
     * void respond(Scope:Private)
     * read the options from a response table.
     * @param responseTable The table of potential responses.
     */
    private void respond(HashMap<Integer, String[]> responseTable)
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
        //System.out.println();
    }

    /**
     * void choose()
     * select and act on a response based on the players input.
     */
    void choose()
    {
        if(this.responseTable.isEmpty())
        {

            System.out.println("Empty choice table, exiting to avoid infinite loop");
            System.exit(-404);
            //just in case
            return;
        }

        Integer result = Console.requestInteger();
        while(!validateChoice(result-1))
        {
            System.out.println("That isn't a choice");
            result = Console.requestInteger();
        }
        this.interpStringCode(this.responseTable.get(result-1)[0]);
        this.story.setVar("status",this.status);
    }

    /**
     * void talk(Scope:Private)
     * Say a line
     * @param actor the actor reading the line
     * @param line the line
     */
    private void talk(String actor, String line)
    {
        if(actor.isEmpty())
        {
            System.out.print(line);
            return;
        }
        System.out.printf("[%s]%-1s", actor,line);
    }

    /**
     * void ask(Scope:Protected)
     * format and print an option
     * @param index the option index
     * @param option the option text
     */
    protected void ask(Integer index, String option)
    {
        System.out.printf("%d:%-2s\n", index, option );
    }


    /**
     * primitive boolean validateChoice(Scope:Private)
     * @param index the index of the option to validate
     * @return whether or not that index is valid as an option
     * Note: originally this function was intended to do more than just return whether the option is in the table.
     * Kept due to laziness
     */
    private boolean validateChoice(int index)
    {
        /*if( this.responseTable.containsKey(index))
            return true;
        return false;*/
        return this.responseTable.containsKey(index);
    }

    /**
     * Function interprets and makes action calls where necessary and adds it to the status code
     * @param code the code string to interpret.
     */
    private void interpStringCode(String code)
    {
        
        if(this.isActionCall(code))
        {
            code = this.makeActionCall(code);
        }
        this.status += code;
    }
    private String insertVars(String line)
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
    private String getVar(String var)
    {
        if(this.story.hasString(var))
            return this.story.getString(var);
        return "<Null Variable Pointer("+ var +") >";
    }


    private boolean isActionCall(String code)
    {
        if(code.startsWith(CONST.READ.FUNCTION_DELIM)) {
            if (this.story.hasAction(code.replace(CONST.READ.FUNCTION_DELIM, "").split(CONST.READ.ARGUMENT_DELIM)[0]))
                return true;
        }
        return false;
    }


    private String makeActionCall(String code)
    {
        String result;
        String call = code.replace(CONST.READ.FUNCTION_DELIM,"").split(CONST.READ.ARGUMENT_DELIM)[0];
        result = this.story.callAction(call,getArguments(code));
        return result;
    }


    private String[] getArguments(String call)
    {
        String[] result;
        result = call.split("%[^"+ CONST.READ.ARGUMENT_DELIM + "]*"+ CONST.READ.ARGUMENT_DELIM +
                "|" + CONST.READ.ARGUMENT_DELIM +
                "|" + CONST.READ.SPLIT_DELIM_2 + "[\\s\\S]*");
        return result;
    }
    
}
