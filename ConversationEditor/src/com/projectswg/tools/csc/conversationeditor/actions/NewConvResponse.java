/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectswg.tools.csc.conversationeditor.actions;

import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import com.projectswg.tools.csc.conversationeditor.nodes.ResponseNode;
import com.projectswg.tools.csc.conversationeditor.scene.SceneView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@ActionID(
        category = "File",
        id = "com.projectswg.tools.csc.conversationeditor.actions.NewConvResponse"
)
@ActionRegistration(
        iconBase = "com/projectswg/tools/csc/conversationeditor/actions/conversation_tb_response.png",
        displayName = "New Response"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1350, separatorAfter = 1375),
    @ActionReference(path = "Toolbars/Edit", position = 350)
})
@Messages("CTL_NewConvResponse=New Response")
public final class NewConvResponse implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent component = TopComponent.getRegistry().getActivated();
        if (component == null || !(component instanceof EditorTopComponent))
            return;
        
        EditorTopComponent editor = (EditorTopComponent) component;
        SceneView scene = editor.getScene();
        
        if (scene == null)
            return;

        int id = scene.getNextId();
        scene.addNode(new ResponseNode("New Conversation Response " + String.valueOf(id), id));
        scene.validate();
    }
}
