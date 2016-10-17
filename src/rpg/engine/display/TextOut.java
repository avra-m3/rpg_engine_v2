package rpg.engine.display;
/**
 * <h1>rpg_engine_v2.${PACKAGE_NAME}</h1>
 * <h5>Created by A on 14/10/2016.</h5>
 * <br>
 * <b>File Name</b>
 * <p>Purpose of this file </p>
 *
 */
import javax.swing.*;

class TextOut extends JTextArea {
    @Override
    public void append(String s)
    {
        super.append(s);
        this.setCaretPosition(this.getDocument().getLength());
    }
}
