package com.projectswg.tools.csc.conversationeditor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.explorer.ExplorerManager;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class SceneView extends GraphScene<ConversationNode, String>{
    private final LayerWidget backgroundLayer;
    private final LayerWidget mainLayer;
    private final LayerWidget connectionLayer;
    private final ExplorerManager mgr;
    
    public SceneView(ExplorerManager mgr) {
        setLookFeel(new ConversationLookFeel());
        
        backgroundLayer = new LayerWidget(this);
        addChild(backgroundLayer);
        
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);
        
        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createWheelPanAction());
        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);
        
        this.mgr = mgr;
        
    }
    
    @Override
    protected Widget attachNodeWidget(ConversationNode n) {
        final ConversationWidget widget = new ConversationWidget(this, mgr, n, n.isEndNode());
        widget.getActions().addAction(createObjectHoverAction());
        
        n.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                TopComponent component = WindowManager.getDefault().findTopComponent("EditorTopComponent");
                if (component == null || !(component instanceof EditorTopComponent))
                    return;
        
                EditorTopComponent editor = (EditorTopComponent) component;
                SceneView scene = editor.getScene();
        
                if (scene == null)
                    return;
                
                switch(evt.getPropertyName()) {
                    case "stf":
                        widget.getLabelWidget().setLabel((String) evt.getNewValue());
                        widget.repaint();
                        
                        scene.validate();
                        break;
                }
            }
        });
        mainLayer.addChild(widget);
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(String e) {
        JOptionPane.showMessageDialog(WindowManager.getDefault().getMainWindow(), 
                "Edge widget attachments is not support yet.", "Conversation Script Editor", JOptionPane.WARNING_MESSAGE);
        return null;
    }

    @Override
    protected void attachEdgeSourceAnchor(String e, ConversationNode n, ConversationNode n1) {
        JOptionPane.showMessageDialog(WindowManager.getDefault().getMainWindow(), 
                "Edge widget attachments is not support yet.", "Conversation Script Editor", JOptionPane.WARNING_MESSAGE);    
    }

    @Override
    protected void attachEdgeTargetAnchor(String e, ConversationNode n, ConversationNode n1) {
        JOptionPane.showMessageDialog(WindowManager.getDefault().getMainWindow(), 
                "Edge widget attachments is not support yet.", "Conversation Script Editor", JOptionPane.WARNING_MESSAGE);    
    }

    public LayerWidget getConnectionLayer() {
        return connectionLayer;
    }
    
}