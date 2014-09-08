package com.projectswg.tools.csc.conversationeditor;

import java.awt.Point;
import java.beans.PropertyVetoException;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.explorer.ExplorerManager;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

public class ConversationWidget extends IconNodeWidget implements LookupListener {
    private final ConversationNode attachedNode;
    private final ExplorerManager mgr;
    
    public ConversationWidget(SceneView scene, final ExplorerManager mgr, final ConversationNode node, boolean endConversation) {
        super(scene.getScene());
        
        this.attachedNode = node;
        this.mgr = mgr;
        
        if (node.isEndNode()) {
            setLabel(node.getStf());
            setImage(ImageUtilities.loadImage("com/projectswg/tools/csc/conversationeditor/conversation_end.png"));
        } else {
            setLabel(node.getStf());
            
            if (node.isStartNode()) {
                setImage(ImageUtilities.loadImage("com/projectswg/tools/csc/conversationeditor/conversation_begin.png"));
            } else {
                setImage(node.isOption() ? ImageUtilities.loadImage("com/projectswg/tools/csc/conversationeditor/conversation_option.png") 
                                        : ImageUtilities.loadImage("com/projectswg/tools/csc/conversationeditor/conversation_response.png"));
            }
        }

        // Alignment/pos/ori
        getLabelWidget().setAlignment(LabelWidget.Alignment.CENTER);
        setLayout(LayoutFactory.createOverlayLayout());
        
        // Add Actions
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getConnectionLayer(), new ConversationConnectProvider(scene)));
        getActions().addAction(ActionFactory.createMoveAction(new MoveStrategy() {
            @Override
            public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation) {
                if (!(widget instanceof ConversationWidget))
                    return suggestedLocation;
                
                if (((ConversationWidget) widget).getAttachedNode().isLocked())
                    return originalLocation;
                
                return suggestedLocation;
            }
        }, ActionFactory.createDefaultMoveProvider()));
        getActions().addAction(ActionFactory.createSelectAction(new SelectProvider() {

            @Override
            public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
                return true;
            }

            @Override
            public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
                return true;
            }

            @Override
            public void select(Widget widget, Point point, boolean bln) {
                if (!(widget instanceof ConversationWidget))
                    return;
                
                ConversationWidget convWidget = (ConversationWidget) widget;
                ConversationNode convNode = convWidget.getAttachedNode();
                convWidget.getExpManager().setRootContext(convNode);
                
                ConversationNode[] nodes = new ConversationNode[1];
                nodes[0] = node;
                try {
                    convWidget.getExpManager().setSelectedNodes(nodes);
                } catch (PropertyVetoException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }, true));
        
    }
    
    public ConversationNode getAttachedNode() {
        return attachedNode;
    }
    
    public ExplorerManager getExpManager() {
        return mgr;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
