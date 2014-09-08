package com.projectswg.tools.csc.conversationeditor;

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
            ConnectionWidget conn = new ConnectionWidget(scene);
            conn.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
            conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(target));
            conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(source));
            scene.getConnectionLayer().addChild(conn);
            
            ((ConversationWidget)source).getAttachedNode().setCompiled(false);
            ((ConversationWidget)target).getAttachedNode().setCompiled(false);
        }        
    }
}
