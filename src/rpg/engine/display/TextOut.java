package rpg.engine.display;

import javax.swing.*;

/**
 * Created by A on 14/10/2016.
 */
public class TextOut extends JTextArea {
    public TextOut(){
        super(100,100);
    }
    @Override
    public void append(String s)
    {
        super.append(s);
        this.setCaretPosition(this.getDocument().getLength());
    }
}
