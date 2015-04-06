package com.projectswg.tools.csc.conversationeditor.scene;

import com.projectswg.tools.csc.conversationeditor.ConversationLookFeel;
import com.projectswg.tools.csc.conversationeditor.ConversationWidget;
import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import com.projectswg.tools.csc.conversationeditor.nodes.ConversationNode;
import com.projectswg.tools.csc.conversationeditor.stf.StfTable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.explorer.ExplorerManager;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class SceneView extends GraphScene<ConversationNode, String>{
    private final LayerWidget backgroundLayer;
    private final LayerWidget mainLayer;
    private final LayerWidget connectionLayer;
    private final ExplorerManager mgr;
    
    private String name = "";
    private String scenePath = "";
    private boolean loaded = false;
    
    private String stfFile = "None";
    
    private StfTable stfTable;
    
    private int id = 1;
    
    public SceneView(ExplorerManager mgr) {
        setLookFeel(new ConversationLookFeel());
        
        backgroundLayer = new LayerWidget(this);
        addChild(backgroundLayer);
        
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);
        
        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);
        
        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createWheelPanAction());
        this.mgr = mgr;
        
    }
    
    @Override
    protected Widget attachNodeWidget(final ConversationNode n) {
        
        final ConversationWidget widget = new ConversationWidget(this, mgr, n);
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
                
                widget.getLabelWidget().setLabel(n.getStf().toString());
                widget.repaint();
                scene.validate();
                
                switch(evt.getPropertyName()) {
                    case "stf":
                        widget.getLabelWidget().setLabel(evt.getNewValue().toString());
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
    
    public String getSceneName() {
        return name;
    }
    
    public void setSceneName(String name) {
        this.name = name;
    }
    
    public String getScenePath() {
        return this.scenePath;
    }
    
    public void setScenePath(String path) {
        this.scenePath = path;
    }

    public ExplorerManager getManager() {
        return mgr;
    }
    
    public int getNextId() {
        this.id = id +1;
        return id++;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
    
    public boolean isLoaded() {
        return loaded;
    }
    
    public String getStfFile() {
        return stfFile;
    }

    public void setStfFile(String stfFile) {
        this.stfFile = stfFile;
        StfTable stfTable = new StfTable();
        try {
            stfTable.readFile(stfFile);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        this.stfTable = stfTable;
    }
    
    public StfTable getStfTable() {
        return stfTable;
    }
    
    public LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> getConversationLinks() {
        List<Widget> connectedNodes = connectionLayer.getChildren();
        LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> conversationLinks = new LinkedHashMap<>();
            
        for (Widget widget : connectedNodes) {
            ConnectionWidget connection = (ConnectionWidget) widget;
                
            ConversationWidget source = (ConversationWidget) connection.getSourceAnchor().getRelatedWidget();
            ConversationNode sNode = source.getAttachedNode();
                
            if (!conversationLinks.containsKey(sNode)) {
                conversationLinks.put(sNode, new ArrayList<ConversationNode>());
            }
                
            ConversationWidget target = (ConversationWidget) connection.getTargetAnchor().getRelatedWidget();
            ConversationNode tNode = target.getAttachedNode();
                
            if (conversationLinks.containsKey(sNode))
                conversationLinks.get(sNode).add(tNode);
            else
                System.out.println("No key for node " + sNode.getStf());
        }
        return conversationLinks;
    }
}