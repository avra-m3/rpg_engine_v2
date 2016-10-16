
package rpg.engine;

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
    private String[] menuIndex = new String[10];

    /** Constructor*/
    Menu()
    {
        menuOptions.put("New Game", this::optionStartGame);
        menuOptions.put("Credits", this::optionShowCredits);
        menuOptions.put("Quit", this::optionExitGame);
        menuIndex[0] = "New Game";
        menuIndex[1] = "Credits";
        menuIndex[2] = "Quit";
    }
    /** show main menu */
    void show()
    {
        System.out.println("Welcome to " + Story.Title);
        for(int i = 0; i< menuIndex.length; i++)
        {
            if(this.menuIndex[i] == null)
                break;
            this.ask(i + 1, menuIndex[i]);
        }
    }
    /**get the selected menu option*/
    void get()
    {
        int result = Input.requestChoice(1, menuOptions.size()+1);
        this.menuOptions.get(menuIndex[result-1]).run();
    }
    /**format an option*/
    private void ask(int index, String option)
    {
        System.out.printf("%d:%-1s\n",index,option);
    }
    /**function to start a game*/
    private void optionStartGame()
    {
        Loop.FLAG_GAME_STATUS = 1;
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
            Input.request();
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
