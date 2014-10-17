/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectswg.tools.csc.conversationeditor.actions;

import com.projectswg.tools.csc.conversationeditor.ConversationWidget;
import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import com.projectswg.tools.csc.conversationeditor.nodes.ConversationNode;
import com.projectswg.tools.csc.conversationeditor.scene.SceneView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Edit",
        id = "com.projectswg.tools.csc.conversationeditor.actions.TrashConvNode"
)
@ActionRegistration(
        iconBase = "com/projectswg/tools/csc/conversationeditor/actions/conversation_tb_trash.png",
        displayName = "Trash Node"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1475),
    @ActionReference(path = "Toolbars/Edit", position = 450),
    @ActionReference(path = "Shortcuts", name = "DELETE")
})
@Messages("CTL_TrashConvNode=Trash  Node")
public final class TrashConvNode implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent component = TopComponent.getRegistry().getActivated();
        if (component == null || !(component instanceof EditorTopComponent))
            return;
        
        final EditorTopComponent editor = (EditorTopComponent) component;
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                trashActiveNodes(editor);
            }
        });

    }
    
    private static void trashActiveNodes(final EditorTopComponent editor) {
        SceneView scene = editor.getScene();
        
        if (scene == null)
            return;
        
        Node[] selectedNodes = scene.getManager().getSelectedNodes();
        
        HashMap<ConversationWidget, ConversationNode> widgets = new HashMap<>();
        
        // Get the nodes widgets & remove from scene
        for (Node n : selectedNodes) {
            ConversationWidget found = (ConversationWidget) scene.findWidget(n);
            if (found == null) {
                System.err.println("COULDNT FIND WIDGET TO REMOVE");
                return;
            }
            widgets.put(found, (ConversationNode) n);
            found.removeFromParent();
        }
        
        // Remove connections for the node
        ArrayList<Widget> connections = new ArrayList<>();

        for (Widget child : scene.getConnectionLayer().getChildren()) {
            if (!(child instanceof ConnectionWidget))
                continue;
            
            ConnectionWidget connection = (ConnectionWidget) child;
            if (widgets.containsKey((ConversationWidget) connection.getTargetAnchor().getRelatedWidget())) {
                connections.add(child);
            }
            
            if (widgets.containsKey((ConversationWidget) connection.getSourceAnchor().getRelatedWidget())) {
                connections.add(child);
            }
        }
        
        scene.getConnectionLayer().removeChildren(connections);
        
        for (Node n : selectedNodes) {
            scene.removeNode((ConversationNode) n);
        }
        scene.validate();
    }
}
