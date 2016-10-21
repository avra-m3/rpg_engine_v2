package rpg.engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * <h1>rpg_engine_v2.rpg.engine</h1>
 * <h5>Created by A on 21/10/2016.</h5>
 * <br>
 * <b>Console.java</b>
 * <p>Functions relating to console i/o</p>
 */
public class Console{
    private static BufferedReader console   = new BufferedReader(new InputStreamReader(System.in));
    private static PrintStream nullOut = new PrintStream(new EmptyStream());
    public static String request()
    {
        if(Loop.FLAG_GAME_STATUS < 0)
            return null;
        String result;
        try
        {
            result = console.readLine();
            if(CONST.DEBUG.ENABLE_SAVE_ENGINE && result.toLowerCase().equals("exit"))
            {
                if(Loop.FLAG_GAME_STATUS == 2)
                {
                    Loop.ply.onExit();
                }
                Loop.FLAG_GAME_STATUS *= -1;
                System.setOut(nullOut);
                result = null;
            }
            return result;
        }
        catch(Exception ex)
        {
            System.exit(-2);
            return null;
        }

    }


    public static String requestString(String regex)
    {
        String result = request();
        while(1==1)
        {
            if(result == null)
                result = "";
            if(result.matches(regex) && !result.equals(""))
                break;
            System.out.println("Sorry could not understand answer, please try again.");
            result = request();
        }
        return result;
    }

    public static String requestString()
    {
        return requestString("[\\s\\S][\\s\\S]*");
    }


    public static Integer requestInteger()
    {
        return Integer.valueOf(requestString("^\\d\\d*$"));
    }
    public static Integer requestInteger(int min, int max)
    {
        return Integer.valueOf(requestString("^["+min+"-"+max+"]["+min+"-"+max+"]*$"));
    }

}
class GameExitException extends Exception{}
class EmptyStream extends OutputStream
{
    public void write(int i)
    {
        return;
    }
}