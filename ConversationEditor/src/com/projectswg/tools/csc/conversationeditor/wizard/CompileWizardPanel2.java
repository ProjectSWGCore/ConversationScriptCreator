/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectswg.tools.csc.conversationeditor.wizard;

import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class CompileWizardPanel2 implements WizardDescriptor.ValidatingPanel<WizardDescriptor>, DocumentListener {

    /**
     * The visual component that displays this panel. If you need to access the component from this class, just use getComponent().
     */
    private CompileVisualPanel2 component;
    private boolean valid = false;
    private final Set<ChangeListener> listeners = new HashSet<>(1);
    
    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public CompileVisualPanel2 getComponent() {
        if (component == null) {
            component = new CompileVisualPanel2();
            component.getTbScriptName().getDocument().addDocumentListener(this);
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isValid() {
        System.out.println(valid);
        return valid;
    }

    public void setValid(boolean val) {

        if (valid == val)
            return;
        
        this.valid = val;
        fireChangeEvent();
    }
    
    protected final void fireChangeEvent() {
        ChangeEvent event = new ChangeEvent(this);

        for (ChangeListener l : listeners) {
            l.stateChanged(event);
        }
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        listeners.add(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        listeners.remove(l);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        Object indents = wiz.getProperty("indent");
        if (indents == null || indents.toString().equals("tabs"))
            component.getRadioTabs().setSelected(true);
        else 
            component.getRadioSpaces().setSelected(true);
        
        Object comments = wiz.getProperty("comments");
        if (comments == null) {
            
        } else {
            component.getChkbxComments().setSelected(Boolean.valueOf(comments.toString()));
        }
        
        Object header = wiz.getProperty("header");
        if (header == null) {
            
        } else {
            component.getTxtAreaHeader().setText(header.toString());
        }
        
        Object sName = wiz.getProperty("script");
        
        if (sName == null) {
            // TODO: Use scene name
        } else {
            component.getTbScriptName().setText(sName.toString());
            valid = true;
        }
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {

        if (component.getRadioTabs().isSelected())
            wiz.putProperty("indent", "tabs");
        else
            wiz.putProperty("indent", "spaces");
        
        wiz.putProperty("comments", component.getChkbxComments().isSelected());
        wiz.putProperty("header", component.getTxtAreaHeader().getText());
        wiz.putProperty("script", component.getTbScriptName().getText());
    }

    private void checkText() {
        if (!component.getTbScriptName().getText().endsWith(".py")) {
            setValid(false);
        } else {
            setValid(true);
        }
    }
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        checkText();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkText();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        checkText();
    }

    @Override
    public void validate() throws WizardValidationException {
        if (!component.getTbScriptName().getText().endsWith(".py")) {
            valid = false;
            throw new WizardValidationException(null, "The script name must end with the .py extension.", null);
        }
    }

}
