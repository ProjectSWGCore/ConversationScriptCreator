package com.projectswg.tools.csc.compiler;

import com.projectswg.tools.csc.conversationeditor.ConversationNode;
import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import com.projectswg.tools.csc.conversationeditor.SceneView;
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
    
    public Compiler(File file) {
        this.file = file;
    }
    
    public void compile() throws IOException {

        TopComponent component = TopComponent.getRegistry().getActivated();
        if (component == null || !(component instanceof EditorTopComponent))
            return;
        
        EditorTopComponent editor = (EditorTopComponent) component;
        SceneView scene = editor.getScene();
        
        if (scene == null) {
            JOptionPane.showMessageDialog(null, "The scene was null! You must restart the editor in order to compile :(", 
                    "Conversation Script Creator", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {
            createBaseFile(bw);
            
            LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> conversationLinks = scene.getConversationLinks();
            
            for (Map.Entry<ConversationNode, ArrayList<ConversationNode>> entry : conversationLinks.entrySet()) {
                /*System.out.println("Handling for node " + entry.getKey().getStf());
                for (ConversationNode node : entry.getValue()) {
                    System.out.println("Node: " + node.getStf());
                }*/
                if (entry.getKey().isStartNode()) {
                    createOptionsAndHandler(bw, entry.getKey(), entry.getValue(), 1, conversationLinks);
                    break;
                }
            }
        }
    }

    private void createBaseFile(BufferedWriter bw) throws IOException {
        bw.write("# Base file generated using Conversation Script Creator for ProjectSWG\n");
        
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
        int count = handleNum + 1;
        for (ConversationNode selectedOption : options) {
            bw.write("    " + (options.indexOf(selectedOption) > 0 ? "elif" : "if") + " selection == " + options.indexOf(selectedOption) + ":\n");
            if (conversationLinks.containsKey(selectedOption)) {
                bw.write("        #Handler for Option Node " + selectedOption.getId() + "\n");
                for (ConversationNode handleNode : conversationLinks.get(selectedOption)) {
                    if (!handleNode.isOption()) {
                        if (conversationLinks.get(handleNode) == null) {
                            bw.write("        core.conversationService.sendStopConversation(actor, npc, 'conversation/c_newbie_secondchance', 's_136')\n");
                        } else {
                            createOptions(bw, conversationLinks.get(handleNode), count++, "        ");
                            bw.write("        core.conversationService.sendConversationMessage(actor, npc, OutOfBand.ProsePackage('@conversation/" 
                                    + handleNode.getStf() + "')\n");
                            handleFuncs.put(handleNode, conversationLinks.get(handleNode)); // Put these nodes in list so we can create the handlers later.
                        }
                    }
                }
            }
            bw.write("        return\n");
            bw.newLine();
        }
        bw.write("    return\n");
        
        bw.newLine();
        
        int currentHandler = handleNum + 1;
        for (Map.Entry<ConversationNode, ArrayList<ConversationNode>> handleNode : handleFuncs.entrySet()) {
            createResponseHandler(bw, handleNode.getValue(), currentHandler++, conversationLinks);
        }
    }
    
    private void createOptionsAndHandler(BufferedWriter bw, ConversationNode response, ArrayList<ConversationNode> options, int handleScreenNum, 
            LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> conversationLinks) throws IOException {
        
        bw.write("    options = new Vector()\n");
        
        if (options.size() > 1) {
            ArrayList<ConversationNode> orderedOptions = new ArrayList<>();
            for (ConversationNode unOrdered : options) {
                orderedOptions.add(unOrdered.getOptionId(), unOrdered);
            }
            for (ConversationNode option : orderedOptions) {
                bw.write("    options.add(ConversationOption(OutOfBand.ProsePackage('@conversation/"  + option.getStf() +"'), " + options.indexOf(option) + ")\n");
            }         
        } else { 
            for (ConversationNode option : options) {
                bw.write("    options.add(ConversationOption(OutOfBand.ProsePackage('@conversation/"  + option.getStf() +"'), " + options.indexOf(option) + ")\n");
            }
        }
        
        bw.write("    core.conversationService.sendConversationOptions(actor, npc, handleOptionScreen" + String.valueOf(handleScreenNum) + ")\n");
        bw.write("    core.conversationService.sendConversationMessage(actor, npc, OutOfBand.ProsePackage('@conversation/" + response.getStf() + "'))\n");
        bw.write("    return\n");
        bw.newLine();
        createResponseHandler(bw, options, handleScreenNum, conversationLinks);
    }
    
    private void createOptions(BufferedWriter bw, ArrayList<ConversationNode> options, int handleScreenNum, String space) throws IOException {
        bw.write(space + "options = new Vector()\n");
        
        if (options.size() > 1) {
            ArrayList<ConversationNode> orderedOptions = new ArrayList<>();
            for (ConversationNode unOrdered : options) {
                orderedOptions.add(unOrdered.getOptionId(), unOrdered);
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
}
