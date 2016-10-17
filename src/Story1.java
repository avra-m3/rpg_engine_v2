import rpg.engine.Input;
import rpg.engine.Loop;
import rpg.engine.Player;

import java.util.Arrays;
import java.util.Random;


/**This is the only class that requires modification to change the story*/
public class Story1 extends Player{
    /**Variable definitions here:*/
    private Random rngDice = new Random();
    /**Program entry point*/
    public static void main(String[] args)
    {
        setTitle("Demonstrative Story");
        Loop.run(new Story1());
    }
    /**the successor to filltables() init just is a nicer name and fits better imo*/
    public void init()
    {
        this.setScript("Story1.script");
        this.story.addVar("name", "child");
        this.story.addVar("gender", STORY.GENDER_TERMS_1[2]);
        this.story.addVar("gender2", STORY.GENDER_TERMS_2[2]);
        this.story.addVar("gender3", STORY.GENDER_TERMS_3[2]);
        this.story.addVar("gender_code",2);
        this.story.addVar("status",this.status);

        this.story.addVar("inventory",new Object[10]);
        // Add functions to the action table
        // No idea how this works DO NOT TOUCH
        // Intelij made changes to what netbeans suggested, still dont know how it works DO NOT TOUCH!!!
        this.story.addAction("end", this::endGame);
        this.story.addAction("setName", this::setName);
        this.story.addAction("randomName", this::randomName);
        this.story.addAction("setGender", this::setGender);
        this.story.addAction("randomGender", this::randomGender);
        this.story.addAction("print", this::printVar);
        this.story.addAction("inventory", this::printInventory);
    }
    /**Story Function: printVar, [DEBUG] used in testing to make sure arguments feature is working*/
    private String printVar(String[] args)
    {
        System.out.print("PRINTER: ");
        System.out.println(Arrays.toString(args));
        return "";
    }
    /**Story Function: setName, sets the players name*/
    private void setName(String name)
    {
        this.story.setVar("name", name);
    }
    /**Story Function: setGender, sets the players gender*/
    private void setGender(int genderIndex)
    {
        this.story.setVar("gender", STORY.GENDER_TERMS_1[genderIndex]);
        this.story.setVar("gender2", STORY.GENDER_TERMS_2[genderIndex]);
        this.story.setVar("gender3", STORY.GENDER_TERMS_3[genderIndex]);
        this.story.setVar("gender_code", genderIndex);
    }

    /**Story Function: setName, sets the players name to player entered input.*/
    private String setName(String[] args)
    {
        System.out.println("What is your name? ");
        this.setName(Input.requestString());
        return "A";
    }
    /**Story Function: randomName, sets the players name randomly.*/
    private String randomName(String[] args)
    {
        this.setName(getRandomName());
        return "B";
    }
    /**Story Function: setGender, sets the players gender based on player entered input.*/
    private String setGender(String[] args)
    {
        System.out.println("What is your gender?");
        this.ask(1, "Male");
        this.ask(2, "Female");
        this.ask(3, "I'm not defined by gender");
        int result = Input.requestChoice(1, 3);
        this.setGender(result -1);
        return "B";
    }
    /**Story Function: randomGender, sets the players gender randomly.*/
    private String randomGender(String[] args)
    {
        setGender(rngDice.nextInt(2));
        return "B";
    }
    /**Story Function: endGame, ends the game.*/
    private String endGame(String[] args)
    {
        Loop.FLAG_GAME_STATUS = 0;
        this.clear();
        this.init();
        System.out.println("The End");
        System.out.println("press enter to continue...");
        Input.request();
        return "";
    }
    /**Utility Function: clear, resets the story and the variables.*/
    private void clear()
    {
        this.status = "";
        this.story.clearVars();
    }
    /**Utility Function: getRandomName, generates a random name*/
    private String getRandomName()
    {
        String firstname,lastname;

        if((Integer) this.story.getVar("gender_code")== 2)
            firstname = STORY.NAMES_FEMALE[rngDice.nextInt(STORY.NAMES_FEMALE.length)];
        else
            firstname = STORY.NAMES_MALE[rngDice.nextInt(STORY.NAMES_MALE.length)];
        lastname = STORY.NAMES_LASTNAMES[rngDice.nextInt(STORY.NAMES_LASTNAMES.length)];
        return firstname + " " + lastname;
    }

    /**
     * prints the player inventory variables
     * @param args technically can accept args but will not actually use them.
     * @return an empty string, this function is not designed to further the story in any way.
     */
    private String printInventory(String[] args)
    {
        System.out.println("Inventory:");
        for(int i=0;i<3;i++) {
            System.out.print("|");
            for (int x = 0; x < 12; x++)
                System.out.print("-");
        }
        System.out.println("|");
        System.out.printf("|%-12s|%-12s|%-12s|\n",
                this.story.getString("inv_slot_1"),
                this.story.getString("inv_slot_2"),
                this.story.getString("inv_slot_3"));
        for(int i=0;i<3;i++) {
            System.out.print("|");
            for (int x = 0; x < 12; x++)
                System.out.print("-");
        }
        System.out.println("|");
        System.out.printf("|%-12s|%-12s|%-12s|\n",
                this.story.getString("inv_slot_4"),
                this.story.getString("inv_slot_5"),
                this.story.getString("inv_slot_6"));
        for(int i=0;i<3;i++) {
            System.out.print("|");
            for (int x = 0; x < 12; x++)
                System.out.print("-");
        }
        System.out.println("|");
        return "";
    }
}
/**Interface: STORY, contains constants that are implemented in the story.*/
interface STORY
{
    String[] GENDER_TERMS_1 =
            {
                    "boy",
                    "girl",
                    "lizard person"
            };
    String[] GENDER_TERMS_2 =
            {
                    "lad",
                    "lass",
                    "you"
            };
    String[] GENDER_TERMS_3 =
            {
                    "young man",
                    "young lady",
                    "young one"
            };
    String[] NAMES_MALE =
            {
                    "John",
                    "Davy",
                    "Jean-Luc",
                    "Chelton",
                    "John",
                    "Nick",
                    "Avrami",
                    "Tom",
            };
    String[] NAMES_FEMALE =
            {
                    "Jane",
                    "Emilia",
                    "Katherine",
                    "Joan",
                    "Yooor",
                    "Kate",
                    "Sarah",
            };
    String[] NAMES_LASTNAMES =
            {
                    "Mulgrew",
                    "Evans",
                    "Einstein",
                    "Doe",
                    "Smith",
                    "Hammer",
                    "Cage",
                    "Cena",
                    "Mother",
                    "Janeway",
                    "Picard",
                    "Lawlor",
            };
}
