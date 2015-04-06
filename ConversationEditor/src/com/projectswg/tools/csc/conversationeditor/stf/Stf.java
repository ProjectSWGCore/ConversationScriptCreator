/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectswg.tools.csc.conversationeditor.stf;

import com.projectswg.tools.csc.conversationeditor.stf.StfTable.Pair;
import java.io.IOException;
import java.util.Vector;
import org.openide.util.Exceptions;

public class Stf {
    private String file;
    
    private String key = "None";
    private String value = "";
    
    private Vector<String> entries = new Vector<>();
    
    public Stf(String file) {
        this.file = file;
        
        if (!getFilename().equals("None")) {
            StfTable table = new StfTable();
            try {
                table.readFile(file);
                
                for (Pair<String, String> pair : table.disorderedTable) {
                    if (pair.getKey().equals("do_not_edit"))
                        continue;
                    
                    entries.add(pair.getKey() + "    |    " + pair.getValue());
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            
        }
    }
    
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public final String getFilename() {
        String r2;
        try {
            String[] base = file.split("conversation");
            r2 = base[1].replace("\\", "").replace(".stf", "");
        } catch (ArrayIndexOutOfBoundsException exc) {
            r2 = "None";
        }
        return r2;
    }
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Vector<String> getEntries() {
        return entries;
    }
    
    public void setEntries(Vector<String> entries) {
        this.entries = entries;
    }
    
    @Override
    public String toString() {
        return getFilename() + ":" + getKey();
    }
    
    
}
