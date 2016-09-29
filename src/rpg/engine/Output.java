/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.engine;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Output {
    FileWriter out;
    Output()
    {
        
        
    }
    public Object save(String filepath, String status)
    {
        try 
        {
            out = new FileWriter(filepath, false);
            out.write(status);
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Output.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    
        return null;
    }
}
