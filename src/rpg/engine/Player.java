package rpg.engine;
import java.util.HashMap;

/***
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

    protected static void setTitle(String name){
        Story.Title=name;
    }
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
            Input.request();
        }
    }
    void respondNext()
    {
        this.respond(this.responseTable);
    }
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
        System.out.println();
    }
    void choose()
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
        this.story.setVar("status",this.status);
    }
    private void talk(String actor, String line)
    {
        if(actor.isEmpty())
        {
            System.out.print(line);
            return;
        }
        System.out.printf("[%s]%-1s", actor,line);
    }
    protected void ask(Integer index, String option)
    {
        System.out.printf("%d:%-2s\n", index, option );
    }
    
    private boolean validateChoice(int index)
    {
        /*if( this.responseTable.containsKey(index))
            return true;
        return false;*/
        return this.responseTable.containsKey(index);
    }
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
