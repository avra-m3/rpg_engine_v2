package rpg.engine.display;

import java.io.IOException;
import java.io.OutputStream;
/**
 * This class extends from OutputStream to redirect output to a TextOut object
 */
class CustomOut extends OutputStream {
    private TextOut tOut;

    public CustomOut(TextOut textOut) {
        this.tOut = textOut;
    }
    @Override
    public void write(int i) throws IOException {

        tOut.append(String.valueOf((char)i));
    }
}