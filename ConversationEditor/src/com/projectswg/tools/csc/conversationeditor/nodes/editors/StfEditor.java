package com.projectswg.tools.csc.conversationeditor.nodes.editors;

import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import com.projectswg.tools.csc.conversationeditor.scene.SceneView;
import com.projectswg.tools.csc.conversationeditor.stf.Stf;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyEditorSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Set;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.windows.TopComponent;

public class StfEditor extends PropertyEditorSupport implements ExPropertyEditor, VetoableChangeListener {

    private StfEditorVisual editor;
    private PropertyEnv env;
    
    public StfEditor() {
        editor = new StfEditorVisual();
    }
    
    @Override
    public Object getValue() {
        if (super.getValue() == null) {
            //System.out.println("NULL VALUE");
            setValue(null);
        }
        Stf val = (Stf) super.getValue();

        val.setKey(editor.getTfStfKey().getText());
        val.setFile(editor.tfStfFile.getText());
        //val.setValue((String) editor.stfValues.getModel().getElementAt(editor.stfValues.getSelectedIndex()));
        
        return super.getValue(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        //System.out.println("Setting as text.");
        super.setAsText(text); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsText() {
        Stf val = (Stf) super.getValue();
        //System.out.println("Getting as text.");
        return val.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Component getCustomEditor() {
        //editor.addPropertyChangeListener(StfEditorVisual.);
        env.addVetoableChangeListener(this);
        return editor; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setValue(Object stf) {
        if (stf != null) {
            super.setValue(stf);
            return;
        }
        
        TopComponent component = TopComponent.getRegistry().getActivated();
        if (component == null || !(component instanceof EditorTopComponent)) {
            Set<TopComponent> components = TopComponent.getRegistry().getOpened();
            boolean success = false;
            for (TopComponent comp : components) {
                if (comp instanceof EditorTopComponent) {
                    component = comp;
                    success = true;
                    break;
                }
            }
            if (!success)
                return;
        }
        
        EditorTopComponent editorComp = (EditorTopComponent) component;
        SceneView scene = editorComp.getScene();
        
        if (scene == null)
            return;
        
        if(stf==null){
            stf = new Stf(scene.getStfFile());
        }
        editor.tfStfFile.setText(((Stf)stf).getFile());
        editor.getTfStfKey().setText(((Stf)stf).getKey());
        editor.stfValues.setListData(((Stf)stf).getEntries());
        super.setValue(stf);
        //System.out.println("Value set (setValue): " + ((Stf)stf).getKey());
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        this.env = env;
        editor.env = env;
        editor.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        // Only thrown when OK is pressed.
        Stf val = (Stf) getValue();
    }
}
