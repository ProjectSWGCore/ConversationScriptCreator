package com.projectswg.tools.csc.conversationeditor;

import com.projectswg.tools.csc.conversationeditor.scene.SceneView;
import com.projectswg.tools.csc.conversationeditor.nodes.ConversationNode;
import com.projectswg.tools.csc.conversationeditor.nodes.OptionNode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.openide.windows.TopComponent;

public class Compiler {

    private final File file;
    private boolean comments = true;
    private boolean tabs = true;
    private String header = "";
    
    public Compiler(File file, boolean comments, String header, boolean tabs) {
        this.file = file;
        this.comments = comments;
        this.header = header;
        this.tabs = tabs;
    }
    
    public boolean compile() throws IOException {

        TopComponent component = TopComponent.getRegistry().getActivated();
        if (component == null || !(component instanceof EditorTopComponent))
            return false;
        
        EditorTopComponent editor = (EditorTopComponent) component;
        SceneView scene = editor.getScene();
        
        if (scene == null) {
            JOptionPane.showMessageDialog(null, "The scene was null! You must restart the editor in order to compile :(", 
                    "Conversation Script Creator", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {
            createBaseFile(bw);
            
            LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> conversationLinks = scene.getConversationLinks();
            
            for (Map.Entry<ConversationNode, ArrayList<ConversationNode>> entry : conversationLinks.entrySet()) {
                if (entry.getKey().getType().equals(ConversationNode.BEGIN)) {
                    createOptionsAndHandler(bw, entry.getKey(), entry.getValue(), 1, conversationLinks);
                    break;
                }
            }
        }
        return true;
    }

    private void createBaseFile(BufferedWriter bw) throws IOException {
        if (comments && !header.isEmpty()) bw.write("#" + header + "\n");
        
        bw.write("from resources.common import ConversationOption\n" +
                 "from resources.common import OutOfBand\n" +
                 "from resources.common import ProsePackage\n" +
                 "from java.util import Vector\n");
        bw.newLine();
        bw.write("import sys\n");
        bw.newLine();
        bw.write("def startConversation(core, actor, npc):\n");
    }
    
    private void createResponseHandler(BufferedWriter bw, ArrayList<ConversationNode> options, int handleNum,
            LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> conversationLinks) throws IOException {
        
        LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> handleFuncs = new LinkedHashMap<>();

        bw.write("def handleOptionScreen" + handleNum + "(core, actor, npc, selection):\n");
        bw.newLine();
        String indent4 = getIndent(4);
        String indent8 = getIndent(8);
        
        int count = handleNum + 1;
        for (ConversationNode selectedOption : options) {
            if (!(selectedOption instanceof OptionNode))
                continue;
                
            OptionNode option = (OptionNode) selectedOption;
            
            bw.write(indent4 + (options.indexOf(option) > 0 ? "elif" : "if") + " selection == " + options.indexOf(option) + ":\n");
            if (conversationLinks.containsKey(option)) {
                
                if (comments) bw.write(indent8 + "# " + (option.getDisplayText().equals("") ? option.getStf() : option.getDisplayText()) + "\n");
                
                for (ConversationNode handleNode : conversationLinks.get(selectedOption)) {
                    switch(handleNode.getType()) {
                        
                        case ConversationNode.RESPONSE:
                            if (conversationLinks.get(handleNode) == null) {
                                bw.write(indent8 + "# NULL Error printing out handle for " + handleNode.getStf());
                            } else {
                                createOptions(bw, conversationLinks.get(handleNode), count++, indent8);
                                bw.write(indent8 + "core.conversationService.sendConversationMessage(actor, npc, OutOfBand.ProsePackage('@conversation/"
                                        + handleNode.getStf() + "')\n");
                                handleFuncs.put(handleNode, conversationLinks.get(handleNode)); // Put these nodes in list so we can create the handlers later.
                            }
                            break;
                            
                        case ConversationNode.END:
                            
                            String[] split = handleNode.getStf().toString().split(":");
                            bw.write(indent8 + "core.conversationService.sendStopConversation(actor, npc, 'conversation/" + split[0] + "', '" + split[1] + "')\n");
                            break;
                            
                    }
                }
                
            }
            bw.write(indent8 + "return\n");
            bw.newLine();
        }
        bw.write(indent4 + "return\n");
        
        bw.newLine();
        
        int currentHandler = handleNum + 1;
        for (Map.Entry<ConversationNode, ArrayList<ConversationNode>> handleNode : handleFuncs.entrySet()) {
            bw.write("# Handle Response for " + handleNode.getKey().getStf() + "\n");
            createResponseHandler(bw, handleNode.getValue(), currentHandler++, conversationLinks);
        }
    }
    
    private void createOptionsAndHandler(BufferedWriter bw, ConversationNode response, ArrayList<ConversationNode> options, int handleScreenNum, 
            LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> conversationLinks) throws IOException {
        
        String indent4 = getIndent(4);
        
        bw.write(indent4 + "options = new Vector()\n");
        
        if (options.size() > 1) {
            for (ConversationNode option : options) {
                bw.write(indent4 + "options.add(ConversationOption(OutOfBand.ProsePackage('@conversation/"  + option.getStf() +"'), " 
                        + String.valueOf(((OptionNode)option).getNumber()) + ")\n");
            }
        } else { 
            for (ConversationNode option : options) {
                bw.write(indent4 + "options.add(ConversationOption(OutOfBand.ProsePackage('@conversation/"  + option.getStf() +"'), " + options.indexOf(option) + ")\n");
            }
        }
        
        bw.write(indent4 + "core.conversationService.sendConversationOptions(actor, npc, handleOptionScreen" + String.valueOf(handleScreenNum) + ")\n");
        bw.write(indent4 + "core.conversationService.sendConversationMessage(actor, npc, OutOfBand.ProsePackage('@conversation/" + response.getStf() + "'))\n");
        bw.write(indent4 + "return\n");
        bw.newLine();
        bw.write("# Handle Response for " + response.getStf() + "\n");
        createResponseHandler(bw, options, handleScreenNum, conversationLinks);
    }
    
    private void createOptions(BufferedWriter bw, ArrayList<ConversationNode> options, int handleScreenNum, String space) throws IOException {
        bw.write(space + "options = new Vector()\n");
        
        if (options.size() > 1) {
            ArrayList<ConversationNode> orderedOptions = new ArrayList<>();
            for (ConversationNode unOrdered : options) {
                orderedOptions.add(((OptionNode)unOrdered).getNumber(), unOrdered);
            }
            for (ConversationNode option : orderedOptions) {
                bw.write(space + "options.add(ConversationOption(OutOfBand.ProsePackage('@conversation/" + option.getStf() +"'), " + options.indexOf(option) + ")\n");
            }         
        } else {
            for (ConversationNode option : options) {
                bw.write(space + "options.add(ConversationOption(OutOfBand.ProsePackage('@conversation/" + option.getStf() +"'), " + options.indexOf(option) + ")\n");
            }
        }
        bw.write(space + "core.conversationService.sendConversationOptions(actor, npc, handleOptionScreen" + String.valueOf(handleScreenNum) + ")\n");
    }
    
    private String getIndent(int length) {
        String rtn = "";
        
        if (!tabs) {
            int i = 0;
            while (i < length) {
                rtn = rtn + " ";
            }
        } else {
            switch(length) {
                case 4:
                    rtn = "\t";
                    break;
                case 8:
                    rtn = "\t\t";
                    break;
            }
        }
        return rtn;
    }
}
