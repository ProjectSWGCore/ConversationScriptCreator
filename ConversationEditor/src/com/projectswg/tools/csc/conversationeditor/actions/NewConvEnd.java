/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectswg.tools.csc.conversationeditor.actions;

import com.projectswg.tools.csc.conversationeditor.ConversationNode;
import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import com.projectswg.tools.csc.conversationeditor.SceneView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Edit",
        id = "com.projectswg.tools.csc.conversationeditor.actions.NewConvEnd"
)
@ActionRegistration(
        iconBase = "com/projectswg/tools/csc/conversationeditor/actions/conversation_tb_end.png",
        displayName = "New End Conversation"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1362),
    @ActionReference(path = "Toolbars/Edit", position = 450)
})
@Messages("CTL_NewConvEnd=New End Conversation")
public final class NewConvEnd implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent component = TopComponent.getRegistry().getActivated();
        if (component == null || !(component instanceof EditorTopComponent))
            return;
        
        EditorTopComponent editor = (EditorTopComponent) component;
        SceneView scene = editor.getScene();
        
        if (scene == null)
            return;
        
        int id = scene.getNodes().size();
        scene.addNode(new ConversationNode("New End Conversation " + String.valueOf(id), false, id, true, false, 0));
        
        scene.validate();
    }
}
