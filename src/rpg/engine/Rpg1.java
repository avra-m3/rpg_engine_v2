package rpg.engine;

/**
 * Main Class
 */
public class Rpg1
{
    static int FLAG_GAME_STATUS;
    public static void main(String[] args) 
    {   
        FLAG_GAME_STATUS = 0;
        Input lookup = new Input();
        Menu men = new Menu();
        Player ply = new Player(lookup);
        lookup.load("Story1.script");
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





