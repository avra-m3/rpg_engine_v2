/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Output {
    static final String SAVE_PATH = "save.dat";

    private ArrayList[] saveData = new ArrayList[9];
    private String[] saveNames = new String[9];
    private Integer lastSaveSlot = -1;
    boolean isEmpty()
    {
        for(ArrayList a:saveData) {
            if (a == null) continue;
            if (!a.isEmpty())
                return false;
        }
        return true;
    }
    String[] getSaveList()
    {
        String[] result = saveNames;
        for(int x=0; x<result.length;++x)
            if(result[x] == null)
                result[x] = "empty";
        return result;
    }
    String getSaveList(Integer slot)
    {
        if(saveNames[slot] == null)
            return "empty";
        return saveNames[slot];
    }
    Integer getLastSaveSlot()
    {
        return this.lastSaveSlot;
    }
    String[] getLastSave()
    {
        return getSave(getLastSaveSlot());
    }
    void loadSaves()
    {
        try {
            if(!Files.exists(Paths.get(this.SAVE_PATH)))
                return;
            String[] arr = read(SAVE_PATH);
            Integer index = null;
            String name = null;
            lastSaveSlot = Integer.valueOf(arr[0]);
            for(String line:arr) {
                if (line.startsWith("&slot=")) {
                    index = Integer.valueOf(line.substring(6));
                    name = null;
                    continue;
                }
                if(index == null) continue;
                if (name.equals(null)) {
                    name = line;
                    saveNames[index] = name;
                    continue;
                }
                saveData[index].add(line);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    static String[] read(String filepath) throws IOException
    {
        String[] result;
        String line;
        if(!Files.exists(Paths.get(filepath))) return new String[1];
        BufferedReader file = new BufferedReader(new FileReader(filepath));
        result = (String[]) file.lines().toArray();
        file.close();
        return result;
    }
    String[] getSave(Integer slot) {
        return (String[]) this.saveData[slot].toArray();
    }

    /**
     * <b>Void:saveState</b>
     * <p>This function writes the state of the game to the save file.</p>
     * @param filepath The filepath of the save
     * @param saveName The name the player chooses to save their game by
     * @param ply Should be the current Instance of the player object
     * @param slot The save slot.
     * @throws IOException
     */
    void saveState(String filepath,String saveName, Player ply, Integer slot) throws IOException
    {
        ArrayList<String> save = new ArrayList<>();
        save.add("@status=" + ply.status);
        save.addAll(Arrays.asList(ply.story.getSaveString()));

        this.saveNames[slot] = saveName;
        this.saveData[slot] = save;

        ArrayList<String> strFile = new ArrayList<>();
        strFile.add(slot.toString());
        for(int x=0; x<9; ++x)
        {
            if(saveData[x].isEmpty()) continue;
            strFile.add("&slot=" + x);
            strFile.add(this.saveNames[x]);
            strFile.addAll(this.saveData[x]);
        }

        strFile.addAll(Arrays.asList(ply.story.getSaveString()));
        Files.write(Paths.get(SAVE_PATH),strFile, Charset.defaultCharset());
    }
}
