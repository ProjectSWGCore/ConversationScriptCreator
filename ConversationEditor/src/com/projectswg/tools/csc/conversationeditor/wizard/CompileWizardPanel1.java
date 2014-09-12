/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectswg.tools.csc.conversationeditor.wizard;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbPreferences;

public class CompileWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor>, TreeSelectionListener {

    private CompileVisualPanel1 component;
    
    private boolean valid = true;
    
    private final Set<ChangeListener> listeners = new HashSet<>(1);
    
    public CompileWizardPanel1() {
        valid = false;
    }
    
    @Override
    public CompileVisualPanel1 getComponent() {
        if (component == null) {
            component = new CompileVisualPanel1();
            component.getScriptFolderTree().addTreeSelectionListener(this);
        }
        return component;
    }
    
    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
        System.out.println("dafd" + valid);
        return valid;
    }
    
    private void setValid(boolean val) {
        if (val == valid)
            return;
        
        valid = val;
        fireChangeEvent();
    }
    
    @Override
    public void addChangeListener(ChangeListener l) {
        listeners.add(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        listeners.remove(l);
    }

    protected final void fireChangeEvent() {
        ChangeEvent event = new ChangeEvent(this);
        
        for (ChangeListener l : listeners) {
            l.stateChanged(event);
        }
    }
    
    @Override
    public void readSettings(WizardDescriptor wiz) {
        Object tree = wiz.getProperty("tree");
        
        if (tree != null) {
            component.setScriptsTree((JTree) tree);
        } else {

            Object scripts = wiz.getProperty("directory");

            if (scripts == null) {
                scripts = NbPreferences.forModule(CompileWizardPanel1.class).get("directory", "");

                if (scripts == "") {
                    return;
                }
            }

            File dir = new File(scripts.toString().split("conversation")[0] + "\\conversation");
            if (!dir.exists()) {
                return;
            }

            component.populateScriptsTree(dir);
        }
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        wiz.putProperty("directory", component.getExportDirectory().getText());
        NbPreferences.forModule(CompileWizardPanel1.class).put("directory", component.getExportDirectory().getText()); // persist across reloads
        
        wiz.putProperty("tree", component.getScriptFolderTree());
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (e.getNewLeadSelectionPath() == null)
            return;
        
        Object[] path = e.getNewLeadSelectionPath().getPath();
        String dir = component.getExportDirectory().getText().split("conversation")[0] + "conversation";

        for (Object p : path) {
            if (!p.toString().equals("scripts/conversation") && !dir.contains(p.toString()))
                dir += "\\" +p.toString();
        }
        component.getExportDirectory().setText(dir);
        
        setValid(true);
    }
}
