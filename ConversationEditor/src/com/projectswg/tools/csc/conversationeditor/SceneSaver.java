package com.projectswg.tools.csc.conversationeditor;

import java.awt.Point;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Widget;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Based off of http://bits.netbeans.org/dev/javadoc/org-netbeans-api-visual/org/netbeans/api/visual/widget/doc-files/documentation.html#Serialization
public class SceneSaver {
    private static final String NODE_ELEMENT = "SceneNode"; // NOI18N
    private static final String VERSION_ATTR = "version"; // NOI18N
    private static final String NODE_NODE = "Node"; // NOI18N
    private static final String NODEID_ATTR = "nodeID"; // NOI18N
    private static final String X_NODE = "posX"; // NOI18N
    private static final String Y_NODE = "posY"; // NOI18N

    private static final String VERSION_VALUE_1 = "1"; // NOI18N

    // creates an sceneXMLNode that has to be placed to the XML file then
    public Node serializeData (GraphScene<ConversationNode, String> scene, Document file) {

        Node sceneXMLNode = file.createElement(NODE_ELEMENT);
        setAttribute(file, sceneXMLNode, VERSION_ATTR, VERSION_VALUE_1); // NOI18N

        for (ConversationNode node : scene.getNodes ()) {
                Widget widget = scene.findWidget(node);
                if (widget != null) {
                    Point location = widget.getPreferredLocation();
                    if (location != null) {
                        Node dataXMLNode = file.createElement(NODE_NODE);
                        
                        setAttribute(file, dataXMLNode, NODEID_ATTR, String.valueOf(node.getId()));
                        setAttribute(file, dataXMLNode, X_NODE, Integer.toString(location.x));
                        setAttribute(file, dataXMLNode, Y_NODE, Integer.toString(location.y));

                        sceneXMLNode.appendChild(dataXMLNode);
                    }
                }
            }
        return sceneXMLNode;
    }

    // Returns true if deserialization is successfull
    // sceneXMLNode has to be found in the XML file first
    public boolean deserializeData(final GraphScene<ConversationNode, String> scene, final Node sceneXMLNode) {
        if (!VERSION_VALUE_1.equals(getAttributeValue(sceneXMLNode, VERSION_ATTR)))
            return false;

        SwingUtilities.invokeLater (new Runnable() {
            @Override
            public void run() {
                deserializeDataVersion1(scene, sceneXMLNode);
                scene.validate ();
            }
        });

        return true;
    }

    private void deserializeDataVersion1(GraphScene<ConversationNode, String> scene, Node data) {
        for (Node node : getChildNode (data)) {
            if(NODE_NODE.equals(node.getNodeName())) {
                String nodeID = getAttributeValue(node, NODEID_ATTR);
                int x = Integer.parseInt(getAttributeValue (node, X_NODE));
                int y = Integer.parseInt(getAttributeValue (node, Y_NODE));
                Widget widget = scene.findWidget(nodeID);
                if (widget != null)
                    widget.setPreferredLocation (new Point (x, y));
            }
        }
    }

    private static String getAttributeValue(Node node, String attr) {
        try {
            if (node != null) {
                NamedNodeMap map = node.getAttributes ();
                if (map != null) {
                    node = map.getNamedItem (attr);
                    if (node != null)
                        return node.getNodeValue ();
                }
            }
        } catch (DOMException e) {
            //Debug.warning (e);
        }
        return null;
    }

    private static void setAttribute(Document xml, Node node, String name, String value) {
        NamedNodeMap map = node.getAttributes ();
        Attr attribute = xml.createAttribute(name);
        attribute.setValue (value);
        map.setNamedItem (attribute);
    }

    private static Node[] getChildNode (Node node) {
        NodeList childNodes = node.getChildNodes ();
        Node[] nodes = new Node[childNodes != null ? childNodes.getLength () : 0];
        for (int i = 0; i < nodes.length; i++)
            nodes[i] = childNodes.item (i);
        return nodes;
    }    
}
