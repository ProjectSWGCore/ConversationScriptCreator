/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectswg.tools.csc.conversationeditor.wizard;

import com.projectswg.tools.csc.conversationeditor.Compiler;
import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;

// An example action demonstrating how the wizard could be called from within
// your code. You can move the code below wherever you need, or register an action:
@ActionID(category="File", id="com.projectswg.tools.csc.conversationeditor.wizard.CompileWizardAction")
@ActionRegistration(displayName="Compile Script")
@ActionReference(path="Menu/File", position=0, separatorAfter=1)
public final class CompileWizardAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent component = TopComponent.getRegistry().getActivated();
        if (component == null || !(component instanceof EditorTopComponent)) {
            JOptionPane.showConfirmDialog(component, "No active scene to compile!", "Conversation Script Creator", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<>();
        panels.add(new CompileWizardPanel1());
        panels.add(new CompileWizardPanel2());
        String[] steps = new String[panels.size()];
        for (int i = 0; i < panels.size(); i++) {
            Component c = panels.get(i).getComponent();
            // Default step name to component name of panel.
            steps[i] = c.getName();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
            }
        }
        WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<>(panels));
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wiz.setTitleFormat(new MessageFormat("{0}"));
        wiz.setTitle("Compile Wizard");
        
        if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION) {
            String directory = (String) wiz.getProperty("directory");
            boolean comments = (Boolean) wiz.getProperty("comments");
            String header = (String) wiz.getProperty("header");
            boolean tabs = (((String) wiz.getProperty("indent")).equals("tabs"));
            String script = (String) wiz.getProperty("script");
            
            File file = new File(directory + "\\" + script);
            if (file.exists()) {
                JOptionPane.showConfirmDialog(null, "The directory " + file.getAbsolutePath() +
                        " already exists!", "Conversation Script Creator", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                return;
            }
            Compiler compiler = new Compiler(file, comments, header, tabs);
            try {
                if (compiler.compile()) {
                    if (JOptionPane.showConfirmDialog(null, "Script created at " + file.getAbsolutePath() + " successfully. Would you like to open it?", 
                            "Conversation Script Creator", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
                        Desktop dt = Desktop.getDesktop();
                        if (dt != null)
                            dt.open(file);
                    }
                } else {
                    JOptionPane.showConfirmDialog(null, "Failed to create script at " + file.getAbsolutePath()
                            + " !", "Conversation Script Creator", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
    }
}
