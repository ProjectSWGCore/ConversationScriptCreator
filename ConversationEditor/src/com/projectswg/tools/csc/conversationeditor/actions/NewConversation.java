/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectswg.tools.csc.conversationeditor.actions;

import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "com.projectswg.tools.csc.conversationeditor.actions.NewConversation"
)
@ActionRegistration(
        iconBase = "com/projectswg/tools/csc/conversationeditor/actions/conversation_tb_new.png",
        displayName = "New Conversation"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = -115),
    @ActionReference(path = "Toolbars/File", position = 400),
    @ActionReference(path = "Shortcuts", name = "D-N")
})
@Messages("CTL_NewConversation=New Conversation")
public final class NewConversation implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorTopComponent newEditor = new EditorTopComponent();
        newEditor.open();
        newEditor.requestActive();
    }
}
