package rpg.engine.display;

/**
 * Created by A on 14/10/2016.
 */

import javax.swing.*;
import java.io.PrintStream;

public class Screen extends JFrame{
    public TextOut txt;
    public CustomOut cout;
    public Screen() {
        super();
    }
    public void init(String title, Integer w, Integer h) {
        JFrame myFrame = new JFrame(title);
        myFrame.setSize(w,h);
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
    }
    public void create()
    {
        this.txt = new TextOut();
        this.cout = new CustomOut(this.txt);
        PrintStream sysOut = new PrintStream(new CustomOut(this.txt));
        System.setOut(sysOut);
    }
}