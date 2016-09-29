package rpg.engine;
import java.util.HashMap;
import java.util.Random;


public final class Player implements StoryGuide
{
    //stuff that we need for the Player
    public String status;

    private Random rngDice;
    private Input lookup;
    // List of random names to draw from

    HashMap<Integer,String[]> responseTable = new HashMap<>();
    HashMap<Integer,String[]> narrationTable = new HashMap<>();
    // Player class constructor
    Player(Input lookup) {
        this.lookup = lookup;
        this.status = "";
        this.rngDice = new Random();
        
        this.fillTables();
        
    }
    public void fillTables()
    {
        // Add key vars to the story table
        Story.addVar("name", "child");
        Story.addVar("gender", GENDER_TERMS_1[2]);
        Story.addVar("gender2", GENDER_TERMS_2[2]);
        Story.addVar("gender3", GENDER_TERMS_3[2]);
        Story.addVar("gender_code",2);
        // Add functions to the action table
        // No idea how this works DO NOT TOUCH
        Story.addAction("end", (args) -> endGame(args));
        Story.addAction("setName", (args) -> setName(args));
        Story.addAction("randomName", (args) -> randomName(args));
        Story.addAction("setGender", (args) -> setGender(args));
        Story.addAction("randomGender", (args) -> randomGender(args));
    }
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
            if(line.startsWith(Format.QUERY_DELIM))
            {
                this.addQuery(line.replace(Format.QUERY_DELIM,""));
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
        String[] line = query.split("(?<!"+ Format.IGNORE_DELIM +")"+ Format.SPLIT_DELIM_1);
        // make sure it is the correct length
        if(line.length != 2)
            return;
        /*
        Make the line grammatically correct if it isnt already
        (Upper case first letter and strip trailing/begining spaces)
        */
        line[1] = line[1].trim().replaceAll("^[\\s\\S]", ("" +line[1].charAt(0)).toUpperCase());
        this.narrationTable.put(this.narrationTable.size(), line);
    }
    public void addResponse(String response)
    {
        // we want to ignore decision delims preceeded by escape chars
        String[] line = response.split("(?<!"+ Format.IGNORE_DELIM +")"+ Format.SPLIT_DELIM_2);
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
        String[] varList = line.split("(?<!"+ Format.IGNORE_DELIM + ")" + Format.VAR_DELIM_1 +"|(?<!" + Format.IGNORE_DELIM + ")"+ Format.VAR_DELIM_2);
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
        if(Story.hasString(var))
            return Story.getString(var);
        return "<Null Variable Pointer("+ var +") >";
    }
    public boolean isActionCall(String code)
    {  // System.out.println(Arrays.toString(this.actionTable.keySet().toArray()));
       // System.out.printf("Call: %s\nPassed check 1: %s\nPassed check 2: %s\nCheck 2 key: %s\n", code,code.startsWith("" + Format.FUNCTION_DELIM), this.actionTable.containsKey(code.replace(""+Format.FUNCTION_DELIM,"").trim()),code.replace(""+Format.FUNCTION_DELIM,"") );
        if(code.startsWith("" + Format.FUNCTION_DELIM))
            if(Story.hasAction(code.replace(Format.FUNCTION_DELIM,"")))       
                return true;
        return false;
    }
    public String makeActionCall(String code)
    {
        String result;
        String call = code.replace(Format.FUNCTION_DELIM,"").split(Format.ARUMENT_DELIM,1)[0];
        //System.out.println(call);
        result = Story.callAction(call,getArguments(code));
        return result;
    }
    public String[] getArguments(String call)
    {
        String[] result;
        result = call.split("%[^"+ Format.ARUMENT_DELIM + "]*"+ Format.ARUMENT_DELIM + "|"+ Format.ARUMENT_DELIM);
        return result;
    }
    // All functions below this comment are custom functions for the story to be called by the script
    
    // hard set functions here
    public void setName(String name)
    {
        Story.setVar("name", name);
    }
    public void setGender(int genderIndex)
    {
        Story.setVar("gender", GENDER_TERMS_1[genderIndex]);
        Story.setVar("gender2", GENDER_TERMS_2[genderIndex]);
        Story.setVar("gender3", GENDER_TERMS_3[genderIndex]);
        Story.setVar("gender_code", genderIndex);
    }
    
    // action functions
    public String setName(String[] args)
    {
        System.out.println("What is your name? ");
        this.setName(Input.requestString());
        return "A";
    }
    public String randomName(String[] args)
    {
        this.setName(getRandomName());
        return "B";
    }
    
    public String setGender(String[] args)
    {
        System.out.println("What is your gender?");
        this.ask(1, "Male");
        this.ask(2, "Female");
        this.ask(3, "I'm not defined by gender");
        int result = Input.requestChoice(1, 3);
        this.setGender(result -1);
        return "B";
    }
    public String randomGender(String[] args)
    {
        setGender(rngDice.nextInt(2));
        return "B";
    }
    public String endGame(String[] args)
    {
        Rpg1.FLAG_GAME_STATUS = 0;
        this.clear();
        this.fillTables();
        System.out.println("The End");
        System.out.println("press enter to continue...");
        Input.request();
        return "";
    }
    // utility functions
    public void clear()
    {
        this.status = "";
        Story.clearVars();
    }
    public String getRandomName()
    {
        String firstname,lastname = "";

        if((Integer) Story.getVar("gender_code")== 2)  
            firstname = NAMES_FEMALE[rngDice.nextInt(NAMES_FEMALE.length)];
        else
            firstname = NAMES_MALE[rngDice.nextInt(NAMES_MALE.length)];
        lastname = NAMES_LASTNAMES[rngDice.nextInt(NAMES_LASTNAMES.length)];
        return firstname + " " + lastname;
    }
    
}
