
package rpg.engine;

import java.util.HashMap;

interface option
{
    void run();
}

public class Menu 
{
    // Title of the game to display
    public String STR_TITLE = "RPG Engine";
    // Map String-Function
    HashMap<String,option> menuOptions = new HashMap<>();
    // Menu order (max 10 options arbitrary)
    String[] menuOptionsStr = new String[10];

    /** Constructor*/
    Menu()
    {
        menuOptions.put("New Game", () -> this.optionStartGame());
        menuOptions.put("Credits", () -> this.optionShowCredits());
        menuOptions.put("Quit", () -> this.optionExitGame());
        menuOptionsStr[0] = "New Game";
        menuOptionsStr[1] = "Credits";
        menuOptionsStr[2] = "Quit";
    }
    /** show main menu */
    void show()
    {
        System.out.println("Welcome to " + this.STR_TITLE);
        for(int i=0; i< menuOptionsStr.length;i++)
        {
            if(this.menuOptionsStr[i] == null)
                break;
            this.ask(i + 1, menuOptionsStr[i]);
        }
    }
    /**get the selected menu option*/
    void get()
    {
        int result = Input.requestChoice(1, menuOptions.size()+1);
        this.menuOptions.get(menuOptionsStr[result-1]).run();
    }
    /**format an option*/
    void ask(int index, String option)
    {
        System.out.printf("%d:%-1s\n",index,option);
    }
    /**function to start a game*/
    void optionStartGame()
    {
        Loop.FLAG_GAME_STATUS = 1;
    }
    /**function to exit the game*/
    void optionExitGame()
    {
        Loop.FLAG_GAME_STATUS = -1;
    }
    /**function to show credits*/
    void optionShowCredits()
    {
        // No idea why we have a try-catch syatement here but ill keep it 
        // just in case.
        try
        {
            for(String str: Menu.STR_CREDITS)
            {
                // we dont want to vomit the entirety of the credits in one go.
                Thread.sleep(500);
                System.out.println(str);
                
            }
            Input.request();
        }
        catch(Exception ex)
        {
            // TODO: figure out what error is supposedly being thrown
        }
    }

    static final String[] STR_CREDITS = {
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
