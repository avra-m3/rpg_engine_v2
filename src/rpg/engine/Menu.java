
package rpg.engine;

import java.util.ArrayList;
import java.util.HashMap;

interface option
{
    void run();
}

class Menu
{
    // Map String-Function
    private HashMap<String,option> menuOptions = new HashMap<>();
    // Menu order (max 10 options arbitrary)
    private ArrayList<String> menuIndex = new ArrayList<>(9);

    /** Constructor*/
    Menu()
    {
        if(CONST.DEBUG.ENABLE_SAVE_ENGINE &&Loop.save.getLastSaveSlot() > -1) {
            menuOptions.put("Continue", this::optionContinueGame);
            menuIndex.add(0,"Continue");
        }
        menuOptions.put("New Game", this::optionStartGame);
        menuIndex.add("New Game");
        if(CONST.DEBUG.ENABLE_SAVE_ENGINE && !Loop.save.isEmpty()) {
            menuOptions.put("Load", this::optionLoadGame);
            menuIndex.add("Load Game");
        }
        menuOptions.put("Credits", this::optionShowCredits);
        menuOptions.put("Quit", this::optionExitGame);

        menuIndex.add("Credits");
        menuIndex.add("Quit");
    }



    /** show main menu */
    void show()
    {
        System.out.println("Welcome to " + Story.Title);
        for(int i = 0; i< menuIndex.size(); i++)
        {
            this.ask(i + 1, menuIndex.get(i));
        }
    }
    /**get the selected menu option*/
    void get()
    {
        int result = Console.requestInteger(1, menuOptions.size()+1);
        this.menuOptions.get(menuIndex.get(result-1)).run();
    }
    /**format an option*/
    private void ask(int index, String option)
    {
        System.out.printf("%d:%-1s\n",index,option);
    }
    /**
     * set the game state to the last saved game.
     */
    void  optionContinueGame()
    {

    }

    /**
     * set the game state to the choice of the player
     */
    void optionLoadGame()
    {

    }
    /**function to start a game*/
    private void optionStartGame()
    {
        Loop.FLAG_GAME_STATUS = 2;
    }
    /**function to exit the game*/
    private void optionExitGame()
    {
        Loop.FLAG_GAME_STATUS = -1;
    }
    /**function to show credits*/
    private void optionShowCredits()
    {
        // No idea why we have a try-catch syatement here but ill keep it 
        // just in case.
        try
        {
            for(String str: Menu.STR_CREDITS)
            {
                // we don't want to vomit the entirety of the credits in one go.
                Thread.sleep(500);
                System.out.println(str);
                
            }
            Console.request();
        }
        catch(InterruptedException ex)
        {
            //Nothing to do as this shouldn't screw everything over hard.
        }
    }

     final private static String[] STR_CREDITS = {
        "",
        "--------------------------------------------------------------------",
        "This game was created by Avrami Hammer and Tom Lawlor as part of the",
        "RMIT Associate degree in information technology 2016",
        "",
        "--- Credits: -------------------------------------------------------",
        "Lead Programmer: Avrami Hammer",
        "Story/Game Designer: Tom Lawlor",
        "Engine Design: Avrami Hammer",
        "Art Director: Tom Lawlor",
        "Director of Lizard Affairs: Tom Lawlor",
        "Head of fake title creation: Avrami Hammer",
        "--------------------------------------------------------------------",
        "No lizards were harmed in the production of this game",
        "Created in NetBeans IDE for Java"
        
    };
}
