package com.projectswg.tools.csc.conversationeditor;

import com.projectswg.tools.csc.conversationeditor.nodes.ConversationNode;
import java.awt.Point;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

public class ConversationConnectProvider implements ConnectProvider {

    private final SceneView scene;
    
    public ConversationConnectProvider(SceneView scene) {
        this.scene = scene;
    }
    
    @Override
    public boolean isSourceWidget(Widget source) {
        return source instanceof ConversationWidget;
    }

    @Override
    public ConnectorState isTargetWidget(Widget source, Widget target) {
        return source != target && target instanceof ConversationWidget ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public boolean hasCustomTargetWidgetResolver(Scene scene) {
        return false;
    }

    @Override
    public Widget resolveTargetWidget(Scene scene, Point point) {
        return null;
    }

    @Override
    public void createConnection(Widget source, Widget target) {
        if (source instanceof ConversationWidget && target instanceof ConversationWidget) {
            if (((ConversationWidget)source).getAttachedNode().isEndNode()) {
                JOptionPane.showMessageDialog(null, "Cannot attach end conversation node to responses/options!", "Conversation Script Creator", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            ConversationNode cSource = ((ConversationWidget) source).getAttachedNode();
            ConversationNode cTarget = ((ConversationWidget) target).getAttachedNode();
            
            if (cSource.isOption()) {
                if (cTarget.isOption())
                    return;
                
                else if (cTarget.isStartNode())
                    return;
                
            } else if (cSource.isStartNode()) {
                if (cTarget.isEndNode())
                    return;
            } else if (cSource.isEndNode()) {
                return;
                
            } else { // Response nodes
                if (cTarget.isEndNode())
                    return;
                
                else if (cTarget.isStartNode())
                    return;
            }

            /* This won't work for preventing linking to same node 2x's. Need to find another way :(
            if (scene.getConnectionLayer().getChildren().size() > 0) {
                for (Widget widget : scene.getConnectionLayer().getChildren()) {
                    System.out.println("CHILD: " + widget.toString());
                    if (!(widget instanceof ConnectionWidget)) {
                        continue;
                    }

                    ConnectionWidget existingConn = (ConnectionWidget) widget;
                    if (existingConn.getTargetAnchor().getRelatedWidget() == target) {
                        System.out.println("SAME");
                        return;
                    }
                }
            }*/
            
            ConnectionWidget conn = new ConnectionWidget(scene);
            conn.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
            conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(target));
            conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(source));
            scene.getConnectionLayer().addChild(conn);
        }        
    }
}
