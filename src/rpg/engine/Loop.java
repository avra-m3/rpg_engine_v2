package rpg.engine;

public class Loop
{
    public static int FLAG_GAME_STATUS;
    /**Main loop*/
    public static void run(Player ply)
    {   
        FLAG_GAME_STATUS = 0;
        Menu men = new Menu();
        /*Screen display = new Screen();
        display.init(Story.Title,100,100);
        display.create();*/
        while(FLAG_GAME_STATUS != -1)
        {
            if(FLAG_GAME_STATUS == 0)
            {
                men.show();
                men.get();
            }
            if(FLAG_GAME_STATUS == 1)
            {
                ply.putNextStatus();
                ply.narrateNext();
                ply.respondNext();
                if(FLAG_GAME_STATUS == 1)
                    ply.choose();
            }
                
        }
        
    }
    
}





