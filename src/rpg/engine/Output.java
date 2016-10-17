/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Output {
    static final String SAVE_PATH = "save.dat";
    HashMap<String,String[]> saveData = new HashMap<>();
    Integer lastSaveSlot = 0;

    static String[] read(String filepath) throws IOException
    {
        String[] result;
        String line;
        BufferedReader file = new BufferedReader(new FileReader(filepath));
        result = (String[]) file.lines().toArray();
        file.close();
        return result;
    }
    static String[] getSave(String filepath, Integer slot) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(filepath));
            Boolean flag = false;
            String line;
            List<String> result = Arrays.asList();
            while(file.ready())
            {
                line = file.readLine();
                if(line.startsWith("&")) {
                    if(flag)return (String[]) result.toArray();
                    if (line.equals("&slot=" + slot))
                        flag = true;
                    continue;
                }
                if(flag)
                {
                    result.add(line);
                }
            }
            return (String[]) result.toArray();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    static void save(String filepath, Player ply, Integer slot) throws IOException, FileNotFoundException
    {
        String[] saveData = read(filepath);
        saveData[0] = "%lastSave=" + slot;
        boolean flag = false;
        List<String> result = Arrays.asList();
        //Essentially delete the save slot data from the file if it exists
        for(String line:saveData)
        {
            if(line.startsWith("&"))
                flag=false;
            if(line.equals("&slot=" + slot))
                flag=true;
            if(!flag)
                result.add(line);
        }
        result.add("&slot="+slot);
        result.add("@status=" + ply.status);
        result.addAll(Arrays.asList(ply.story.getSaveString()));
        Files.write(Paths.get(SAVE_PATH),result, Charset.defaultCharset());
    }
}
