package rpg.engine;

import java.io.PrintStream;

public class Loop
{
    public static int FLAG_GAME_STATUS;
    static Output save;
    static Menu men;
    static Player ply;
    static PrintStream SystemOut = System.out;
    /**Main loop*/
    public static void run(Player ply)
    {
        FLAG_GAME_STATUS = 1;
        Loop.ply = ply;
        Loop.save= new Output();
        save.loadSaves();
        Loop.men = new Menu();

        /*Screen display = new Screen();
        display.init(Story.Title,100,100);
        display.create();*/

        while(FLAG_GAME_STATUS >= 0)
        {

            System.setOut(SystemOut);
            if(FLAG_GAME_STATUS == 1)
            {
                men.show();
                men.get();
            }
            if(FLAG_GAME_STATUS == 2)
            {
                ply.putNextStatus();
                ply.narrateNext();
                ply.respondNext();
                if(FLAG_GAME_STATUS == 2)
                    ply.choose();
                if(FLAG_GAME_STATUS == -2)
                    FLAG_GAME_STATUS = 1;

            }
                
        }
        
    }
    
}





