package com.projectswg.tools.csc.conversationeditor.actions;

import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import com.projectswg.tools.csc.conversationeditor.nodes.OptionNode;
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
        id = "com.projectswg.tools.csc.conversationeditor.actions.NewConvOption"
)
@ActionRegistration(
        iconBase = "com/projectswg/tools/csc/conversationeditor/actions/conversation_tb_option.png",
        displayName = "New Option"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1250),
    @ActionReference(path = "Toolbars/Edit", position = 250)
})
@Messages("CTL_NewConvOption=New Option")
public final class NewConvOption implements ActionListener {

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
        scene.addNode(new OptionNode("New Conversation Option " + String.valueOf(id), id));
        
        scene.validate();
    }
}
