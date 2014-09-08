package com.projectswg.tools.csc.conversationeditor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "File", id = "org.mycore.OpenFileAction")
@ActionRegistration(displayName = "Open Conversation")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 10, separatorAfter = 11),
    @ActionReference(path = "Shortcuts", name = "D-O")
})
@Messages("CTL_OpenFileAction=Open Conversation")
public final class OpenConversation implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        //The default dir to use if no value is stored
        File home = new File(System.getProperty("user.home"));

        File toAdd = new FileChooserBuilder("user-dir").setTitle("Open Conversation").
                setDefaultWorkingDirectory(home).setApproveText("Open").showOpenDialog();
        //Result will be null if the user clicked cancel or closed the dialog w/o OK
        if (toAdd != null) {
            // TODO:
        }
    }
    
}
